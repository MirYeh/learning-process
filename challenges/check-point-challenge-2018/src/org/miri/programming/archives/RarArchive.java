package org.miri.programming.archives;

import java.io.File;

public class RarArchive {
	static String filepath = "C:/Users/מירי/Desktop/CheckPoint Challenge/Programming - Careful Steps/ee1208d287a4bac5330901b3d4227844f08234f645a4eba2b26697e906bc49cf/rar/unzipme.";
	static String rarExt = ".rar";
	
	public static void main(String[] args) {
		for (int i=0; i < 2000; i++) {
			rename(i);
		}
	}
	
	static void rename(int index) {
		String indexedFile = filepath + index;
		File file = new File(indexedFile); //specific file
		boolean isSuccess = file.renameTo(new File(indexedFile + rarExt));
		log(index, isSuccess);
	}
	
	
	static void log(int index, boolean isSuccess) {
		System.out.printf("%s at index %d\n", isSuccess, index);
	}
	
	
}
