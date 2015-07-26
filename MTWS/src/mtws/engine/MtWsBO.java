package mtws.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;

import mtws.data.FTPConfig;
import mtws.data.MtWsDO;
import mtws.data.SettingPropertiesEnum;
import mtws.engine.fileEngine.ApplicationContextManipulator;
import mtws.engine.fileEngine.ClassPathManipulator;
import mtws.engine.fileEngine.FTPJarFinder;
import mtws.engine.fileEngine.SettingFileParser;
import mtws.views.MtWsTableManager;
import mtws.views.MtwsView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.ServerPreferences;

public class MtWsBO {
	private static MtWsBO fMtWsBO;
	static StringBuffer errorSB = new StringBuffer();
	static HashSet<String> repoPath = new HashSet<String>();
	static TreeMap<String, IProject> workspaceProjects;
	HashSet<String> SavantProjects = new HashSet<String>();
	HashSet<String> scriptPath = new HashSet<String>();
	static HashSet<String> projectNotAvialable = new HashSet<String>();
	private static int FTP_PROFILE_COUNT=15;
	static HashSet<IResource> resourses = new HashSet<IResource>();
	SettingFileParser pSettingFileParser = new SettingFileParser();
	ClassPathManipulator pClassPathManipulator = new ClassPathManipulator();
	ApplicationContextManipulator pApplicationContextManipulator = new ApplicationContextManipulator();
	FTPJarFinder pFTPJarFinder = FTPJarFinder.getInstance();
	static TreeSet<FTPConfig> ftpConfigs = new TreeSet<FTPConfig>();
	FTPConfig validFtpConfig=null;
	MtWsDO validFTPMtWsDO=null;
	/**
	 * @return the validFTPMtWsDO
	 */
	public MtWsDO getValidFTPMtWsDO() {
		return validFTPMtWsDO;
	}

	/**
	 * @param validFTPMtWsDO the validFTPMtWsDO to set
	 */
	public void setValidFTPMtWsDO(MtWsDO validFTPMtWsDO) {
		this.validFTPMtWsDO = validFTPMtWsDO;
	}

	HashSet<IFile> classPathfiles = new HashSet<IFile>();
	HashSet<IFile> applicationXmlFiles = new HashSet<IFile>();
	HashSet<IFile> componentXmlFiles = new HashSet<IFile>();
	
	void reset() {

		MtwsView.debug.println(new Date() + ": Resetting the config Values...");
		pSettingFileParser = new SettingFileParser();
		pClassPathManipulator = new ClassPathManipulator();
		pApplicationContextManipulator = new ApplicationContextManipulator();
		pFTPJarFinder = FTPJarFinder.getInstance();
		fMtWsBO = new MtWsBO();
		errorSB = new StringBuffer();
		repoPath = new HashSet<String>();
		workspaceProjects = new TreeMap<String, IProject>();
		SavantProjects = new HashSet<String>();
		projectNotAvialable = new HashSet<String>();
		ftpConfigs = new TreeSet<FTPConfig>();
		classPathfiles = new HashSet<IFile>();
		applicationXmlFiles = new HashSet<IFile>();
		componentXmlFiles = new HashSet<IFile>();

		MtwsView.debug.println(new Date() + ": Resetting the Config Values completed.");
	}

	static MtWsBO getInstance() {

		if (fMtWsBO == null)
			fMtWsBO = new MtWsBO();
		return fMtWsBO;
	}

	private MtWsBO() {

	}

