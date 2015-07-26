package mtws.views;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import mtws.data.MtWsDO;
import mtws.engine.ModelProvider;
import mtws.extract.WsTableManager;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServer;

public class ConfigDialog extends TitleAreaDialog {

	private static final String NOT_VALID_JAR_PATH = "Jar Path is Not Valid :";
	private Text jarPathText;
	private String jarPathStr;
	private ModelProvider pModelProvider;
	private LeftTable leftTable;
	private WsTableManager rightTable;
	private Text scriptPath;
	private Button executeCheck;
	private Button buildCheck;
	private String scriptPathStr;
	private Button downloadJarsCheck;
	private static TreeSet<MtWsDO> pMtWsDOs;
	private static TableViewer lsTableViewer = null;
	private static TableViewer rsTableViewer = null;
	private boolean executeSwitch;
	private boolean buildSwitch;
	private boolean downloadJarsSwitch;
	private Combo serverCombo;
	private Button serverCleanCheck;
	private String tempJarPath;

	/**
	 * @return the scriptPath
	 */
	public Text getScriptPath() {
		return scriptPath;
	}

	/**
	 * @param scriptPath
	 *            the scriptPath to set
	 */
	public void setScriptPath(Text scriptPath) {
		this.scriptPath = scriptPath;
	}

	/**
	 * @return the executeCheck
	 */
	public Button getExecuteCheck() {
		return executeCheck;
	}

	/**
	 * @param executeCheck
	 *            the executeCheck to set
	 */
	public void setExecuteCheck(Button executeCheck) {
		this.executeCheck = executeCheck;
	}

	/**
	 * @return the buildCheck
	 */
	public Button getBuildCheck() {
		return buildCheck;
	}

	/**
	 * @param buildCheck
	 *            the buildCheck to set
	 */
	public void setBuildCheck(Button buildCheck) {
		this.buildCheck = buildCheck;
	}

	/**
	 * @return the scriptPathStr
	 */
	public String getScriptPathStr() {
		return scriptPathStr;
	}

	/**
	 * @param scriptPathStr
	 *            the scriptPathStr to set
	 */
	public void setScriptPathStr(String scriptPathStr) {
		this.scriptPathStr = scriptPathStr;
	}

	/**
	 * @return the downloadJarsCheck
	 */
	public Button getDownloadJarsCheck() {
		return downloadJarsCheck;
	}

	/**
	 * @param downloadJarsCheck
	 *            the downloadJarsCheck to set
	 */
	public void setDownloadJarsCheck(Button downloadJarsCheck) {
		this.downloadJarsCheck = downloadJarsCheck;
	}

	/**
	 * @return the executeSwitch
	 */
	public boolean isExecuteSwitch() {
		return executeSwitch;
	}

