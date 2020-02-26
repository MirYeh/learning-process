package org.miri.networking.protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Protocol {
	static BufferedReader reader;
	static DataOutputStream writer;
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("35.157.111.68", 20106);
			System.out.println("Created socket");
			System.out.println("income: ");
			writer = new DataOutputStream(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			System.out.println(reader.readLine());
			
			System.out.println("writing...");
			writer.writeChars("1 5 HELLO \n");
			writer.flush();
			Thread.sleep(10000);
			System.out.println("...");
			System.out.println(reader.readLine());
			
		} catch (IOException | InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
}
