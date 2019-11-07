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
import raf.rs.FIleStorageSpi.FileStorage;
import raf.rs.FIleStorageSpi.MyDir;
import raf.rs.FIleStorageSpi.User;

public class FileStorageLocal implements FileStorage {

	private MyLocalDirectory rootDir;
	private List<String> forbiddenExtensions;
	private String fileStorageName;
	private ArrayList<User> users;
	private User currentUser;
	private String rootDirPath;

	public FileStorageLocal(MyLocalDirectory rootDir, String fileStorageName, User rootUser) {
		super();
		rootUser.setRootUser(true);
		this.initFileStorage(rootDir, fileStorageName, rootUser);
	}

	public FileStorageLocal(MyLocalDirectory rootDir, User rootUser) throws Exception {
		super();
		this.forbiddenExtensions = new ArrayList<String>();
		this.currentUser = rootUser;
		this.users = new ArrayList<User>();
		connect(rootDir, rootUser);
	}

	public boolean connect(MyLocalDirectory rootDir, User rootUser) throws Exception {
		this.rootDir = (MyLocalDirectory) rootDir;
		this.forbiddenExtensions = new ArrayList<String>();
		this.currentUser = rootUser;
		this.users = new ArrayList<User>();
		// users.add(rootUser);
		openConnectionWithUser(rootUser);
		return true;
	}

	@Override
	public boolean initFileStorage(MyDir rootDir, String name, User rootUser) {
		// TODO  dodati i da moze usera da primi
		this.rootDir = (MyLocalDirectory) rootDir;
		this.fileStorageName = name;
		this.forbiddenExtensions = new ArrayList<String>();
		this.currentUser = rootUser;
		this.currentUser.setRootUser(true);
		this.users = new ArrayList<User>();
		users.add(rootUser);
		this.createFileStorageMetaData(this.rootDir);
		return false;
	}

	private void createFileStorageMetaData(MyLocalDirectory dir) {
		String storagePath = FilenameUtils.separatorsToSystem(dir.getWholePath() + "\\" + dir.getRootDirectoryName() + ".settings");
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
		this.rootDir.setSettingsFile(storageFile);
	}

	@Override
	public void addForbiddenExtension(String extension) {
		if (extension.contains(".")) {
			extension.replace(".", "");
		}
		this.forbiddenExtensions.add(extension);
	}

	@Override
	public boolean closeConnectionWithUser(User user) {
		// TODO Auto-generated method stub
		File settings = new File(this.rootDir.getSettingsFile().getPath().toString());
		JSONArray jsa = new JSONArray();
		//users.add(new User("laco", "slaco", null));
		//rootUser.createNewUser("laco", "slaco");
		//rootUser.revokePrivilage(new User("laco", "slaco"),"del");
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
		File settings = new File(this.rootDir.getSettingsFile().getPath().toString());
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
		if (this.users.contains(user) && this.users.get(this.users.indexOf(user)).getPassword().equals(user.getPassword())) {
			System.out.println("Connection established");
			return true;
		} else {
			throw new NoSuchUserException();
		}
	}

	public MyLocalDirectory getRootDir() {
		return rootDir;
	}

	public void setRootDir(MyLocalDirectory rootDir) {
		this.rootDir = rootDir;
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
