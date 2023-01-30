package nl.novadoc.challenges.jrwer.bron.kerbosch;

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

public class WordLoader {
	public static final Map<Character, Integer> charsToBit = new HashMap<>();
	public static final Map<Integer, Character> bitsToChar = new HashMap<>();
	
	public Map<Integer, List<String>> words = new HashMap<>();
	public Set<Integer> fingerprints = new HashSet<>();
	
	static {
		for(int i=0; i<26; i++) {
			charsToBit.put((char)(i+97), 1 << i);
			bitsToChar.put(1 << i, (char)(i+97));
		}
	}
	
	public void loadWords() throws FileNotFoundException, IOException {
		
		try (InputStream is = WordLoader.class.getClassLoader().getResourceAsStream("words_alpha.txt");
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
			    		
			    		if(i == 4) {
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
