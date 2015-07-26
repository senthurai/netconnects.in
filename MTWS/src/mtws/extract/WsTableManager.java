package mtws.extract;

import java.awt.MouseInfo;

import mtws.engine.ModelProvider;
import mtws.views.MTWSComparator;
import mtws.views.MtWSTableEditingSupport;
import mtws.views.MtwsView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public abstract class WsTableManager {
	protected Table mtWsTable = null;
	protected TableViewer mtWsTableViewer = null;

	protected WsTableManager WsTableManager;
	protected ModelProvider pModelProvider;
	protected Action doubleClickAction = null;

	abstract protected Object[][] getCOLUMNS();
	private SelectionAdapter getSelectionAdapter(final MTWSComparator comparator, final TableColumn column,
		      final int index) {
		    SelectionAdapter selectionAdapter = new SelectionAdapter() {
		      @Override
		      public void widgetSelected(SelectionEvent e) {
		        comparator.setColumn(index);
		        int dir = getComparator().getDirection();
		        mtWsTableViewer.getTable().setSortDirection(dir);
		        mtWsTableViewer.getTable().setSortColumn(column);
		        mtWsTableViewer.refresh();
		      }
		    };
		    return selectionAdapter;
		  }

	protected TableViewerColumn createColumn(TableLayout tableLayout, TableViewer table, String columnName, String heading, int alignment, int weight, int minimumWidth,
			boolean resizable, String pCellEditor, boolean canEdit, int colNumber) {
		tableLayout.addColumnData(new ColumnWeightData(weight, minimumWidth, resizable));
		final TableViewerColumn viewerColumn = new TableViewerColumn(table, SWT.CHECK | SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		viewerColumn.setLabelProvider(new WsViewLabelProvider(columnName));
		TableColumn column = viewerColumn.getColumn();
		final MtWSTableEditingSupport editingSupport = new MtWSTableEditingSupport(viewerColumn.getViewer(), columnName, pCellEditor, canEdit);
		viewerColumn.setEditingSupport(editingSupport);
		
		column.addSelectionListener(getSelectionAdapter(new MTWSComparator(),column, colNumber));
		if (pCellEditor.equals("CheckboxCellEditor")) {
			Button b = new Button(table.getTable(), SWT.CHECK);
			b.setVisible(true);
			b.setEnabled(true);
		}

		viewerColumn.getViewer().getControl().setVisible(true);
		/*
		 * column.setCellRenderer(new DefaultTableCellRenderer() { public
		 * Component getTableCellRendererComponent(JTable table, Object value,
		 * boolean isSelected, boolean hasFocus, int row, int column) {
		 * check.setSelected(((Boolean)value).booleanValue()) ; return check; }
		 * });
		 */
		viewerColumn.getViewer().getControl().addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// setEnabled(false);
				// oCellEditor.deactivate();
			}
		});
		viewerColumn.getViewer().getControl().addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) viewerColumn.getViewer().getSelection();
				if (selection.toList().size() != 1) {
					return;
				}
				final ViewerCell cell = viewerColumn.getViewer().getCell(new Point(event.x, event.y));
				editingSupport.getCellEditor(null).activate();
				viewerColumn.getViewer().editElement(selection.getFirstElement(), cell != null ? cell.getColumnIndex() : 0);
			}
		});
		viewerColumn.getViewer().getControl().addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) viewerColumn.getViewer().getSelection();
				if (selection.toList().size() != 1) {
					return;
				}
				final ViewerCell cell = viewerColumn.getViewer().getCell(new Point(event.x, event.y));
				viewerColumn.getViewer().editElement(selection.getFirstElement(), cell != null ? cell.getColumnIndex() : 0);
			}
		});

		column.setText(heading);

		column.setAlignment(alignment);
		return viewerColumn;
	}

	public WsTableManager(Composite shell, Object pMtWsDOs, ModelProvider pModelProvider2, int options) {
		super();
		initiateTable(shell, pMtWsDOs, pModelProvider2, options);
	}
	public abstract void setModelData(TableViewer mtWsTableViewer,Object pMtWsDOs, ModelProvider pModelProvider2);
	void initiateTable(Composite shell, Object pMtWsDOs, ModelProvider pModelProvider2, int options){
		mtWsTable = new Table(shell,options);
		mtWsTable.setHeaderVisible(true);
		mtWsTable.setLinesVisible(true);
		TableLayout tableLayout = new TableLayout();
		mtWsTable.setLayout(tableLayout);
		mtWsTableViewer = new TableViewer(mtWsTable);
		mtWsTableViewer.setComparator(getComparator());
		for (int i = 0; i < getCOLUMNS().length; i++)
			createColumn(tableLayout, mtWsTableViewer, getCOLUMNS()[i][5] + "", getCOLUMNS()[i][0] + "",(Integer)  getCOLUMNS()[i][1], (Integer)getCOLUMNS()[i][2],
					(Integer) getCOLUMNS()[i][3],(Boolean)getCOLUMNS()[i][4], getCOLUMNS()[i][6] + "", (Boolean) getCOLUMNS()[i][7], i);

		mtWsTableViewer.setContentProvider(new ArrayContentProvider());
		setModelData(mtWsTableViewer,pMtWsDOs, pModelProvider2);
		if (getModelData() == null) {
			populateAndSetData(mtWsTableViewer);
		}else{
			
		}
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		hookDoubleClickAction();
		mtWsTableViewer.getControl().setLayoutData(gridData);
	}
	abstract public MTWSComparator getComparator();
	public abstract Class getModelType();

	public abstract Object getModelData();

	public abstract void populateAndSetData(TableViewer mtWsTableViewer);

	public TableViewer getViewer() {
		// TODO Auto-generated method stub
		return mtWsTableViewer;
	}

	
	private void hookDoubleClickAction() {
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) mtWsTableViewer.getSelection();
				 doubleClickAction( selection.toArray());
			}
		};
		mtWsTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
				
			}
		});
		mtWsTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent paramMouseEvent) {
				//Right Click
					if(paramMouseEvent.button==3)
						doubleClickAction.run();
					
				
			}
			
			@Override
			public void mouseDown(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	protected abstract void doubleClickAction(Object obj[]);
}