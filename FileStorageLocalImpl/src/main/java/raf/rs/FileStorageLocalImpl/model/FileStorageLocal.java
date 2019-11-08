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
import exceptions.NotFoundException;
import exceptions.StorageInitException;
import raf.rs.FIleStorageSpi.FileStorage;
import raf.rs.FIleStorageSpi.MyDir;
import raf.rs.FIleStorageSpi.PrivilageType;
import raf.rs.FIleStorageSpi.User;

public class FileStorageLocal implements FileStorage {

	private List<String> forbiddenExtensions;

	private String rootDirPath;

	private ArrayList<User> users;
	private ArrayList<User> connectedUsers;
	private User currentUser;

	/**
	 * @param rootPath    - Setuje vrednost tog usera da je root.
	 * @param initBoolean - True = u pitanju je kreiraje novog skladista
	 * @param user
	 * @throws Exception
	 */
	public FileStorageLocal(String rootPath, boolean initBoolean, User user) throws Exception {
		super();
		this.rootDirPath = FilenameUtils.separatorsToSystem(rootPath);
		this.connectedUsers = new ArrayList<User>();

		if (initBoolean) {
			user.setRootUser(true);
			this.users = new ArrayList<User>();
			this.users.add(user);
			this.initFileStorage(rootPath, user);
		} else {
			connect(user);
		}
		((LocalUser) user).setFileStorage(this);
		this.currentUser = user;
	}

	@Override
	public boolean initFileStorage(String rootDir, User rootUser) throws Exception {
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

		this.forbiddenExtensions = new ArrayList<String>();
		this.connectedUsers.add(rootUser);

		return true;
	}

	public boolean connect(User user) throws Exception {
		this.forbiddenExtensions = new ArrayList<String>();
		this.currentUser = user;
		this.users = new ArrayList<User>();
		openConnectionWithUser(user);

		if (connectedUsers.contains(user)) {
			System.out.println("User je vec konektovan!");
			return false;
		}
		System.out.println("Iz connect " + user);
		this.connectedUsers.add(user);

		return true;
	}

	@Override
	public void addForbiddenExtension(String extension) {
		if (extension.contains(".")) {
			extension = extension.replace(".", "");
		}
		this.forbiddenExtensions.add(extension.toLowerCase());
	}

	@Override
	public boolean closeConnectionWithUser(User user) {
		if (!this.connectedUsers.contains(user)) {
			System.out.println(user + " nije ni konektovan na skladiste!");
		}

		String path = FilenameUtils.separatorsToSystem(rootDirPath + "\\" + getFileStorageName() + ".settings");
		System.out.println("Putanja do fajla sa settings: " + path);
		File settings = new File(path);

		JSONArray jsa = new JSONArray();
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
			pw.write(js.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			fw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (settings.exists()) {
			System.out.println("Uspesno je napravljen " + getFileStorageName() + ".settings");
		} else {
			System.out.println("Greska prilikom pravljenja " + getFileStorageName() + ".settings");
		}

		this.connectedUsers.remove(user);

		return true;
	}

	@Override
	public boolean openConnectionWithUser(User user) throws Exception {
		String path = FilenameUtils.separatorsToSystem(rootDirPath + "\\" + getFileStorageName() + ".settings");
		System.out.println("Putanja do fajla sa settings: " + path);
		File settings = new File(path);

		if (!settings.exists()) {
			throw new NotFoundException("Nije pronadjen fajl " + path);
		}

		String jsonStr = FileUtils.readFileToString(settings, Charset.defaultCharset());
		System.out.println(jsonStr);

		JSONObject mainObj = new JSONObject(jsonStr);

		// JSONArray schemaObject = new JSONArray();
		for (int i = 0; i < mainObj.getJSONArray("users").length(); i++) {
			ArrayList<PrivilageType> prv = new ArrayList<PrivilageType>();
			for (Object object : mainObj.getJSONArray("users").getJSONObject(i).getJSONArray("privilages").toList()) {
				PrivilageType type = PrivilageType.valueOf((String) object);
				prv.add(type);
			}

			User newUser = new User(mainObj.getJSONArray("users").getJSONObject(i).getString("username").toString(),
					mainObj.getJSONArray("users").getJSONObject(i).getString("password").toString(),
					mainObj.getJSONArray("users").getJSONObject(i).getBoolean("isRoot"), prv);
			this.users.add(newUser);
			// System.out.println("Novi user " + newUser);
		}

		// JSONArray extensions = new JSONArray(mainObj.get("extensions"));
		for (int i = 0; i < mainObj.getJSONArray("extensions").length(); i++) {
			forbiddenExtensions.add((String) mainObj.getJSONArray("extensions").getString(i));
		}

		if (this.users.contains(user)
				&& this.users.get(this.users.indexOf(user)).getPassword().equals(user.getPassword())) {
			System.out.println("Connection established");
			return true;
		} else {
			throw new NoSuchUserException();
		}
	}

	public String getRootDirPath() {
		return rootDirPath;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public void setRootDirPath(String rootDirPath) {
		this.rootDirPath = rootDirPath;
	}

	public List<String> getForbiddenExtensions() {
		return forbiddenExtensions;
	}

	public void setForbiddenExtensions(List<String> forbiddenExtensions) {
		this.forbiddenExtensions = forbiddenExtensions;
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

	public ArrayList<User> getConnectedUsers() {
		return connectedUsers;
	}

	public void setConnectedUsers(ArrayList<User> connectedUsers) {
		this.connectedUsers = connectedUsers;
	}

	private String getFileStorageName() {
		System.out.println(this.rootDirPath);
		String[] parts = this.rootDirPath.split("\\\\");
		return parts[parts.length - 1];
	}

}
