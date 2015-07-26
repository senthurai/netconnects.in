package mtws.engine.fileEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import mtws.views.MtwsView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;

public class ClassPathManipulator {

	private static final String PROJECT_ENTRY_FIND = "<classpathentry[^>]*?['/\\\"]{{PROJECT_PATH}}['\"][^>]*/>";
	private static final String PROJECT_ENTRY_REPLACE = "<classpathentry kind=\"src\" combineaccessrules=\"false\" path=\"{{PROJECT_PATH}}\"/>";
	private static final String JAR_ENTRY_FIND = "<classpathentry[^>]*?['\\\\\"/]{{JAR_PATH}}['\"][^>]*/>";
	private static final String JAR_ENTRY_REPLACE = "<classpathentry kind=\"lib\" path=\"{{JAR_PATH}}\"/>";
	private static final String PROJECT_PATH = "\\{\\{PROJECT_PATH\\}\\}";
	private static final String JAR_PATH = "\\{\\{JAR_PATH\\}\\}";
	public static final String CLASS_PATH = "/.classpath";
	public static final String CLASS_PATH_FILE_NAME = "/.classpath";
	public static final String ARCHIVES = ".*\\.(JAR|WAR|EAR)";

	public String searchJars(HashSet<String> jarPath, final String prjName) {
		for (String jarPath2 : jarPath) {
			File dir = new File(jarPath2);
			File[] files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(prjName + ".");
				}
			});
			if (files != null && files.length > 0 && files[0]!=null && files[0].isFile() )
				return files[0].getAbsolutePath();
		}
		return "";
	}

	String searchAndReplaceProjectsWithJars(String fileContent, HashMap<String, String> projects) {
		if (projects != null && projects.size() > 0) {
			Iterator<Entry<String, String>> prjs = projects.entrySet().iterator();
			while (prjs.hasNext()) {
				Entry<String, String> prj = prjs.next();
				String key = prj.getKey();
				String value = prj.getValue();
				if (value.toUpperCase().matches(ARCHIVES)) {
					String jarName = value.substring(value.lastIndexOf(File.separator) + 1, value.length());
					fileContent = fileContent.replaceAll(getJarExpr4Find(jarName), Matcher.quoteReplacement(getProjectExpr4Replace(key)));
					fileContent = fileContent.replaceAll(getProjectExpr4Find(key), Matcher.quoteReplacement(getJarExpr4Replace(value)));
				} else {
					key = key.substring(key.lastIndexOf(File.separator) + 1, key.length());
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
		if (classPathFile.exists()) {
			try {
				FileReader fr = new FileReader(classPathFile.getLocation().toFile());
				BufferedReader br = new BufferedReader(fr);
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str);
					sb.append("\r\n");
				}
			} catch (FileNotFoundException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::readFile  & getting FileNotFoundException "+ classPathFile.getFullPath());
				e.printStackTrace();
			} catch (IOException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::readFile  & getting IOException "+ classPathFile.getFullPath());
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void updateJarReference(IFile classPathFile, HashMap<String, String> projects) {
		if(classPathFile != null && classPathFile.isReadOnly()){
			ResourceAttributes res = classPathFile.getResourceAttributes();
			res.setReadOnly(false);
			try {
				classPathFile.setResourceAttributes(res);
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::updateJarReference -> setResourceAttributes  & getting CoreException "+ classPathFile.getFullPath());
				e.printStackTrace();
			}
		}
		if (classPathFile != null && !classPathFile.isReadOnly() && projects.size() > 0) {

			String content = readFile(classPathFile);
			content = searchAndReplaceProjectsWithJars(content, projects);
			writeFile(classPathFile, content);
			try {
				classPathFile.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::updateJarReference ->refreshLocal & getting CoreException "+ classPathFile.getFullPath());
				e.printStackTrace();
			}
		}

	}

	boolean writeFile(IFile classPathFile, String updateContent) {
		if (classPathFile.exists()) {
			try {
				FileWriter fr = new FileWriter(classPathFile.getLocation().toFile());
				BufferedWriter br = new BufferedWriter(fr);
				br.write(updateContent);
				br.close();
				fr.close();

				return true;
			} catch (FileNotFoundException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::writeFile  & getting FileNotFoundException "+ classPathFile.getFullPath());
				e.printStackTrace(System.err);
			} catch (IOException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::writeFile  & getting IOException "+ classPathFile.getFullPath());
				e.printStackTrace(System.err);
			}
		}
		return false;
		
	}

	public void updateResetJarReference(IFile classPathFile, HashMap<String, String> projects) {
		// TODO Auto-generated method stub

		if(classPathFile != null && classPathFile.isReadOnly()){
			ResourceAttributes res = classPathFile.getResourceAttributes();
			res.setReadOnly(false);
			try {
				classPathFile.setResourceAttributes(res);
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::updateResetJarReference ->setResourceAttributes & getting CoreException "+ classPathFile.getFullPath());

				e.printStackTrace();
			}
		}
		if (classPathFile != null && !classPathFile.isReadOnly() && projects.size() > 0) {

			String content = readFile(classPathFile);
			content = searchAndReplaceProjectsWithJars(content, projects);
			writeFile(classPathFile, content);
			try {
				classPathFile.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				MtwsView.debug.println(new Date() + ": Error while doing ClassPathManipulator::updateResetJarReference ->refreshLocal & getting CoreException "+ classPathFile.getFullPath());
				e.printStackTrace();
			}
		}

	
	}

}
