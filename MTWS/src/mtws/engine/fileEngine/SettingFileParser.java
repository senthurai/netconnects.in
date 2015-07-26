package mtws.engine.fileEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import mtws.data.FTPConfig;
import mtws.data.MtWsDO;
import mtws.data.SettingPropertiesEnum;
import mtws.engine.Password;
import mtws.views.MtwsView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class SettingFileParser {

	static final String FILE_SEPERATOR = ";";

	public static final String JAR_PATHS = "jarPaths";
	public static final String JAR_PATH = "jarPath";
	static final String IS_Savant_CLOSED = "isSavantClosed";
	public static final String Savant_PROJECTS = "SavantProjects";
	public static final String FTP_PROFILE = "ftpProfile";
	public static final String CONFIG_PATH = "ConfigPath";
	public static final String SERVER_NAME = "ServerName";
	public static final String PORT_NUM = "PortNum";
	public static final String USER_NAME = "UserName";
	public static final String PASSWORD = "HashCode";
	public static final String FTP_PATH = "FtpPath";
	public static final String BUILD_SWITCH = "buildSwitch";
	public static final String SCRIPT_SWITCH = "scriptSwitch";
	public static final String SCRIPT_PATH = "scriptPath";
	public static final String PRJ_NOT_AVIALABLE = "projectNotAvialable";
	public static final String DOWNLOAD_SWITCH = "downloadSwitch";

	public static final String IS_PATCHED = "isPatched";
	public static final String FTP_SET = "FTPSET";
	public static final String SETTING_FILE = "/.settings/org.Savant.config.prefs";
	public static final String SETTING_FOLDER = ".settings";

	private static final String SSH = "SSH";

	public static final String INSTALL_SWITCH = "installSwitch";

	public static final String SERVER_INSTANCE = "serverInstance";
	private static Properties INTIAL_PROPS_VAL = new Properties();

	static {
		INTIAL_PROPS_VAL.put(JAR_PATHS, "");
		INTIAL_PROPS_VAL.put(IS_PATCHED, "false");
		INTIAL_PROPS_VAL.put(PRJ_NOT_AVIALABLE, "");
		INTIAL_PROPS_VAL.put(IS_Savant_CLOSED, "");
		INTIAL_PROPS_VAL.put(Savant_PROJECTS, "");
		INTIAL_PROPS_VAL.put(CONFIG_PATH, "");
		INTIAL_PROPS_VAL.put(FTP_PROFILE, "");
		INTIAL_PROPS_VAL.put(SERVER_NAME, "");
		INTIAL_PROPS_VAL.put(PORT_NUM, "");
		INTIAL_PROPS_VAL.put(USER_NAME, "");
		INTIAL_PROPS_VAL.put(PASSWORD, "");
		INTIAL_PROPS_VAL.put(FTP_PATH, "");
		INTIAL_PROPS_VAL.put(SCRIPT_PATH, "");
		INTIAL_PROPS_VAL.put(DOWNLOAD_SWITCH, "false");
		INTIAL_PROPS_VAL.put(SCRIPT_SWITCH, "false");
		INTIAL_PROPS_VAL.put(FTP_SET, "");

		INTIAL_PROPS_VAL.put(BUILD_SWITCH, "false");
		INTIAL_PROPS_VAL.put(INSTALL_SWITCH, "false");
		INTIAL_PROPS_VAL.put(SERVER_INSTANCE, "");

	}

	void appendProperties(Properties input, Properties settingProps) {
		if (settingProps != null) {
			settingProps.put(JAR_PATHS, appendPaths(input.getProperty(JAR_PATHS), settingProps.getProperty(JAR_PATHS)));
			settingProps.put(Savant_PROJECTS, appendSavantProjectsStr(input.getProperty(Savant_PROJECTS), settingProps.getProperty(Savant_PROJECTS)));
			settingProps.put(CONFIG_PATH, input.getProperty(CONFIG_PATH) != null ? input.getProperty(CONFIG_PATH) : "");
			settingProps.put(FTP_PROFILE, input.getProperty(FTP_PROFILE) != null ? input.getProperty(FTP_PROFILE) : "");
			settingProps.put(SERVER_NAME, input.getProperty(SERVER_NAME) != null ? input.getProperty(SERVER_NAME) : "");
			settingProps.put(PORT_NUM, input.getProperty(PORT_NUM) != null ? input.getProperty(PORT_NUM) : "");

			settingProps.put(USER_NAME, input.getProperty(USER_NAME) != null ? input.getProperty(USER_NAME) : "");
			settingProps.put(PASSWORD, input.getProperty(PASSWORD) != null ? input.getProperty(PASSWORD) : "");
			settingProps.put(FTP_PATH, input.getProperty(FTP_PATH) != null ? input.getProperty(FTP_PATH) : "");
			settingProps.put(FTP_SET, input.getProperty(FTP_SET) != null ? input.getProperty(FTP_SET) : "");

			settingProps.put(JAR_PATH, input.getProperty(JAR_PATH) != null ? input.getProperty(JAR_PATH) : "");
			settingProps.put(SCRIPT_PATH, input.getProperty(SCRIPT_PATH) != null ? input.getProperty(SCRIPT_PATH) : "");
			settingProps.put(BUILD_SWITCH, String.valueOf(Boolean.valueOf(input.getProperty(BUILD_SWITCH)) || Boolean.valueOf(input.getProperty(JAR_PATH))));
			settingProps.put(SCRIPT_SWITCH, String.valueOf(Boolean.valueOf(input.getProperty(SCRIPT_SWITCH)) || Boolean.valueOf(input.getProperty(SCRIPT_SWITCH))));
			settingProps.put(DOWNLOAD_SWITCH, String.valueOf(Boolean.valueOf(input.getProperty(DOWNLOAD_SWITCH)) || Boolean.valueOf(input.getProperty(DOWNLOAD_SWITCH))));
			settingProps.put(INSTALL_SWITCH, String.valueOf(Boolean.valueOf(input.getProperty(INSTALL_SWITCH)) || Boolean.valueOf(input.getProperty(INSTALL_SWITCH))));
			settingProps.put(SERVER_INSTANCE, input.getProperty(SERVER_INSTANCE) != null ? input.getProperty(SERVER_INSTANCE) : "");
		}
	}

	String appendPaths(String input, String settingProps) {
		return validatePath(input + ";" + settingProps);
	}

	String appendSavantProjectsStr(String input, String settingProps) {
		return validateSavantProjectsStr(input + ";" + settingProps);
	}

	public String appendSavantProjectsStr(HashSet<String> SavantProjects) {
		StringBuffer sb = new StringBuffer();
		if (SavantProjects != null)
			for (String iSavantProject : SavantProjects)
				if (iSavantProject != null && !iSavantProject.isEmpty())
					sb.append(iSavantProject).append(";");
		return sb.toString();
	}

	public HashSet<String> getSavantProjectsSet(String input) {
		HashSet<String> SavantProjects = new HashSet<String>();
		if (input != null)
			for (String s : input.split(FILE_SEPERATOR)) {
				SavantProjects.add(s);
			}
		return SavantProjects;
	}

	String validateSavantProjectsStr(String input) {
		HashSet<String> SavantProjects = new HashSet<String>();
		for (String s : input.split(FILE_SEPERATOR)) {
			if (!s.equals("null"))
				SavantProjects.add(s);
		}
		StringBuffer sb = new StringBuffer();
		for (String iSavantProject : SavantProjects)
			if (!(iSavantProject.isEmpty() || iSavantProject == null))
				sb.append(iSavantProject).append(";");

		return sb.toString();
	}

	String appendSavantProjectsStr(HashSet<String> SavantProjects, String input) {
		StringBuffer sb = new StringBuffer();
		SavantProjects.add(input);
		for (String iSavantProject : SavantProjects)
			sb.append(iSavantProject).append(";");
		return sb.toString();
	}

	/**
	 * @param path
	 */
	public static String validatePath(String path) {

		HashSet<String> StrPathArr = splitPathArray(path);
		StringBuffer validPath = new StringBuffer();
		for (String s : StrPathArr) {
			validPath.append(s + ";");
		}

		return validPath.toString();
	}

	public static HashSet<String> splitPathArray(String path) {
		HashSet<String> StrPathArr = new HashSet<String>();
		if (path != null)
			for (String s : path.split(FILE_SEPERATOR)) {
				File f = new File(s);
				if (f.exists()) {
					try {
						StrPathArr.add(f.getCanonicalPath());
					} catch (IOException e) {
						MtwsView.debug.println(new Date() + ": Error while splitting the Jar Path with ;");
						e.printStackTrace(System.err);
					}
				}
			}
		return StrPathArr;
	}

	Properties constructNewSettings(Properties settingProps) {
		if (settingProps == null || (settingProps != null && settingProps.size() == 0)) {
			settingProps = new Properties();
			settingProps.putAll(INTIAL_PROPS_VAL);
		} else if (settingProps.size() < INTIAL_PROPS_VAL.size()) {
			appendProperties(INTIAL_PROPS_VAL, settingProps);
		}
		return settingProps;
	}

	public void extractProperties(IFile settingFile, Properties settingProps) {
		if (settingFile != null) {
			try {

				Properties temp = new Properties();

				temp.load(new FileInputStream(settingFile.getLocation().toFile()));
				appendProperties(temp, settingProps);
			} catch (IOException e) {
				MtwsView.debug.println(new Date() + ": Error while doing SettingFileParser::extractProperties  & getting IOException " + settingProps.toString());
				e.printStackTrace(System.err);
			}
		} else {
			createSettingFile(settingFile, settingProps);
		}

	}

	public void updateFtpProperties(FTPConfig ftpConfig, Properties props) {
		props.put(SettingFileParser.FTP_PROFILE, ftpConfig.getFtpEnv() != null ? ftpConfig.getFtpEnv() : "");
		props.put(SettingFileParser.CONFIG_PATH, ftpConfig.getConfigPath() != null ? ftpConfig.getConfigPath() : "");
		props.put(SettingFileParser.SERVER_NAME, ftpConfig.getServerName() != null ? ftpConfig.getServerName() : "");
		props.put(SettingFileParser.PORT_NUM, ftpConfig.getPort() != null ? ftpConfig.getPort() : "");
		props.put(SettingFileParser.SSH, ftpConfig.issSH() ? "TRUE" : "FALSE");
		props.put(SettingFileParser.USER_NAME, ftpConfig.getUserName() != null ? ftpConfig.getUserName() : "");
		props.put(SettingFileParser.PASSWORD, Password.byteToBase64(ftpConfig.getPassword() != null ? ftpConfig.getPassword() : ""));
		props.put(SettingFileParser.FTP_PATH, ftpConfig.getFtpPath() != null ? ftpConfig.getFtpPath() : "");
		props.put(SettingFileParser.JAR_PATH, ftpConfig.getPath() != null ? ftpConfig.getPath() : "");
	}

	public void createSettingFile(IFile settingFile, Properties settingProps) {
		settingProps = constructNewSettings(settingProps);
		writeSettingFile(settingFile, settingProps);
	}

	public void writeSettingFile(IFile settingFile, Properties settingProps) {
		FileOutputStream fos = null;

		try {
			if (settingFile != null) {
				fos = new FileOutputStream(settingFile.getLocation().toFile());

				settingProps.store(fos, "");
			}
		} catch (IOException e) {
			MtwsView.debug.println(new Date() + ": Error while doing SettingFileParser::writeSettingFile  & getting IOException " + settingProps.toString());
			e.printStackTrace(System.err);
		} finally {
			try {
				if (settingFile != null) {
					settingFile.refreshLocal(1, null);
				}
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing SettingFileParser::writeSettingFile  & getting IOException " + settingProps.toString());
				e.printStackTrace(System.err);
			}
		}
	}

	public void updateRepoPath(MtWsDO temp, String text) {
		if (temp != null) {

			if (temp.getSettingProps() == null || temp.getSettingProps().size() == 0) {
				temp.setSettingProps(constructNewSettings(temp.getSettingProps()));
			}
			temp.getSettingProps().put(JAR_PATHS, validatePath(text));
			writeSettingFile(temp.getSettingFile(), temp.getSettingProps());
			temp.setUpdated(true);
		}
	}

	public String getRepoPath(MtWsDO rMtWsDO) {

		if (rMtWsDO.getSettingProps().getProperty(JAR_PATHS) == null) {
			if (rMtWsDO.getSettingFile() != null) {
				extractProperties(rMtWsDO.getSettingFile(), rMtWsDO.getSettingProps());
			}
			updateRepoPath(rMtWsDO, rMtWsDO.getJarPath());
		} else {
			return rMtWsDO.getSettingProps().get(JAR_PATHS) + "";
		}

		return rMtWsDO.getSettingProps().get(JAR_PATHS) + "";
	}

	void preserveSettingFiles(HashSet<MtWsDO> sMtWsDO) {
		for (MtWsDO iMtWsDO : sMtWsDO) {
			writeSettingFile(iMtWsDO.getSettingFile(), iMtWsDO.getSettingProps());
		}
	}

	public Properties appendProperties(IFile settingFile, Properties settingProps, SettingPropertiesEnum properties, String value) {
		switch (properties) {
		case JAR_PATHS:
			if (value != null)
				settingProps.put(JAR_PATHS, appendPaths(settingProps.getProperty(JAR_PATHS), value));
			break;
		case Savant_PROJECTS:
			if (value != null)
				settingProps.put(Savant_PROJECTS, appendSavantProjectsStr("", value));
			break;
		case IS_PATCHED:
			if (value != null)
				settingProps.put(IS_PATCHED, value);
			break;
		default:
			if (properties != null && value != null)
				settingProps.put(properties.toString(), value);

		}
		return settingProps;

	}

	void refreshFile(IFile settingFile) {
		try {
			if (settingFile != null) {

				settingFile.refreshLocal(IResource.DEPTH_ZERO, null);
				settingFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public FTPConfig getFtpProperties(Properties props) {
		FTPConfig ftpConfig = new FTPConfig();
		// props.put(SettingFileParser.FTP_PROFILE, ftpConfig.getFtpEnv() !=
		// null ? ftpConfig.getFtpEnv() : "");
		ftpConfig.setFtpEnv((String) (props.get(SettingFileParser.FTP_PROFILE) != null ? props.get(SettingFileParser.FTP_PROFILE) : ""));
		ftpConfig.setConfigPath((String) (props.get(SettingFileParser.CONFIG_PATH) != null ? props.get(SettingFileParser.CONFIG_PATH) : ""));
		ftpConfig.setServerName((String) (props.get(SettingFileParser.SERVER_NAME) != null ? props.get(SettingFileParser.SERVER_NAME) : ""));
		ftpConfig.setPort((String) (props.get(SettingFileParser.PORT_NUM) != null ? props.get(SettingFileParser.PORT_NUM) : ""));
		ftpConfig.setsSH((Boolean) ((Boolean) ((props.get(SettingFileParser.SSH) != null) ? props.get(SettingFileParser.SSH).equals("TRUE") : true) ? true : false));
		ftpConfig.setUserName((String) (props.get(SettingFileParser.USER_NAME) != null ? props.get(SettingFileParser.USER_NAME) : ""));
		ftpConfig.setPassword(Password.base64ToByte((String) (props.get(SettingFileParser.PASSWORD) != null ? props.get(SettingFileParser.PASSWORD) : "")));
		ftpConfig.setFtpPath((String) (props.get(SettingFileParser.FTP_PATH) != null ? props.get(SettingFileParser.FTP_PATH) : ""));
		ftpConfig.setPath((String) (props.get(SettingFileParser.JAR_PATH) != null ? props.get(SettingFileParser.JAR_PATH) : ""));
		if (ftpConfig.getServerName() == null || ftpConfig.getServerName().isEmpty())
			return null;
		return ftpConfig;
	}

	public void updateFtpSet(Set<FTPConfig> ftpConfigs, Properties props) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		String ftpConfigsString = null;
		try {
			os = new ObjectOutputStream(bos);
			os.writeObject(ftpConfigs);
			ftpConfigsString = bos.toString();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (props != null)
			props.put(FTP_SET, ftpConfigsString);

	}

	public TreeSet<FTPConfig> getFtpSet(Properties props) {
		TreeSet<FTPConfig> ftpConfigs = null;
		if (props.get(FTP_SET) != null && !"".equals(props.get(FTP_SET)))
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(((String) props.get(FTP_SET)).getBytes());
				ObjectInputStream oInputStream = new ObjectInputStream(bis);
				Object obj = oInputStream.readObject();
				if (obj instanceof Set<?>)
					ftpConfigs = (TreeSet<FTPConfig>) obj;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ftpConfigs;
	}

}
