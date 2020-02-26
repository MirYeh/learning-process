package org.miri.programming.archives;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadExtracted {
	static String path = "C:/Users/מירי/Desktop/CheckPoint Challenge/Programming - Careful Steps/ee1208d287a4bac5330901b3d4227844f08234f645a4eba2b26697e906bc49cf/";
	static BufferedReader reader = null;
	static StringBuilder builder = new StringBuilder();
	static String outputFile = "archives/output";
	static String indexedFile = "archives/unzipme.";
	
	public static void main(String[] args) {
		//read(new File(path+outputFile));
		
		for (int i=0; i < 4; i++) {
			builder.append(i + ": \n");
			read(new File(indexedFile + i));
			builder.append("\n--------------------------------------\n");
			
		}
		
		System.out.printf("Result: \n%s \n", builder);
	}
	
	
	
	static void read(File file) {
		int lineCount = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			int val = 0;
			do {
				val = reader.read();
				builder.append(String.format("%d -- ", val));
				if (lineCount++ % 12 == 0)
					builder.append("\n");
			} while (val != -1);
		} catch (IOException e) {
			System.out.println(String.format("ERR: could not read file %s", file.getName()));
		}
	}
	
	
}
