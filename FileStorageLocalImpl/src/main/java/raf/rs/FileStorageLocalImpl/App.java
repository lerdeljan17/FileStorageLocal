package raf.rs.FileStorageLocalImpl;

import java.io.File;
import java.util.Arrays;

import raf.rs.FileStorageLocalImpl.model.myLocalDir;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println( "Hello World!" );
        myLocalDir rootDir = new myLocalDir("C:\\Users\\laxy9\\Desktop", "root");
        rootDir.createMultipleDirectories(rootDir.getPath()+"\\"+rootDir.getRootDirName(), "silvija", 20);
        rootDir.delDir("C:\\Users\\laxy9\\Desktop\\root", "silvija2");
       File matches[] = rootDir.searchDirectory("C:\\Users\\laxy9\\Desktop\\root", "silvija");
        System.out.println(Arrays.toString(matches));
	}

}
