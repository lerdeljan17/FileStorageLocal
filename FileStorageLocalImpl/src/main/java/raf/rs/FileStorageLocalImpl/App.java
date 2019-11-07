package raf.rs.FileStorageLocalImpl;

import raf.rs.FIleStorageSpi.PrivilageType;
import raf.rs.FIleStorageSpi.User;
import raf.rs.FileStorageLocalImpl.model.FileStorageLocal;
import raf.rs.FileStorageLocalImpl.model.LocalUser;

public class App {

	public static void main(String[] args) {
		
		LocalUser admin = new LocalUser("Student", "Studentic", true);
	//	System.out.println(admin);
		
		try {
			FileStorageLocal storage = new FileStorageLocal("C://Users//subot//Desktop//Lokalni", true, admin);
			System.out.println(storage.getUsers());
			User newUser = admin.createNewUser("Mali", "Maleni");
			admin.addPrivilage(newUser, PrivilageType.UPLOAD);
			admin.addPrivilage(newUser, PrivilageType.CREATE);
			admin.revokePrivilage(newUser, PrivilageType.READ);
			System.out.println(newUser);
			System.out.println(storage.getUsers());
			admin.disconnectFromFileStorage(storage.getRootDirPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
