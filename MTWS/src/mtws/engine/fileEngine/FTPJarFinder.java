package mtws.engine.fileEngine;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;

import mtws.engine.Password;
import mtws.views.MtwsStatus;
import mtws.views.MtwsView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FTPJarFinder extends Job {
	IStatus status = null;
	HashMap<String,String> jarsAvailable;
	/**
	 * @return the jarsAvailable
	 */
	public HashMap<String,String> getJarsAvailable() {
		return jarsAvailable;
	}

	/**
	 * @param jarsAvailable the jarsAvailable to set
	 */
	public void setJarsAvailable(HashMap<String,String> jarsAvailable) {
		this.jarsAvailable = jarsAvailable;
	}

	/**
	 * @return the filesToDownload
	 */
	public TreeSet<String> getFilesToDownload() {
		return filesToDownload;
	}

	/**
	 * @param filesToDownload the filesToDownload to set
	 */
	public void setFilesToDownload(TreeSet<String> filesToDownload) {
		this.filesToDownload = filesToDownload;
	}

	/**
	 * @return the canDownload
	 */
	public boolean isCanDownload() {
		return canDownload;
	}

	/**
	 * @param canDownload the canDownload to set
	 */
	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	private TreeSet<String> filesToDownload;

	public FTPJarFinder(String name) {

		super(name);
		status = new MtwsStatus(1, name, name);
		jarsAvailable = new HashMap<String,String>();

	}

	IProgressMonitor s = new IProgressMonitor() {

		@Override
		public void worked(int arg0) {
			// TODO Auto-generated method stub

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
	Properties props;
	private boolean canceling = false;
	private boolean canDownload;
	private static FTPJarFinder fTPJarFinder;

	public static FTPJarFinder getInstance() {
		if (fTPJarFinder == null) {
			fTPJarFinder = new FTPJarFinder("Downloading Jars ");
		}
		return fTPJarFinder;
	}

	public void replicate(Properties props, TreeSet<String> projectJars, boolean canDownload,IProgressMonitor s) {
		this.canDownload = canDownload;
		jarsAvailable=new HashMap<String,String>();
		this.filesToDownload = projectJars;
		if (props == null)
			props = new Properties();
		this.props = props;
		setPriority(Job.LONG );
		if(!canDownload)
			schedule();
		else
			run(s);

	}

	public IStatus getStatus() {
		return status;
	}

	public void updateFile(String host, String port, String username, String password, String remotepath, String localpath, TreeSet<String> filesToDownload2, IProgressMonitor paramIProgressMonitor) {

		JSch jsch = new JSch();
		TreeSet<LsEntry> filesToFetch;
		Session session = null;
		try {
			session = jsch.getSession(username, host, Integer.valueOf((String) (port != null && !port.isEmpty() ? port : "22")));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(Password.base64ToByte(password));
			session.connect();
		
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			
			filesToFetch = checkforFiles(sftpChannel, remotepath, localpath, filesToDownload2);
			int totalWork=filesToFetch.size();
			
			for (LsEntry fetchFile : filesToFetch) {

				try {
					if (!remotepath.endsWith("/"))
						remotepath = remotepath.concat("/");
					if (!canceling && !fetchFile.getAttrs().isDir() && canDownload) {
						
						this.setName("Downloading " + fetchFile.getFilename() + " . . . .");
						paramIProgressMonitor.setTaskName("Downloading " + fetchFile.getFilename() + " . . . .");
						sftpChannel.get(remotepath + fetchFile.getFilename(), localpath + fetchFile.getFilename());
						paramIProgressMonitor.worked(100/totalWork);
					}
				} catch (Exception e) {
					MtwsView.debug.println(new Date() + ": Error while doing FTPJarFinder::updateFile ->sftpChannel.get & getting Exception "+ localpath);
					
					e.printStackTrace();
				}
			}

			sftpChannel.exit();
			session.disconnect();
		} catch (JSchException e) {
			MtwsView.debug.println(new Date() + ": Error while doing FTPJarFinder::updateFile  & getting JSchException "+ localpath);
		}
		canceling = false;
	}

	public static String testFTP(String host, String port, boolean isSSH, String username, String password, String remotepath, String localpath) {

		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(username, host, Integer.valueOf((String) (port != null && !port.isEmpty() ? port : "22")));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			Channel channel = session.openChannel(isSSH ? "sftp" : "ftp");
			if (channel != null) {
				channel.connect();
				ChannelSftp sftpChannel = (ChannelSftp) channel;
				sftpChannel.exit();
			} else {
				return "Check the Protocol :" + ("SSH Flag");
			}
			session.disconnect();

		} catch (JSchException e) {
			MtwsView.debug.println(new Date() + ": Error while doing FTPJarFinder::testFTP  & getting JSchException "+ localpath);
			e.printStackTrace(System.err);
			if (e.getMessage().contains("UnknownHostException")) {
				return "Unknown Host : " + host;
			}
			return e.getMessage().substring(e.getMessage().indexOf(":") + 1);
		}

		return " Connection Successful";
	}

	public static String testFTPFolder(String host, String port, boolean isSSH, String username, String password, String remotepath, String localpath) {

		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(username, host, Integer.valueOf((String) (port != null && !port.isEmpty()? port : "22")));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			Channel channel = session.openChannel(isSSH ? "sftp" : "ftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.ls(remotepath);
			sftpChannel.exit();
			session.disconnect();
		} catch (JSchException e) {
			MtwsView.debug.println(new Date() + ": Error while doing FTPJarFinder::testFTPFolder  & getting JSchException "+ localpath);
			e.printStackTrace(System.err);
			if (e.getMessage().contains("UnknownHostException")) {
				return "Unknown Host : " + host;
			}
			//$ANALYSIS-IGNORE
			return e.getMessage().substring(e.getMessage().indexOf(":") + 1);
		} catch (SftpException e) {
			MtwsView.debug.println(new Date() + ": Error while doing FTPJarFinder::testFTPFolder  & getting SftpException "+ localpath);
			e.printStackTrace(System.err);
			return e.getMessage().substring(e.getMessage().indexOf(":") + 1);

		}

		return "Path Exist";
	}

	public TreeSet<LsEntry> checkforFiles(ChannelSftp sftpChannel, String remotePath, final String localPath, final TreeSet<String> filesToDownload2) {

		TreeSet<LsEntry> availableFiles = new TreeSet<LsEntry>();
		File folder = new File(localPath);
		if(!(folder.exists()&&folder.isDirectory())){
			folder.mkdirs();
		}
		try {
			Vector<LsEntry> listOfFiles = sftpChannel.ls(remotePath);
			for (LsEntry file : listOfFiles) {
				for (String s : filesToDownload2) {

					if (file.getFilename().startsWith(s) && !canceling) {
						File f = new File(localPath + file.getFilename());
						if (((!f.exists() || !canDownload) && !file.getAttrs().isDir()) || (f.isFile() && f.canWrite() && (f.length() != file.getAttrs().getSize()))) {
							availableFiles.add(file);
							jarsAvailable.put(s, localPath + file.getFilename());
						}
					}
				}
			}

			return availableFiles;

		} catch (SftpException e) {
			MtwsView.debug.println(new Date() + ": Error while doing FTPJarFinder::checkforFiles  & getting SftpException "+ localPath);
			e.printStackTrace(System.err);
		}

		return availableFiles;
	}

	@Override
	protected IStatus run(IProgressMonitor paramIProgressMonitor) {
		if (paramIProgressMonitor == null)
			paramIProgressMonitor=s;
		paramIProgressMonitor.beginTask("Downloading Jars...", 100);
		

		if (canDownload)
			fTPJarFinder.updateFile(props.getProperty(SettingFileParser.SERVER_NAME), props.getProperty(SettingFileParser.PORT_NUM),
					props.getProperty(SettingFileParser.USER_NAME), props.getProperty(SettingFileParser.PASSWORD), props.getProperty(SettingFileParser.FTP_PATH),
					props.getProperty(SettingFileParser.JAR_PATH), filesToDownload, paramIProgressMonitor);
		else {
			fTPJarFinder.updateFile(props.getProperty(SettingFileParser.SERVER_NAME), props.getProperty(SettingFileParser.PORT_NUM),
					props.getProperty(SettingFileParser.USER_NAME), props.getProperty(SettingFileParser.PASSWORD), props.getProperty(SettingFileParser.FTP_PATH),
					props.getProperty(SettingFileParser.JAR_PATH), filesToDownload, paramIProgressMonitor);
		}
		return status;
	}

	@Override
	protected void canceling() {
		canceling = true;
		super.canceling();
	}
}
