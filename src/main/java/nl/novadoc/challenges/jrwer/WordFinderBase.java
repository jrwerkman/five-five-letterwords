package nl.novadoc.challenges.jrwer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.novadoc.challenges.jrwer.loop.old.WordLoader;

public abstract class WordFinderBase {
	public static final Map<Character, Integer> charsToBit = new HashMap<>();
	public static final Map<Integer, Character> bitsToChar = new HashMap<>();
	
	static {
		for(int i=0; i<26; i++) {
			charsToBit.put((char)(i+97), 1 << i);
			bitsToChar.put(1 << i, (char)(i+97));
		}
	}
	
	public Map<Integer, List<String>> words = new HashMap<>();
	public Set<Integer> fingerprints = new HashSet<>();
	public int totalWords = 0;
	public int word5_5letters = 0;
	
	public abstract void start() throws Exception;
	
	public String getCharactersUsed(Data data) {
		StringBuilder sb = new StringBuilder();
		int fingerprint = data.fingerprint;
		
		sb.append("a b c d e f g h i j k l m n o p q r s t u v w x y z \n");
		if((fingerprint & charsToBit.get('a')) == 0)
			sb.append("  ");
		
		String fingerPrintString = Integer.toBinaryString(data.fingerprint); 
		
		for(int i=fingerPrintString.length() - 1; i>=0; i--)
			sb.append(fingerPrintString.charAt(i)).append(' ');
		
		return sb.toString();
	}
	
	public String getCharactersUsed(Set<Integer> results) {
		StringBuilder sb = new StringBuilder();
		int fingerprint = 0;
		
		for(Integer i : results)
			fingerprint = fingerprint | i;
		
		sb.append("a b c d e f g h i j k l m n o p q r s t u v w x y z \n");
		if((fingerprint & charsToBit.get('a')) == 0)
			sb.append("  ");
		
		String fingerPrintString = Integer.toBinaryString(fingerprint); 
		for(int i=fingerPrintString.length() - 1; i>=0; i--)
			sb.append(fingerPrintString.charAt(i)).append(' ');
		
		return sb.toString();
	}
	
	public String toString(Data data, Map<Integer, List<String>> words) {
		if(data.length() == 0)
			return "";
		
		StringBuilder sb = new StringBuilder();
		
		for(int r : data.words) {
			List<String> wordList = words.get(r);
			
			for(int i=0; i<wordList.size(); i++) {
				sb.append(wordList.get(i));
				
				if(i<wordList.size() - 1)
					sb.append('|');
			}
			
			sb.append(' ');
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public String toString(Set<Integer> results, Map<Integer, List<String>> words) {
		if(results.isEmpty())
			return "";
		
		StringBuilder sb = new StringBuilder();
		
		for(Integer r : results) {
			List<String> wordList = words.get(r);
			
			for(int i=0; i<wordList.size(); i++) {
				sb.append(wordList.get(i));
				
				if(i<wordList.size() - 1)
					sb.append('|');
			}
			
			sb.append(' ');
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public void loadWords() throws FileNotFoundException, IOException {
		try (InputStream is = WordLoader.class.getClassLoader().getResourceAsStream("words_alpha.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			
			while ((line = br.readLine()) != null) {
				totalWords++;
			    if(line.length() == 5) {
			    	int fingerprint = 0;
			    	char[] charArray = line.toCharArray();
			    	for(int i=0; i<5; i++) {
//			    		int bitChar = Chars.get(charArray[i]).bit;
			    		int bitChar = charsToBit.get(charArray[i]);
			    		
			    		if((bitChar & fingerprint) == 0)
			    			fingerprint = bitChar | fingerprint;
			    		else
			    			break;
			    		
			    		if(i == 4) {
			    			word5_5letters++;
			    			fingerprints.add(fingerprint);
			    			
			    			if(words.containsKey(fingerprint)) {
			    				words.get(fingerprint).add(line);
			    			} else {
			    				List<String> l = new ArrayList<>();
			    				l.add(line);
			    				
			    				words.put(fingerprint, l);
			    			}
			    		}
			    	}
			    }
			}
		}
	}
	
}
