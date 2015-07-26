package mtws.data;

public enum SettingPropertiesEnum {
	JAR_PATHS("jarPaths"), Savant_PROJECTS("SavantProjects"), IS_PATCHED("isPatched"), CONFIG_PATH("ConfigPath"),
	SERVER_NAME("ServerName"),PORT_NUM("PortNum"),USER_NAME("UserName"),PASSWORD("HashCode"),FTP_PATH("FtpPath"),
	JAR_PATH("jarPath"),	BUILD_SWITCH("buildSwitch"),SCRIPT_SWITCH("scriptSwitch"), SCRIPT_PATH("scriptPath"), 
	DOWNLOAD_SWITCH("downloadSwitch"), INSTALL_SWITCH("installSwitch"),SERVER_INSTANCE("serverInstance");
	private final String text;

    /**
     * @param text
     */
    private SettingPropertiesEnum(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
