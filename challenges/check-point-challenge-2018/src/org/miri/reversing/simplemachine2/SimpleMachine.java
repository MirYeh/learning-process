package org.miri.reversing.simplemachine2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class SimpleMachine {
	ArrayList<Integer> instructions;
	protected Stack<Integer> stack;
	int ip;
	StringBuilder output;
	private boolean print;
	int readCount = 0, writeCount = 0;
	
	public SimpleMachine(ArrayList<Integer> instructions, boolean print) {
		this.print = print;
		this.instructions = instructions;
		stack = new Stack<>();
		output = new StringBuilder();
	}
	
	/**
	 * Evaluate given command, execute and return next IP
	 */
	public int start() {
		int command = instructions.get(ip);
		assert (command >= 0 && command < 256);
		
		if (command > 0x20) {
			if (command >= 0x80)// 127 < command < 256
				return push(command-0x80);
			else if (command >= 0x40)// 63 < command < 128
				return load(command-0x40);
			//else command > 0x20
			return swap(command-0x20);// 32 < command < 64
		}
		else if (command > 0x09) {
			switch (command) {
			case 0x10:
				return jump();
			
			case 0x11:
				return call();
			
			case 0x12:
				return ret();
			
			case 0x14:
				return CJE();
			
			case 0x18:
				return JSE();
			
			case 0x20:
				return pop();
			}
		}
		//else command >= 0
		switch (command) {
		case 0x00:
			return add();
		
		case 0x01:
			return subtract();
		
		case 0x02:
			return divide();
		
		case 0x03:
			return multiply();
		
		case 0x08:
			return read();
		
		case 0x09:
			return write();
		}
		
		throw new RuntimeException(String.format("commnd %d is not supported", command));
	}
	
	
	protected int push(int i) {
		console(String.format("push %d", i));
		stack.push(i);
		return ++ip;
	}

	protected int load(int i) {
		console(String.format("load %d", i));
		int pos = stack.size() - i - 1;
		stack.push(stack.elementAt(pos));
		return ++ip;
	}
	
	protected int pop() {
		console("pop");
		stack.pop();
		return ++ip;
	}

	protected int swap(int i) {
		console(String.format("swap %d", i));
		int position = stack.size() - i - 1;
		int temp = stack.elementAt(position);
		stack.remove(position);
		stack.add(position, stack.pop());
		stack.push(temp);
		return ++ip;
	}

	protected int add() {
		byte a = stack.pop().byteValue(); //signed byte
		byte b = stack.pop().byteValue(); //signed byte
		int signedSum = a + b;
		stack.push(signedSum);
		console(String.format("add signed (%d + %d = %d)", a, b, signedSum));
		return ++ip;
	}

	protected int subtract() {
		byte a = stack.pop().byteValue(); //signed byte
		byte b = stack.pop().byteValue(); //signed byte
		int signedDiff = a - b;
		stack.push(signedDiff);
		console(String.format("subtract signed (%d - %d = %d)", a, b, signedDiff));
		return ++ip;
	}

	protected int multiply() {
		byte a = stack.pop().byteValue(); //signed byte
		byte b = stack.pop().byteValue(); //signed byte
		int signedProd = a * b;
		stack.push(signedProd);
		console(String.format("multiply signed (%d * %d = %d)", a, b, signedProd));
		return ++ip;
	}
	
	
	protected int divide() {
		int a = asUnsignedByte(stack.pop());
		int b = asUnsignedByte(stack.pop());
		stack.push(a / b);
		stack.push(a % b);
		console(String.format("divide(%d, %d): a/b = %d \t a mod b = %d", a, b, a/b, a%b));
		return ++ip;
	}
	
	/**
	 * change bits to 0 excluding the last 8 bits
	 */
	private int asUnsignedByte(int num) {
		if (num < 0)
			num = 0x000000FF & num;
		if (num < 0)
			throw new RuntimeException(String.format("asUnsignedByte(%d)", num));
		return num;
	}

	protected int read() {
		System.out.printf("\nInput read: ");
		int read = Integer.valueOf(Main.stdin.nextLine());
		if (read == -555)
			throw new RuntimeException("used value of break");
		console(String.format("read '%d'", read));
		stack.push(read);
		readCount++;
		return ++ip;
	}

	protected int write() {
		int out = stack.pop();
		console(String.format("write %d ('%c')", out, (char)out));
		output.append(String.format("%d ('%c'), ", out, (char)out));
		writeCount++;
		return ++ip;
	}

	
	protected int jump() {
		byte signedOffset = stack.pop().byteValue();
		int newIp = ip + 1 + signedOffset;
		console(String.format("jump signed IP = %d", newIp));
		ip = newIp;
		return ip;
	}

	protected int call() {
		byte signedOffset = stack.pop().byteValue();
		int newIp = ip + 1 + signedOffset;
		console(String.format("call signed IP = %d  push %d", newIp, ip + 1));
		stack.push(ip + 1);
		ip = newIp;
		return ip;
	}

	protected int ret() {
		int retVal = stack.pop();
		console("ret " + retVal);
		ip = retVal;
		return ip;
	}

	protected int CJE() {
		byte signedOffset = stack.pop().byteValue();
		int newIp = ip + 1 + signedOffset;
		int a = stack.pop();
		int b = stack.pop();
		boolean flag = a == b;
		console(String.format("CJE signed IP = %d  (%d==%d) %s",
				newIp, a, b, String.valueOf(flag).toUpperCase()));
		if (flag)
			ip = newIp;
		else
			ip++;
		return ip;
	}

	protected int JSE() {
		int offset = stack.pop();
		int newIp = ip + 1 + offset;
		boolean flag = stack.isEmpty();
		console(String.format("JSE IP = %d  %s", newIp, String.valueOf(flag).toUpperCase()));
		if (stack.isEmpty())
			ip = newIp;
		else
			ip++;
		return ip;
	}
	
	
	
	
	
	private void console(String str) {
		if (print)
			System.out.printf("IP %d:   %s\n", ip, str);
	}
	
	
	public void state() {
		Iterator<Integer> it = stack.iterator();
		StringBuilder stackState = new StringBuilder();
		while (it.hasNext()) {
			stackState.insert(0, it.next() + ", ");
		}
		stackState.insert(0, "SP>  ");
		System.out.println(stackState + "\n");
	}
	
}
