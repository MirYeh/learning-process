package org.miri.programming.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the puzzle game. Holds logic for puzzle solving steps.
 * @author Miri Yehezkel
 */
public class PuzzleGame {
	/** Maps successive slices of a piece to corresponding puzzle pieces. */
	private Map<SuccessiveSlices, List<PuzzlePiece>> toSolve;
	
	/** Holds the solution to the puzzle. */
	private PuzzlePiece[] solution;
	
	/** Holds length of row in NxN solution puzzle. */
	int rowLength;


	/**
	 * Constructs a puzzle game.
	 */
	public PuzzleGame() {
		toSolve = new HashMap<>();
	}

	
	/**
	 * Maps puzzle by slices.
	 * @param pieces List to map by
	 */
	public PuzzleGame mapPieces(List<PuzzlePiece> pieces) {
		for (PuzzlePiece piece : pieces)
			mapPiece(piece);
		rowLength = (int) Math.sqrt(pieces.size());
		solution = new PuzzlePiece[pieces.size() + rowLength]; //added extra row for padding
		return this;
	}
	
	/** Maps piece by its successive inner slices. */
	private void mapPiece(PuzzlePiece piece) {
		int[] slices = piece.getSlices();
		int len = slices.length;
		for (int i = 0; i < len - 1; i++) {
			toMap(new SuccessiveSlices(slices[i], slices[i+1]), piece);
		}
		toMap(new SuccessiveSlices(slices[len-1], slices[0]), piece);
	}
	
	/** Adds given piece to list of values mapped by given key. */
	private void toMap(SuccessiveSlices key, PuzzlePiece piece) {
		List<PuzzlePiece> mappedPieces = toSolve.get(key);
		if (mappedPieces == null)
			mappedPieces = new ArrayList<>();
		mappedPieces.add(piece);
		toSolve.put(key, mappedPieces);
	}
	
	
	public PuzzleGame printMap() {
		System.out.println("Map of Successive Slices: ");
		Iterator<SuccessiveSlices> it = toSolve.keySet().iterator();
		while (it.hasNext()) {
			SuccessiveSlices key = it.next();
			System.out.printf("%s: %s \n", key, toSolve.get(key));
		}
		System.out.println("\n");
		return this;
	}
	
	
	/** Attempts to solve puzzle game. */
	public boolean solve(boolean printMatrix) {
		SuccessiveSlices key = new SuccessiveSlices(0, 0);
		List<PuzzlePiece> possiblePieces = toSolve.get(key);
		if (possiblePieces == null) {
			throw new RuntimeException(String.format("No pieces found for given key (%s)", key));
		}
		
		padFirstRow();
		boolean isSolved = findMatchingPiece(key, possiblePieces, rowLength);
		output(printMatrix);
		return isSolved;
	}
	
	
	/** Outputs solution to screen */
	void output(boolean printMatrix) {
		System.out.println("Output:");
		int lineCount = 0;
		//matrix print out
		StringBuilder matrixPrint = new StringBuilder();
		StringBuilder output = new StringBuilder();
		
		for (int i = rowLength, len = solution.length; i < len; i++) {
			PuzzlePiece curr = solution[i];
			output.append(String.format("%d,%d; ", curr.getCubeId(), curr.getAmountRotate()));
			matrixPrint.append(String.format("(%d)%s   ", curr.getAmountRotate(), curr));
			if (++lineCount == rowLength) {
				matrixPrint.append("\n");
				lineCount = 0;
			}
		}
		if (printMatrix)
			System.out.println(matrixPrint + "\n");
		System.out.println(output);
	}
	
