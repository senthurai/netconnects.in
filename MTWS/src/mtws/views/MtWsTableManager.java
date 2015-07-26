package mtws.views;


import java.util.TreeSet;

import mtws.data.MtWsDO;
import mtws.engine.ModelProvider;
import mtws.extract.WsTableManager;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class MtWsTableManager extends WsTableManager {
	private static MtWsTableManager sMtWsTableManager ;
	public static MtWsTableManager getInstance(Composite shell, TreeSet<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2){
		if(shell!=null && pMtWsDOs2!=null&&pModelProvider2!=null)
			sMtWsTableManager = new MtWsTableManager(shell, pMtWsDOs2, pModelProvider2);
		
		
		return sMtWsTableManager;
	}
	protected Object[][] getCOLUMNS() {
		Object[][] COLUMNS = { { "Project", SWT.LEFT, 10, 15, true, "projectName", "TextCellEditor", false },
				{ "Dependencies", SWT.LEFT, 10, 120, true, "dependencies", "TextCellEditor", false },
				{ "Dependents", SWT.LEFT, 10, 120, true, "dependent", "TextCellEditor", false },
				{ "Is .classpath editable?", SWT.CENTER, 10, 15, false, "editableClasspath", "CheckboxCellEditor", false },
				{ "Is Project Avialable? ", SWT.CENTER, 10, 10, false, "projectAvailable", "CheckboxCellEditor", false },
				{ "Is JAR avialable?", SWT.CENTER, 10, 8, false, "projectAvailAsJar", "CheckboxCellEditor", false },
				{ "Using the jar?", SWT.CENTER, 10, 6, false, "maventypeClosed", "CheckboxCellEditor", true } };
		return COLUMNS;
	}
	private TreeSet<MtWsDO> pMtWsDOs; 
	private MtWsTableManager(Composite shell, TreeSet<MtWsDO> hashSet, ModelProvider pModelProvider) {
		super(shell, hashSet,pModelProvider, SWT.MULTI | SWT.FULL_SELECTION );
	}

	void assignControls() {
		if (mtWsTableViewer != null) {
			mtWsTableViewer.setCellEditors(setCellEditors(mtWsTableViewer.getTable()));
		}
	}
	public void setModelData(Object pMtWsDOs){
		this.pMtWsDOs=(TreeSet<MtWsDO>) pMtWsDOs;
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

	

	@Override
	public Class getModelType() {

		return MtWsDO.class;
	}

	@Override
	public Object getModelData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populateAndSetData(TableViewer mtWsTableViewer) {

		pModelProvider = new ModelProvider();
		mtWsTableViewer.setInput(pModelProvider.getProjects());
	/*	pMtWsDOs = pModelProvider.getProjects();
		Iterator<MtWsDO> ir = pMtWsDOs.iterator();
		while (ir.hasNext()) {
			MtWsDO pMTWSBo = ir.next();
			mtWsTableViewer.setData(pMTWSBo.getProjectName(), pMTWSBo);
		}*/
	

	}


	@Override
	protected void doubleClickAction(Object obj[]) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setModelData(TableViewer mtWsTableViewer, Object pMtWsDOs, ModelProvider pModelProvider2) {
		this.pMtWsDOs= ((TreeSet<MtWsDO>)pMtWsDOs);
		
		mtWsTableViewer.setInput(pMtWsDOs);
	
	}
	@Override
	public MTWSComparator getComparator() {
		MTWSComparator comparator= new MTWSComparator();
		return comparator;
	}
}
