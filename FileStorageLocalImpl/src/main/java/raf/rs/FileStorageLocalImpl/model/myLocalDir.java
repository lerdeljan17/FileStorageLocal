package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.io.FileUtils;

import raf.rs.FIleStorageSpi.myDir;

public class myLocalDir implements myDir{
	private String path;
	private String rootDirName;

	public myLocalDir(String path, String rootDirName) {
		super();
		this.path = path;
		this.rootDirName = rootDirName;
		try {
			boolean b = initFileStorage(path, rootDirName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean initFileStorage(String path, String rootDirName) throws Exception {
		// TODO Auto-generated method stub
		File dir = createEmptyDir(path, rootDirName);
        boolean storageFile = new File(dir.getPath().toString()+"\\"+rootDirName+".settings").createNewFile();
		return true;
	}

	public File[] searchDirectory(String dirPath, String searchFor) {
		final String search = searchFor;
		// TODO Auto-generated method stub
		File dir = new File(dirPath);

		File[] matches = dir.listFiles(new FilenameFilter()
		{
		  public boolean accept(File dir, String name)
		  {
		     return name.contains(search);
		  }
		});
		return matches;
	}

	public boolean createMultipleDirectories(String path, String dirsName, int numberOfDirs) {
		for (int i = 0; i < numberOfDirs; i++) {
			File dir = createEmptyDir(path, dirsName+i);
		            
		}
		// TODO Auto-generated method stub
		return true;
	}

	public File createEmptyDir(String path, String fileName) {
		// TODO Auto-generated method stub
		File dir = new File(path+"\\" +fileName);
		 if (!dir.exists()) {
			 	System.out.println(dir.toString());
	            if (dir.mkdir()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	                
	            }
	            
	}
		return dir;
	}

	public boolean delDir(String ToDelPath, String dirName) {
		File toDel = new File(ToDelPath+"\\"+dirName);
		String[]entries = toDel.list();
		for(String s: entries){
		    File currentFile = new File(toDel.getPath(),s);
		    currentFile.delete();
		}
	        if(toDel.delete()) 
	        { 
	            System.out.println("File deleted successfully"); 
	        } 
	        else
	        { 
	            System.out.println("Failed to delete the file"); 
	            return false;
	        } 
		// TODO Auto-generated method stub
		return true;
	}

	public boolean downloadDir(String pathSource, String pathDest) {
		// TODO Auto-generated method stub
		File sourceFile = new File(pathSource);
		File destFile = new File(pathDest);
		try {
			FileUtils.moveDirectory(sourceFile, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public String listDirs(String dirPathToList) {
		// TODO Auto-generated method stub
		return null;
	}

	public String listFiles(String dirPathTolist) {
		// TODO Auto-generated method stub
		return null;
	}

	public File getFilesWithExtension(String extension) {
		// TODO Auto-generated method stub
		return null;
	}

	public File getFilesWithMetadata(boolean withMetaData) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<File> getAllFiles(boolean Sorted, String fromDirPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isFileStorageRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRootDirName() {
		return rootDirName;
	}

	public void setRootDirName(String rootDirName) {
		this.rootDirName = rootDirName;
	}

}
