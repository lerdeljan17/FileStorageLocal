package raf.rs.FileStorageLocalImpl.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.NoSuchUserException;
import exceptions.StorageInitException;
import raf.rs.FIleStorageSpi.FileStorage;
import raf.rs.FIleStorageSpi.MyDir;
import raf.rs.FIleStorageSpi.User;

public class FileStorageLocal implements FileStorage {

	private List<String> forbiddenExtensions;

	private String rootDirPath;
	private String fileStorageName;

	private ArrayList<User> users;
	private User currentUser;

	public FileStorageLocal(String rootPath, String fileStorageName, User rootUser) throws Exception {
		super();
		rootUser.setRootUser(true);
		this.initFileStorage(rootPath, fileStorageName, rootUser);
	}

	public FileStorageLocal(String rootPath, User rootUser) throws Exception {
		super();
		this.forbiddenExtensions = new ArrayList<String>();
		this.currentUser = rootUser;
		this.users = new ArrayList<User>();
		connect(rootUser);
	}

	@Override
	public boolean initFileStorage(String rootDir, String name, User rootUser) throws Exception {
		String path = FilenameUtils.separatorsToSystem(rootDir);
		File rootFile = new File(path);
		if (rootFile.exists()) {
			throw new StorageInitException("Na prosledjenoj putanji vec postoji fajl!");
		} else {
			if (rootFile.mkdir()) {
				System.out.println("Napravljen je root folder na putanji " + path);
			} else {
				throw new StorageInitException("Ne moze da se napravi root na putanji " + path);
			}
		}

		this.rootDirPath = path;
		this.currentUser = rootUser;
		this.fileStorageName = name;

		return true;
	}

	public boolean connect(User rootUser) throws Exception {
		this.forbiddenExtensions = new ArrayList<String>();
		this.currentUser = rootUser;
		this.users = new ArrayList<User>();
		users.add(rootUser);
		openConnectionWithUser(rootUser);
		return true;
	}

	/*
	private void createFileStorageMetaData() {
		String storagePath = FilenameUtils.separatorsToSystem(rootDirPath + ".settings");
		File storageFile = new File(storagePath);
		JSONArray jsa = new JSONArray();
		JSONObject jso = new JSONObject();
		jso.put("username", this.currentUser.getUsername());
		jso.put("password", this.currentUser.getPassword());
		jso.put("isRoot", this.currentUser.isRootUser());
		jso.put("privilages", this.currentUser.getPrivilages());
		jsa.put(jso);
		System.out.println(jsa.toString());

		FileWriter fw;
		try {
			fw = new FileWriter(storageFile);
			PrintWriter pw = new PrintWriter(fw);
			pw.append(jsa.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			fw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// this.rootDir.setSettingsFile(storageFile);
	}
	*/

	@Override
	public void addForbiddenExtension(String extension) {
		if (extension.contains(".")) {
			extension.replace(".", "");
		}
		this.forbiddenExtensions.add(extension);
	}

	@Override
	public boolean closeConnectionWithUser(User user) {
		String path = FilenameUtils.separatorsToSystem(rootDirPath + ".settings");
		File settings = new File(path);
		JSONArray jsa = new JSONArray();
		// users.add(new User("laco", "slaco", null));
		// rootUser.createNewUser("laco", "slaco");
		// rootUser.revokePrivilage(new User("laco", "slaco"),"del");
		for (User o : users) {
			JSONObject jso = new JSONObject();
			jso.put("username", o.getUsername());
			jso.put("password", o.getPassword());
			jso.put("isRoot", o.isRootUser());
			jso.put("privilages", o.getPrivilages());
			jsa.put(jso);

		}
		JSONArray jse = new JSONArray(forbiddenExtensions);
		JSONObject js = new JSONObject();
		js.put("users", jsa);
		js.put("extensions", jse);
		FileWriter fw;
		try {
			fw = new FileWriter(settings);
			PrintWriter pw = new PrintWriter(fw);
			pw.write(jsa.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			fw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean openConnectionWithUser(User user) throws Exception {
		String path = FilenameUtils.separatorsToSystem(rootDirPath + ".settings");
		File settings = new File(path);
		String jsonStr = FileUtils.readFileToString(settings, Charset.defaultCharset());
		System.out.println(jsonStr);
		JSONObject mainObj = new JSONObject(jsonStr);
		JSONArray schemaObject = new JSONArray(mainObj.getJSONArray("users"));
		for (int i = 0; i < schemaObject.length(); i++) {
			ArrayList<String> prv = new ArrayList<String>();
			for (Object object : schemaObject.getJSONObject(i).getJSONArray("privilages").toList()) {
				prv.add((String) object);
			}
			User newUser = new User(schemaObject.getJSONObject(i).getString("username").toString(),
					schemaObject.getJSONObject(i).getString("password").toString(),
					schemaObject.getJSONObject(i).getBoolean("isRoot"), prv);
			this.users.add(newUser);
			// System.out.println("Novi user " + newUser);
		}
		JSONArray extensions = new JSONArray(mainObj.get("extensions"));
		for (int i = 0; i < extensions.length(); i++) {
			forbiddenExtensions.add((String) extensions.getString(i));
		}
		if (this.users.contains(user)
				&& this.users.get(this.users.indexOf(user)).getPassword().equals(user.getPassword())) {
			System.out.println("Connection established");
			return true;
		} else {
			throw new NoSuchUserException();
		}
	}

	public List<String> getForbiddenExtensions() {
		return forbiddenExtensions;
	}

	public void setForbiddenExtensions(List<String> forbiddenExtensions) {
		this.forbiddenExtensions = forbiddenExtensions;
	}

	public String getFileStorageName() {
		return fileStorageName;
	}

	public void setFileStorageName(String fileStorageName) {
		this.fileStorageName = fileStorageName;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public User getRootUser() {
		return currentUser;
	}

	public void setRootUser(User rootUser) {
		this.currentUser = rootUser;
	}

}
