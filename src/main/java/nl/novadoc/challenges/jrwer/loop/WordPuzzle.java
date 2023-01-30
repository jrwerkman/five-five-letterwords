package nl.novadoc.challenges.jrwer.loop;

import java.util.HashSet;
import java.util.Set;

import nl.novadoc.challenges.jrwer.IWordLoader;
import nl.novadoc.challenges.jrwer.loader.WordLoader;

/**
 * words: https://github.com/dwyl/english-words/blob/master/words_alpha.txt
 * 
 * 5 word, 5 letters, 25 different letters
 * 
 * @author jan
 *
 */
public class WordPuzzle extends WordLoader implements IWordLoader {
	public static void main(String[] args) {
		try {
			WordPuzzle p = new WordPuzzle();
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long loading, start, end;
	public Set<Integer> best = new HashSet<>();
	public Set<Integer> visited = new HashSet<>();

	@Override
	public void start() throws Exception {
		loading = System.currentTimeMillis();
		loadWords();
		end = System.currentTimeMillis();
		System.out.println("Loading wordlist took: " + (end - loading) + " ms");
		System.out.println(String.format("Loaded distinct %d words", words.size()));

		start = System.currentTimeMillis();
		Set<Integer> results = execute();
		print(results, words);
		
		end = System.currentTimeMillis();
		
		System.out.println(String.format("Finding sentence took: %d ms", end - start));
		System.out.println(String.format("Total time took: %d ms", end - loading));
	}
	
	public Set<Integer> execute() {
		loopWords(best);
		
		return best;
	}
	
	public void loopWords(Set<Integer> s) {
		if(best.size() == 5)
			return;
		
		for(Integer w : fingerprints) {
			Set<Integer> newS = new HashSet<>();
			newS.addAll(s);
			newS.add(w);
			
			if(visited.contains(w))
				continue;

			visited.add(w);
			
			if(best.size() < newS.size())
				best = newS;

			if(correct(newS))
				loopWords(newS);
		}
	}
	
	public boolean correct(Set<Integer> s) {
		int fp = 0;
		
		for(Integer w:s) {
			if ((fp & w) != 0)
				return false;
			
			fp = fp | w;
		}
		
		return true;
	}

}
