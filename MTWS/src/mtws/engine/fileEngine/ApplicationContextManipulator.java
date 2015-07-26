package mtws.engine.fileEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import mtws.views.MtwsView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;

public class ApplicationContextManipulator {

	private static final String PROJECT_ENTRY_FIND = "module:/resource{{PROJECT_PATH}}{{PROJECT_PATH}}";
	private static final String PROJECT_ENTRY_REPLACE = "module:/resource{{PROJECT_PATH}}{{PROJECT_PATH}}";
	private static final String JAR_ENTRY_FIND = "module:/classpath/lib/.*?{{JAR_PATH}}";
	private static final String JAR_ENTRY_REPLACE = "module:/classpath/lib/{{JAR_PATH}}";
	private static final String PROJECT_PATH = "\\{\\{PROJECT_PATH\\}\\}";
	private static final String JAR_PATH = "\\{\\{JAR_PATH\\}\\}";
	
	public static final String APPLICATION_XML_FILE="/META-INF/application.xml";
	public static final String APPLICATION_XML_FILE_NAME="application.xml";
	public static final String COMPONENT_XML_FILE ="/.settings/org.eclipse.wst.common.component";
	public static final String COMPONENT_XML_FILE_NAME="org.eclipse.wst.common.component";
	public static final String ARCHIVES = ".*\\.(JAR|WAR|EAR)";


	public void updatecomponentReference(IFile componentXmlfile, HashMap<String, String> projects) {
		if(componentXmlfile != null && componentXmlfile.isReadOnly()){
			ResourceAttributes res = componentXmlfile.getResourceAttributes();
			res.setReadOnly(false);
			try {
				componentXmlfile.setResourceAttributes(res);
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ApplicationContextManipulator::updatecomponentReference  in setResourceAttributes ");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (componentXmlfile != null && !componentXmlfile.isReadOnly() && projects.size() > 0) {

			String content = readFile(componentXmlfile);
			content = searchAndReplaceProjectsWithJars4compenent(content, projects);
			writeFile(componentXmlfile, content);
			try {
				componentXmlfile.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ApplicationContextManipulator::updatecomponentReference  in refreshLocal ");
				e.printStackTrace();
			}
		}
		
	}
	String searchAndReplaceProjectsWithJars4compenent(String fileContent, HashMap<String, String> projects) {
		if (projects != null && projects.size() > 0) {
			Iterator<Entry<String, String>> prjs = projects.entrySet().iterator();
			while (prjs.hasNext()) {
				Entry<String, String> prj = prjs.next();
				String key = prj.getKey();
				String value = prj.getValue();
				if (value.toUpperCase().matches(ARCHIVES))
					fileContent = fileContent.replaceAll(getProjectExpr4Find(key), Matcher.quoteReplacement(getJarExpr4Replace(value)));
				else {
					key=key.substring(key.lastIndexOf(File.separator)+1, key.length());
					fileContent = fileContent.replaceAll(getJarExpr4Find(key), Matcher.quoteReplacement(getProjectExpr4Replace(value)));
				}
			}
		}
		return fileContent;
	}
	String getProjectExpr4Find(String prj) {
		return Matcher.quoteReplacement(PROJECT_ENTRY_FIND.replaceAll(PROJECT_PATH, Matcher.quoteReplacement(prj)));
	}
	String getProjectExpr4Replace(String prj) {
		return PROJECT_ENTRY_REPLACE.replaceAll(PROJECT_PATH, Matcher.quoteReplacement(prj));
	}
	String getJarExpr4Find(String jarPath) {
		return JAR_ENTRY_FIND.replaceAll(JAR_PATH, Matcher.quoteReplacement(Matcher.quoteReplacement(jarPath)));
	}
	String getJarExpr4Replace(String jarPath) {
		return JAR_ENTRY_REPLACE.replaceAll(JAR_PATH, Matcher.quoteReplacement(Matcher.quoteReplacement(jarPath)));
	}
	String readFile(IFile classPathFile) {
		StringBuffer sb = new StringBuffer();
		if (classPathFile !=null && classPathFile.exists()) {
			try {
				FileReader fr = new FileReader(classPathFile.getLocation().toFile());
				BufferedReader br = new BufferedReader(fr);
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str);
					sb.append("\r\n");
				}
			} catch (FileNotFoundException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ApplicationContextManipulator::readFile  & getting FileNotFoundException "+ classPathFile.getFullPath());
				e.printStackTrace();
			} catch (IOException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ApplicationContextManipulator::readFile  & getting IOException "+ classPathFile.getFullPath());
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	boolean writeFile(IFile classPathFile, String updateContent) {
		if (classPathFile !=null && classPathFile.exists()) {
			try {
				FileWriter fr = new FileWriter(classPathFile.getLocation().toFile());
				BufferedWriter br = new BufferedWriter(fr);
				br.write(updateContent);
				br.close();
				fr.close();

				return true;
			} catch (FileNotFoundException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ApplicationContextManipulator::writeFile  & getting FileNotFoundException "+ classPathFile.getFullPath());
				e.printStackTrace(System.err);
			} catch (IOException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ApplicationContextManipulator::writeFile  & getting IOException "+ classPathFile.getFullPath());
				e.printStackTrace(System.err);
			}
		}
		return false;
	}
}
