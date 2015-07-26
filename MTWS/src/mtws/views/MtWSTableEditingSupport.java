package mtws.views;

import java.lang.reflect.InvocationTargetException;

import mtws.data.MtWsDO;
import mtws.extract.WsViewLabelProvider;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

public class MtWSTableEditingSupport extends EditingSupport {

	MtWsDO oMTWSBo = null;
	WsViewLabelProvider oMtWsViewLabelProvider = null;
	String columnName;
	CellEditor oCellEditor = null;
	boolean canEdit = false;

	public MtWSTableEditingSupport(final ColumnViewer viewer, String columnName, String pCellEditor, boolean canEdit) {
		super(viewer);
		this.columnName = columnName;
		this.canEdit = canEdit;
		try {
			oCellEditor = (CellEditor) Class.forName("org.eclipse.jface.viewers." + pCellEditor).newInstance();
			// Control control=oCellEditor.getControl();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		oMtWsViewLabelProvider = new WsViewLabelProvider(columnName);

	}

	@Override
	protected void setValue(Object arg0, Object arg1) {
		try {
			oMtWsViewLabelProvider.invokeSetter(arg0, arg1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Object getValue(Object arg0) {

		return oMtWsViewLabelProvider.invokeGetter(arg0);
	}

	@Override
	public CellEditor getCellEditor(Object arg0) {

		// TODO Auto-generated method stub
		return oCellEditor;
	}

	@Override
	protected boolean canEdit(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}