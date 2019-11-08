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
import exceptions.PrivilageException;
import exceptions.UploadException;
import raf.rs.FIleStorageSpi.MyFile;
import raf.rs.FIleStorageSpi.PrivilageType;

public class LocalFileService implements MyFile {

	private String name;
	private FileStorageLocal storage;

	public LocalFileService(FileStorageLocal storage) {
		this.storage = storage;
		try {
			// createEmptyFile(path, name);
		} catch (Exception e) {
		//	e.printStackTrace();
			System.out.println("Nije bilo moguce napraviti LocalFileService");
		}
	}

	private boolean checkPrivilage(PrivilageType prv) {
		if(storage.getCurrentUser().getPrivilages().contains(prv)) return true;
		return false;
	}
	
	@Override
	public boolean delFile(String path, String fileName) throws Exception {
		if(!checkPrivilage(PrivilageType.DELETE)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		String filePath = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + path + "\\" + fileName);
		File file = new File(filePath);
		if (!file.exists()) {
			throw new NotFoundException(fileName);
		}

		if (file.delete()) {
			System.out.println("Uspesno obisan fajl sa lokacije " + filePath);
			return true;
		}

		throw new DeleteException();
	}

	@Override
	public boolean createEmptyFile(String path, String fileName) throws Exception {
		if(!checkPrivilage(PrivilageType.CREATE)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
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
			} else {
				System.out.println("Nije bilo moguce napraviti fajl na putanji " + filePath);
				return false;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Nije bilo moguce kreirati novi fajl na putanji " + filePath);
		}

		throw new CreateException();
	}

	@Override
	public boolean downloadFile(String pathSource, String pathDest) throws Exception {
		if(!checkPrivilage(PrivilageType.DOWNLOAD)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
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
		
		System.out.println("Uspesno je preuzet fajl sa lokacije {" + sourcePath + "} na lokaciju {" + destinationPath + "}" );

		return true;
	}

	@Override
	public boolean uploadFile(String pathSource, String pathDest) throws Exception {
		if(!checkPrivilage(PrivilageType.UPLOAD)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
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
		System.out.println("upload destinacija: " + destinationPath);
		File newFile = new File(destinationPath);

		if (newFile.exists()) {
			throw new CreateException("Postoji fajl sa tim nazivom!");
		}

		if (!newFile.createNewFile()) {
			throw new CreateException("Nije moguce skinuti fajl na prosledjenu putanju!");
		}

		FileUtils.copyFile(file, newFile);
		
		System.out.println("Uspesan upload fajla sa lokacije {" + sourcePath + "} na lokaciju {" + destinationPath + "}" );

		return true;
	}

	@Override
	public boolean createMultipleFiles(String path, String fileName, int numberOfFiles) throws Exception {
		if(!checkPrivilage(PrivilageType.CREATE)) {
			throw new PrivilageException("Nemate privilegiju");
		}

		for (int i = 0; i < numberOfFiles; i++) {
			String name = fileName + " " + i;
			createEmptyFile(path, name);
		}
		
		return true;
	}

	@Override
	public boolean uploadMultipleFiles(String pathDest, List<File> files) throws Exception {
		if(!checkPrivilage(PrivilageType.UPLOAD)) {
			throw new PrivilageException("Nemate privilegiju");
		}
		
		for (File file : files) {
			uploadFile(file.getPath().toString(), pathDest);
		}	
		
		return true;
	}

	@Override
	public boolean createMetaDataFile(String FilePath, Hashtable<String, String> metaData) {
		if(!checkPrivilage(PrivilageType.CREATE)) {
			System.out.println("Nemate privilegiju");
			return false;
		}
		
		JSONObject js = new JSONObject(metaData);
		try {
			String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + FilePath + ".metaData");
			FileWriter file = new FileWriter(path);
			PrintWriter pw = new PrintWriter(file);
			pw.append(js.toString());
	//		System.out.println("Successfully Copied JSON Object to File...");
			file.close();
			pw.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Greska prilikom dodavanja meta podataka!");
		}
		System.out.println("Uspesno dodati meta podaci!");
		return true;
	}

	@Override
	public boolean addMetaData(String metaFilePath, Hashtable<String, String> metaData) {
		if(!checkPrivilage(PrivilageType.META)) {
			System.out.println("Nemate privilegiju");
			return false;
		}
		
		JSONObject js = new JSONObject(metaData);
	//	System.out.println(js.toString());
		try {
			String path = FilenameUtils.separatorsToSystem(storage.getRootDirPath() + "\\" + metaFilePath);
			FileWriter file = new FileWriter(path, true);
			PrintWriter pw = new PrintWriter(file);
			pw.append("\n" + js.toString());
		//	System.out.println("Successfully Copied JSON Object to File...");
			file.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean uploadArchive(String archivePath, String destPath) throws Exception {
		if(!checkPrivilage(PrivilageType.UPLOAD)) {
			System.out.println("Nemate privilegiju");
			return false;
		}
		
		if(storage.getForbiddenExtensions().contains("zip")) {
			throw new UploadException("Nedozvoljena ekstenzija! (zip)");
		}
		if(!archivePath.contains("zip")) {
			System.out.println("Prosledjeni fajl nije .zip arhiva!");
		}
		uploadFile(archivePath, destPath);
		return true;
	}

}
