package org.miri.networking.pingpong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PingPong {
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("35.157.111.68", 10106);
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		PrintWriter dataOut = new PrintWriter(out);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int counter = 0;
		int max = 0;
		while((line = reader.readLine()) != null) {
			try {
				System.out.printf("(%d)'%s'... ", counter++, line);
				int res = findResponse(line);
				dataOut.write(res + "\n");
				dataOut.flush();
				System.out.printf(">>> wrote %d... \n", res);
			} catch (RuntimeException e) {
			} catch (Exception e) {
				System.out.printf("ERR: %s (for '%s')\n", e.getMessage(), line);
			}
		}
		
		System.out.printf("THE END: loop ran %d times, max = %d\n", counter, max);
		socket.close();
	}
	
	
	static int findResponse(String line) {
		int lastSpace = line.lastIndexOf(": ");
		if (lastSpace == -1)
			throw new RuntimeException();
		return Integer.valueOf(line.substring(lastSpace+2));
	}
	
}
