package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.zeroturnaround.zip.ZipUtil;

import exceptions.CreateException;
import exceptions.CustomException;
import exceptions.DeleteException;
import exceptions.NotFoundException;
import raf.rs.FIleStorageSpi.MyFile;

public class MyLocalFile extends File implements MyFile{
	
	private String name;
	private MyLocalDirectory storage;
	
	public MyLocalFile(String name, String path, MyLocalDirectory storage) {
		super(FilenameUtils.separatorsToSystem(path + "\\" + name));
		this.name = name;
		this.storage = storage;
		try {
			//createEmptyFile(path, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean delFile(String path, String fileName) throws Exception {
		String filePath = FilenameUtils.separatorsToSystem(path + "\\" + fileName);
		File file = new File(filePath);
		if(!file.exists()) {
			throw new NotFoundException(fileName);
		}
		
		if(file.delete()) {
			return true;
		}
		
		throw new DeleteException();
	}

	@Override
	public boolean createEmptyFile(String path, String fileName) throws Exception{
		String filePath = FilenameUtils.separatorsToSystem(path + "\\" + fileName);
		File file = new File(filePath);
		
		if(file.exists()){
			throw new CreateException("Vec postoji fajl sa tim imenom na prosledjenoj putanji!");
		}
		
		try {
			if(file.createNewFile()) {
				System.out.println("Uspesno napravljen prazan fajl!");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new CreateException();
	}

	@Override
	public boolean downloadFile(String pathSource, String pathDest) throws Exception{
		String sourcePath = FilenameUtils.separatorsToSystem(pathSource);
		File file = new File(sourcePath);
		
		if (!file.exists()) {
			throw new NotFoundException("Ne postoji fajl na toj lokaciji!");
		}
		
		String destinationPath = FilenameUtils.separatorsToSystem(pathDest + "\\" + file.getName());
		File newFile = new File(destinationPath);
		
		if(newFile.exists()) {
			throw new CreateException("Postoji fajl sa tim nazivom!");
		}
		
		if(!newFile.createNewFile()) {
			throw new CreateException("Nije moguce skinuti fajl na prosledjenu putanju!");
		}
		
		FileUtils.copyFile(file, newFile);
		
		return true;
	}

	@Override
	public boolean uploadFile(String pathSource, String pathDest) throws Exception {
		return downloadFile(pathSource, pathDest);
	}

	@Override
	public boolean createMultipleFiles(String path, String fileName, int numberOfFiles) throws Exception{
				
		for(int i = 0; i < numberOfFiles; i++) {
			String name = fileName + " " + i;
			createEmptyFile(path, name);
		}
		
		return true;
	}

	@Override
	public boolean uploadMultipleFiles(String pathDest, List<File> files, List<File> metaDataFiles) throws Exception{
		for (File file : files) {
			uploadFile(file.getPath().toString(), pathDest);
		}
		if (metaDataFiles != null) {
			if (!metaDataFiles.isEmpty()) {
				for (File file : metaDataFiles) {
					uploadFile(file.getPath().toString(), pathDest);

				}
			}
		}
		return true;
	}

	@Override
	public boolean createMetaDataFile(String FilePath, String metaFileName, Hashtable<String, String> metaData) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean addMetaData(String metaFilePath, Hashtable<String, String> metaData) {
		JSONObject js = new JSONObject(metaData);
		System.out.println(js.toString());
		try {
			FileWriter file = new FileWriter(metaFilePath);
			PrintWriter pw = new PrintWriter(file);
			pw.append(js.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			file.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean uploadArchive(String archivePath, String destPath) throws Exception {
		
		return false;
	}

	@Override
	public boolean uploadFilesAsArchive(String archiveName, String destPath, List<File> filesToArchive)
			throws Exception {
		if(filesToArchive.isEmpty()) {
			throw new CreateException("Nije prosledjen nijedan fajl za arhivirati!");
		}
		
		String parentPath = filesToArchive.get(0).getParent();
		for(int i = 1; i < filesToArchive.size(); i++) {
			String currentParentPath = filesToArchive.get(i).getParent();
			if(!parentPath.equals(currentParentPath)) {
				throw new CustomException("Svi fajlovi koji se arhiviraju moraju biti u istom direktorijumu!");
			}
		}
		
		if(archiveName == null) {
			File parentFile = new File(parentPath);
			archiveName = parentFile.getName();
		}
		
		if(archiveName.isEmpty() || archiveName.isBlank()) {
			File parentFile = new File(parentPath);
			archiveName = parentFile.getName();
		}
		
		//System.out.println(archiveName);
		
		String destinationPath = FilenameUtils.separatorsToSystem(destPath + "\\" + "tempArhive");
		File newDirectory = new File(destinationPath);
		if (!newDirectory.exists()) {
			//System.out.println(dir.toString());
			if (newDirectory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				throw new CreateException();
			}
		} else {
			
		}
		
		for(File f : filesToArchive) {
			if(!f.exists()) {
				throw new NotFoundException(f.getName());
			}
			
			if(f.isFile()) {
				FileUtils.copyFileToDirectory(f, newDirectory);
			} else if(f.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(f, newDirectory);
			}
			
		}
		
		String arhivePath = FilenameUtils.separatorsToSystem(destPath + "\\" + archiveName +  ".zip");
		File arhiveFile = new File(arhivePath);
		if(arhiveFile.exists()) {
			FileUtils.deleteDirectory(newDirectory);
			throw new CustomException("Vec postoji folder sa imenom " + archiveName + ".zip!");
		}
		
		ZipUtil.pack(newDirectory, arhiveFile);
		
		FileUtils.deleteDirectory(newDirectory);
		
		return true;
	}



}
