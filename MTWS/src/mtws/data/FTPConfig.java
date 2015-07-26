package mtws.data;

import java.io.Serializable;

public class FTPConfig implements Comparable<FTPConfig>, Serializable {

	private static final long serialVersionUID = 1L;
	String configPath="", serverName="", port="", userName="", password="", path="", ftpPath="", errorMessage="", ftpEnv="";
	private boolean sSH=true, valid=false;

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the sSH
	 */
	public boolean issSH() {
		return sSH;
	}

	/**
	 * @param sSH
	 *            the sSH to set
	 */
	public void setsSH(boolean sSH) {
		this.sSH = sSH;
	}

	/**
	 * @return the configPath
	 */
	public String getConfigPath() {
		return configPath;
	}

	/**
	 * @param configPath
	 *            the configPath to set
	 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName
	 *            the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the ftpPath
	 */
	public String getFtpPath() {
		return ftpPath;
	}

	/**
	 * @param ftpPath
	 *            the ftpPath to set
	 */
	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	/**
	 * @return the ftpEnv
	 */
	public String getFtpEnv() {
		return ftpEnv;
	}

	/**
	 * @param ftpEnv
	 *            the ftpEnv to set
	 */
	public void setFtpEnv(String ftpEnv) {
		this.ftpEnv = ftpEnv;
	}

	@Override
	public int compareTo(FTPConfig o) {
		int i =0;
		if(ftpEnv.compareTo(o.getFtpEnv())!=0){
		/* i = serverName.compareTo(o.getServerName()) + port.compareTo(o.getPort())
				+ ftpPath.compareTo(o.getFtpPath());*/
			
			return 1;
		}
		if (i == 0)
			return 0;
			return 1;
	}

}
