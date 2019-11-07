package raf.rs.FileStorageLocalImpl.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.CustomException;
import exceptions.PrivilageException;
import exceptions.StorageInitException;
import raf.rs.FIleStorageSpi.PrivilageType;
import raf.rs.FIleStorageSpi.User;

public class LocalUser extends User {

	private FileStorageLocal fileStorage;

	public LocalUser(String username, String password, boolean isRoot) {
		super(username, password, isRoot, new ArrayList<PrivilageType>());
		
		if(isRoot) {
			super.getPrivilages().add(PrivilageType.ADD);
			super.getPrivilages().add(PrivilageType.DELETE);
			super.getPrivilages().add(PrivilageType.READ);
			super.getPrivilages().add(PrivilageType.CREATE);
			super.getPrivilages().add(PrivilageType.UPLOAD);
			super.getPrivilages().add(PrivilageType.DOWNLOAD);
		}
	}

	@Override
	public User createNewUser(String username, String password) {
		User user = new User(username, password);
		fileStorage.getUsers().add(user);
		return user;
	}

	@Override
	public boolean connectToFileStorage(String rootPath) throws Exception {
		this.fileStorage = new FileStorageLocal(rootPath, false, this);
		return true;
	}

	@Override
	public boolean disconnectFromFileStorage(String rootPath) throws Exception {
		if(fileStorage == null) {
			throw new CustomException("Nije moguce diskonektovati se sa skladista!");
		}
		
		this.fileStorage.closeConnectionWithUser(this);
		this.fileStorage = null;
		return true;
	}
	
	@Override
	public void addPrivilage(User user, PrivilageType privilage) throws Exception {
		if(fileStorage == null) {
			throw new StorageInitException("Ne postoji skladiste!");
		}
				
		if (!fileStorage.getUsers().contains(user)) {
			throw new PrivilageException("Korisnik za koga zelite da dodate privilegije ne postoji u skladistu!");
		}
		
		int index = fileStorage.getUsers().indexOf(user);
		
		if (!fileStorage.getUsers().get(index).getPrivilages().contains(privilage)) {
			throw new PrivilageException("Korisnik vec ima privilegiju koju zelite da dodate!");
		}
		
		fileStorage.getUsers().get(index).getPrivilages().add(privilage);
	}

	@Override
	public void revokePrivilage(User revokeFromUser, PrivilageType privilage) throws Exception {
		if(fileStorage == null) {
			throw new StorageInitException("Ne postoji skladiste!");
		}
		
		if (!fileStorage.getUsers().contains(revokeFromUser)) {
			throw new PrivilageException("Korisnik za koga zelite da povucete privilegije ne postoji u skladistu!");
		}

		User user = fileStorage.getUsers().get(fileStorage.getUsers().indexOf(revokeFromUser));

		if (!user.getPrivilages().contains(privilage)) {
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
