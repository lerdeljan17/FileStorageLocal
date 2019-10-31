package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import raf.rs.FIleStorageSpi.myFile;

public class MyLocalFile implements myFile{

	@Override
	public boolean delFile(String path, String fileName) {
		String filePath = FilenameUtils.separatorsToSystem(path + "\\" + fileName);
		File file = new File(filePath);
		if(!file.exists()) {
		}
		
		return false;
	}

	@Override
	public boolean createEmptyFile(String patest, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean downloadFile(String pathSource, String pathDest) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean uploadFile(String pathSource, String pathDest) {
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
	public boolean uploadArchive(String archivePath, String destPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean uploadFilesAsArchive(String archiveName, String destPath, List<File> filesToArchive) {
		// TODO Auto-generated method stub
		return false;
	}

}
