package mtws.views;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class MtwsStatus extends Status {
	
	public MtwsStatus(int severity, String pluginId, String message) {
		super(severity, pluginId, message);
		// TODO Auto-generated constructor stub
	}

	IStatus[] children =null;
	public static final IStatus OK_STATUS = new Status(0, "unknown", 0, "ok", null);
	/**
	 * @param children the children to set
	 */
	public void setChildren(IStatus[] children) {
		this.children = children;
	}
	

	@Override
	public IStatus[] getChildren() {
		// TODO Auto-generated method stub
		return children;
	}

	@Override
	public int getCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Throwable getException() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage() {
		
		return "";
	}

	@Override
	public String getPlugin() {
		
		return "Savant";
	}

	@Override
	public int getSeverity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMultiStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean matches(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
