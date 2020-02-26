package org.miri.reversing.simplemachine2;

import java.util.Scanner;

public class FuncImpl {
	static SimpleMachine machine;
	static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		try {
			machine = new SimpleMachine(HexFileReader.readFillStack(), false);
			for (int i = 0; i < 39; i++)
				machine.start();
		} catch (Exception e) {
			System.out.println("ERR: " + e.getMessage());
		}
		machine.push(3); //push read
		machine.push(43); //IP acts as buffer when loading value
		System.out.println("START:");
		machine.state();
		func();
	}
	
	
	static void func() {
		System.out.print("press to continue... ");
		input.nextLine();
		System.out.printf("\nfunc(%d, %d) \n", machine.stack.get(machine.stack.size()-3), machine.stack.get(machine.stack.size()-2));
		//if a==0
		machine.load(2);
		machine.load(2);
		machine.push(0);
		machine.push(37);
		if (gotoRet())
			end();
		//if b==0
		machine.load(2);
		machine.swap(1);
		machine.push(0);
		machine.push(32);
		if (gotoRet())
			end();
		//if a==b
		machine.push(0);
		machine.swap(1);
		machine.load(4);
		machine.push(27);
		if (gotoRet())
			end();
		//else
		machine.pop();
		machine.push(2);
		machine.load(2);
		
		stateDivide();
		
		machine.push(2);
		machine.load(5);
		
		stateDivide();
		
		machine.swap(1);
		machine.swap(2);
		machine.add();
		machine.push(2);
		machine.swap(1);
		
		stateDivide();
		
		machine.swap(1);
		machine.pop();
		machine.load(2);
		machine.load(2);
		machine.push(36);
		machine.push(0);
		machine.subtract();
		machine.pop();
		
		machine.state();
		func();
	}
	
	static void stateDivide() {
		System.out.println("divide:");
		machine.state();
		machine.divide();
		machine.state();
	}
	
	static boolean gotoRet() { //TODO change: IP not as expected
		//return machine.CJE() == 97;
		int offset = popVal();
		int a = popVal();
		int b = popVal();
		System.out.printf("CJE(%d):  %d == %d ? %s \n", offset, a, b, String.valueOf(a == b).toUpperCase());
		return a == b;
	}
	
	static int popVal() {
		return machine.stack.pop();
	}
	
	static void end() {
		System.out.println("THE END");
		machine.state();
		System.exit(0);
	}
	
}
