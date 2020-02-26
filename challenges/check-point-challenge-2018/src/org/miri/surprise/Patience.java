package org.miri.surprise;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/*
 * LAST GUESS ATTEMPTED: 13187
 */
public class Patience {
	static String path = "C:/Users/מירי/Desktop/CheckPoint Challenge/Surprise - Test My Patience/e85d45150eae2eda19bd2db7b946bcdebaf424bb3c259308750f11db3fb8dd8e.exe";
	static Scanner stdin = new Scanner(System.in);
	static int errCounter;
	static long totalElapsed, start;
	static int value = -2;
	
	public static void main(String[] args) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(path);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		InputStream in = process.getInputStream();
		OutputStream out = process.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out);
		Thread.currentThread().sleep(2000);
		
		System.out.println("START");

		readOrPause(in); //read first msg
		while(process.isAlive() && value < 1000000) {
			writer.write(value + "\n");
			writer.flush();
			readOrPause(in);
			System.out.printf(" (%d - Time elapsed: %d) ", value, totalElapsed);
			value++;
		}
		
		in.close();
		out.close();
		process.destroy();
		System.out.println("END");
		
	}//end of main
	
	/** Read from InputStream */
	static void readOrPause(InputStream in) throws Exception {
		start = System.nanoTime();
		int amtToRead;
		while ((amtToRead = in.available()) < 3) {
			
		}
		totalElapsed = System.nanoTime() - start;
		
		System.out.print("msg:");
		String msg = read(in, amtToRead);
		if (! msg.trim().isEmpty()) {
			System.out.printf("%s ", msg);
			checkMsg(msg);
		}
		
	}
	
	/** Read given amount from InputStream */
	static String read(InputStream in, int amtToRead) throws Exception {
		StringBuilder builder = new StringBuilder();
		while(--amtToRead > 0)
			builder.append((char) in.read());
		return builder.toString();
	}
	
	/** Check incoming message */
	static void checkMsg(String msg) {
		if (msg.contains("error") || msg.contains("Input too long")) {
			if (++errCounter > 10) {
				pressToContinue();
				errCounter = 0;
			}
		}
		else if (msg.contains("lag") || msg.contains("rack")) { //flag or crack, first letter ignored
			System.out.println("\n\n********* FOUND!!! *********");
			pressToContinue();
		}
		
	}
	
	/** Provides pause in program */
	static void pressToContinue() {
		System.out.print("Press ENTER to continue... ");
		stdin.nextLine();
	}
	
}
