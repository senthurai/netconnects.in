package mtws.views;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import mtws.data.MtWsDO;
import mtws.engine.ModelProvider;
import mtws.extract.WsTableManager;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class LeftTable extends WsTableManager {

	/**
	 * @return the doubleClickAction
	 */
	public Action getDoubleClickAction() {
		return doubleClickAction;
	}

	/**
	 * @param doubleClickAction the doubleClickAction to set
	 */
	public void setDoubleClickAction(Action doubleClickAction) {
		this.doubleClickAction = doubleClickAction;
	}

	/**
	 * @return the mtWsTable
	 */
	public static Table getMtWsTable() {
		return mtWsTable;
	}

	/**
	 * @param mtWsTable the mtWsTable to set
	 */
	public static void setMtWsTable(Table mtWsTable) {
		LeftTable.mtWsTable = mtWsTable;
	}

	/**
	 * @return the pMtWsDOs
	 */
	public TreeSet<MtWsDO> getpMtWsDOs() {
		return pMtWsDOs;
	}

	/**
	 * @param pMtWsDOs the pMtWsDOs to set
	 */
	public void setpMtWsDOs(TreeSet<MtWsDO> pMtWsDOs) {
		this.pMtWsDOs = pMtWsDOs;
	}

	/**
	 * @return the fLeftTable
	 */
	public static LeftTable getfLeftTable() {
		return fLeftTable;
	}

	/**
	 * @param fLeftTable the fLeftTable to set
	 */
	public static void setfLeftTable(LeftTable fLeftTable) {
		LeftTable.fLeftTable = fLeftTable;
	}

	private static Table mtWsTable = null;
	

	private TreeSet<MtWsDO> pMtWsDOs;

	private static LeftTable fLeftTable ;
	public static LeftTable getInstance(Composite shell, Set<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2){
		if(shell!=null && pMtWsDOs2!=null&&pModelProvider2!=null)
			fLeftTable = new LeftTable(shell, pMtWsDOs2, pModelProvider2);
		return fLeftTable;
	}

	private  LeftTable(Composite shell, Set<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
		super(shell, pMtWsDOs2, pModelProvider2, SWT.MULTI | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
	}
	
	void assignControls() {
		if (mtWsTableViewer != null) {
			mtWsTableViewer.setCellEditors(setCellEditors(mtWsTableViewer.getTable()));
		}
	}
	
	public  void setModelData(TableViewer mtWsTableViewer,Object pMtWsDOs, ModelProvider pModelProvider2){
		this.pMtWsDOs= (TreeSet<MtWsDO>) pMtWsDOs;
		this.pModelProvider=pModelProvider2;
		mtWsTableViewer.setInput(getLeftTableData((TreeSet<MtWsDO>)pMtWsDOs));
	}
	
	public void populateAndSetData(TableViewer mtWsTableViewer) {
		pModelProvider = new ModelProvider();
		mtWsTableViewer.setInput(getLeftTableData(pModelProvider.getProjects()));

	}
	
	TreeSet<MtWsDO> getLeftTableData(TreeSet<MtWsDO> rMtWsDOs) {
		TreeSet<MtWsDO>  pMtWsDOs = new TreeSet<MtWsDO>();
		Iterator<MtWsDO> ir = rMtWsDOs.iterator();
		while (ir.hasNext()) {
			MtWsDO pMTWSBo = ir.next();
			if (pMTWSBo.isProjectAvailAsJar()&&!pMTWSBo.isMaventypeClosed()) {
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
		RightTable r = RightTable.getInstance(null, null, null);
		r.getMtWsTableViewer().setInput(r.getRightTableData(pMtWsDOs));
		r.getMtWsTableViewer().refresh();
	}

	public StructuredViewer getMtWsTableViewer() {
		// TODO Auto-generated method stub
		return mtWsTableViewer;
	}

	@Override
	public MTWSComparator getComparator() {
		MTWSComparator comparator = new MTWSComparator();
		return comparator;
	}

}
