package org.miri.reversing.simplemachine2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents File Reader of Hex data.
 * @author Miri Yehezkel
 *
 */
public class HexFileReader {
	private static String folder = "C:\\Users\\מירי\\Desktop\\CheckPoint Challenge\\Reversing - Simple Machine 2\\";
	private static ArrayList<Integer> instructions;
	
	
	public static ArrayList<Integer> read() {
		return read("codes.txt");
	}
	
	public static ArrayList<Integer> readTest() {
		return read("testcodes.txt");
	}
	
	public static ArrayList<Integer> readFillStack() {
		return read("func.txt");
	}
	
	/**
	 * Reads data from given location.
	 */
	private static ArrayList<Integer> read(String location) {
		instructions = new ArrayList<>();
		location = folder + location;
		try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
			String line = reader.readLine().trim();
			int i;
			while (line != null) {
			 	i = Integer.valueOf(line, 16);
				instructions.add(i);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.printf("<%d instructions loaded> \n\n", instructions.size());
		return instructions;
	}
	
}
