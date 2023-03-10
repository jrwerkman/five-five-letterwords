package nl.jrwer.challenges.bron.kerbosch.old;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordLoaderBK {
	public static final Map<Character, Integer> charsToBit = new HashMap<>();
	public static final Map<Integer, Character> bitsToChar = new HashMap<>();
	
	static {
		for(int i=0; i<26; i++) {
			charsToBit.put((char)(i+97), 1 << i);
			bitsToChar.put(1 << i, (char)(i+97));
		}
	}
	
//	public static Set<EdgesBK> edges = new HashSet<>();
//	public static Set<WordBK> words = new HashSet<>();
	
	public static Set<WordBK> loadWords() throws FileNotFoundException, IOException {
		Set<WordBK> words = new HashSet<>();
		
		try (InputStream is = WordLoaderBK.class.getClassLoader().getResourceAsStream("words_alpha.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			
			while ((line = br.readLine()) != null) {
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
			    		
			    		if(i == 4)
			    			words.add(new WordBK(line, fingerprint, words));
			    	}
			    }
			}
		}
		
		return words;
	}
}
