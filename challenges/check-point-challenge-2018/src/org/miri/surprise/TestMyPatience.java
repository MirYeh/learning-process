package org.miri.surprise;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class TestMyPatience {
	static String path = "C:/Users/מירי/Desktop/CheckPoint Challenge/Surprise - Test My Patience/e85d45150eae2eda19bd2db7b946bcdebaf424bb3c259308750f11db3fb8dd8e.exe";
	static Scanner stdin = new Scanner(System.in);
	static int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(path);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		InputStream in = process.getInputStream();
		OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
		
		long guess = 119289883422127;
		int index = 0;
		
		boolean toWrite = read(in); //read first msg
		
		while(process.isAlive() && guess != -1) {
			if (toWrite) {
				guess = Long.parseLong(stdin.nextLine());
				System.out.printf(" writing '%d'... ", guess);
				writer.write(guess + "\n");
				writer.flush();
				guess++;
			}
			toWrite = read(in);
		}
		
		in.close();
		writer.close();
		process.destroy();
		System.out.println("\nEND");
	}
	
	
	static boolean read(InputStream in) throws IOException, InterruptedException {
		StringBuilder builder = new StringBuilder();
		long start = System.nanoTime();
		while(in.available()==0) {}
		
		while(in.available() > 0) {
			builder.append((char)in.read());
		}
		long total = System.nanoTime() - start;		
		String msg = builder.toString();
		System.out.printf("msg: %s (took %d nano-secs)\n", msg, total);
		return msg.contains("Your guess>");
	}
	
}
