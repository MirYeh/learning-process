package org.miri.reversing.simplemachine2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Read .bin file and show as Hex
 */
public class Main {
	static StringBuilder allOutputs;
	public static Scanner stdin = new Scanner(System.in);
	static ArrayList<Integer> instructions = null;
	
	/** main program */
	public static void main(String[] args) {
		instructions = HexFileReader.read();
		for (int i = 0, len = instructions.size(); i < len; i++)
			System.out.printf("%d: %d \n", i, instructions.get(i));
		stdin.nextLine();
		
		allOutputs = new StringBuilder();
		
		testRun();
		
		boolean isOutputFound = run(20, false);
		System.out.printf("\nPrint all output? ");
		if(stdin.nextLine().contains("y"))
			System.out.println("\nAllOutputs: \n" + allOutputs.toString());
		System.out.println("\nFound output? " + isOutputFound);
		 
	}


	/** runs a test on given input value, prints out stack state for each step */
	static void testRun() {
		run(true);
		System.out.printf("\t continue?");
		stdin.nextLine();
	}

	/** run program on a given range of input values */
	static boolean run(int times, boolean print) {
		boolean isOutputFound = false;
		for (int i=0; i < times; i++) {
			isOutputFound = run(print);
			if (isOutputFound)
				break;
		}
		return isOutputFound;
	}


	/** run program on a single input value */
	static boolean run(boolean print) {
		SimpleMachine machine = new SimpleMachine(instructions, print);
		int ip = -1;
		try {
			while(true) {
				ip = machine.start();
				if (print)
					machine.state();
			}
		} catch (Exception e) {
			System.out.printf("ERR: (ip=%d) message=%s \n", ip, e.getMessage());
		}
		System.out.printf("readCount=%d, writeCount=%d \nOutput = %s", 
				machine.readCount, machine.writeCount, machine.output);

		String s = String.format("read = %d, write = %d, output = \n%s \n-\n", 
				machine.readCount, machine.writeCount, machine.output.toString());
		allOutputs.append(s);
		return machine.writeCount > 1;
	}





}
