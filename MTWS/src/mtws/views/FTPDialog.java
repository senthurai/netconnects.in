package mtws.views;

/**
 * Copyright (C) netConnects  Author - Tin

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import mtws.data.FTPConfig;
import mtws.data.MtWsDO;
import mtws.engine.ModelProvider;
import mtws.engine.MtWsBO;
import mtws.engine.fileEngine.FTPJarFinder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class FTPDialog extends TitleAreaDialog {
	private Text ftpPathText;
	private Text ftpConfigPathText;
	private Text ftpServerText;
	private Text ftpServerPortText;
	private Text ftpProfileName;

	private Text ftpServerUserNameText;
	private Text ftpServerPasswordText;
	private Text jarPathText;
	private Label ftpConfigLabel;
/*	private TreeSet<MtWsDO> pMtWsDOs;*/
/*	private ModelProvider pModelProvider;*/
	private Button testFTP;
	StringBuffer status = new StringBuffer();
	private Action ftpTest;
	Job job;
	private Thread statusThread;
	private static FTPDialog fTPDialog;
	boolean runThread = false;
	private Button checkFolder;
	private Button ftpServerPortCheck;
	private TreeSet<FTPConfig> ftpConfigs;
	private Combo ftpProfileCombo;
	private Button deleteButton;

	public static FTPDialog getInstance(Shell shell, TreeSet<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
		if (fTPDialog == null)
			fTPDialog = new FTPDialog(shell, pMtWsDOs2, pModelProvider2);
		return fTPDialog;
	}

	public FTPDialog(Shell parentShell, TreeSet<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
		super(parentShell);
		setIntialVaues(pMtWsDOs2, pModelProvider2);

	}

	void setIntialVaues(TreeSet<MtWsDO> pMtWsDOs2, ModelProvider pModelProvider2) {
	/*	pMtWsDOs = pMtWsDOs2;
		pModelProvider = pModelProvider2;
		if (pMtWsDOs == null || pModelProvider == null)
			pMtWsDOs = populateData();*/
		this.setTitle("FTP Setting");

		FTPDialog.fTPDialog = this;
		status = status.delete(0, status.length()).append("Status : ");
	}

	@Override
	public void create() {
		super.create();
		super.getButton(OK).setText("Save and Exit");
		super.getButton(CANCEL).setText("Close");
		setTitle("FTP Config for Savant, Configuring the Maven like Environment");
		setMessage("Configure the jar path, select the project that needs to refered using jar instead of projects which increase overall build performance", IMessageProvider.NONE);
		// populateFtpConfigData(pMtWsDOs);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.RESIZE);

		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout outer = new GridLayout(1, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(outer);

		Composite ftpConfigModel = new Composite(container, SWT.NONE);
		GridLayout ftpConfigModelLayout = new GridLayout(4, false);
		ftpConfigModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ftpConfigModel.setLayout(ftpConfigModelLayout);

		// createFtpConfig(ftpConfigModel);

		Composite saveModel = new Composite(container, SWT.BORDER);
		GridLayout saveGridLayout = new GridLayout(4, false);
		saveModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		saveModel.setLayout(saveGridLayout);

		Composite jarPathmodel = new Composite(container, SWT.BORDER);
		GridLayout jarPathGridLayout = new GridLayout(4, false);
		jarPathmodel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jarPathmodel.setLayout(jarPathGridLayout);

		createSaveModel(saveModel);
		createFTPSetting(jarPathmodel);
		createStatus(container);

		populateFtpConfigData(MtwsView.getInstance().getpMtWsDOs());
		
		return area;
	}

	private void createSaveModel(Composite saveModel) {

		GridData ftpSettingSmall = new GridData();
		ftpSettingSmall.horizontalSpan = 1;
		ftpSettingSmall.grabExcessHorizontalSpace = true;
		ftpSettingSmall.horizontalAlignment = GridData.FILL;

		GridData ftpSetting = new GridData();
		ftpSetting.horizontalSpan = 4;
		ftpSetting.grabExcessHorizontalSpace = true;
		ftpSetting.horizontalAlignment = GridData.FILL;

		Label saveModelLabel = new Label(saveModel, SWT.NONE);

		saveModelLabel.setText("Profile: ");
		ftpProfileCombo = new Combo(saveModel, SWT.DROP_DOWN | SWT.BORDER);

		ftpProfileCombo.setLayoutData(ftpSettingSmall);

		Label profileNameLabel = new Label(saveModel, SWT.NONE);
		profileNameLabel.setText("Profile Name: ");
		Composite serverPortModel = new Composite(saveModel, SWT.NONE);
		GridLayout jarPathGridLayout = new GridLayout(2, true);
		serverPortModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		serverPortModel.setLayout(jarPathGridLayout);

		GridData ftpPortSettingSmall = new GridData();
		ftpPortSettingSmall.horizontalSpan = 1;
		ftpPortSettingSmall.grabExcessHorizontalSpace = true;
		ftpPortSettingSmall.horizontalAlignment = GridData.FILL;

		ftpProfileName = new Text(serverPortModel, SWT.BORDER);
		ftpProfileName.setLayoutData(ftpPortSettingSmall);
		deleteButton = createButton(serverPortModel, " Delete Profile ", null);
		addDeleteAction();
	}

	private void addDeleteAction() {
		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FTPConfig ftpConfig = new FTPConfig();
				String deleteProfile = ftpProfileName.getText();
				ftpProfileName.setText(ftpConfig.getFtpEnv());

				ftpPathText.setText(ftpConfig.getFtpPath());

			//	ftpConfigPathText.setText(ftpConfig.getConfigPath());

				ftpServerText.setText(ftpConfig.getServerName());

				ftpServerPortText.setText(ftpConfig.getPort());

				ftpServerUserNameText.setText(ftpConfig.getUserName());

				ftpServerPasswordText.setText(ftpConfig.getPassword());

				jarPathText.setText(ftpConfig.getPath());
				ftpServerPortCheck.setSelection(ftpConfig.issSH());
				ftpConfig.setFtpEnv(deleteProfile);
				DialogOkThread okThread = new DialogOkThread(MtwsView.getInstance().getpModelProvider(), MtwsView.getInstance().getpMtWsDOs(), ftpConfig);
				okThread.schedule();
				populateProfile(ftpProfileCombo);
				runThread = false;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	/*private TreeSet<MtWsDO> populateData() {

		pModelProvider = new ModelProvider();
		pMtWsDOs = pModelProvider.getProjects();
		return pMtWsDOs;

	}*/

	void populateFtpConfigData(TreeSet<MtWsDO> pMtWsDOs2) {
		//pModelProvider.getpMtWsBO();
		setFtpConfigs(MtWsBO.getFtpConfigs());

		populateProfile(ftpProfileCombo);
		addChangeListener(ftpProfileCombo);

		FTPConfig ftpConfig = MtwsView.getInstance().getpModelProvider().getpMtWsBO().retrieveFtpProperties();
		populateFtpField(ftpConfig);
	}

	/**
	 * @param ftpConfig
	 */
	private void populateFtpField(FTPConfig ftpConfig) {
		if (ftpConfig != null) {
			if (ftpConfig.getFtpEnv() != null && !ftpConfig.getFtpEnv().isEmpty())
				ftpProfileName.setText(ftpConfig.getFtpEnv());
			if (ftpConfig.getFtpPath() != null && !ftpConfig.getFtpPath().isEmpty())
				ftpPathText.setText(ftpConfig.getFtpPath());
			if (ftpConfig.getConfigPath() != null && !ftpConfig.getConfigPath().isEmpty())
				ftpConfigPathText.setText(ftpConfig.getConfigPath());
			if (ftpConfig.getServerName() != null && !ftpConfig.getServerName().isEmpty())
				ftpServerText.setText(ftpConfig.getServerName() + "");
			if (ftpConfig.getPort() != null && !ftpConfig.getPort().isEmpty())
				ftpServerPortText.setText(ftpConfig.getPort());
			if (ftpConfig.getUserName() != null && !ftpConfig.getUserName().isEmpty())
				ftpServerUserNameText.setText(ftpConfig.getUserName());
			if (ftpConfig.getPassword() != null && !ftpConfig.getPassword().isEmpty())
				ftpServerPasswordText.setText(ftpConfig.getPassword());
			if (ftpConfig.getPath() != null && !ftpConfig.getPath().isEmpty())
				jarPathText.setText(ftpConfig.getPath());
			ftpServerPortCheck.setSelection(ftpConfig.issSH());

		}
	}

	void populateProfile(Combo ftpProfileCombo2) {
		ftpProfileCombo2.removeAll();
		if (getFtpConfigs() != null)
			for (FTPConfig conf : getFtpConfigs()) {
				ftpProfileCombo2.setData(getFtpConfigs());
				if (conf != null)
					ftpProfileCombo2.add(conf.getFtpEnv()
							+ " : "
							+ conf.getServerName()
							+ ": ..."
							+ conf.getFtpPath().substring(conf.getFtpPath().length() - Double.valueOf((conf.getFtpPath().length() / 100) * .60).intValue(),
									conf.getFtpPath().length()));
			}
		ftpProfileCombo2.setText("Swtich Profile...");
	}

	void addChangeListener(Combo ftpProfileCombo2) {
		SelectionListener listener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Combo s = (Combo) arg0.getSource();
				int k = s.getSelectionIndex();
				Iterator<FTPConfig> it = ((TreeSet<FTPConfig>) s.getData()).iterator();
				int i = 0;
				FTPConfig ftpConfig = null;
				while (it.hasNext() && i <= k) {
					ftpConfig = it.next();
					i++;
				}
				populateFtpField(ftpConfig);
				// System.out.println(getFtpConfigs().s.getSelectionIndex());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		};
		ftpProfileCombo2.addSelectionListener(listener);
	}

	FTPConfig saveFtpConfigData() {
		FTPConfig ftpConfig = new FTPConfig();
		ftpConfig.setValid(true);
		String ftpPath = ftpPathText.getText();
		if (ftpPath.matches(".*?[^/\\\\]$"))
			ftpPath = ftpPath.concat("/");
		if (ftpPath != null && !ftpPath.isEmpty()) {
			ftpConfig.setFtpPath(ftpPath.trim());
		} else {
			ftpConfig.setValid(false);
			ftpConfig.setErrorMessage(ftpConfig.getErrorMessage() != null ? ftpConfig.getErrorMessage().concat(" FTP Path;") : "Enter FTP Path;  ");
		}

		// ftpConfig.setConfigPath(ftpConfigPathText.getText());
		if (ftpServerText.getText() != null && !ftpServerText.getText().isEmpty())
			ftpConfig.setServerName(ftpServerText.getText().trim());
		else {
			ftpConfig.setValid(false);
			ftpConfig.setErrorMessage(ftpConfig.getErrorMessage() != null ? ftpConfig.getErrorMessage().concat("Server Name/IP; ") : " Enter Server Name/IP; ");
		}

		ftpConfig.setPort(ftpServerPortText.getText().trim());
		ftpConfig.setFtpEnv(ftpProfileName.getText().trim());
		ftpConfig.setUserName(ftpServerUserNameText.getText().trim());
		ftpConfig.setPassword(ftpServerPasswordText.getText().trim());
		String path = jarPathText.getText();
		if (path.matches(".*?[^/\\\\]$"))
			path = path.concat(File.separator);
		ftpConfig.setPath(path.trim());
		ftpConfig.setsSH(ftpServerPortCheck.getSelection());
		return ftpConfig;
	}

	private void createStatus(Composite outer) {
		GridData ftpSetting = new GridData();

		ftpSetting.horizontalSpan = 3;
		ftpSetting.grabExcessHorizontalSpace = true;
		ftpSetting.horizontalAlignment = GridData.FILL;
		setFtpConfigLabel(new Label(outer, SWT.NONE));
		getFtpConfigLabel().setText("Status : ");
		getFtpConfigLabel().setLayoutData(ftpSetting);
	}

	private void createFtpConfig(Composite ftpConfigModel) {

		GridData ftpSetting = new GridData();
		ftpSetting.horizontalSpan = 3;
		ftpSetting.grabExcessHorizontalSpace = true;
		ftpSetting.horizontalAlignment = GridData.FILL;
		setFtpConfigLabel(new Label(ftpConfigModel, SWT.NONE));
		ftpConfigPathText = new Text(ftpConfigModel, SWT.BORDER);
		ftpConfigPathText.setLayoutData(ftpSetting);
		getFtpConfigLabel().setText("FTP Config Path: ");
		getFtpConfigLabel().setLayoutData(ftpSetting);
	}

	private void createFTPPath(Composite container) {

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		// ftpPath.setText(getFTPPathText());
		ftpPathText.setLayoutData(dataFirstName);

	}

	private void createFTPSetting(Composite container) {
		GridData ftpSettingSmall = new GridData();
		ftpSettingSmall.horizontalSpan = 1;
		ftpSettingSmall.grabExcessHorizontalSpace = true;
		ftpSettingSmall.horizontalAlignment = GridData.FILL;

		GridData ftpSetting = new GridData();
		ftpSetting.horizontalSpan = 3;
		ftpSetting.grabExcessHorizontalSpace = true;
		ftpSetting.horizontalAlignment = GridData.FILL;

		Label ftpServerAddressLabel = new Label(container, SWT.NONE);

		ftpServerAddressLabel.setText("Server Address: ");

		ftpServerText = new Text(container, SWT.BORDER);

		ftpServerText.setLayoutData(ftpSettingSmall);

		Label ftpServerPortLabel = new Label(container, SWT.NONE);
		ftpServerPortLabel.setText("Port: ");

		Composite serverPortModel = new Composite(container, SWT.NONE);
		GridLayout jarPathGridLayout = new GridLayout(2, true);
		serverPortModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		serverPortModel.setLayout(jarPathGridLayout);

		GridData ftpPortSettingSmall = new GridData();
		ftpPortSettingSmall.horizontalSpan = 1;
		ftpPortSettingSmall.grabExcessHorizontalSpace = true;
		ftpPortSettingSmall.horizontalAlignment = GridData.FILL;

		ftpServerPortText = new Text(serverPortModel, SWT.BORDER);
		ftpServerPortText.setLayoutData(ftpPortSettingSmall);
		ftpServerPortCheck = new Button(serverPortModel, SWT.CHECK);
		ftpServerPortCheck.setText("SSH");
		ftpServerPortCheck.setSelection(true);
		Label ftpServerUserNameLabel = new Label(container, SWT.NONE);
		ftpServerUserNameLabel.setText("UserName: ");
		ftpServerUserNameText = new Text(container, SWT.BORDER);
		ftpServerUserNameText.setLayoutData(ftpSettingSmall);
		Label ftpServerPasswordLabel = new Label(container, SWT.NONE);
		ftpServerPasswordLabel.setText("Password: ");
		ftpServerPasswordText = new Text(container, SWT.BORDER | SWT.PASSWORD);

		ftpServerPasswordText.setLayoutData(ftpSettingSmall);
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("FTP Jar Path: ");
		ftpPathText = new Text(container, SWT.BORDER);
		ftpPathText.setLayoutData(ftpSetting);
		// createFTPPath(container);
		// ftpPath.setText(getJarPathStr());
		// ftpServerText.setLayoutData();
		Label jarPathLabel = new Label(container, SWT.NONE);
		jarPathLabel.setText("Jar Path: ");

		jarPathText = new Text(container, SWT.BORDER);
		// jarPathText.setText(getJarPathStr());
		jarPathText.setLayoutData(ftpSetting);

		createButton(container, "Retrive FTP Config", null).setVisible(false);
		createButton(container, "        Ping          ", null).setVisible(false);
		testFTP = createButton(container, "      Test FTP      ", null);
		checkFolder = createButton(container, " Check FTP Folder ", null);
		testFTPButtonAction();
		testFTPFolder();
	}

	Thread getStatusThread() {
		return new Thread(new Runnable() {
			boolean runs = true;
			String dots = ".";

			public void run() {

				while (runThread || runs) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					if (!runThread) {
						dots = ".";
						runs = false;
					} else {
						if (dots.length() > 150)
							dots = " . ";
						dots = dots.concat(" . ");
						runs = true;
					}
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							try {

								FTPDialog.getInstance(null, null, null).getFtpConfigLabel().setText(status.toString().concat(dots));
							} catch (Exception e) {
								dots = ".";
								runThread = false;
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
	}

	void testFTPFolder() {

		checkFolder.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final FTPConfig ftpConfig = saveFtpConfigData();
				runThread = true;
				statusThread = getStatusThread();
				job = new Job("Testing FTP") {

					@Override
					protected IStatus run(IProgressMonitor arg0) {
						try {
							if (statusThread.getState() != Thread.State.RUNNABLE)
								statusThread.start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						status.delete(0, status.length()).append("Status : wait...");
						status.append("Status : "
								+ FTPJarFinder.testFTPFolder(ftpConfig.getServerName(), ftpConfig.getPort(), ftpConfig.issSH(), ftpConfig.getUserName(), ftpConfig.getPassword(),
										ftpConfig.getFtpPath(), ftpConfig.getPath()) + status.delete(0, status.length()));
						runThread = false;
						return new MtwsStatus(1, "MTWS", "Done");
					}
				};
				job.schedule();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * @return the ftpProfileName
	 */
	public Text getFtpProfileName() {
		return ftpProfileName;
	}

	/**
	 * @param ftpProfileName
	 *            the ftpProfileName to set
	 */
	public void setFtpProfileName(Text ftpProfileName) {
		this.ftpProfileName = ftpProfileName;
	}

	void testFTPButtonAction() {

		testFTP.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final FTPConfig ftpConfig = saveFtpConfigData();
				runThread = true;

				statusThread = getStatusThread();

				job = new Job("Testing FTP") {

					@Override
					protected IStatus run(IProgressMonitor arg0) {
						try {
							if (statusThread.getState() != Thread.State.RUNNABLE)
								statusThread.start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						status.delete(0, status.length()).append("Status : Wait...");
						status.append("Status : "
								+ FTPJarFinder.testFTP(ftpConfig.getServerName(), ftpConfig.getPort(), ftpConfig.issSH(), ftpConfig.getUserName(), ftpConfig.getPassword(),
										ftpConfig.getFtpPath(), ftpConfig.getPath()) + status.delete(0, status.length()));
						runThread = false;
						return new MtwsStatus(1, "MTWS", "Done");
					}
				};
				job.schedule();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public Button getOKButton() {
		return super.getButton(OK);

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

	private void createJarPath(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Jar Path: ");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		jarPathText = new Text(container, SWT.BORDER);

		jarPathText.setLayoutData(dataFirstName);

	}

	@Override
	protected void okPressed() {
		FTPConfig ftpConfig = saveFtpConfigData();
		if (ftpConfig.isValid()) {
			
			DialogOkThread okThread = new DialogOkThread(MtwsView.getInstance().getpModelProvider(), MtwsView.getInstance().getpMtWsDOs(), ftpConfig);
			okThread.schedule();
			runThread = false;

			super.okPressed();
		} else {
			status = new StringBuffer(ftpConfig.getErrorMessage());

			getFtpConfigLabel().setText(status.toString());
		}

	}
	
	@Override
	protected boolean canHandleShellCloseEvent() {
		runThread = false;
		return super.canHandleShellCloseEvent();
	}

	public Set<FTPConfig> getFtpConfigs() {
		return ftpConfigs;
	}

	public void setFtpConfigs(TreeSet<FTPConfig> ftpConfigs) {
		this.ftpConfigs = ftpConfigs;
	}

	public Label getFtpConfigLabel() {
		return ftpConfigLabel;
	}

	public void setFtpConfigLabel(Label ftpConfigLabel) {
		this.ftpConfigLabel = ftpConfigLabel;
	}

	class DialogOkThread extends Job {

		ModelProvider pModelProvider;
		TreeSet<MtWsDO> pMtWsDOs;
		FTPConfig ftpConfig;

		IStatus s = null;

		public DialogOkThread(ModelProvider pModelProvider, TreeSet<MtWsDO> pMtWsDOs, FTPConfig ftpConfig) {
			super("MTWS FTP config update");
			this.pModelProvider = pModelProvider;
			this.pMtWsDOs = pMtWsDOs;
			this.ftpConfig = ftpConfig;
			s = new MtwsStatus(1, "MTWS", "Done");
		}
		boolean updateJarPath(String jarPath) {

			boolean valid = true;
			for (MtWsDO temp : pMtWsDOs)
				valid = valid && pModelProvider.getpMtWsBO().updateRepoPathFromDialog(temp, jarPath);
			return valid;

		}
		@Override
		protected IStatus run(IProgressMonitor arg0) {
			if (pModelProvider.getpMtWsBO().getRepoPath().isEmpty()) {
				pModelProvider.getpMtWsBO().getRepoPath().add(ftpConfig.getPath());
				updateJarPath(ftpConfig.getPath());	
			}
			
			pModelProvider.getpMtWsBO().updateFtpProperties(ftpConfig);

			runThread = false;
			return s;
		}

		@Override
		protected void canceling() {
			runThread = false;
			cancel();
			super.canceling();
		}
	}
}
