package mtws.views;

import mtws.data.MtWsDO;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class MTWSComparator extends ViewerComparator  {
	private int propertyIndex;
	private static final int DESCENDING = 0;
	private static final int ASCENDING = 1;
	private int direction = DESCENDING;

	public MTWSComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		
		return direction == ASCENDING ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = ASCENDING;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		MtWsDO p1 = (MtWsDO) e1;
		MtWsDO p2 = (MtWsDO) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			rc = p1.getProjectName().toLowerCase().compareTo(p2.getProjectName().toLowerCase());
			break;
		case 1:
			rc = p1.getDependencies().compareTo(p2.getDependencies());
			break;
		case 2:
			rc = p1.getDependent().compareTo(p2.getDependent());
			break;
		case 3:
			if (p1.isProjectAvailable() == p2.isProjectAvailable()) {
				rc = 0;
			} else
				rc = (p1.isProjectAvailable() ? 1 : -1);
			break;
		case 4:
			if (p1.isEditableClasspath() == p2.isEditableClasspath()) {
				rc = 0;
			} else
				rc = (p1.isEditableClasspath() ? 1 : -1);
			break;
		case 5:
			if (p1.isProjectAvailAsJar() == p2.isProjectAvailAsJar()) {
				rc = 0;
			} else
				rc = (p1.isProjectAvailAsJar() ? 1 : -1);
			break;
		case 6:
			if (p1.isMaventypeClosed() == p2.isMaventypeClosed()) {
				rc = 0;
			} else
				rc = (p1.isMaventypeClosed() ? 1 : -1);
			break;
		default:
			rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

}