	/**
	 * @param executeSwitch
	 *            the executeSwitch to set
	 */
	public void setExecuteSwitch(boolean executeSwitch) {
		this.executeSwitch = executeSwitch;
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
	 * @return the downloadJarsSwitch
	 */
	public boolean isDownloadJarsSwitch() {
		return downloadJarsSwitch;
	}

	/**
	 * @param downloadJarsSwitch
	 *            the downloadJarsSwitch to set
	 */
	public void setDownloadJarsSwitch(boolean downloadJarsSwitch) {
		this.downloadJarsSwitch = downloadJarsSwitch;
	}

	/**
	 * @param jarPathStr
	 *            the jarPathStr to set
	 */
	public void setJarPathStr(String jarPathStr) {
		this.jarPathStr = jarPathStr;
	}

	public ConfigDialog(Shell parentShell, TreeSet<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
		super(parentShell);
		try {
			MtwsView.debug.write(new Date() + ":Savan UI - Contructing Savan Dialog\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setTitle("Add Project/Jar References");
		jarPathStr = "";
		setIntialValues(pMtWsDOs2, pModelProvider2);
		if (pMtWsDOs == null || pModelProvider == null)
			populateData();
		for (String s : pModelProvider.getpMtWsBO().getRepoPath())
			if (s != null && !s.isEmpty())
				jarPathStr = jarPathStr.concat(s + ";");
		for (String s : pModelProvider.getpMtWsBO().getScriptPath())
			if (s != null && !s.isEmpty()) {
				scriptPathStr = s;
			}

		try {
			MtwsView.debug.write(new Date() + ":Savan UI - Contructed Savan Dialog\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void setIntialValues(TreeSet<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
		ConfigDialog.pMtWsDOs = pMtWsDOs2;
		pModelProvider = pModelProvider2;
	}

	private TreeSet<MtWsDO> populateData() {
		pModelProvider = new ModelProvider();
		pMtWsDOs = pModelProvider.getProjects();
		for (String s : pModelProvider.getpMtWsBO().getRepoPath())
			if (s != null && !s.isEmpty())
				jarPathStr = jarPathStr.concat(s);

		return pMtWsDOs;
	}

	@Override
	public void create() {
		super.create();

		setTitle("Savant, Configuring the maven like Environment");
		setMessage("Configure the jar path, select the project that needs to refered using jar instead of projects which increase overall build performance", IMessageProvider.NONE);
		buildCheck.setSelection(pModelProvider.getpMtWsBO().isBuildSwitch());
		executeCheck.setSelection(pModelProvider.getpMtWsBO().isScriptSwitch());
		downloadJarsCheck.setSelection(pModelProvider.getpMtWsBO().isDownloadSwitch());
		if (scriptPathStr != null && !scriptPathStr.isEmpty())
			scriptPath.setText(scriptPathStr);

		scriptPath.setEnabled(pModelProvider.getpMtWsBO().isScriptSwitch());
		HashMap<String, IServer> servers = pModelProvider.getpMtWsBO().getServers();
		for (Entry<String, IServer> server : servers.entrySet()) {
			serverCombo.setData(server.getKey(), server.getValue());
			serverCombo.add(server.getKey());
			serverCombo.setText(server.getKey());
		}
		if (pModelProvider.getpMtWsBO().getServerInstance() != null && !pModelProvider.getpMtWsBO().getServerInstance().isEmpty())
			serverCombo.setText(pModelProvider.getpMtWsBO().getServerInstance());
		serverCleanCheck.setSelection(pModelProvider.getpMtWsBO().isInstallSwitch());
		serverCombo.setEnabled(pModelProvider.getpMtWsBO().isInstallSwitch());
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.RESIZE);

		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout outer = new GridLayout(1, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(outer);

		Composite jarPathmodel = new Composite(container, SWT.NONE);
		GridLayout jarPathGridLayout = new GridLayout(2, false);
		jarPathmodel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jarPathmodel.setLayout(jarPathGridLayout);

		createJarPath(jarPathmodel);
		projectSelector(container);
		createBuildmodel(container);
		createServerModel(container);
		jarPathText.setText(getJarPathStr());
		return area;
	}

	private void createBuildmodel(Composite inputLayer) {
		Composite outer = new Composite(inputLayer, SWT.NONE);
		GridLayout outerGridLayout = new GridLayout(4, false);
		outer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outer.setLayout(outerGridLayout);

		GridData ftpPortSettingSmall = new GridData();
		ftpPortSettingSmall.horizontalSpan = 1;
		ftpPortSettingSmall.horizontalAlignment = GridData.FILL;
		downloadJarsCheck = new Button(outer, SWT.CHECK);
		downloadJarsCheck.setText("Download Latest Jars");
		executeCheck = new Button(outer, SWT.CHECK);
		executeCheck.setText("Run this Script:");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;
		dataFirstName.horizontalSpan = 1;
		scriptPath = new Text(outer, SWT.BORDER);
		scriptPath.setLayoutData(dataFirstName);
		addScriptListener();
		addMouseListenser4jarPathText();

	}

	private void createServerModel(Composite saveModel) {
		Composite outer = new Composite(saveModel, SWT.NONE);
		GridLayout outerGridLayout = new GridLayout(4, false);
		outer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outer.setLayout(outerGridLayout);

		GridData ftpPortSettingSmall = new GridData();
		ftpPortSettingSmall.horizontalSpan = 1;
		ftpPortSettingSmall.horizontalAlignment = GridData.FILL;

		GridData ftpSettingSmall = new GridData();
		ftpSettingSmall.horizontalSpan = 1;
		ftpSettingSmall.grabExcessHorizontalSpace = true;
		ftpSettingSmall.horizontalAlignment = GridData.FILL;

		buildCheck = new Button(outer, SWT.CHECK);
		buildCheck.setText("Clean/Build");
		/*
		 * Label saveModelLabel = new Label(outer, SWT.NONE);
		 * saveModelLabel.setText("Servers: ");
		 */

		serverCleanCheck = new Button(outer, SWT.CHECK);
		serverCleanCheck.setText("Clean/Install");
		serverCleanCheck.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				serverCombo.setEnabled(serverCleanCheck.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		serverCombo = new Combo(outer, SWT.DROP_DOWN | SWT.BORDER);
		serverCombo.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
				arg0.keyCode = 0;

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				arg0.doit = false;

			}
		});
		serverCombo.setLayoutData(ftpSettingSmall);

		/*
		 * GridData ftpPortSettingSmall = new GridData();
		 * ftpPortSettingSmall.horizontalSpan = 1;
		 * ftpPortSettingSmall.grabExcessHorizontalSpace = true;
		 * ftpPortSettingSmall.horizontalAlignment = GridData.FILL;
		 */

		// serverCleanCheck.setLayoutData(ftpPortSettingSmall);

	}

	private void createJarPath(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Jar Path: ");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		jarPathText = new Text(container, SWT.BORDER);
		jarPathText.setText(getJarPathStr());
		jarPathText.setLayoutData(dataFirstName);

	}

	void addChangeListenser4jarPathText() {
		jarPathText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
				updateJarPath();
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	void addMouseListenser4jarPathText() {
		jarPathText.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent arg0) {

				Color col = new Color(null, 255, 255, 255);
				jarPathText.setBackground(col);
				col = new Color(null, 0, 0, 0);
				jarPathText.setForeground(col);
				if (tempJarPath != null && !tempJarPath.isEmpty()) {
					int cur = jarPathText.getCaretPosition();
					jarPathText.setText(tempJarPath);
					tempJarPath = "";
					jarPathText.setSelection(cur > NOT_VALID_JAR_PATH.length() ? cur - NOT_VALID_JAR_PATH.length() : 0);

				}

			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	boolean updateJarPath() {

		boolean valid = true;
		for (MtWsDO temp : pMtWsDOs)
			valid = valid && pModelProvider.getpMtWsBO().updateRepoPathFromDialog(temp, jarPathText.getText());
		return valid;

	}

	private void projectSelector(Composite container) {
		Composite outer = new Composite(container, SWT.NONE);
		GridLayout outerGridLayout = new GridLayout(5, false);
		outer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		outer.setLayout(outerGridLayout);

		leftTable = LeftTable.getInstance(outer, pMtWsDOs, pModelProvider);

		Composite model = new Composite(outer, SWT.NONE);
		model.setSize(100, 300);
		GridLayout inner = new GridLayout(1, false);
		model.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		model.setLayout(inner);
		createButton(model, " Double click on List  ", addButtonListener()).setEnabled(false);

		setRightTable(RightTable.getInstance(outer, pMtWsDOs, pModelProvider));
	}

	protected void addScriptListener() {
		executeCheck.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				executeCheck = (Button) arg0.getSource();
				if (executeCheck.getSelection()) {
					scriptPath.setEnabled(true);
				} else {
					scriptPath.setEnabled(false);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private SelectionListener addButtonListener() {

		return null;
	}

	private Button createButton(Composite model, String label, SelectionListener listener) {
		GridData addButtonGridData = new GridData();
		addButtonGridData.grabExcessHorizontalSpace = true;
		addButtonGridData.horizontalAlignment = GridData.CENTER;
		Button button = new Button(model, SWT.NONE);
		button.setLayoutData(addButtonGridData);
		button.setText(label);
		button.setSize(120, 50);
		return button;
	}

	@Override
	protected boolean isResizable() {
		return false;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private boolean saveInput() {
		return updateJarPath();

	}

	@Override
	protected void okPressed() {
		if (saveInput()) {

			MtWsTableManager.getInstance(null, null, null).getViewer().setInput(pMtWsDOs);

			DialogOkThread okThread = new DialogOkThread(pModelProvider, pMtWsDOs, buildCheck.getSelection(), executeCheck.getSelection(), scriptPath.getText(),
					downloadJarsCheck.getSelection(), serverCleanCheck.getSelection(), serverCombo.getText());
			okThread.schedule();
			super.okPressed();
		} else {

			synchronized (jarPathText) {
				Color col = new Color(null, ((int) (Math.random() * 100) % 100), ((int) (Math.random() * 100) % 100), ((int) (Math.random() * 100) % 100));
				jarPathText.setBackground(col);
				col = new Color(null, ((int) (Math.random() * 100) % 50) + 150, ((int) (Math.random() * 100) % 50) + 150, ((int) (Math.random() * 100) % 50) + 150);
				jarPathText.setForeground(col);
				if (tempJarPath == null || tempJarPath.isEmpty()) {
					tempJarPath = jarPathText.getText();
					jarPathText.setText(NOT_VALID_JAR_PATH + tempJarPath);
				}

			}

			jarPathText.setMessage("error");
		}
	}

	public String getJarPathStr() {
		return jarPathStr;
	}

	@Override
	protected void cancelPressed() {
		leftTable = null;
		setRightTable(null);
		// TODO Auto-generated method stub
		super.cancelPressed();
	}

	public WsTableManager getRightTable() {
		return rightTable;
	}

	public void setRightTable(WsTableManager rightTable) {
		this.rightTable = rightTable;
	}

}

class DialogOkThread extends Job {

	ModelProvider pModelProvider;
	TreeSet<MtWsDO> pMtWsDOs;
	private boolean buildSwitch;
	private boolean scriptSwitch;
	private String scriptURL;
	private boolean downloadJarsSwitch;

	public DialogOkThread(ModelProvider pModelProvider, TreeSet<MtWsDO> pMtWsDOs, boolean buildSwitch, boolean scriptSwitch, String scriptURL, boolean downloadJarsSwitch,
			boolean installSwitch, String serverInstance) {
		super("Savan Updating the Project References");
		this.pModelProvider = pModelProvider;
		this.pMtWsDOs = pMtWsDOs;
		this.downloadJarsSwitch = downloadJarsSwitch;
		this.buildSwitch = buildSwitch;
		this.scriptSwitch = scriptSwitch;
		this.scriptURL = scriptURL;
		this.serverInstance = serverInstance;
		this.installSwitch = installSwitch;
		s = new MtwsStatus(1, "MTWS", "");
	}

	IStatus s = null;
	private String serverInstance;
	private boolean installSwitch;

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		arg0.beginTask("Updating Project Reference...", 100);
		pModelProvider.getpMtWsBO().updateProjectReferences(arg0, pModelProvider.getpMtWsBO().getSavanTypeClosable(pMtWsDOs, true), buildSwitch, scriptSwitch, scriptURL,
				downloadJarsSwitch, installSwitch, serverInstance);

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

		return s;
	}

}