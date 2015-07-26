package mtws.extract;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import mtws.data.MtWsDO;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class WsViewLabelProvider extends ColumnLabelProvider implements IStructuredContentProvider {
	String type = "mtws.data.MtWsDO";
	Method getterMethod;
	Method setterMethod;
	CellEditor oCellEditor = null;
	private final Image CHECKED = getImage("checked.gif");
	private final Image UNCHECKED = getImage("unchecked.gif");

	protected void getGetterAndSetterMethods(String columnName) throws ClassNotFoundException {
		Class homeClass = Class.forName(getTYPE());
		Method[] methods = homeClass.getMethods();
		String tMethodName = columnName.toLowerCase();
		String name;

		for (int i = 0; i < methods.length; i++) {
			name = methods[i].getName().toLowerCase().contains(columnName.toLowerCase()) ? methods[i].getName().toLowerCase() : null;
			if (name != null && (name.contentEquals("is".concat(tMethodName)) || name.contentEquals("get".concat(tMethodName))))
				getterMethod = methods[i];
			else if (name != null && name.contentEquals("set".concat(tMethodName))) {
				setterMethod = methods[i];
			}
			if (getterMethod != null && setterMethod != null)
				break;
		}

	}

	public WsViewLabelProvider(String columnName) {
		try {
			getGetterAndSetterMethods(columnName);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
public Color getBackground(Object element) {
	MtWsDO oMtWsDO=(MtWsDO) element;
	Color c=null;
	
	try {
		Device device = null;
		RGB rgb= new RGB(Integer.parseInt(oMtWsDO.getColor().substring(0,2),16),Integer.parseInt(oMtWsDO.getColor().substring(2,4),16),Integer.parseInt(oMtWsDO.getColor().substring(4,6),16));
		 c= new Color(device, rgb);
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch (StringIndexOutOfBoundsException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return c;
			
};
	WsViewLabelProvider(String columnName, String cellEditerType) {
		try {
			getGetterAndSetterMethods(columnName);
			if (cellEditerType == "CheckboxCellEditor")
				oCellEditor = new CheckboxCellEditor();
			else if (cellEditerType == "TextCellEditor") {
				oCellEditor = new TextCellEditor();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Image getImage(String file) {

		// assume that the current class is called View.java
		Bundle bundle = FrameworkUtil.getBundle(WsViewLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}

	private String getTYPE() {
		return type;
	}

	public Object invokeGetter(Object obj) {

		try {
			Class homeClass = Class.forName(getTYPE());
			// Object objs = homeClass.newInstance();
			homeClass.cast(obj);
			Object args[] = null;
			return getterMethod.invoke(homeClass.cast(obj), args);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return "Error While Getting Value";
	}

	public String invokeSetter(Object obj, Object value) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IllegalArgumentException,
			InvocationTargetException {
		Class homeClass = Class.forName(getTYPE());
		// Object objs = homeClass.newInstance();
		obj = homeClass.cast(obj);
		Object args[] = {value};
		return String.valueOf(setterMethod.invoke(homeClass.cast(obj), args));
	}

	public String getColumnText(Object obj, int index) {
		String label = null;

		try {
			label = (String) invokeGetter(obj);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getText(label);
	}

	public Image getColumnImage(Object obj, int index) {
		return null;
	}

	public Image getImage(Object obj) {
		Object o=invokeGetter(obj);
		 if ( o instanceof Boolean) {
			 
			 if ((Boolean)o) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
		}
		return null;

	}

	public String getText(Object obj) {
		Object o=invokeGetter(obj);
		 if ( o instanceof String) 
			 return (String) o;
		return null;
	}

	String checkbox(boolean val) {
		if (val) {
			return val + "";
		} else {
			return val + "";
		}

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		arg0.refresh();
		
	}

	@Override
	public Object[] getElements(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
