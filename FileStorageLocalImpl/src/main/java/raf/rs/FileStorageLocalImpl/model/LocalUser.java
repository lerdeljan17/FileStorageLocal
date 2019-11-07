package raf.rs.FileStorageLocalImpl.model;

import java.util.ArrayList;

import exceptions.PrivilageException;
import raf.rs.FIleStorageSpi.User;

public class LocalUser extends User {

	private FileStorageLocal fileStorage;

	public LocalUser(String username, String password,boolean isRoot, ArrayList<String> privilages) {
		super(username, password,isRoot, privilages);
	}

	@Override
	public boolean createNewUser(String username, String password) {
		fileStorage.getUsers().add(new User(username, password));
		return true;
	}

	@Override
	public boolean connectToFileStorage(String rootDirPath, String FileStorageRootDirName) throws Exception {
		this.fileStorage = new FileStorageLocal(rootDirPath, FileStorageRootDirName, this);
		return true;
	}

	@Override
	public boolean disconnectFromFileStorage(String fileStorageRootDir) {
		this.fileStorage.closeConnectionWithUser(this);
		this.fileStorage = null;
		return true;
	}
	
	@Override
	public void revokePrivilage(User revokeFromUser, String privilage) throws Exception {
		//TODO da li treba proveriti da li fileStorage postoji
		
		if(!fileStorage.getUsers().contains(revokeFromUser)) {
			throw new PrivilageException("Korisnik za koga zelite da povucete privilegije ne postoji u skladistu!");
		}
		
		User user = fileStorage.getUsers().get(fileStorage.getUsers().indexOf(revokeFromUser));
		
		if(!user.getPrivilages().contains(privilage)) {
			throw new PrivilageException("Korisnik nema privilegiju koju zelite da obrisete!");
		}
		
		user.getPrivilages().remove(privilage);		
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public FileStorageLocal getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(FileStorageLocal fileStorage) {
		this.fileStorage = fileStorage;
	}
}
