package org.miri.programming.puzzle;

/**
 * Receives a puzzle in the form of a string and translates it to an puzzle peace.
 * @author Miri Yehezkel
 */
public class PuzzlePiece {
	private int cubeId;
	private int [] slices;
	private int amountRotate;
	private boolean isConnected;
	
	public PuzzlePiece() {
		slices = new int[4];
	}
	
	/** 
	 * Receives a piece data in the following format: <pre>cube_id, [slices]</pre>
	 */
	public PuzzlePiece(String pieceData) {
		this();
		String [] parsed = pieceData.split("[^0-9]+");
		
		int parsedLen = parsed.length;
		if (parsedLen != 5) // parsed data not as anticipate
			throw new RuntimeException
				(String.format("Expected 5 pieces, got %s instead.", parsedLen));
		
		cubeId = Integer.parseInt(parsed[0]);
		for(int slice=1; slice < parsedLen; slice++)
			slices[slice-1] = Integer.parseInt(parsed[slice]);
		
	}
	
	
	/* Setters and Getters */
	
	
	public int getCubeId() {
		return cubeId;
	}

	public void setCubeId(int cubeId) {
		this.cubeId = cubeId;
	}

	public int[] getSlices() {
		return slices;
	}

	public void setSlices(int[] slices) {
		this.slices = slices;
	}
	
	public int getAmountRotate() {
		return amountRotate;
	}
	
	public void setAmountRotate(int amountRotate) {
		if (amountRotate >= 4)
			amountRotate %= 4;
		this.amountRotate = amountRotate;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	
	
	/**
	 * returns a string formatted as {@code cubeId, [slices]}:<pre>0,[7, 2, 5, 7]; </pre>
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(cubeId + ",[");
		int lastSlicePosition = slices.length - 1;
		for (int slice=0; slice < lastSlicePosition; slice++)
			result.append(slices[slice] + ", "); 
		result.append(slices[lastSlicePosition] + "]");
		return result.toString();
	}
	
	
}
