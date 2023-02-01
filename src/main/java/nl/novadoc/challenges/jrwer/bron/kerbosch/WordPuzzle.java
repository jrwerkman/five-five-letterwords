package nl.novadoc.challenges.jrwer.bron.kerbosch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nl.novadoc.challenges.jrwer.WordFinderBase;

/**
 * words: https://github.com/dwyl/english-words/blob/master/words_alpha.txt
 * 
 * Source from:
 * https://en.wikipedia.org/wiki/Clique_problem
 * https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
 * https://www.youtube.com/watch?v=j_uQChgo72I
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
	
	@Override
	public void start() throws Exception {
		loadWords();
		
		findFirst();
//		findAll();
	}
	
	public Map<Integer, Set<Integer>> neighboursCache = new HashMap<>();
	public long loading, start, end;
	
	public void findAll() throws Exception {
		start = System.currentTimeMillis();
		Set<Integer> candidateSet = new HashSet<>();
		candidateSet.addAll(fingerprints);
		findAll(new HashSet<>(), candidateSet, new HashSet<>());
		end = System.currentTimeMillis();

		System.out.println(String.format("\nFinding sentences took: %d ms", end - start));
		System.out.println(String.format("Total time took: %d ms", end - loading));
	}
	
	public void findFirst() throws Exception {
		start = System.currentTimeMillis();
		Set<Integer> candidateSet = new HashSet<>();
		candidateSet.addAll(fingerprints);
		Set<Integer> results = execute(new HashSet<>(), candidateSet, new HashSet<>());
		end = System.currentTimeMillis();
		
		System.out.println("\nResult:");
		System.out.println(toString(results, words));
		System.out.println("\nCharacters used:");
		System.out.println(getCharactersUsed(results));
		
		System.out.println(String.format("\nFinding sentence took: %d ms", end - start));
		System.out.println(String.format("Total time took: %d ms", end - loading));
	}
	
	public void loadWords() throws FileNotFoundException, IOException {
		loading = System.currentTimeMillis();
		super.loadWords();
		end = System.currentTimeMillis();
		System.out.println(String.format("Procesed %d words", totalWords));
		System.out.println(String.format("Loaded distinct %d five letter words, with five different chars", word5_5letters));
		System.out.println(String.format("Loaded distinct unique letter %d words, with five different chars", words.size()));
		System.out.println("Loading wordlist took: " + (end - loading) + " ms\n");		
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

	public void findAll(Set<Integer> currentClique, Set<Integer> candidateSet, Set<Integer> exclusionSet) {
		if((candidateSet.isEmpty() && exclusionSet.isEmpty()) || currentClique.size() == 5) {
			if(currentClique.size() == 5)
				System.out.println(toString(currentClique, words));
		} else {
			Iterator<Integer> it = candidateSet.iterator();
			while(it.hasNext()) {
				Integer candidate = it.next();
				
				Set<Integer> newClique = getNewClique(currentClique, candidate);
				Set<Integer> neighbours = neighbours(candidate);
				Set<Integer> newCandidateSet = intersectSet(candidateSet, neighbours);
				Set<Integer> newExclusionSet = intersectSet(exclusionSet, neighbours);
				
				findAll(newClique, newCandidateSet, newExclusionSet);
				it.remove();
				exclusionSet.add(candidate);
			}
		}
	}
	
	public Set<Integer> getNewClique(Set<Integer> currentClique, Integer candidate) {
		Set<Integer> newClique = new HashSet<>();
		newClique.addAll(currentClique);
		newClique.add(candidate);
		
		return newClique;
	}
	
	public Set<Integer> intersectSet(Set<Integer> a, Set<Integer> b) {
		Set<Integer> newSet = new HashSet<>();
		
		for(Integer i : a)
			if(b.contains(i))
				newSet.add(i);
		
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
