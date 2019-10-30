package raf.rs.FileStorageLocalImpl;

import java.io.File;
import java.util.Arrays;

import raf.rs.FIleStorageSpi.myDir;
import raf.rs.FileStorageLocalImpl.model.myLocalDirectory;

public class App {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		myLocalDirectory rootDir = new myLocalDirectory("C:\\Users\\subot\\Desktop", "root");
	//	rootDir.createMultipleDirectories(rootDir.getPath() + "\\" + rootDir.getRootDirectoryName(), "silvija", 20);
		rootDir.delDirectory("C:\\Users\\subot\\Desktop\\root", "silvija5");
	//	File matches[] = rootDir.searchDirectory("C:\\Users\\subot\\Desktop\\root", "silvija");
	//	System.out.println(Arrays.toString(matches));
		rootDir.listDirectories("C:\\Users\\subot\\Desktop\\root");
		rootDir.listFiles("C:\\Users\\subot\\Desktop\\root");
		System.out.println(rootDir.getFilesWithExtension("C:\\Users\\subot\\Desktop\\root", "txt"));
	//	rootDir.downloadDirectory("C:\\Users\\subot\\Desktop\\root\\silvija5", "C:\\Users\\subot\\Desktop\\silvija5");
	}

}
