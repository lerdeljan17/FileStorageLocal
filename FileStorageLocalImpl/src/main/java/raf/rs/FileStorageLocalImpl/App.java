package raf.rs.FileStorageLocalImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import raf.rs.FIleStorageSpi.MyDir;
import raf.rs.FIleStorageSpi.MyFile;
import raf.rs.FIleStorageSpi.User;
import raf.rs.FileStorageLocalImpl.model.FileStorageLocal;
import raf.rs.FileStorageLocalImpl.model.LocalUser;
import raf.rs.FileStorageLocalImpl.model.MyLocalDirectory;
import raf.rs.FileStorageLocalImpl.model.MyLocalFile;

public class App {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		MyLocalDirectory rootDir = new MyLocalDirectory("C:\\Users\\laxy9\\Desktop\\root", "noviroot");
		ArrayList<String> prv = new ArrayList<String>();
		prv.add("sdad");
		LocalUser user = new LocalUser("silvija", "slaco", prv);
		//FileStorageLocal fsl = new FileStorageLocal(rootDir, "stogod",user);
		try {
			user.connectToFileStorage(rootDir.getPath().toString(), "noviroot");
			user.disconnectFromFileStorage(null);
			//MyLocalFile file = new MyLocalFile(null, null, null);
			//file.uploadArchive("C:\\Users\\laxy9\\Desktop\\try.zip", "C:\\Users\\laxy9\\Desktop\\root");
			//fsl.openConnectionWithUser(user);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		String path = FilenameUtils.separatorsToSystem(rootDir.getPath() + "\\" + rootDir.getRootDirectoryName());
//		MyLocalFile file = new MyLocalFile("Proba3.txt", path, rootDir);
//		Hashtable<String, String> metaData = new Hashtable<String, String>();
//		metaData.put("Silvija", "188cm");
//		metaData.put("Lazar", "190cm");
//		System.out.println(file.getPath().toString());
//		file.addMetaData(file.getPath().toString(), metaData);
		/*List<File> files = new ArrayList<File>();
		files.add(new File("C:\\Users\\subot\\Desktop\\root\\hajde 2"));
		files.add(new File("C:\\Users\\subot\\Desktop\\root\\hajde 4"));
		files.add(new File("C:\\Users\\subot\\Desktop\\root\\hajde 6"));
		files.add(new File("C:\\Users\\subot\\Desktop\\root\\hajde 8"));
		files.add(new File("C:\\Users\\subot\\Desktop\\root\\silvija13"));*/
		try {
			//file.uploadFilesAsArchive("try", "C:\\Users\\subot\\Desktop", files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//file.uploadArchive("C:\\Users\\subot\\Desktop\\try", "C:\\Users\\subot\\Desktop\\queue");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		try {
			file.createMultipleFiles(path, "hajde", 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		/*
		try {
			file.downloadFile("C:\\Users\\subot\\Desktop\\root\\Proba", "C:\\Users\\subot\\Desktop\\root\\silvija14");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
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
