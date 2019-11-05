package raf.rs.FileStorageLocalImpl.model;

import java.util.ArrayList;

import raf.rs.FIleStorageSpi.User;

public class LocalUser extends User{
	
	private FileStorageLocal FileStorage;
	public LocalUser(String username, String password, ArrayList<String> privilages) {
		super(username, password, privilages);
		// TODO Auto-generated constructor stub
	}
	
@Override
public boolean connectToFileStorage(String rootDirPath, String FileStorageRootDirName) throws Exception {
	// TODO Auto-generated method stub
	this.FileStorage = new FileStorageLocal(new MyLocalDirectory(rootDirPath, FileStorageRootDirName), this);
	return true;
}
@Override
public boolean disconnectFromFileStorage(String fileStorageRootDir) {
	// TODO Auto-generated method stub
	this.FileStorage.closeConnectionWithUser(this);
	this.FileStorage = null;
	return true;
}
@Override
public boolean equals(Object obj) {
	// TODO Auto-generated method stub
	return super.equals(obj);
}

public FileStorageLocal getFileStorage() {
	return FileStorage;
}

public void setFileStorage(FileStorageLocal fileStorage) {
	FileStorage = fileStorage;
}
}
