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
import exceptions.UploadException;
import raf.rs.FIleStorageSpi.MyFile;

public class LocalFileService implements MyFile {

	private String name;
	private FileStorageLocal storage;

	public LocalFileService(FileStorageLocal storage) {
		this.storage = storage;
		try {
			// createEmptyFile(path, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean delFile(String path, String fileName) throws Exception {
		String filePath = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + path + "\\" + fileName);
		File file = new File(filePath);
		if (!file.exists()) {
			throw new NotFoundException(fileName);
		}

		if (file.delete()) {
			return true;
		}

		throw new DeleteException();
	}

	@Override
	public boolean createEmptyFile(String path, String fileName) throws Exception {
		String filePath = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + path + "\\" + fileName);
		File file = new File(filePath);
		
		String extension = FilenameUtils.getExtension(file.getName());
		if(storage.getForbiddenExtensions().contains(extension.toLowerCase())) {
			throw new CreateException("Nedozvoljena ekstenzija prilikom pravljenja novog fajla! (" + extension + ")");
		}

		if (file.exists()) {
			throw new CreateException("Vec postoji fajl sa tim imenom na prosledjenoj putanji!");
		}

		try {
			if (file.createNewFile()) {
				System.out.println("Uspesno napravljen prazan fajl!");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new CreateException();
	}

	@Override
	public boolean downloadFile(String pathSource, String pathDest) throws Exception {
		String sourcePath = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + pathSource);
		File file = new File(sourcePath);

		if (!file.exists()) {
			throw new NotFoundException("Ne postoji fajl na toj lokaciji!");
		}

		String destinationPath = FilenameUtils.separatorsToSystem(pathDest + "\\" + file.getName());
		File newFile = new File(destinationPath);

		if (newFile.exists()) {
			throw new CreateException("Postoji fajl sa tim nazivom!");
		}

		if (!newFile.createNewFile()) {
			throw new CreateException("Nije moguce skinuti fajl na prosledjenu putanju!");
		}

		FileUtils.copyFile(file, newFile);

		return true;
	}

	@Override
	public boolean uploadFile(String pathSource, String pathDest) throws Exception {
		String sourcePath = FilenameUtils.separatorsToSystem(pathSource);
		File file = new File(sourcePath);

		if (!file.exists()) {
			throw new NotFoundException("Ne postoji fajl na toj lokaciji!");
		}
		
		String extension = FilenameUtils.getExtension(file.getName());
		if(storage.getForbiddenExtensions().contains(extension.toLowerCase())) {
			throw new UploadException("Nedozvoljena ekstenzija! (" + extension + ")");
		}

		String destinationPath = FilenameUtils
				.separatorsToSystem(storage.getRootDirPath() + "\\" + pathDest + "\\" + file.getName());
		File newFile = new File(destinationPath);

		if (newFile.exists()) {
			throw new CreateException("Postoji fajl sa tim nazivom!");
		}

		if (!newFile.createNewFile()) {
			throw new CreateException("Nije moguce skinuti fajl na prosledjenu putanju!");
		}

		FileUtils.copyFile(file, newFile);

		return true;
	}

	@Override
	public boolean createMultipleFiles(String path, String fileName, int numberOfFiles) throws Exception {

		for (int i = 0; i < numberOfFiles; i++) {
			String name = fileName + " " + i;
			createEmptyFile(path, name);
		}

		return true;
	}

	@Override
	public boolean uploadMultipleFiles(String pathDest, List<File> files) throws Exception {
		for (File file : files) {
			uploadFile(file.getPath().toString(), pathDest);
		}	
		
		return true;
	}

	@Override
	public boolean createMetaDataFile(String FilePath, String metaFileName, Hashtable<String, String> metaData) {
		JSONObject js = new JSONObject(metaData);
		try {
			String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + FilePath + ".metaData");
			FileWriter file = new FileWriter(path);
			PrintWriter pw = new PrintWriter(file);
			pw.append(js.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			file.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean addMetaData(String metaFilePath, Hashtable<String, String> metaData) {
		JSONObject js = new JSONObject(metaData);
		System.out.println(js.toString());
		try {
			String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + metaFilePath);
			FileWriter file = new FileWriter(path, true);
			PrintWriter pw = new PrintWriter(file);
			pw.append("\n" + js.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			file.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean uploadArchive(String archivePath, String destPath) throws Exception {
		if(storage.getForbiddenExtensions().contains("zip")) {
			throw new UploadException("Nedozvoljena ekstenzija! (zip)");
		}
		uploadFile(archivePath, destPath);
		return true;
	}

}
