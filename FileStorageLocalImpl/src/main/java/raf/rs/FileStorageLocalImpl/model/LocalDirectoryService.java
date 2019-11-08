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
import org.apache.commons.io.filefilter.TrueFileFilter;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DownloadException;
import exceptions.NotFoundException;
import exceptions.PrivilageException;
import exceptions.StorageInitException;
import raf.rs.FIleStorageSpi.MyDir;
import raf.rs.FIleStorageSpi.PrivilageType;

public class LocalDirectoryService implements MyDir {

	private String path;
	private String name;
	private File settingsFile;
	private FileStorageLocal storage;

	public LocalDirectoryService(FileStorageLocal storage) {
		super();
		// this.path = FilenameUtils.separatorsToSystem(path);
		// this.name = name;
		this.storage = storage;
	}

	private boolean checkPrivilage(PrivilageType prv) {
		if(storage.getCurrentUser().getPrivilages().contains(prv)) return true;
		return false;
	}
	
	public File[] searchDirectory(String dirPath, String searchFor) {
		final String search = searchFor;
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + dirPath);
		File dir = new File(path);

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains(search);
			}
		});
		return matches;
	}

	public boolean createMultipleDirectories(String dirPath, String dirsName, int numberOfDirs) throws Exception {
		if(!checkPrivilage(PrivilageType.CREATE)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		if (numberOfDirs <= 0) {
			throw new CreateException();
		}
		String path = FilenameUtils.separatorsToSystem(dirPath);
		for (int i = 0; i < numberOfDirs; i++) {
			File dir = createEmptyDirectory(path, dirsName + " " + i);
		}
		return true;
	}

	public File createEmptyDirectory(String dirPath, String fileName) throws Exception{
		if(!checkPrivilage(PrivilageType.CREATE)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + dirPath + "\\" + fileName);
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
		if(!checkPrivilage(PrivilageType.DELETE)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + ToDelPath + "\\" + dirName);
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

	public boolean downloadDirectory(String pathSource, String pathDest) throws Exception {
		if(!checkPrivilage(PrivilageType.DOWNLOAD)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		String newPathSource = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + pathSource);
		String newPathDest = FilenameUtils.separatorsToSystem(pathDest);
		File sourceFile = new File(newPathSource);
		File destFile = new File(newPathDest);
		try {
			FileUtils.copyDirectory(sourceFile, destFile);
			System.out.println("Direktorijum se skinut na putanju (" + newPathDest + ")");
		} catch (IOException e) {
			throw new DownloadException();
			// e.printStackTrace();
		}
		return true;
	}

	public String listDirectories(String dirPath) {
		if(!checkPrivilage(PrivilageType.READ)) {
			System.out.println("Nemate privilegiju");
			return "";
		}
		
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + dirPath);
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

	public String listFiles(String directoryPath, boolean withMetaData) {
		if(!checkPrivilage(PrivilageType.DELETE)) {
			System.out.println("Nemate privilegiju");
			return "";
		}
		
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + directoryPath);
		if (directoryPath.equals("")) {
			path = path.substring(0, path.length() - 1);
		}
		System.out.println("listFiles path: " + path);
		File file = new File(path);

		Collection<File> toRet = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, null);
		/*
		 * String[] directories = file.list(new FilenameFilter() {
		 * 
		 * @Override public boolean accept(File current, String name) { return new
		 * File(current, name).isFile(); } });
		 */
		// toRet.toArray(directories);
		// System.out.println("toRet: " + Arrays.toString(directories));
		/*
		 * if (withMetaData) { System.out.println("if u listFiles: " + withMetaData);
		 * for (int i = 0; i < directories.length; i++) { File tmp = new
		 * File(directories[i] + ".metaData"); if (!tmp.exists()) { toRet.remove(i); } }
		 * }
		 */
		// System.out.println(Arrays.toString(directories));
		return toRet.toString();
	}


	public List<String> getAllFiles(boolean sorted, String dirPath) throws Exception {
		if(!checkPrivilage(PrivilageType.DOWNLOAD)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + dirPath);
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

	public List<File> getFilesWithExtension(String dirPath, String extension) {
		if(!checkPrivilage(PrivilageType.DELETE)) {
			System.out.println("Nemate privilegiju");
			return new ArrayList<File>();
		}
		
		extension = extension.replace(".", "");
		String[] ext = { extension.toLowerCase() };
		String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + dirPath);
		File file = new File(path);
		List<File> files = (List<File>) FileUtils.listFiles(file, ext, false);
		// TODO Da li treba da se vrati lista imena ili lista fajlova?
		return files;
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
		//Ne implementirati ovde
		return null;
	}

	@Override
	public boolean createEmptyDirectoryB(String path, String fileName) throws Exception {
		//Ne implementirati ovde
		return false;
	}

	public FileStorageLocal getStorage() {
		return storage;
	}

	public void setStorage(FileStorageLocal storage) {
		this.storage = storage;
	}

	

	// public String getFilesWithMetadata(boolean withMetaData) {

	// return null;
	// }

}
