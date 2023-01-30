package nl.novadoc.challenges.jrwer.bron.kerbosch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
	
	public Map<Integer, Set<Integer>> neighboursCache = new HashMap<>();
	public long loading, start, end;
	
	@Override
	public void start() throws Exception {
		loading = System.currentTimeMillis();
		loadWords();
		end = System.currentTimeMillis();
		System.out.println("Loading wordlist took: " + (end - loading) + " ms");
		System.out.println(String.format("Loaded distinct %d words", words.size()));

		start = System.currentTimeMillis();
		Set<Integer> candidateSet = new HashSet<>();
		candidateSet.addAll(fingerprints);
		
		Set<Integer> results = execute(new HashSet<>(), candidateSet, new HashSet<>());
		System.out.println(String.format("Results: [%s]", print(results, words)));
		end = System.currentTimeMillis();
		
		System.out.println(String.format("Finding sentence took: %d ms", end - start));
		System.out.println(String.format("Total time took: %d ms", end - loading));
	}

	public Set<Integer> execute(Set<Integer> currentClique, Set<Integer> candidateSet, Set<Integer> exclusionSet) {
		if((candidateSet.isEmpty() && exclusionSet.isEmpty()) || currentClique.size() == 5)
			if(currentClique.size() == 5)
				return currentClique;
	
		Iterator<Integer> it = candidateSet.iterator();
		while(it.hasNext()) {
			Integer candidate = it.next();
			
			Set<Integer> newClique = getNewClique(currentClique, candidate);
			Set<Integer> neighbours = neighbours(candidate);
			Set<Integer> newCandidateSet = intersectSet(candidateSet, neighbours);
			Set<Integer> newExclusionSet = intersectSet(exclusionSet, neighbours);
			
			Set<Integer> result = execute(newClique, newCandidateSet, newExclusionSet);
			if(result != null)
				return result;
			
			it.remove();
			exclusionSet.add(candidate);
		}
		
		return null;
	}
	
	public Set<Integer> getNewClique(Set<Integer> currentClique, Integer candidate) {
		Set<Integer> newClique = new HashSet<>();
		newClique.addAll(currentClique);
		newClique.add(candidate);
		
		return newClique;
	}
	
	public Set<Integer> intersectSet(Set<Integer> a, Set<Integer> b) {
		Set<Integer> newSet = new HashSet<>();
		
		newSet.addAll(a);
		newSet.retainAll(b);
		
		return newSet;
		
	}
	
	public Set<Integer> neighbours(int fingerprint) {
		if(neighboursCache.containsKey(fingerprint))
			return neighboursCache.get(fingerprint);
		
		Set<Integer> neighbours = new HashSet<>();
		neighboursCache.put(fingerprint, neighbours);
		
		for(int f: fingerprints) 
			if((f & fingerprint) == 0)
				neighbours.add(f);
		
		return neighbours;
	}
}
