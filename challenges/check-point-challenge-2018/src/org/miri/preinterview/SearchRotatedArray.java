package org.miri.preinterview;

public class SearchRotatedArray {
	
	public static int searchRotated(int[] arr, int value) {
		int start = 0, end = arr.length - 1;
		int index = -1;
		int mid = (end + start)/2;
		
		if(value == arr[mid]) return mid;
		if(value < arr[mid]) {
			if
		}
		
		return index;
	}
	
	/** Search rotated sorted array, returns index of value or -1 if value not found. */
	public static int searchRotated(int[] arr, int value, int start, int end) {
		int index = -1;
		
		return index;
	}
	
}
