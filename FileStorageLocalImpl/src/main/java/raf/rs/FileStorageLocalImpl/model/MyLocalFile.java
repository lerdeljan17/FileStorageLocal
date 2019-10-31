package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.NotFoundException;
import raf.rs.FIleStorageSpi.MyFile;

public class MyLocalFile extends File implements MyFile{
	
	private String name;
	private MyLocalDirectory storage;
	
	public MyLocalFile(String name, String path, MyLocalDirectory storage) {
		super(path);
		this.name = name;
		this.storage = storage;
		try {
			createEmptyFile(path, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean delFile(String path, String fileName) throws Exception {
		String filePath = FilenameUtils.separatorsToSystem(path + "\\" + fileName);
		File file = new File(filePath);
		if(!file.exists()) {
			new NotFoundException(fileName);
			return false;
		}
		
		if(file.delete()) {
			return true;
		}
		
		new DeleteException();
		return false;
	}

	@Override
	public boolean createEmptyFile(String path, String fileName) throws Exception{
		String filePath = FilenameUtils.separatorsToSystem(path + "\\" + fileName);
		File file = new File(filePath);
		
		if(file.exists()){
			System.out.println("Fajl vec postoji!");
			new Exception("Proba");
			new CreateException("Vec postoji fajl sa tim imenom na prosledjenoj putanji!");
		}
		
		try {
			if(file.createNewFile()) {
				System.out.println("Uspesno napravljen prazan fajl!");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new CreateException();
		return false;
	}

	@Override
	public boolean downloadFile(String pathSource, String pathDest) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean uploadFile(String pathSource, String pathDest) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createMultipleFiles(String path, String fileName, int numberOfFiles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean uploadMultipleFiles(String pathDest, List<File> files, List<File> metaDataFiles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createMetaDataFile(String FilePath, String metaFileName, Hashtable<String, String> metaData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addMetaData(String metaFilePath, Hashtable<String, String> metaData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean uploadArchive(String archivePath, String destPath) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean uploadFilesAsArchive(String archiveName, String destPath, List<File> filesToArchive)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}



}
