package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.util.List;

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
		File dir = new File(path+"\\" +rootDirName);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
                return false;
            }
        }
        boolean storageFile = new File(dir.getPath().toString()+"\\"+rootDirName+".settings").createNewFile();
		return true;
	}

	public File searchDirectory(String dirPath, String searchFor) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean createMultipleDirectories(String path, String dirsName, int numberOfDirs) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createEmptyDir(String path, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delDir(String ToDelPath, String dirName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean downloadDir(String pathSource, String pathDest) {
		// TODO Auto-generated method stub
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

}
