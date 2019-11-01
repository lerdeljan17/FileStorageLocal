package raf.rs.FileStorageLocalImpl;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;

import raf.rs.FIleStorageSpi.MyDir;
import raf.rs.FileStorageLocalImpl.model.MyLocalDirectory;
import raf.rs.FileStorageLocalImpl.model.MyLocalFile;

public class App {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		MyLocalDirectory rootDir = new MyLocalDirectory("C:\\Users\\subot\\Desktop", "root");
		
		String path = FilenameUtils.separatorsToSystem(rootDir.getPath() + "\\" + rootDir.getRootDirectoryName());
		MyLocalFile file = new MyLocalFile("Proba2", path, rootDir);
		
		try {
			file.downloadFile("C:\\Users\\subot\\Desktop\\root\\Proba", "C:\\Users\\subot\\Desktop\\root\\silvija14");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	//	rootDir.createMultipleDirectories(rootDir.getPath() + "\\" + rootDir.getRootDirectoryName(), "silvija", 20);
	
		/*
		try {
			rootDir.delDirectory("C:\\Users\\subot\\Desktop\\root", "silvija10");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
	//	File matches[] = rootDir.searchDirectory("C:\\Users\\subot\\Desktop\\root", "silvija");
	//	System.out.println(Arrays.toString(matches));
		
	//	rootDir.listDirectories("C:\\Users\\subot\\Desktop\\root");
	//	rootDir.listFiles("C:\\Users\\subot\\Desktop\\root");
		
	//	System.out.println(rootDir.getFilesWithExtension("C:\\Users\\subot\\Desktop\\root", "txt"));
		
	//	rootDir.downloadDirectory("C:\\Users\\subot\\Desktop\\root\\silvija5", "C:\\Users\\subot\\Desktop\\silvija5");
		
	//	System.out.println(rootDir.getAllFiles(true, "C:\\Users\\subot\\Desktop\\root"));
	}

}