	/** Adds padding to first row. */
	private void padFirstRow() {
		PuzzlePiece padding = new PuzzlePiece();
		for (int i = 0; i < rowLength; i++)
			solution[i] = padding;
	}
	

	
	/** Recursive call to solve puzzle by given data */
	private boolean findMatchingPiece(SuccessiveSlices key, List<PuzzlePiece> possiblePieces, int index) {
		if (index == solution.length) //connected all pieces
			return true;
		if (possiblePieces == null)
			return false;
		
		for (PuzzlePiece piece : possiblePieces) {
			if ( ! piece.isConnected()) {
				SuccessiveSlices nextKey = matchPiece(key, piece, index);
				if (findMatchingPiece(nextKey, toSolve.get(nextKey), index+1))
					return true;
				//else undo changes
				unmatchPiece(piece, index);
			}
		}//for loop
		return false; //no matching piece found
	}
	
	
	SuccessiveSlices matchPiece(SuccessiveSlices key, PuzzlePiece piece, int index) {
		int amount = findRotation(key, piece);
		rotate(piece.getSlices(), amount);
		piece.setAmountRotate(amount);
		piece.setConnected(true);
		solution[index] = piece;
		return nextKey(piece.getSlices(), index);
	}
	
	void unmatchPiece(PuzzlePiece piece, int index) {
		int[] slices = piece.getSlices();
		int prevRotation = piece.getAmountRotate();
		int diff = slices.length - prevRotation;
		rotate(slices, diff); //rotate back to original
		piece.setAmountRotate(prevRotation + diff);
		piece.setConnected(false);
		solution[index] = null;
	}
	
	
	SuccessiveSlices nextKey(int[] slices, int index) {
		int leftSlice;
		
		if (index % 10 == (rowLength - 1)) // last in row
			leftSlice = 0;
		else
			leftSlice = slices[1];
		return new SuccessiveSlices(leftSlice, solution[index - rowLength + 1].getSlices()[2]);
	}
	
	/**
	 * Rotates given piece to match key and updates amount of rotations for piece.
	 * @return 
	 */
	private int findRotation(SuccessiveSlices key, PuzzlePiece toRotate) {
		int[] slices = toRotate.getSlices();
		if (isMatchingKey(key, slices[3], slices[0])) //no need to rotate
			return toRotate.getAmountRotate();
		
		// compute distance to last index in order to find number of rotations needed
		for (int i = 0, lastIndex = slices.length - 1; i < lastIndex; i++) {
			if (isMatchingKey(key, slices[i], slices[i+1]))
				return lastIndex - i;
		}
		
		throw new RuntimeException(String.format("No rotation found for given key %s and piece %s", 
				key, toRotate));
	}
	
	
	/** Rotates slices by given rotation amount. */
	private void rotate(int[] slices, int amount) {
		int len = slices.length;
		int[] rotated = new int[len];
		for (int i = 0; i < len; i++) {
			int newIndex = i + amount;
			if (newIndex >= len) //not valid index
				newIndex -= len;
			rotated[newIndex] = slices[i];
		}
		//copy rotated to original array
		for (int i = 0; i < len; i++) {
			slices[i] = rotated[i];
		}
	}
	
	
	/** Checks if given key values matches given values */
	private boolean isMatchingKey(SuccessiveSlices key, int leftValue, int topValue) {
		return leftValue == key.leftSlice && topValue == key.topSlice;
	}
	
	
	
	/**
	 * Represents two successive inner slices of a puzzle piece.
	 * @author Miri Yehezkel
	 */
	public static class SuccessiveSlices {
		private int leftSlice;
		private int topSlice;
		
		public SuccessiveSlices(int leftSlice, int topSlice) {
			this.leftSlice = leftSlice;
			this.topSlice = topSlice;
		}
		
		public SuccessiveSlices(PuzzlePiece piece) {
			int [] slices = piece.getSlices();
			this.leftSlice = slices[3];
			this.topSlice = slices[0];
		}
		
		public int getLeftSlice() {
			return leftSlice;
		}
		
		public void setLeftSlice(int leftSlice) {
			this.leftSlice = leftSlice;
		}
		
		public int getTopSlice() {
			return topSlice;
		}
		
		public void setTopSlice(int topSlice) {
			this.topSlice = topSlice;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj != null && obj instanceof SuccessiveSlices) {
				SuccessiveSlices other = (SuccessiveSlices) obj;
				if (other.leftSlice == this.leftSlice && other.topSlice== this.topSlice)
					return true;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(leftSlice, topSlice);
		}
		
		@Override
		public String toString() {
			return String.format("%d,%d  ", leftSlice, topSlice);
		}
		
	} // inner class
	
	
}
