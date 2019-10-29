package raf.rs.FileStorageLocalImpl;

import raf.rs.FileStorageLocalImpl.model.myLocalDir;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println( "Hello World!" );
        myLocalDir rootDir = new myLocalDir("C:\\Users\\laxy9\\Desktop", "root");
        rootDir.createMultipleDirectories(rootDir.toString(), "silvija", 20);
	}

}
