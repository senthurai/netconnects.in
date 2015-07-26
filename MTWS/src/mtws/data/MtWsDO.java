package mtws.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class MtWsDO implements Comparable<MtWsDO> {
	IFile settingFile;
	/**
	 * @return the settingFile
	 */
	IProject pIProject;
	
	long moduleId = 0l;
	String ejbJarName;
	String jarPath;
	String projectName;
	String dependencies;
	String color;
	String controlFiles;
	String scriptPath;
	String serverInstance;
	/**
	 * @return the serverInstance
	 */
	public String getServerInstance() {
		return serverInstance;
	}

	/**
	 * @param serverInstance the serverInstance to set
	 */
	public void setServerInstance(String serverInstance) {
		this.serverInstance = serverInstance;
	}

	/**
	 * @return the cleanInstallSwitch
	 */
	public boolean isCleanInstallSwitch() {
		return cleanInstallSwitch;
	}

	/**
	 * @param cleanInstallSwitch the cleanInstallSwitch to set
	 */
	public void setCleanInstallSwitch(boolean cleanInstallSwitch) {
		this.cleanInstallSwitch = cleanInstallSwitch;
	}

	String dependent;
	
	String[] DependentArr;
	String[] dependenciesArr;
	
	IFile componentXmlFile;
	IFile ApplicationXmlFile;
	IFile classPathFile;
	Properties settingProps = new Properties();
	HashSet<MtWsDO> DependentDOs = new HashSet<MtWsDO>();
	HashSet<MtWsDO> dependenciesDOs = new HashSet<MtWsDO>();
	
	boolean editableClasspath;
	boolean maventypeClosed;
	boolean projectAvailAsJar;
	boolean closed;
	boolean projectAvailable;
	boolean projectfromDependency;
	boolean updated;
	boolean editableApplicationXmlFile;
	boolean editableControlFile;
	boolean editablecomponentXmlFile;
	boolean buildSwitch;
	boolean scriptSwitch;
	boolean jarDownloadSwitch;
	boolean cleanInstallSwitch;
	/**
	 * @return the jarDownloadSwitch
	 */
	public boolean isJarDownloadSwitch() {
		return jarDownloadSwitch;
	}

	/**
	 * @param jarDownloadSwitch the jarDownloadSwitch to set
	 */
	public void setJarDownloadSwitch(boolean jarDownloadSwitch) {
		this.jarDownloadSwitch = jarDownloadSwitch;
	}

	public boolean isBuildSwitch() {
		return buildSwitch;
	}

	public void setBuildSwitch(boolean buildSwitch) {
		this.buildSwitch = buildSwitch;
	}

	public boolean isScriptSwitch() {
		return scriptSwitch;
	}

	public void setScriptSwitch(boolean scriptSwitch) {
		this.scriptSwitch = scriptSwitch;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the moduleId
	 */
	public long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId
	 *            the moduleId to set
	 */
	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the ejbJarName
	 */
	public String getEjbJarName() {
		return ejbJarName;
	}

	/**
	 * @param ejbJarName
	 *            the ejbJarName to set
	 */
	public void setEjbJarName(String ejbJarName) {
		this.ejbJarName = ejbJarName;
	}

	/**
	 * @return the editablecomponentXmlFile
	 */
	public boolean isEditablecomponentXmlFile() {
		return editablecomponentXmlFile;
	}

	/**
	 * @param editablecomponentXmlFile
	 *            the editablecomponentXmlFile to set
	 */
	public void setEditablecomponentXmlFile(boolean editablecomponentXmlFile) {
		this.editablecomponentXmlFile = editablecomponentXmlFile;
	}

	/**
	 * @return the componentXmlFile
	 */
	public IFile getComponentXmlFile() {
		return componentXmlFile;
	}

	/**
	 * @param componentXmlFile
	 *            the componentXmlFile to set
	 */
	public void setComponentXmlFile(IFile componentXmlFile) {
		this.componentXmlFile = componentXmlFile;
	}

	public IFile getApplicationXmlFile() {
		return ApplicationXmlFile;
	}

	public void setApplicationXmlFile(IFile applicationXmlFile) {
		ApplicationXmlFile = applicationXmlFile;
	}

	public boolean isEditableApplicationXmlFile() {
		return editableApplicationXmlFile;
	}

	public void setEditableApplicationXmlFile(boolean editableApplicationXmlFile) {
		this.editableApplicationXmlFile = editableApplicationXmlFile;
	}

	public String getControlFiles() {
		return controlFiles;
	}

	public void setControlFiles(String controlFiles) {
		this.controlFiles = controlFiles;
	}

	public boolean isEditableControlFile() {
		return editableControlFile;
	}

	public void setEditableControlFile(boolean editableControlFile) {
		this.editableControlFile = editableControlFile;
	}

	/**
	 * @return the projectAvailable
	 */
	public boolean isProjectAvailable() {
		return projectAvailable;
	}

	/**
	 * @param projectAvailable
	 *            the projectAvailable to set
	 */
	public void setProjectAvailable(boolean projectAvailable) {
		this.projectAvailable = projectAvailable;
	}

	/**
	 * @return the projectfromDependency
	 */
	public boolean isProjectfromDependency() {
		return projectfromDependency;
	}

	/**
	 * @param projectfromDependency
	 *            the projectfromDependency to set
	 */
	public void setProjectfromDependency(boolean projectfromDependency) {
		this.projectfromDependency = projectfromDependency;
	}

	public MtWsDO(String projectName, String dependencies, String dependent, boolean editableClasspath, boolean maventypeClosed, boolean projectAvailAsJar, boolean closed) {

		super();
		this.projectName = projectName;
		this.dependencies = dependencies;
		this.dependent = dependent;
		this.editableClasspath = editableClasspath;
		this.maventypeClosed = maventypeClosed;
		this.projectAvailAsJar = projectAvailAsJar;
		this.closed = closed;

	}

	public MtWsDO(String projectName, String[] dependenciesArr, String[] dependentArr, boolean editableClasspath, boolean maventypeClosed, boolean projectAvailAsJar, boolean closed) {
		super();
		setProjectName(projectName);
		this.dependenciesArr = dependenciesArr;
		DependentArr = dependentArr;
		this.editableClasspath = editableClasspath;
		this.maventypeClosed = maventypeClosed;
		this.projectAvailAsJar = projectAvailAsJar;
		this.closed = closed;
	}

	public MtWsDO(String projectName) {
		setProjectName(projectName);

	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	public IFile getSettingFile() {
		return settingFile;
	}

	/**
	 * @param settingFile
	 *            the settingFile to set
	 */
	public void setSettingFile(IFile settingFile) {
		this.settingFile = settingFile;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		propertyChangeSupport.firePropertyChange("projectName", this.projectName, this.projectName = projectName);
		if (projectName.endsWith("EJB"))
			this.projectName = projectName.substring(0, projectName.length() - 3);
		else
			this.projectName = projectName;
	}

	/**
	 * @return the dependencies
	 */
	public String getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies
	 *            the dependencies to set
	 */
	public void setDependencies(String dependencies) {
		propertyChangeSupport.firePropertyChange("dependencies", this.dependencies, this.dependencies = dependencies);

	}

	/**
	 * @return the dependenciesArr
	 */
	public String[] getDependenciesArr() {
		return dependenciesArr;
	}

	/**
	 * @param dependenciesArr
	 *            the dependenciesArr to set
	 */
	public void setDependenciesArr(String[] dependenciesArr) {
		setDependencies(toArrayString(dependenciesArr));
		this.dependenciesArr = dependenciesArr;
	}

	public String toArrayString(String[] strArr) {
		if (strArr != null && strArr.length > 0) {
			StringBuffer sb = new StringBuffer("[");
			for (String str : strArr) {
				sb.append(str + ",");
			}
			return sb.delete(sb.length() - 1, sb.length()).append("]").toString();
		}
		return "";
	}

	/**
	 * @return the dependent
	 */
	public String getDependent() {
		return dependent;
	}

	/**
	 * @param dependent
	 *            the dependent to set
	 */
	public void setDependent(String dependent) {
		propertyChangeSupport.firePropertyChange("dependent", this.dependent, this.dependent = dependent);

	}

	/**
	 * @return the dependentArr
	 */
	public String[] getDependentArr() {
		return DependentArr;
	}

	/**
	 * @param dependentArr
	 *            the dependentArr to set
	 */
	public void setDependentArr(String[] dependentArr) {
		setDependent(toArrayString(dependentArr));
		DependentArr = dependentArr;
	}

	/**
	 * @return the dependenciesDOs
	 */
	public HashSet<MtWsDO> getDependenciesDOs() {
		return dependenciesDOs;
	}

	/**
	 * @param dependenciesDOs
	 *            the dependenciesDOs to set
	 */
	public void setDependenciesDOs(HashSet<MtWsDO> dependenciesDOs) {
		this.dependenciesDOs = dependenciesDOs;
	}

	/**
	 * @return the dependentDOs
	 */
	public HashSet<MtWsDO> getDependentDOs() {
		return DependentDOs;
	}

	/**
	 * @param dependentDOs
	 *            the dependentDOs to set
	 */
	public void setDependentDOs(HashSet<MtWsDO> dependentDOs) {
		DependentDOs = dependentDOs;
	}

	/**
	 * @return the editableClasspath
	 */
	public boolean isEditableClasspath() {
		return editableClasspath;
	}

	/**
	 * @param editableClasspath
	 *            the editableClasspath to set
	 */
	public void setEditableClasspath(boolean editableClasspath) {
		propertyChangeSupport.firePropertyChange("editableClasspath", this.editableClasspath, this.editableClasspath = editableClasspath);
	}

	/**
	 * @return the maventypeClosed
	 */
	public boolean isMaventypeClosed() {
		return maventypeClosed;
	}

	/**
	 * @param maventypeClosed
	 *            the maventypeClosed to set
	 */
	public void setMaventypeClosed(boolean maventypeClosed) {
		propertyChangeSupport.firePropertyChange("maventypeClosed", this.maventypeClosed, this.maventypeClosed = maventypeClosed);
	}

	/**
	 * @return the projectAvailAsJar
	 */
	public boolean isProjectAvailAsJar() {
		return projectAvailAsJar;
	}

	/**
	 * @param projectAvailAsJar
	 *            the projectAvailAsJar to set
	 */
	public void setProjectAvailAsJar(boolean projectAvailAsJar) {
		this.projectAvailAsJar = projectAvailAsJar;
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(boolean closed) {
		propertyChangeSupport.firePropertyChange("closed", this.closed, this.closed = closed);

	}

	/**
	 * @return the propertyChangeSupport
	 */
	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	/**
	 * @param propertyChangeSupport
	 *            the propertyChangeSupport to set
	 */
	public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
		this.propertyChangeSupport = propertyChangeSupport;
	}

	/**
	 * @return the classPathFile
	 */
	public IFile getClassPathFile() {
		return classPathFile;
	}

	/**
	 * @param classPathFile
	 *            the classPathFile to set
	 */
	public void setClassPathFile(IFile classPathFile) {
		this.classPathFile = classPathFile;
	}

	/**
	 * @return the jarPath
	 */
	public String getJarPath() {
		return jarPath;
	}

	/**
	 * @param jarPath
	 *            the jarPath to set
	 */
	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	/**
	 * @return the settingProps
	 */
	public Properties getSettingProps() {
		return settingProps;
	}

	/**
	 * @param settingProps
	 *            the settingProps to set
	 */
	public void setSettingProps(Properties settingProps) {
		this.settingProps = settingProps;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	/**
	 * @return the pIProject
	 */
	public IProject getpIProject() {
		return pIProject;
	}

	/**
	 * @param pIProject
	 *            the pIProject to set
	 */
	public void setpIProject(IProject pIProject) {
		this.pIProject = pIProject;
	}

	public String toString() {
		return "[" + getProjectName() + ":" + Arrays.toString(getDependenciesArr()) + "; Jar?:" + isProjectAvailAsJar() + "; ";
	}

	@Override
	public int compareTo(MtWsDO o) {
		return projectName.compareToIgnoreCase(o.projectName);

	}
}