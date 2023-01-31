package nl.novadoc.challenges.jrwer.loop;

import java.util.HashSet;
import java.util.Set;

import nl.novadoc.challenges.jrwer.Data;
import nl.novadoc.challenges.jrwer.WordFinderBase;

/**
 * words: https://github.com/dwyl/english-words/blob/master/words_alpha.txt
 * 
 * 5 word, 5 letters, 25 different letters
 * 
 * @author jan
 *
 */
public class WordPuzzle extends WordFinderBase {
	public static void main(String[] args) {
		try {
			WordPuzzle p = new WordPuzzle();
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long loading, start, end;
	public Data best = new Data();
	public Set<Data> visited = new HashSet<>();

	@Override
	public void start() throws Exception {
		loading = System.currentTimeMillis();
		loadWords();
		end = System.currentTimeMillis();
		System.out.println(String.format("Procesed %d words", totalWords));
		System.out.println(String.format("Loaded distinct %d five letter words, with five different chars", word5_5letters));
		System.out.println(String.format("Loaded distinct unique letter %d words, with five different chars", words.size()));
		System.out.println("Loading wordlist took: " + (end - loading) + " ms");

		start = System.currentTimeMillis();
		Data results = execute();
		end = System.currentTimeMillis();
		
		System.out.println("\nResult:");
		System.out.println(toString(results, words));
		System.out.println("\nCharacters used:");
		System.out.println(getCharactersUsed(results));
		
		System.out.println(String.format("\nFinding sentence took: %d ms", end - start));
		System.out.println(String.format("Total time took: %d ms", end - loading));
	}
	
	public Data execute() {
		loopWords(best);
		
		return best;
	}
	
	public void loopWords(Data currentData) {
		if(best.complete())
			return;
		
		for(Integer fp : fingerprints) {
			Data nextData = new Data(currentData, fp);
			
			if(!nextData.correct() || visited.contains(nextData))
				continue;

			visited.add(nextData);
			
			if(best.length() < nextData.length())
				best = nextData;
			
			loopWords(nextData);
		}
	}
}
