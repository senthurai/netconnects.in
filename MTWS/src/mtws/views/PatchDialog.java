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
import org.eclipse.ui.internal.ide.dialogs.CleanDialog;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class PatchDialog extends TitleAreaDialog {




	private Text jarPathText;

	public PatchDialog(Shell parentShell) {
		super(parentShell);
		
	}

	void setIntialValues() {}


	@Override
	public void create() {
		super.create();

		setTitle("Savant, Configuring the maven like Environment");
		setMessage("Configure the Patch, select the project that needs to refered using jar instead of projects which increase overall build performance", IMessageProvider.NONE);
		
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
		
		createBuildmodel(container);
	
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
	
		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;
		dataFirstName.horizontalSpan = 1;
	
		
		
	}

	

	

	private void createJarPath(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Jar Path: ");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		jarPathText = new Text(container, SWT.BORDER);
		jarPathText.setText(" ");
		jarPathText.setLayoutData(dataFirstName);

	}

	void addChangeListenser4jarPathText() {
		jarPathText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
			
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
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


	@Override
	protected void okPressed() {
	
		super.okPressed();
	}



	@Override
	protected void cancelPressed() {

		// TODO Auto-generated method stub
		super.cancelPressed();
	}


}

class PatchDialogOkThread extends Job {

	ModelProvider pModelProvider;
	TreeSet<MtWsDO> pMtWsDOs;
	private boolean buildSwitch;
	private boolean scriptSwitch;
	private String scriptURL;
	private boolean downloadJarsSwitch;

	public PatchDialogOkThread(ModelProvider pModelProvider, TreeSet<MtWsDO> pMtWsDOs, boolean buildSwitch, boolean scriptSwitch, String scriptURL, boolean downloadJarsSwitch,boolean installSwitch, String serverInstance) {
		super("Savan Updating the Project References");
		this.pModelProvider = pModelProvider;
		this.pMtWsDOs = pMtWsDOs;
		this.downloadJarsSwitch = downloadJarsSwitch;
		this.buildSwitch = buildSwitch;
		this.scriptSwitch = scriptSwitch;
		this.scriptURL = scriptURL;
		this.serverInstance=serverInstance;
		this.installSwitch=installSwitch;
		s = new MtwsStatus(1, "MTWS", "");
	}

	IStatus s = null;
	private String serverInstance;
	private boolean installSwitch;

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		 arg0.beginTask("Updating Project Reference...", 100);
		pModelProvider.getpMtWsBO().updateProjectReferences(arg0,pModelProvider.getpMtWsBO().getSavanTypeClosable(pMtWsDOs, true), buildSwitch, scriptSwitch, scriptURL,
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