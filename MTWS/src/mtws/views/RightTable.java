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

public class RightTable extends WsTableManager {

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
	 * @return the fRightTable
	 */
	public static WsTableManager getfRightTable() {
		return fRightTable;
	}

	/**
	 * @param fRightTable the fRightTable to set
	 */
	public static void setfRightTable(RightTable fRightTable) {
		RightTable.fRightTable = fRightTable;
	}

	/**
	 * @return the columns
	 */
	public static Object[][] getColumns() {
		return COLUMNS;
	}

	private final static Object[][] COLUMNS = { { "Project to be referenced", SWT.LEFT, 10, 15, false, "projectName", "TextCellEditor", false } };


	private  TreeSet<MtWsDO> pMtWsDOs;
	private static RightTable fRightTable ;
	public static RightTable getInstance(Composite shell, Set<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2){
		if(shell!=null && pMtWsDOs2!=null&&pModelProvider2!=null)
			fRightTable = new RightTable(shell, pMtWsDOs2, pModelProvider2);
		
		
		return fRightTable;
	}

	private RightTable(Composite shell, Set<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
		super(shell, pMtWsDOs2, pModelProvider2, SWT.MULTI | SWT.FULL_SELECTION | SWT.HIDE_SELECTION|SWT.MULTI );
	}

	void assignControls() {
		if (mtWsTableViewer != null) {
			mtWsTableViewer.setCellEditors(setCellEditors(mtWsTableViewer.getTable()));

		}
	}
	
	public  void setModelData(TableViewer mtWsTableViewer,Object pMtWsDOs,ModelProvider pModelProvider2){
		this.pMtWsDOs= (TreeSet<MtWsDO>) pMtWsDOs;
		this.pModelProvider=pModelProvider2;
		mtWsTableViewer.setInput(getRightTableData((TreeSet<MtWsDO>)pMtWsDOs));
	}
	
	@Override
	public void populateAndSetData(TableViewer mtWsTableViewer) {
		pModelProvider = new ModelProvider();
		mtWsTableViewer.setInput(getRightTableData(pModelProvider.getProjects()));

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
		return COLUMNS;
	}

	@Override
	public Class getModelType() {
		return MtWsDO.class;
	}

	@Override
	public Object getModelData() {

		return pMtWsDOs;
	}

	
	@Override
	protected void doubleClickAction(Object objs[]) {

		for (Object Temp: objs) {
			MtWsDO pMtWsDO = (MtWsDO) Temp;
			pMtWsDO.setMaventypeClosed(false);
			pMtWsDOs.add(pMtWsDO);
		}
	//	pModelProvider.getpMtWsBO().removeSavantProject(pMtWsDO.getProjectName());
		mtWsTableViewer.setInput(getRightTableData(pMtWsDOs));
		mtWsTableViewer.refresh();
		LeftTable l =LeftTable.getInstance(null,null,null);
		 l.getMtWsTableViewer().setInput( l.getLeftTableData(pMtWsDOs));
		 l.getMtWsTableViewer().refresh();
	}

	public StructuredViewer getMtWsTableViewer() {
		// TODO Auto-generated method stub
		return mtWsTableViewer;
	}

	@Override
	public MTWSComparator getComparator() {
		MTWSComparator comparator= new MTWSComparator();
		return comparator;
	}

}
