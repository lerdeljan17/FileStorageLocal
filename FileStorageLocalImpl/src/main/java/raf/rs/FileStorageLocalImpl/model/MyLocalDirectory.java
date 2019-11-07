package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DownloadException;
import exceptions.NotFoundException;
import exceptions.StorageInitException;
import raf.rs.FIleStorageSpi.MyDir;

public class MyLocalDirectory implements MyDir {

	private String path;
	private String name;
	private File settingsFile;

	public MyLocalDirectory(String path, String name) {
		super();
		this.path = FilenameUtils.separatorsToSystem(path);
		this.name = name;
		try {
			boolean b = initFileStorage(path, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean initFileStorage(String dirPath, String rootDirName) throws StorageInitException {
		String path = FilenameUtils.separatorsToSystem(dirPath);
		File dir = new File(path);
		try {
			if (!dir.exists()) {
				dir = createEmptyDirectory(path, rootDirName);
			} else {
				this.settingsFile = new File(dir.getPath().toString() + "\\" + rootDirName +"\\"+ rootDirName + ".settings");
			}
		} catch (CreateException e1) {
			e1.printStackTrace();
		}
//		try {
//			boolean storageFile = new File(dir.getPath().toString() + "\\" + rootDirName + ".settings").createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return true;
	}

	public File[] searchDirectory(String dirPath, String searchFor) {
		final String search = searchFor;
		String path = FilenameUtils.separatorsToSystem(dirPath);
		File dir = new File(path);

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains(search);
			}
		});
		return matches;
	}

	public boolean createMultipleDirectories(String dirPath, String dirsName, int numberOfDirs) throws CreateException {
		if (numberOfDirs <= 0) {
			throw new CreateException();
		}
		String path = FilenameUtils.separatorsToSystem(dirPath);
		for (int i = 0; i < numberOfDirs; i++) {
			File dir = createEmptyDirectory(path, dirsName + i);
		}
		return true;
	}

	public File createEmptyDirectory(String dirPath, String fileName) throws CreateException {
		String path = FilenameUtils.separatorsToSystem(dirPath + "\\" + fileName);
		File dir = new File(path);
		if (!dir.exists()) {
			// System.out.println(dir.toString());
			if (dir.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				throw new CreateException();
			}
		}
		return dir;
	}

	public boolean delDirectory(String ToDelPath, String dirName) throws Exception {
		String path = FilenameUtils.separatorsToSystem(ToDelPath + "\\" + dirName);
		File toDel = new File(path);
		if (!toDel.exists()) {
			throw new NotFoundException(dirName);
		}
		String[] entries = toDel.list();
		for (String s : entries) {
			File currentFile = new File(toDel.getPath(), s);
			currentFile.delete();
		}
		if (toDel.delete()) {
			System.out.println("File deleted successfully");
		} else {
			throw new DeleteException();
		}
		return true;
	}

	public boolean downloadDirectory(String pathSource, String pathDest) throws DownloadException {
		String newPathSource = FilenameUtils.separatorsToSystem(pathSource);
		String newPathDest = FilenameUtils.separatorsToSystem(pathDest);
		File sourceFile = new File(newPathSource);
		File destFile = new File(newPathDest);
		try {
			FileUtils.copyDirectory(sourceFile, destFile);
		} catch (IOException e) {
			throw new DownloadException();
			// e.printStackTrace();
		}
		return true;
	}

	public String listDirectories(String dirPath) {
		String path = FilenameUtils.separatorsToSystem(dirPath);
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		// System.out.println(Arrays.toString(directories));
		return Arrays.toString(directories);
	}

	public String listFiles(String directoryPath,boolean withMetaData) {
		String path = FilenameUtils.separatorsToSystem(directoryPath);
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		ArrayList<String> toRet = new ArrayList<String>();
		toRet.toArray(directories);
		if(withMetaData) {
		for (int i = 0; i < directories.length; i++) {
			File tmp = new File(directories[i]+".metaData");
			if(!tmp.exists()) {
				toRet.remove(i);
			}
		}}
		// System.out.println(Arrays.toString(directories));
		return toRet.toString();
	}

	public List<File> getFilesWithExtension(String dirPath, String extension) {
		String[] ext = { extension };
		String path = FilenameUtils.separatorsToSystem(dirPath);
		File file = new File(path);
		List<File> files = (List<File>) FileUtils.listFiles(file, ext, false);
		// TODO Da li treba da se vrati lista imena ili lista fajlova?
		return files;
	}

	//public String getFilesWithMetadata(boolean withMetaData) {
		
		//return null;
	//}

	public List<String> getAllFiles(boolean sorted, String dirPath) throws Exception {
		String path = FilenameUtils.separatorsToSystem(dirPath);
		File root = new File(path);
		// Za slucaj da se na prosledjenoj putanji ne nalazi direktorijum
		if (!root.isDirectory()) {
			throw new NotFoundException(dirPath);
		}

		List<File> files = (List<File>) FileUtils.listFiles(root, null, true);
		if (files.isEmpty()) {
			return null;
		}

		List<String> filesName = new ArrayList<String>();
		for (File f : files) {
			filesName.add(f.getName());
		}

		if (sorted) {
			Collections.sort(filesName);
		}

		return filesName;
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

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getWholePath() {
		return FilenameUtils.separatorsToSystem(this.path + "\\" + name);
	}

	public File getSettingsFile() {
		return settingsFile;
	}

	public void setSettingsFile(File settingsFile) {
		this.settingsFile = settingsFile;
	}

	@Override
	public File getFilesWithMetadata(boolean withMetaData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createEmptyDirectoryB(String path, String fileName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
