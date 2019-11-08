package raf.rs.FileStorageLocalImpl;

import java.util.Arrays;
import java.util.Hashtable;

import raf.rs.FIleStorageSpi.PrivilageType;
import raf.rs.FIleStorageSpi.User;
import raf.rs.FileStorageLocalImpl.model.FileStorageLocal;
import raf.rs.FileStorageLocalImpl.model.LocalUser;
import raf.rs.FileStorageLocalImpl.model.LocalDirectoryService;
import raf.rs.FileStorageLocalImpl.model.LocalFileService;

public class App {

	public static void main(String[] args) {
		
		LocalUser admin = new LocalUser("Student", "Studentic", true);
	//	System.out.println(admin);
		
		try {
			FileStorageLocal storage = new FileStorageLocal("C://Users//subot//Desktop//Lokalni", true, admin);
			System.out.println(storage.getUsers());
			User newUser = admin.createNewUser(admin, "Maleni", "Mali");
			admin.addPrivilage(newUser, PrivilageType.UPLOAD);
			admin.addPrivilage(newUser, PrivilageType.CREATE);
			admin.revokePrivilage(newUser, PrivilageType.READ);
			System.out.println(newUser);
			System.out.println(storage.getUsers());
			LocalDirectoryService md = new LocalDirectoryService(storage);
			md.createMultipleDirectories("", "silvija", 10);
			md.delDirectory("", "silvija5");
			md.createEmptyDirectory("", "lacaaa");
			System.out.println(Arrays.toString(md.searchDirectory("", "silvija")));
			
			md.downloadDirectory("silvija3", "C:\\Users\\subot\\Desktop\\silvija3");
			//System.out.println(md.getAllFiles(true, ""));
			LocalFileService mf = new LocalFileService(storage);
			mf.createMultipleFiles("", "probe", 10);
			mf.delFile("", "probe 5");
			Hashtable<String, String> metaData = new Hashtable<String, String>();
			metaData.put("laca", "190");
			metaData.put("sicko", "188");
			mf.createMetaDataFile("probe 7","probe 7", metaData);
			mf.addMetaData("probe 7.metaData", metaData);
			mf.uploadFile("C:\\Users\\subot\\Desktop\\App.txt", "");//prosledjuje se dir na koji se uploaduje nema potrebe za imenom fajla
			System.out.println(md.listFiles("", false));
			/*
			System.out.println("Konekt: " + storage.getConnectedUsers());
			System.out.println("Konekt: " + storage.getUsers());
			admin.disconnectFromFileStorage(storage.getRootDirPath());
			System.out.println("Diskonekt " + storage.getConnectedUsers());
			System.out.println("Diskonekt: " + storage.getUsers());
			admin.connectToFileStorage("C://Users//subot//Desktop//Lokalni");
			System.out.println("Konekt: " + storage.getConnectedUsers());
			System.out.println("Konekt: " + storage.getUsers());
			*/
			md.createEmptyDirectory("", "posle connecta");
			
			System.out.println(storage.getForbiddenExtensions());
			storage.addForbiddenExtension(".zip");
			System.out.println(storage.getForbiddenExtensions());
			mf.uploadArchive("C:\\Users\\subot\\Desktop\\Desktop.zip", "");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
