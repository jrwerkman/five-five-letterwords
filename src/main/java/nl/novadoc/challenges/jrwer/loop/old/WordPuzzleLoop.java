package nl.novadoc.challenges.jrwer.loop.old;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * words: https://github.com/dwyl/english-words/blob/master/words_alpha.txt
 * 
 * 5 word, 5 letters, 25 different letters
 * 
 * @author jan
 *
 */
public class WordPuzzleLoop {
	public static void main(String[] args) {
		try {
			WordPuzzleLoop p = new WordPuzzleLoop();
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Set<Word> words = new HashSet<>();
	private Set<Sentence> visited = new HashSet<>();
	Sentence best = new Sentence();
	
	public void start() throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		this.words = WordLoader.loadWords();
		System.out.println(words.size());
		System.out.println(findSentence());
		long end = System.currentTimeMillis();
		
		System.out.println("Process took: " + (end - start) + " ms\n");
	}

	
	public Sentence findSentence() {
		loopWords(new Sentence());
		
		return best;
	}
	
	public void loopWords(Sentence s) {
		if(best.complete())
			return;
		
		
		for(Word w : words) {
			Sentence newS = new Sentence(s, w);
			
			if(visited.contains(newS))
				continue;

			visited.add(newS);
			
			if(best.words.length < newS.words.length)
				best = newS;

			if(newS.correct())
				loopWords(newS);
		}
	}
	

}
