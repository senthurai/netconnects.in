package mtws.views;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import mtws.data.MtWsDO;
import mtws.engine.ModelProvider;
import mtws.engine.fileEngine.FTPJarFinder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class MtwsView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "mtws.views.MtwsView";

	public static MessageConsoleStream debug = MtwsView.findConsole("Savan").newMessageStream();
	
	private TableViewer viewer;
	/**
	 * @return the viewer
	 */
	public TableViewer getViewer() {
		return viewer;
	}
 


	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

	private Action mtWsSettings; // @jve:decl-index=0:
	private Action refresh;
	private Action ftpConfig;
	private Action doubleClickAction;

	private Composite shell;
	private TreeSet<MtWsDO> pMtWsDOs;
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
	 * @return the pModelProvider
	 */
	public ModelProvider getpModelProvider() {
		return pModelProvider;
	}



	/**
	 * @param pModelProvider the pModelProvider to set
	 */
	public void setpModelProvider(ModelProvider pModelProvider) {
		this.pModelProvider = pModelProvider;
	}

	private ModelProvider pModelProvider;
	private FTPDialog ftp;

	private Action downloadJar;

	private Action patch;
	private static MtwsView mtwsView = new  MtwsView();
	
	public static MtwsView getInstance(){
		
			
		return mtwsView;
	}
	
	
	
	public static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return ((MessageConsole) existing[i]);
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });

		return myConsole;

	}

	class mtwsViewDO {
		String project;

		/**
		 * @return the project
		 */
		public String getProject() {
			return project;
		}

		/**
		 * @param project
		 *            the project to set
		 */
		public void setProject(String project) {
			this.project = project;
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
			this.dependencies = dependencies;
		}

		String dependencies;
	}

	class NameSorter extends ViewerSorter {
	}

	public TreeSet<MtWsDO> populateData() {

		pModelProvider = new ModelProvider();
		return pMtWsDOs = pModelProvider.getProjects();

	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite shell) {
		try {
			debug.write(new Date() + ": Savan UI initialized\n");
			this.shell = shell;
			shell.setSize(360, 400);
			shell.setLayout(new FillLayout());

			viewer = MtWsTableManager.getInstance(shell, populateData(), pModelProvider).getViewer();
			
			constructSettings();
			constructRefresh();
			constructFtpSetting();
			constructDownloadJar();
			constructPatch();
			// table= viewer.getTable();
			// getSite().setSelectionProvider(viewer);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "MTWS.viewer");

			hookContextMenu();
			contributeToActionBars();
			debug.write(new Date() + ": Savan UI Finialized\n");
			mtwsView=this;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void constructFtpSetting() throws IOException {
		debug.write(new Date() + ": Savan UI - Contructing FTP Dialag\n");
		ftpConfig = new Action("Settings") {
			public void run() {
				FTPDialog ftp = new FTPDialog(null, pMtWsDOs, pModelProvider);
				ftp.open();
			}
		};
		ftpConfig.setText("Settings");
		ftpConfig.setToolTipText("Settings");
		ftpConfig.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DEF_PERSPECTIVE));
		debug.write(new Date() + ":Savan UI - Contructing FTP Dialag\n");

	}

	private void constructSettings() {
		mtWsSettings = new Action() {
			public void run() {
				ConfigDialog c = new ConfigDialog(null, pMtWsDOs, pModelProvider);
				c.open();
			}
		};
		mtWsSettings.setText("Add Project Reference");
		mtWsSettings.setToolTipText("Add Project Reference");
		mtWsSettings.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MtwsView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(mtWsSettings);
		manager.add(refresh);
		manager.add(new Separator());

		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(mtWsSettings);
		manager.add(refresh);
		manager.add(downloadJar);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		
		manager.add(patch);
		manager.add(new Separator());
		manager.add(ftpConfig);
		manager.add(downloadJar);
		manager.add(new Separator());
		manager.add(mtWsSettings);
		manager.add(refresh);

	}

	public void dispose() {

		super.dispose();
	}

	private void constructRefresh() {
		refresh = new Action("Refresh MtWs") {
			public void run() {
				viewer.setInput(populateData());
				viewer.refresh();
			}
		};
		
		refresh.setText("Refresh");
		refresh.setToolTipText("Refresh");
		refresh.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
	}

	private void constructPatch() {
		patch = new Action("Workspace Patch") {
			public void run() {
				//PatchDialog c = new PatchDialog(null);
				//c.open();
			}
		};

		patch.setText("Workspace Patch");
		patch.setToolTipText("Workspace Patch");
		patch.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
	}
	private void constructDownloadJar() {
		downloadJar = new Action("DownloadJars") {
			
			public void run() {
				TreeSet<MtWsDO> selectedDOs = new TreeSet<MtWsDO>();
				StructuredSelection ss = null;
				ISelection s = viewer.getSelection();
				if (s != null && !s.isEmpty()) {
					ss = (StructuredSelection) s;

					Iterator<MtWsDO> x = ss.iterator();
					while (x.hasNext())
						selectedDOs.add(x.next());
				} else {
					selectedDOs = pMtWsDOs;
				}
				final TreeSet<String> downLoadJars = new TreeSet<String>();
				for (MtWsDO prj : selectedDOs) {
					if (prj.isMaventypeClosed())
						downLoadJars.add(prj.getEjbJarName());
				}
				Job downloadJars = null;

				downloadJars = new Job("Downloading " + downLoadJars.toString()) {

					@Override
					protected IStatus run(IProgressMonitor arg0) {
						if (downLoadJars.size() > 0) {

							FTPJarFinder.getInstance().replicate(pModelProvider.getpMtWsBO().getValidFTPMtWsDO().getSettingProps(), downLoadJars, true, arg0);

						}
						return Status.OK_STATUS;
					}

				};
				downloadJars.schedule();

			}
		};
		
		downloadJar.setText("Download");
		downloadJar.setToolTipText("Download");
		downloadJar.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_DND_BOTTOM_SOURCE));
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Savan", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}

class RefreshThread extends Job {

	ModelProvider pModelProvider;
	TreeSet<MtWsDO> pMtWsDOs;

	public RefreshThread(ModelProvider pModelProvider, TreeSet<MtWsDO> pMtWsDOs) {
		super("MTWS Updating References");
		this.pModelProvider = pModelProvider;
		this.pMtWsDOs = pMtWsDOs;
		s = new MtwsStatus(1,"MTWS","Done");
	}

	IStatus s = null;

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		pModelProvider = new ModelProvider();
		pModelProvider.getProjects();
		return s;
	}
}