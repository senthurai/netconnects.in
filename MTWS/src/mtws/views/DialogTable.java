package mtws.views;

import java.util.TreeSet;
import java.util.Iterator;
import java.util.Set;

import mtws.data.MtWsDO;
import mtws.engine.ModelProvider;
import mtws.extract.WsTableManager;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class DialogTable extends WsTableManager {

	// ModelProvider pModelProvider;

	/**
	 * @return the doubleClickAction
	 */
	public Action getDoubleClickAction() {
		return doubleClickAction;
	}

	/**
	 * @param doubleClickAction
	 *            the doubleClickAction to set
	 */
	public void setDoubleClickAction(Action doubleClickAction) {
		this.doubleClickAction = doubleClickAction;
	}

	/**
	 * @return the mDialogTable
	 */
	public static DialogTable getmDialogTable() {
		return mDialogTable;
	}

	/**
	 * @param mDialogTable
	 *            the mDialogTable to set
	 */
	public static void setmDialogTable(DialogTable mDialogTable) {
		DialogTable.mDialogTable = mDialogTable;
	}

	/**
	 * @return the mtWsTable
	 */
	public static Table getMtWsTable() {
		return mtWsTable;
	}

	/**
	 * @param mtWsTable
	 *            the mtWsTable to set
	 */
	public static void setMtWsTable(Table mtWsTable) {
		DialogTable.mtWsTable = mtWsTable;
	}

	/**
	 * @return the side
	 */
	public String getSide() {
		return side;
	}

	/**
	 * @param side
	 *            the side to set
	 */
	public void setSide(String side) {
		this.side = side;
	}

	/**
	 * @return the pMtWsDOs
	 */
	public TreeSet<MtWsDO> getpMtWsDOs() {
		return pMtWsDOs;
	}

	/**
	 * @param pMtWsDOs
	 *            the pMtWsDOs to set
	 */
	public void setpMtWsDOs(TreeSet<MtWsDO> pMtWsDOs) {
		this.pMtWsDOs = pMtWsDOs;
	}

	private static DialogTable mDialogTable = null;

	private static Table mtWsTable = null;

	private String side = null;

	private TreeSet<MtWsDO> pMtWsDOs;

	DialogTable getIntance(Composite shell, Set<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2, String side) {
		if (mDialogTable != null)
			return mDialogTable;
		else
			mDialogTable = new DialogTable(shell, pMtWsDOs2, pModelProvider2, side);
		this.side = side;
		return mDialogTable;

	}

	public DialogTable(Composite shell, Set<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2, String side2) {
		super(shell, pMtWsDOs2, pModelProvider2, SWT.MULTI | SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
	}

	void assignControls() {
		if (mtWsTableViewer != null) {
			mtWsTableViewer.setCellEditors(setCellEditors(mtWsTableViewer.getTable()));
		}
	}

	public void setModelData(TableViewer mtWsTableViewer, Object pMtWsDOs, ModelProvider pModelProvider2) {
		this.pMtWsDOs = getLeftTableData((TreeSet<MtWsDO>) pMtWsDOs);
		this.pModelProvider = pModelProvider2;
		mtWsTableViewer.setInput(this.pMtWsDOs);
	}

	public void populateAndSetData(TableViewer mtWsTableViewer) {
		pModelProvider = new ModelProvider();
		mtWsTableViewer.setInput(getLeftTableData(pModelProvider.getProjects()));

	}

	TreeSet<MtWsDO> getLeftTableData(TreeSet<MtWsDO> rMtWsDOs) {
		TreeSet<MtWsDO> pMtWsDOs = new TreeSet<MtWsDO>();
		Iterator<MtWsDO> ir = rMtWsDOs.iterator();
		while (ir.hasNext()) {
			MtWsDO pMTWSBo = ir.next();
			if (pMTWSBo.isProjectAvailAsJar() && !pMTWSBo.isMaventypeClosed()) {
				pMtWsDOs.add(pMTWSBo);
			}
		}
		return pMtWsDOs;
	}

	TreeSet<MtWsDO> getRightTableData(TreeSet<MtWsDO> rMtWsDOs) {
		TreeSet<MtWsDO> pMtWsDOs = new TreeSet<MtWsDO>();
		Iterator<MtWsDO> ir = rMtWsDOs.iterator();
		while (ir.hasNext()) {
			MtWsDO pMTWSBo = ir.next();
			if (pMTWSBo.isMaventypeClosed()) {
				pMtWsDOs.add(pMTWSBo);
			}
		}
		return pMtWsDOs;
	}

	CellEditor[] setCellEditors(Composite table) {
		CellEditor[] editors = new CellEditor[8];
		editors[0] = new TextCellEditor(table);
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);
		editors[3] = new CheckboxCellEditor(table);
		editors[3].activate();

		editors[4] = new CheckboxCellEditor(table);
		editors[5] = new CheckboxCellEditor(table);
		editors[6] = new CheckboxCellEditor(table);
		editors[6] = new CheckboxCellEditor(table);

		return editors;

	}

	void addDoubleClickOnTableItem(TableViewer viewer) {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	@Override
	protected Object[][] getCOLUMNS() {
		Object[][] COLUMNS = { { "Project can referenced", SWT.LEFT, 10, 20, false, "projectName", "TextCellEditor", false } };
		return COLUMNS;
	}

	@Override
	public Class getModelType() {
		// TODO Auto-generated method stub
		return MtWsDO.class;
	}

	@Override
	public Object getModelData() {
		// TODO Auto-generated method stub
		return pMtWsDOs;
	}

	@Override
	protected void doubleClickAction(Object objs[]) {
		for (Object Temp : objs) {
			MtWsDO pMtWsDO = (MtWsDO) Temp;
			if (pMtWsDO.isProjectAvailAsJar()) {
				pMtWsDO.setMaventypeClosed(true);
				pMtWsDOs.add(pMtWsDO);
			}
		}
		;
		mtWsTableViewer.setInput(getLeftTableData(pMtWsDOs));
		mtWsTableViewer.refresh();
	}

	@Override
	public MTWSComparator getComparator() {
		MTWSComparator comparator = new MTWSComparator();
		return comparator;
	}

}
