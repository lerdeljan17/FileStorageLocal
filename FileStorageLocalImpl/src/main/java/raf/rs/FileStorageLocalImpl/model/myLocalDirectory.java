package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import exceptions.ExceptionHandler;
import raf.rs.FIleStorageSpi.myDir;

public class myLocalDirectory implements myDir {
	
	private String path;
	private String rootDirName;

	public myLocalDirectory(String path, String rootDirName) {
		super();
		this.path = path;
		this.rootDirName = rootDirName;
		try {
			boolean b = initFileStorage(path, rootDirName);
		} catch (Exception e) {
			ExceptionHandler.storageInitException();
			//e.printStackTrace();
		}
	}

	public boolean initFileStorage(String path, String rootDirName) throws Exception {
		File dir = createEmptyDirectory(path, rootDirName);
		boolean storageFile = new File(dir.getPath().toString() + "\\" + rootDirName + ".settings").createNewFile();
		return true;
	}

	public File[] searchDirectory(String dirPath, String searchFor) {
		final String search = searchFor;
		File dir = new File(dirPath);

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains(search);
			}
		});
		return matches;
	}

	public boolean createMultipleDirectories(String path, String dirsName, int numberOfDirs) {
		for (int i = 0; i < numberOfDirs; i++) {
			File dir = createEmptyDirectory(path, dirsName + i);
		}
		return true;
	}

	public File createEmptyDirectory(String path, String fileName) {
		File dir = new File(path + "\\" + fileName);
		if (!dir.exists()) {
			System.out.println(dir.toString());
			if (dir.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				ExceptionHandler.messageException("Greska prilikom kreiranje direktorijuma!");
			}
		}
		return dir;
	}

	public boolean delDirectory(String ToDelPath, String dirName){
		File toDel = new File(ToDelPath + "\\" + dirName);
		if(!toDel.exists()) {
			ExceptionHandler.noFilesFoundException();
			return false;
		}
		String[] entries = toDel.list();
		for (String s : entries) {
			File currentFile = new File(toDel.getPath(), s);
			currentFile.delete();
		}
		if (toDel.delete()) {
			System.out.println("File deleted successfully");
		} else {
			ExceptionHandler.messageException("Greska prilikom brisanja direktorijuma!");
			return false;
		}
		return true;
	}

	public boolean downloadDirectory(String pathSource, String pathDest) {
		File sourceFile = new File(pathSource);
		File destFile = new File(pathDest);
		try {
			FileUtils.copyDirectory(sourceFile, destFile);
		} catch (IOException e) {
			ExceptionHandler.messageException("Greska prilikom preuzimanja direktorijuma!");
			//e.printStackTrace();
		}
		return false;
	}

	public String listDirectories(String directoryPath) {
		File file = new File(directoryPath);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		System.out.println(Arrays.toString(directories));
		return Arrays.toString(directories);
	}

	public String listFiles(String directoryPath) {
		File file = new File(directoryPath);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isFile();
		  }
		});
		System.out.println(Arrays.toString(directories));
		return Arrays.toString(directories);
	}

	public List<File> getFilesWithExtension(String path, String extension) {
		String [] ext = {extension};
		File file = new File(path);
		List<File> files = (List<File>) FileUtils.listFiles(file, ext, false);
		// TODO Da li treba da se vrati lista imena ili lista fajlova?
		return files;
	}

	public File getFilesWithMetadata(boolean withMetaData) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<File> getAllFiles(boolean sorted, String fromDirectoryPath) {
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

	public String getRootDirectoryName() {
		return rootDirName;
	}

	public void setRootDirectoryName(String rootDirName) {
		this.rootDirName = rootDirName;
	}

}