	static IProgressMonitor fIProgressMonitor = new IProgressMonitor() {

		@Override
		public void worked(int arg0) {

		}

		@Override
		public void subTask(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setTaskName(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setCanceled(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isCanceled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void internalWorked(double arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void done() {
			// TODO Auto-generated method stub

		}

		@Override
		public void beginTask(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * @return the errorSB
	 */
	public static StringBuffer getErrorSB() {
		return errorSB;
	}

	/**
	 * @param errorSB
	 *            the errorSB to set
	 */
	public static void setErrorSB(StringBuffer errorSB) {
		MtWsBO.errorSB = errorSB;
	}

	/**
	 * @return the jarPath
	 */
	public HashSet<String> getRepoPath() {
		return repoPath;
	}

	/**
	 * @param repoPath
	 *            the jarPath to set
	 */
	public static void setRepoPath(HashSet<String> repoPath) {
		MtWsBO.repoPath = repoPath;
	}

	/**
	 * @param args
	 */
	TreeMap<String, MtWsDO> mMtWsDoMap = new TreeMap<String, MtWsDO>();
	private boolean buildSwitch;
	private boolean scriptSwitch;
	private boolean downloadSwitch;
	private boolean installSwitch;
	private String serverInstance;
	private String scriptPathStr;
	private ServerPreferences preferences;
	/**
	 * @return the installSwitch
	 */
	public boolean isInstallSwitch() {
		return installSwitch;
	}

	/**
	 * @param installSwitch the installSwitch to set
	 */
	public void setInstallSwitch(boolean installSwitch) {
		this.installSwitch = installSwitch;
	}

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

	private static HashMap<String, IServer> servers = new HashMap<String, IServer>();

	/**
	 * @return the downloadSwitch
	 */
	public boolean isDownloadSwitch() {
		return downloadSwitch;
	}

	/**
	 * @param downloadSwitch
	 *            the downloadSwitch to set
	 */
	public void setDownloadSwitch(boolean downloadSwitch) {
		this.downloadSwitch = downloadSwitch;
	}

	/**
	 * @return the mMtWsDO
	 */
	public TreeMap<String, MtWsDO> getmMtWsDO() {
		return mMtWsDoMap;
	}

	/**
	 * @param mMtWsDO
	 *            the mMtWsDO to set
	 */
	public void setmMtWsDO(TreeMap<String, MtWsDO> mMtWsDO) {
		this.mMtWsDoMap = mMtWsDO;
	}

	public static TreeMap<String, IProject> getProjects() {
		workspaceProjects = new TreeMap<String, IProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			workspaceProjects.put(project.getName(), project);
		}
		return workspaceProjects;

	}

	public static IProject getProject(String projectName) {
		if (projectName != null && !projectName.isEmpty())
			return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return null;

	}

	public static String[] getProjectsNames() {

		int i = 0;
		TreeMap<String, IProject> projMap = getProjects();
		Iterator<Entry<String, IProject>> projectSet = projMap.entrySet().iterator();

		String[] projectNames = new String[projMap.size()];
		while (projectSet.hasNext()) {
			IProject iIProject = projectSet.next().getValue();
			projectNames[i++] = iIProject.getName();
		}
		return projectNames;
	}

	public static String[] getProjectsNameReferencing(IProject iProject) throws CoreException {
		IProject[] projects = iProject.getReferencingProjects();
		String[] rProjectNames = new String[projects.length];
		int i = 0;
		for (IProject rgProject : projects) {
			rProjectNames[i++] = rgProject.getName();
		}
		MtwsView.debug.println(new Date() + ": Project - " + iProject.getName() + " were Refering  " + rProjectNames);
		return rProjectNames;
	}

	public static String[] getProjectsNameReferenced(IProject iProject) throws CoreException {
		String[] rProjectNames = null;
		if (iProject.isOpen()) {
	//		MtwsView.debug.println(new Date() + ": Accessing Project - " + iProject.getName());
			IProject[] projects = iProject.getReferencedProjects();

			rProjectNames = new String[projects.length];
			int i = 0;
			for (IProject rgProject : projects) {
				rProjectNames[i++] = rgProject.getName();
			}

		} else {
		//	MtwsView.debug.println(new Date() + ": Project - " + iProject.getName() + " is in Closed State");
			return null;
		}
		MtwsView.debug.println(new Date() + ": Project - " + iProject.getName() + " were Referenced in  " + rProjectNames);
		return rProjectNames;
	}

	// Entry Point
	public void constructDOs() {
		reset();
		Iterator<Entry<String, IProject>> projectSet = getProjects().entrySet().iterator();
		while (projectSet.hasNext()) {
			IProject iIProject = projectSet.next().getValue();
			if (mMtWsDoMap.get(iIProject.getName()) == null) {
				MtWsDO oMtWsDO = constructDO(iIProject);
				if (iIProject.getName() != null && !iIProject.getName().isEmpty())
					mMtWsDoMap.put(iIProject.getName(), oMtWsDO);
			}
		}
		populateDependenciesSet();
		populateServers();
		retrieveFtpProperties();
		populateSavantProjectFromProperties();
		refreshFiles(true);
	}

	void populateDependenciesSet() {
		Iterator<Entry<String, MtWsDO>> iMtWsDO = mMtWsDoMap.entrySet().iterator();
		while (iMtWsDO.hasNext()) {

			Entry<String, MtWsDO> setMtWsDO = (Entry<String, MtWsDO>) iMtWsDO.next();
			MtWsDO pMtWsDO = setMtWsDO.getValue();
			String[] pDependenciesArr = pMtWsDO.getDependenciesArr();

			if (pDependenciesArr != null)
				for (int i = 0; i < pDependenciesArr.length; i++) {
					pMtWsDO.getDependenciesDOs().add(mMtWsDoMap.get(pDependenciesArr[i]));
				}
			String[] pDependentArr = pMtWsDO.getDependentArr();
			if (pDependentArr != null) {
				for (int i = 0; i < pDependentArr.length; i++) {
					pMtWsDO.getDependentDOs().add(mMtWsDoMap.get(pDependentArr[i]));
				}
			}
		}
	}

	void populateSavantProjectFromProperties() {
		Iterator<Entry<String, MtWsDO>> iMtWsDO = mMtWsDoMap.entrySet().iterator();
		while (iMtWsDO.hasNext()) {

			Entry<String, MtWsDO> setMtWsDO = (Entry<String, MtWsDO>) iMtWsDO.next();
			MtWsDO pMtWsDO = setMtWsDO.getValue();
			Properties SettingProps = pMtWsDO.getSettingProps();

			SavantProjects.addAll(pSettingFileParser.getSavantProjectsSet(SettingProps.getProperty(SettingFileParser.Savant_PROJECTS)));
		}
		for (String Savant : SavantProjects) {
			if (Savant != null && !Savant.trim().isEmpty())
				mMtWsDoMap.put(Savant, constructDO(Savant));

		}
		iMtWsDO = mMtWsDoMap.entrySet().iterator();

		while (iMtWsDO.hasNext()) {
			Entry<String, MtWsDO> setMtWsDO = (Entry<String, MtWsDO>) iMtWsDO.next();
			MtWsDO pMtWsDO = setMtWsDO.getValue();
			if (pMtWsDO != null) {
				if (SavantProjects.contains(pMtWsDO.getProjectName()) && pMtWsDO.isProjectAvailAsJar()) {
					pMtWsDO.setMaventypeClosed(true);
				} else {
					pMtWsDO.setMaventypeClosed(false);
				}

				pMtWsDO.getSettingProps().setProperty(SettingFileParser.Savant_PROJECTS, pSettingFileParser.appendSavantProjectsStr(SavantProjects));
			}
		}
	}

	public void removeSavantProject(String project) {

		collectSavantProjects();
		SavantProjects.remove(project);
		preserveSavantProject();
	}

	public void removeSavantProjects(HashSet<String> project) {
		collectSavantProjects();
		SavantProjects.removeAll(project);
		preserveSavantProject();
	}

	void collectSavantProjects() {
		Iterator<Entry<String, MtWsDO>> iMtWsDO = mMtWsDoMap.entrySet().iterator();
		while (iMtWsDO.hasNext()) {

			Entry<String, MtWsDO> setMtWsDO = (Entry<String, MtWsDO>) iMtWsDO.next();
			MtWsDO pMtWsDO = setMtWsDO.getValue();
			Properties SettingProps = pMtWsDO.getSettingProps();

			SavantProjects.addAll(pSettingFileParser.getSavantProjectsSet(SettingProps.getProperty(SettingFileParser.Savant_PROJECTS)));
		}
	}

	void preserveSavantProject() {
		Iterator<Entry<String, MtWsDO>> iMtWsDO = mMtWsDoMap.entrySet().iterator();
		iMtWsDO = mMtWsDoMap.entrySet().iterator();
		while (iMtWsDO.hasNext()) {
			Entry<String, MtWsDO> setMtWsDO = (Entry<String, MtWsDO>) iMtWsDO.next();
			MtWsDO pMtWsDO = setMtWsDO.getValue();
			pSettingFileParser.appendProperties(pMtWsDO.getSettingFile(), pMtWsDO.getSettingProps(), SettingPropertiesEnum.Savant_PROJECTS,
					pSettingFileParser.appendSavantProjectsStr(SavantProjects));
			resourses.add(pMtWsDO.getSettingFile());
		}
	}

	public void addProject(MtWsDO oMtWsDO) {
		MtWsDO tMtWsDO = mMtWsDoMap.get(oMtWsDO.getProjectName());
		if (tMtWsDO == null) {
			if (oMtWsDO.getProjectName() != null && !oMtWsDO.getProjectName().isEmpty())
				mMtWsDoMap.put(oMtWsDO.getProjectName(), oMtWsDO);
		}
	}

	public MtWsDO constructDO(String iIProjectstr) {
		MtWsDO oMtWsDO = new MtWsDO(iIProjectstr);
		IProject pIProject = getProject(iIProjectstr);
		if (pIProject != null) {
			oMtWsDO.setpIProject(pIProject);
			determineProject(oMtWsDO, pIProject);
			CheckFlags(oMtWsDO, pIProject);

		} else {
			oMtWsDO.setProjectfromDependency(true);
			oMtWsDO.setClosed(true);
			oMtWsDO.setEditableClasspath(false);
			oMtWsDO.setMaventypeClosed(false);
		}
		return oMtWsDO;
	}

	MtWsDO determineProject(MtWsDO rMtWsDO, IProject iIProject) {
		MtWsDO pMtWsDO = rMtWsDO;
		IFile classPathFile = null;
		IFile applicationContextFile = null;
		IFile componentFile = null;
		if (iIProject.exists()) {
			classPathFile = iIProject.getFile(ClassPathManipulator.CLASS_PATH);
			applicationContextFile = iIProject.getFile(ApplicationContextManipulator.APPLICATION_XML_FILE);
			if (classPathFile.exists()) {
				pMtWsDO.setControlFiles(ClassPathManipulator.CLASS_PATH_FILE_NAME);
				pMtWsDO.setEditableClasspath(!classPathFile.isReadOnly());
				pMtWsDO.setClassPathFile(classPathFile);
			} else if (applicationContextFile.exists()) {
				pMtWsDO.setControlFiles(ApplicationContextManipulator.APPLICATION_XML_FILE_NAME);
				pMtWsDO.setEditableClasspath(!classPathFile.isReadOnly());
				pMtWsDO.setApplicationXmlFile(applicationContextFile);
				componentFile = iIProject.getFile(ApplicationContextManipulator.COMPONENT_XML_FILE);
				if (componentFile.exists()) {
					pMtWsDO.setComponentXmlFile(componentFile);
					pMtWsDO.setEditableApplicationXmlFile(!componentFile.isReadOnly());
				}
			}
		}
		return pMtWsDO;
	}

	void CheckFlags(MtWsDO oMtWsDO, IProject iIProject) {

		IFile settingFile = null;
		String jarPathstr = null;
		oMtWsDO.setColor("FFC1C2");
		oMtWsDO.setProjectAvailable(iIProject.exists() && iIProject.isOpen());

		if (iIProject.exists()) {
			if (iIProject.isOpen())
				oMtWsDO.setColor("FFFFFF");
			else
				oMtWsDO.setClosed(true);
			settingFile = iIProject.getFile(SettingFileParser.SETTING_FILE);
			IFolder settingFolder = iIProject.getFolder(SettingFileParser.SETTING_FOLDER);

			oMtWsDO.setSettingFile(iIProject.getFile(SettingFileParser.SETTING_FILE));
			if (settingFile.getLocation().toFile().exists()) {
				pSettingFileParser.extractProperties(settingFile, oMtWsDO.getSettingProps());
			} else {
				try {
					if (!settingFolder.exists())
						settingFolder.create(true, true, fIProgressMonitor);
					settingFile.create(null, true, fIProgressMonitor);
					resourses.add(settingFile);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pSettingFileParser.createSettingFile(settingFile, oMtWsDO.getSettingProps());
			}
			buildSwitch = buildSwitch || Boolean.valueOf((oMtWsDO.getSettingProps().getProperty(SettingFileParser.BUILD_SWITCH)));
			scriptSwitch = scriptSwitch || Boolean.valueOf((oMtWsDO.getSettingProps().getProperty(SettingFileParser.SCRIPT_SWITCH)));
			downloadSwitch = downloadSwitch || Boolean.valueOf((oMtWsDO.getSettingProps().getProperty(SettingFileParser.DOWNLOAD_SWITCH)));
			installSwitch = installSwitch || Boolean.valueOf((oMtWsDO.getSettingProps().getProperty(SettingFileParser.INSTALL_SWITCH)));
			if (oMtWsDO.getSettingProps().getProperty(SettingFileParser.SCRIPT_PATH) != null && !oMtWsDO.getSettingProps().getProperty(SettingFileParser.SCRIPT_PATH).isEmpty())
				scriptPathStr = oMtWsDO.getSettingProps().getProperty(SettingFileParser.SCRIPT_PATH);
			scriptPath.add(oMtWsDO.getSettingProps().getProperty(SettingFileParser.SCRIPT_PATH));
			if (oMtWsDO.getSettingProps().getProperty(SettingFileParser.SERVER_INSTANCE) != null && !oMtWsDO.getSettingProps().getProperty(SettingFileParser.SERVER_INSTANCE).isEmpty())
				serverInstance = oMtWsDO.getSettingProps().getProperty(SettingFileParser.SERVER_INSTANCE);
			

			jarPathstr = pSettingFileParser.getRepoPath(oMtWsDO);
			if (jarPathstr != null) {
				repoPath.addAll(SettingFileParser.splitPathArray(jarPathstr));
			}
		} else {
			projectNotAvialable.add(iIProject.getName());
		}

		jarPathstr = pClassPathManipulator.searchJars(repoPath, iIProject.getName());
		pFTPJarFinder.getJarsAvailable();
		oMtWsDO.setEjbJarName(getJarName(jarPathstr, oMtWsDO.getProjectName()));
		if (jarPathstr != null && !jarPathstr.isEmpty()) {
			oMtWsDO.setJarPath(jarPathstr);
			oMtWsDO.setProjectAvailAsJar(true);
			oMtWsDO.setMaventypeClosed(false);
			oMtWsDO.setColor("FFFFFF");
		} else if (pFTPJarFinder.getJarsAvailable().containsKey(getJarName(jarPathstr, oMtWsDO.getProjectName())))

		{

			oMtWsDO.setJarPath(pFTPJarFinder.getJarsAvailable().get(getJarName(jarPathstr, oMtWsDO.getProjectName())));
			oMtWsDO.setProjectAvailAsJar(true);
		}

		try {
			oMtWsDO.setDependenciesArr(getProjectsNameReferenced(iIProject));
			oMtWsDO.setDependentArr(getProjectsNameReferencing(iIProject));
		} catch (CoreException e) {
			errorSB.append(iIProject.getName() + "  : Project Not avialable \n ");
			e.printStackTrace();
		}

	}

	/**
	 * @return the scriptPathStr
	 */
	public String getScriptPathStr() {
		return scriptPathStr;
	}

	/**
	 * @param scriptPathStr the scriptPathStr to set
	 */
	public void setScriptPathStr(String scriptPathStr) {
		this.scriptPathStr = scriptPathStr;
	}

	/**
	 * @return the scriptPath
	 */
	public HashSet<String> getScriptPath() {
		return scriptPath;
	}

	/**
	 * @param scriptPath
	 *            the scriptPath to set
	 */
	public void setScriptPath(HashSet<String> scriptPath) {
		this.scriptPath = scriptPath;
	}

	/**
	 * @return the buildSwitch
	 */
	public boolean isBuildSwitch() {
		return buildSwitch;
	}

	/**
	 * @param buildSwitch
	 *            the buildSwitch to set
	 */
	public void setBuildSwitch(boolean buildSwitch) {
		this.buildSwitch = buildSwitch;
	}

	/**
	 * @return the scriptSwitch
	 */
	public boolean isScriptSwitch() {
		return scriptSwitch;
	}

	/**
	 * @param scriptSwitch
	 *            the scriptSwitch to set
	 */
	public void setScriptSwitch(boolean scriptSwitch) {
		this.scriptSwitch = scriptSwitch;
	}

	private String getJarName(String jarPathstr, final String projectName) {
		File f = null;
		if (jarPathstr != null && !jarPathstr.isEmpty())
			f = new File(jarPathstr);

		if (f != null && f.exists())
			return (f.getName());
		else {
			return projectName + ".";
		}

	}

	public MtWsDO constructDO(IProject iIProject) {
		MtWsDO oMtWsDO = new MtWsDO(iIProject.getName());
		oMtWsDO.setpIProject(iIProject);
		oMtWsDO = determineProject(oMtWsDO, iIProject);
		CheckFlags(oMtWsDO, iIProject);
		if (oMtWsDO.getDependenciesArr() != null)
			for (String prt : oMtWsDO.getDependenciesArr()) {
				if (!workspaceProjects.containsKey(prt))
					addProject(constructDO(getProjectName(prt)));
			}
		if (oMtWsDO.getDependentArr() != null)

			for (String prt : oMtWsDO.getDependentArr()) {
				if (!workspaceProjects.containsKey(prt))
					addProject(constructDO(prt));
			}
		return oMtWsDO;
	}

	String buildProjects(IProgressMonitor arg0) {

		doClean(true, null);
		MtwsView.debug.println(new Date() + " :Build Started...");
		// monitor.beginTask("Building Workspace", 100);
		try {
			arg0.beginTask("Building Workspace", 100);
			ResourcesPlugin.getWorkspace().build(10, new SubProgressMonitor(arg0, 100));
		} catch (CoreException e) {
			IStatus localIStatus = e.getStatus();
			// return localIStatus;
		} finally {
			arg0.done();
			MtwsView.debug.println(new Date() + " :Build Completed");
		}
		/*
		 * Job buildJob = new
		 * Job(IDEWorkbenchMessages.GlobalBuildAction_jobTitle) {
		 * 
		 * 
		 * protected IStatus run(IProgressMonitor monitor) {
		 * MtwsView.debug.println(new Date() + " :Build Started...");
		 * doClean(true, monitor); monitor.beginTask("Building Workspace", 100);
		 * try {
		 * 
		 * ResourcesPlugin.getWorkspace().build(10, new
		 * SubProgressMonitor(monitor, 100)); } catch (CoreException e) {
		 * IStatus localIStatus = e.getStatus();return localIStatus; } finally {
		 * monitor.done(); MtwsView.debug.println(new Date() +
		 * " :Build Completed"); } return Status.OK_STATUS; } public boolean
		 * belongsTo(Object family) { return ResourcesPlugin.FAMILY_MANUAL_BUILD
		 * == family; } }; buildJob.setUser(true);
		 * buildJob.setPriority(WorkspaceJob.BUILD); buildJob.schedule();
		 */

		/*
		 * Display.getDefault().asyncExec(new Runnable() {
		 * 
		 * public void run() { IWorkbench wb = PlatformUI.getWorkbench();
		 * IWorkbenchWindow wins = wb.getActiveWorkbenchWindow(); WorkspaceJob
		 * cleanJob = new
		 * WorkspaceJob(IDEWorkbenchMessages.CleanDialog_cleanAllTaskName) {
		 * 
		 * public boolean belongsTo(Object family) { return
		 * ResourcesPlugin.FAMILY_MANUAL_BUILD.equals(family); }
		 * 
		 * IWorkbench wb = PlatformUI.getWorkbench(); IWorkbenchWindow wbw =
		 * wb.getActiveWorkbenchWindow(); IPartService ps =
		 * wbw.getPartService();
		 * 
		 * public IStatus runInWorkspace(IProgressMonitor monitor) throws
		 * CoreException { doClean(true, monitor);
		 * 
		 * GlobalBuildAction build = new GlobalBuildAction(wbw, 10);
		 * 
		 * build.doBuild();
		 * 
		 * return Status.OK_STATUS; } };
		 * cleanJob.setPriority(WorkspaceJob.BUILD);
		 * cleanJob.setRule(ResourcesPlugin
		 * .getWorkspace().getRuleFactory().buildRule());
		 * cleanJob.setUser(true); cleanJob.schedule(); } });
		 */

		return "";
	}

	void cleanInstall(IProgressMonitor arg0, final IServer server, Boolean sync) {
		
		String s = "";
		Job publish = null;
		boolean notTriggered=true;
		if (server.getServerState() != 2) {
			MtwsView.debug.println(new Date() + " :Starting the Server in Debug Mode...");
			arg0.beginTask("Starting the Server in Debug Mode...", 100);
			try {
				notTriggered=false;
				server.start("debug", arg0);
				MtwsView.debug.println(new Date() + " : Success: Server Started in Debug Mode successfully...");
				MtwsView.debug.println(new Date() + " :Module Installation Started...");
				arg0.beginTask("Deploying to the Server...", 100);
				server.publish(4, arg0);
				MtwsView.debug.println(new Date() + " :Installation Done");
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + " : Failed : Starting the Server. ");
				e.printStackTrace();
			}
		}else	if (server.getServerState() != 4 && notTriggered) {
		
			if (server != null)
				try {
					MtwsView.debug.println(new Date() + " :Module Installation Started...");
					arg0.beginTask("Deploying to the Server...", 100);
					server.publish(4, arg0);
					MtwsView.debug.println(new Date() + " :Installation Done");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					arg0.done();
					
				}
			
			/*
			 * publish = new Job("Publishing...") {
			 * 
			 * @Override protected IStatus run(IProgressMonitor arg0) {
			 * MtwsView.debug.println(new Date() + " :Install Started..."); if
			 * (server != null) try {
			 * 
			 * server.publish(4, null); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * MtwsView.debug.println(new Date() + " :Installation Done");
			 * return null; } }; publish.setPriority(Job.DECORATE);
			 * publish.schedule();
			 */
		}
		sync = false;
	}

	public HashMap<String, IServer> getServers() {

		return servers;

	}

	void populateServers() {
		MtwsView.debug.println(new Date() + " : Listing Server List");
		for (IServer s : ServerCore.getServers()) {
			servers.put(s.getName(), s);
			MtwsView.debug.println("Server : " + s.getName());
		}
	}

	protected void doClean(boolean cleanAll, IProgressMonitor monitor) {
		MtwsView.debug.println(new Date() + " : Cleaning Workspace...");
		try {
			ResourcesPlugin.getWorkspace().build(15, monitor);
		} catch (CoreException e) {
			MtwsView.debug.println(new Date() + " : Error While Building the Workspace.");
			e.printStackTrace();
		}
		MtwsView.debug.println(new Date() + " : Cleaning Completed.");
	}

	private String getProjectName(String reference) {
		if (reference.endsWith("EJB"))
			return reference.substring(0, reference.length() - 3);
		else
			return reference;
	}

	public boolean updateRepoPathFromDialog(MtWsDO temp, String text) {

		String jarPath = "";
		if ((jarPath = SettingFileParser.validatePath(text)) != null && !jarPath.isEmpty())
			try {
				MtwsView.debug.println(new Date() + " : updating JAR Repo path to setting files started...");
				repoPath.clear();
				repoPath.add(jarPath);
				pSettingFileParser.updateRepoPath(temp, text);
				if (temp.getSettingFile() != null)
					temp.getSettingFile().refreshLocal(IResource.DEPTH_ONE, fIProgressMonitor);
				MtwsView.debug.println(new Date() + " : updating JAR Repo path to setting files Completed.");
				return true;
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		else
			return false;
	}

	public void updateProjectReferences(IProgressMonitor arg0, TreeMap<MtWsDO, Boolean> hashMap, boolean buildSwitch, boolean scriptSwitch, String scriptPath,
			boolean jarDownloadSwitch, boolean cleanInstallSwitch, String serverInstance) {
		MtwsView.debug.println(new Date() + " : updating Project References...");
		boolean refreshWorkspace = false;

		TreeSet<String> projectJars = new TreeSet<String>();

		Properties props = null;

		HashMap<String, String> projectJarPathMap = new HashMap<String, String>();
		Iterator<Entry<MtWsDO, Boolean>> ihashMap = hashMap.entrySet().iterator();

		while (ihashMap.hasNext()) {
			Entry<MtWsDO, Boolean> keySet = ihashMap.next();
			MtWsDO pMtWsDO = keySet.getKey();
			String projPath = "";
			pMtWsDO.setBuildSwitch(buildSwitch);
			pMtWsDO.setScriptPath(scriptPath);
			pMtWsDO.setScriptSwitch(scriptSwitch);
			pMtWsDO.setJarDownloadSwitch(jarDownloadSwitch);
			if (keySet.getValue() && pMtWsDO.getJarPath() != null && pMtWsDO.getProjectName() != null && !pMtWsDO.getProjectName().isEmpty()) {

				if (pMtWsDO.getpIProject() != null && pMtWsDO.getpIProject().exists() && pMtWsDO.getpIProject().isOpen()) {

					try {
						projPath = pMtWsDO.getpIProject().getFullPath().toString();
						pMtWsDO.getpIProject().close(arg0);
						refreshWorkspace = true;
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (pMtWsDO.getpIProject() != null && pMtWsDO.getpIProject().exists())
					projPath = pMtWsDO.getpIProject().getFullPath().toString();
				if (projPath == null || projPath == "") {
					projPath = "/" + pMtWsDO.getProjectName();
				}
				SavantProjects.add(pMtWsDO.getProjectName());
				projectJars.add(pMtWsDO.getEjbJarName());
				projectJarPathMap.put(projPath, pMtWsDO.getJarPath());
			} else {
				if (pMtWsDO.getpIProject() != null && pMtWsDO.getpIProject().exists()) {
					projPath = pMtWsDO.getpIProject().getFullPath().toString();
					props = pMtWsDO.getSettingProps();
				}

				if (projPath == null || projPath.isEmpty()) {
					projPath = "/" + pMtWsDO.getProjectName();
				}
				if (pMtWsDO.isProjectAvailAsJar()) {
					projectJarPathMap.put(pMtWsDO.getJarPath(), projPath);
				}

				try {
					if (pMtWsDO.getpIProject() != null && pMtWsDO.getpIProject().exists() && !pMtWsDO.getpIProject().isOpen() && SavantProjects.contains(pMtWsDO.getProjectName())) {
						pMtWsDO.getpIProject().open(fIProgressMonitor);
						refreshWorkspace = true;
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
				SavantProjects.remove(pMtWsDO.getProjectName());
			}
			for (MtWsDO prj : pMtWsDO.getDependenciesDOs()) {
				if (prj != null && prj.getClassPathFile() != null)
					classPathfiles.add(prj.getClassPathFile());
			}
			for (MtWsDO prj : pMtWsDO.getDependentDOs()) {
				if (prj != null && prj.getClassPathFile() != null)
					classPathfiles.add(prj.getClassPathFile());
			}

		}
		if (jarDownloadSwitch) {
			pFTPJarFinder.replicate(props, projectJars, true,arg0);
		}

		Iterator<Entry<String, MtWsDO>> iMtWsDO = mMtWsDoMap.entrySet().iterator();

		while (iMtWsDO.hasNext()) {

			Entry<String, MtWsDO> setMtWsDO = (Entry<String, MtWsDO>) iMtWsDO.next();
			MtWsDO pMtWsDO = setMtWsDO.getValue();
			if (pMtWsDO.getClassPathFile() != null)
				classPathfiles.add(pMtWsDO.getClassPathFile());

			if (pMtWsDO.getApplicationXmlFile() != null)
				applicationXmlFiles.add(pMtWsDO.getApplicationXmlFile());

			if (pMtWsDO.getComponentXmlFile() != null)
				componentXmlFiles.add(pMtWsDO.getComponentXmlFile());
		}

		for (IFile classPathFile : classPathfiles) {
			pClassPathManipulator.updateResetJarReference(classPathFile, projectJarPathMap);
			pClassPathManipulator.updateJarReference(classPathFile, projectJarPathMap);
		}
		/*
		 * for (IFile applicationXmlfile : applicationXmlFiles) {
		 * pApplicationContextManipulator.updateEARReference(applicationXmlfile,
		 * projectJarPathMap); }
		 */
		for (IFile componentXmlfile : componentXmlFiles) {
			pApplicationContextManipulator.updatecomponentReference(componentXmlfile, projectJarPathMap);
		}

		SettingPropertiesEnum KeyArr[] = { SettingPropertiesEnum.Savant_PROJECTS, SettingPropertiesEnum.BUILD_SWITCH, SettingPropertiesEnum.SCRIPT_SWITCH,
				SettingPropertiesEnum.SCRIPT_PATH, SettingPropertiesEnum.DOWNLOAD_SWITCH, SettingPropertiesEnum.INSTALL_SWITCH, SettingPropertiesEnum.SERVER_INSTANCE };
		String valueArr[] = { pSettingFileParser.appendSavantProjectsStr(SavantProjects), String.valueOf(buildSwitch), String.valueOf(scriptSwitch), scriptPath,
				String.valueOf(jarDownloadSwitch), String.valueOf(cleanInstallSwitch), serverInstance };
		preserveSettingProperties(mMtWsDoMap, KeyArr, valueArr);
		Boolean sync = true;
		synchronized (sync) {
			sync = false;
			if (scriptSwitch && scriptPath != null && !scriptPath.isEmpty()) {
				try {
					Process p = Runtime.getRuntime().exec(scriptPath);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = null;
					while ((line = in.readLine()) != null) {
						MtwsView.debug.println(line);
					}
					in.close();
				} catch (IOException e) {
					sync = false;
					e.printStackTrace();
				}
			}
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					try {

						MtWsTableManager.getInstance(null, null, null).getViewer().refresh();
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			});

			refreshFiles(refreshWorkspace);
			sync = false;
		}
		arg0.done();
		if (buildSwitch) {

			sync = false;
			buildProjects(arg0);
			sync = true;

		}
		if (cleanInstallSwitch) {
			synchronized (sync) {
				sync = false;
				cleanInstall(arg0, servers.get(serverInstance), sync);
				sync = true;
			}
		}
		MtwsView.debug.println(new Date() + " :completed updating Project References.");
	}

	public TreeMap<MtWsDO, Boolean> getSavanTypeClosable(TreeSet<MtWsDO> rMtWsDO, boolean shouldClosed) {
		TreeMap<MtWsDO, Boolean> closableMtWsDO = new TreeMap<MtWsDO, Boolean>();

		for (MtWsDO iMtWsDO : rMtWsDO) {
			if (iMtWsDO.isProjectAvailAsJar() && shouldClosed && iMtWsDO.isMaventypeClosed()) {
				closableMtWsDO.put(iMtWsDO, true);
			} else {
				closableMtWsDO.put(iMtWsDO, false);
			}

		}

		return closableMtWsDO;
	}

	void preserveSettingProperties(TreeSet<MtWsDO> sMtWsDO, SettingPropertiesEnum properties, String value) {
		for (MtWsDO iMtWsDO : sMtWsDO) {
			MtwsView.debug.println(new Date() + " : updating setting file in " + iMtWsDO.getProjectName() + "...");
			pSettingFileParser.appendProperties(iMtWsDO.getSettingFile(), iMtWsDO.getSettingProps(), properties, value);
			resourses.add(iMtWsDO.getSettingFile());
		}
	}

	void preserveSettingProperties(TreeMap<String, MtWsDO> sMtWsDO, SettingPropertiesEnum properties[], String value[]) {
		Iterator<Entry<String, MtWsDO>> iMtWsDO = mMtWsDoMap.entrySet().iterator();
		while (iMtWsDO.hasNext()) {
			MtWsDO pMtWsDO = iMtWsDO.next().getValue();
			for (int i = 0; i < properties.length; i++)
				pSettingFileParser.appendProperties(pMtWsDO.getSettingFile(), pMtWsDO.getSettingProps(), properties[i], value[i]);
			pSettingFileParser.writeSettingFile(pMtWsDO.getSettingFile(), pMtWsDO.getSettingProps());
			resourses.add(pMtWsDO.getSettingFile());
		}
	}

	public void updateFtpProperties(FTPConfig ftpConfig) {
		MtwsView.debug.println(new Date() + " : updating FTP Details ...");
		Properties props = null;
		addToFtpConfigs(ftpConfig);
		Iterator<Entry<String, MtWsDO>> iMtWsDOs = mMtWsDoMap.entrySet().iterator();
		TreeSet<String> projectJars = new TreeSet<String>();
		while (iMtWsDOs.hasNext()) {
			MtWsDO iMtWsDO = iMtWsDOs.next().getValue();
			props = iMtWsDO.getSettingProps();
			pSettingFileParser.updateFtpProperties(ftpConfig, props);
			pSettingFileParser.updateFtpSet(ftpConfigs, props);
			pSettingFileParser.writeSettingFile(iMtWsDO.getSettingFile(), props);
			projectJars.add(iMtWsDO.getEjbJarName());
			resourses.add(iMtWsDO.getSettingFile());

		}
		pFTPJarFinder.replicate(props, projectJars, false,null);
		while (iMtWsDOs.hasNext()) {
			MtWsDO iMtWsDO = iMtWsDOs.next().getValue();
			iMtWsDO.setProjectAvailAsJar(pFTPJarFinder.getJarsAvailable().containsKey(iMtWsDO.getEjbJarName()));
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					try {
						MtWsTableManager.getInstance(null, null, null).getViewer().refresh();
						MtwsView.getInstance().getViewer().setInput(MtwsView.getInstance().populateData());
						MtwsView.getInstance().getViewer().refresh();
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			});
		}
		MtwsView.debug.println(new Date() + " : Done with updating FTP Details.");
	}

	private boolean addToFtpConfigs(FTPConfig ftpConfig) {
		boolean ret=false;
		if (ftpConfigs.size() < FTP_PROFILE_COUNT) {
			if (ftpConfigs.contains(ftpConfig)) {
				ftpConfigs.remove(ftpConfig);
			}
			if (ftpConfig.getServerName() != null && !ftpConfig.getServerName().isEmpty()) {
				ftpConfigs.add(ftpConfig);
				validFtpConfig = ftpConfig;
				ret=true;
			}
		}
		return ret;
	}
	/**
	 * @return the validFtpConfig
	 */
	public FTPConfig getValidFtpConfig() {
		return validFtpConfig;
	}

	/**
	 * @param validFtpConfig the validFtpConfig to set
	 */
	public void setValidFtpConfig(FTPConfig validFtpConfig) {
		this.validFtpConfig = validFtpConfig;
	}

	private void addToFtpConfigs(TreeSet<FTPConfig> pftpConfigs) {
		if(ftpConfigs.size()<FTP_PROFILE_COUNT){
			for(FTPConfig i:pftpConfigs){
				addToFtpConfigs(i);
			}
		}
			
		
	}
	void refreshFiles(boolean refreshWorkspace) {

		if (refreshWorkspace) {

			for (IResource i : classPathfiles) {
				try {
					if (i != null)
						i.refreshLocal(IResource.PROJECT, fIProgressMonitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		for (IResource i : resourses)
			try {
				if (i != null)
					i.refreshLocal(IResource.PROJECT, fIProgressMonitor);

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * @return the ftpConfigs
	 */
	public static TreeSet<FTPConfig> getFtpConfigs() {
		return ftpConfigs;
	}

	/**
	 * @param ftpConfigs
	 *            the ftpConfigs to set
	 */
	public static void setFtpConfigs(TreeSet<FTPConfig> ftpConfigs) {
		MtWsBO.ftpConfigs = ftpConfigs;
	}

	public FTPConfig retrieveFtpProperties() {
		TreeSet<FTPConfig> ftpConfigSet = null;
		FTPConfig ftpConfig = null;
		FTPConfig returnftpConfig = null;
		Iterator<Entry<String, MtWsDO>> iMtWsDOs = mMtWsDoMap.entrySet().iterator();
		while (iMtWsDOs.hasNext()) {
			MtWsDO iMtWsDO = iMtWsDOs.next().getValue();
			ftpConfigSet = pSettingFileParser.getFtpSet(iMtWsDO.getSettingProps());
			ftpConfig = pSettingFileParser.getFtpProperties(iMtWsDO.getSettingProps());
			if (ftpConfigs != null) {
				if (ftpConfig != null && addToFtpConfigs(ftpConfig)) {
					returnftpConfig = ftpConfig;
					validFTPMtWsDO=iMtWsDO;
				}
				if (ftpConfigSet != null) {
					addToFtpConfigs(ftpConfigSet);
				}

			}

		}
		return returnftpConfig;
	}

}
