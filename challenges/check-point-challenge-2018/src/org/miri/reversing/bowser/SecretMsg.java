package org.miri.reversing.bowser;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class SecretMsg {
	static String filepath = "C:/Users/מירי/Desktop/CheckPoint Challenge/Reversing - Bowsers Secret Message/secret.gif";
	static Scanner stdin = new Scanner(System.in);
	static int [] token = {0x21, 0xf9, 0x04, 0x05};
	static String flagset = "WFT}YMRGO{_SL@UEK3AHI";
	static StringBuilder flag = new StringBuilder();
	
	public static void main(String[] args) {
		try {
			findFlag();
			//test();
		}catch (Error | Exception err) {
			err.printStackTrace();
			System.out.printf("State of flag(%d): '%s' \n", flag.length(), flag);
		}
	}
	
	private static void test() throws IOException {
		FileInputStream in = new FileInputStream(filepath);
		byte [] data = {(byte) 0xf5, 0x01};
		//in.read(data);
		int val = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
		System.out.println(val);
		in.close();
	}
	
	
	private static void findFlag() throws IOException {
		FileInputStream in = new FileInputStream(filepath);
		int index = 0, val = 0;
		
		while((val = in.read()) != -1) {
			if(val == token[index])
				index++; //expect next index
			else index = 0;
			if(index == token.length) { //found mark
				extractData(in);
				index = 0;
			}
		}
		
		in.close();
		System.out.printf("OUTPUT: \n%s\n", flag);
	}
	
	// read: delay(2B) tidx(1B) \x00 \x2c x(2B) y(2B) w(2B) h(2B) \x00
	private static void extractData(FileInputStream in) throws IOException {
		int delay = readLE(in);
		int tidx = in.read();
		readLE(in); // 0x00 0x2c
		int x = readLE(in);
		int y = readLE(in);
		int w = readLE(in);
		int h = readLE(in);
		int closingChunk = in.read();
		if (closingChunk != 0) throw new RuntimeException(String.format("Err: extracted data not as expected (%d)", closingChunk));
		constructFlag(tidx, x, y, w, h);
	}
	
	private static void constructFlag(int tidx, int x, int y, int w, int h) {
		boolean isUpper = (tidx % 2 == 0)? false : true;
		int index = 0;
		if(x == 0) {
			if(y == 1) 			//h = mapindex << 2
				index = h / 4;
			else //(y == 0) 	  w = mapindex / h
				index = w * h;
		}
		else //(x == 1) 		  y = mapindx << 1
			index = y / 2;
		buildFlag(index, isUpper);
	}
	
	private static void buildFlag(int index, boolean isUpper) {
		char value = flagset.charAt(index);
		if (!isUpper) {
			value = Character.toLowerCase(value);
		}
		flag.append(value);
	}
	

	private static int readLE(FileInputStream in) throws IOException {
		byte [] data = new byte[2];
		in.read(data);
		return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
}
