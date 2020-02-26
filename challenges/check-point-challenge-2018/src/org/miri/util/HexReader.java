package org.miri.util;

import java.util.Scanner;

public class HexReader {
	
	public static int[] fromInput() {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter hex values separated by space: ");
		String[] hexvalues = input.nextLine().split(" ");
		int len = hexvalues.length;
		int[] result = new int[len];
		for (int i = 0; i < len; i++) 
			result[i] = Integer.valueOf(hexvalues[i], 16);
		input.close();
		return result;
	}
	
	public static char hexToAscii(int hex) {
		return (char)hex;
	}
	
	
}
