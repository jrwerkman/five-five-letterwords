package nl.novadoc.challenges.jrwer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * words: https://github.com/dwyl/english-words/blob/master/words_alpha.txt
 * 
 * 5 word, 5 letters, 25 different letters
 * 
 * @author jan
 *
 */
public class WordPuzzle {
	private static final Map<Character, Integer> charsToBit = new HashMap<>();
	private static final Map<Integer, Character> bitsToChar = new HashMap<>();

	static {
		for(int i=0; i<26; i++) {
			charsToBit.put((char)(i+97), 1 << i);
			bitsToChar.put(1 << i, (char)(i+97));
		}
	}
	
	public static void main(String[] args) {
		try {
			WordPuzzle p = new WordPuzzle();
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Word> words = new ArrayList<>();
	private int index = 0;
	
	public void start() throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		loadWords();

		System.out.println(findSentence());
		long end = System.currentTimeMillis();
		
		System.out.println("Process took: " + (end - start) + " ms\n");
	}
	// 11111 10101 01010 00011 110000
	public Sentence findSentence() {
		Queue<Sentence> sentences = new ArrayDeque<>();
		Set<Sentence> visited = new HashSet<>();
		
		sentences.add(new Sentence());
		visited.add(new Sentence());
		
		while(!sentences.isEmpty()) {
			Sentence s = sentences.poll();
			
			for(Word w : words) {
				Sentence newS = new Sentence(s, w);
				
				visited.add(newS);
				if(newS.complete())
					return newS;
				
				if(newS.correct())
					sentences.add(newS);
			}
		}
 		
		return null;
	}
	
	public void loadWords() throws FileNotFoundException, IOException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("words_alpha.txt");
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
			    			words.add(new Word(line, fingerprint));
			    	}
			    }
			}
		}
	}
	
	class Word {
		final String word;
		final int fingerprint;
		
		public Word(String word, int fingerprint) {
			this.word = word;
			this.fingerprint = fingerprint;
		}
		
		@Override
		public String toString() {
			return word;
		}
	}
	
	class Sentence {
		int fingerprint = 0;
		final Word[] words;
		boolean correct = true;
		
		public Sentence() {
			this.words = new Word[0];
		}
		
		public Sentence(Sentence s) {
			this.fingerprint = s.fingerprint;
			this.words = s.words;
		}
		
		public Sentence(Sentence s, Word newWord) {
			this.fingerprint = s.fingerprint;
			this.words = new Word[s.words.length + 1];
			
			for(int i=0; i<s.words.length; i++)
				this.words[i] = s.words[i];

			addWord(s.words.length, newWord);
		}
		
		public Sentence(Word w) {
			this.words = new Word[1];
			this.words[0] = w;
			this.fingerprint = w.fingerprint;
		}
		
		public Sentence(Word[] words) {
			this.words = new Word[words.length];
			
			for(int i=0; i<words.length; i++)
				addWord(i, words[i]);
		}
		
		/**
		 * Return true if word can be added
		 * 
		 * @param word
		 * @return
		 */
		private void addWord(int i, Word word) {
			if((word.fingerprint & fingerprint) == 0) {
    			fingerprint = fingerprint | word.fingerprint;
    			words[i] = word;
			} else {
				correct = false;
			}
		}
		
		public boolean correct() {
			return correct;
		}
		
		/**
		 * true if sentence is complete
		 * 
		 * @return
		 */
		public boolean complete() {
			return correct && words.length == 5;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			for(Word w : words)
				if(w != null)
					sb.append(w).append(' ');
			
			return sb.toString();
		}
		
		@Override
		public int hashCode() {
			return fingerprint;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Sentence) {
				Sentence s = (Sentence) obj;
				
				return s.fingerprint == fingerprint;
			}
			
			return false;
		}
	}
}
