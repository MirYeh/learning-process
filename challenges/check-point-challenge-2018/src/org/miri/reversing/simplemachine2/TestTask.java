package org.miri.reversing.simplemachine2;

import java.util.ArrayList;

public class TestTask {
	
	public static void main(String[] args) {
		System.out.println("TEST");
		printHexAscii();
		
	}
	
	static void printHexAscii() {
		StringBuilder builder = new StringBuilder();
		ArrayList<Integer> instructions = HexFileReader.read();
		for (int curr : instructions) {
			if (curr > 128)
				curr = toAscii(curr);
			System.out.printf("%d \t %c \n", curr, curr);
			if(curr < 128)
				builder.append((char)curr);
			/*if (lineCount++ % 12 == 0)
				System.out.println();*/
		}
		//System.out.println("\n\n" + builder);
	}
	
	static char toAscii(int num) {
		assert (num >= 128 && num < 256);
		return (char)(num-128);
	}
	
	
	
	static void intToByte(int... nums) {
		for (int num : nums) {
			System.out.printf("intToByte(%d) \t byte = %d \t .byteValue() = %d \n",
					num, (byte)num, ((Integer)num).byteValue());
			byteToInt((byte)num);
		}
	}
	
	static void byteToInt(byte num) {
		System.out.printf("byteToInt(%d) \t int = %d \n", num, (int)num);
	}
	
	
	static void sumBytes(byte a, byte b) {
		byte sum = (byte) (a + b);
		System.out.printf("sumBytes(%d, %d) = %d", a, b, sum);
	}
	
	
	
}
