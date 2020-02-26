package org.miri.preinterview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SortAnagrams {
	
	public static void sort(String[] words) {
		Map<Integer, List<String>> map = new HashMap<>();
		
		for(String word : words) { //sort by words length
			int len = word.length();
			if (! map.containsKey(len))
				map.put(len, new ArrayList<>());
			map.get(len).add(word);
		}
		
		Iterator<Integer> it = map.keySet().iterator();
		//search anagrams
		int insertIdx = 0;
		while(it.hasNext()) {
			int key = it.next();
			List<String> values = map.get(key);
			insertIdx = sortAnagrams(words, values, insertIdx);
		}
		
	}
	
	/** Sorts given list of words into given index in array */
	public static int sortAnagrams(String[] words, List<String> toSort, int insertIdx) {
		for(int i=0, n=toSort.size(); i < n; i++) {
			for (int j=1; j < n; j++) {
				
			}
		}
		return insertIdx;
	}
	
}
