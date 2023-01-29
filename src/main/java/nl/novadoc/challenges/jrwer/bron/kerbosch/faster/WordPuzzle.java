package nl.novadoc.challenges.jrwer.bron.kerbosch.faster;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * words: https://github.com/dwyl/english-words/blob/master/words_alpha.txt
 * 
 * 5 word, 5 letters, 25 different letters
 * 
 * @author jan
 *
 */
public class WordPuzzle extends WordLoader{
	public static void main(String[] args) {
		try {
			WordPuzzle p = new WordPuzzle();
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<Integer, Set<Integer>> neighboursCache = new HashMap<>();
	
	public WordPuzzle() throws FileNotFoundException, IOException {
		loadWords();
	}
	
	public void start() throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		Set<Integer> candidateSet = new HashSet<>();
		candidateSet.addAll(fingerprints);
		
		Set<Integer> results = execute(new HashSet<>(), candidateSet, new HashSet<>());
		print(results);

		
		long end = System.currentTimeMillis();
		
		System.out.println("Process took: " + (end - start) + " ms\n");
	}

	public Set<Integer> execute(Set<Integer> currentClique, Set<Integer> candidateSet, Set<Integer> exclusionSet) {
		if((candidateSet.isEmpty() && exclusionSet.isEmpty()) || currentClique.size() == 5)
			if(currentClique.size() == 5) {
				print(candidateSet);
				return currentClique;
			}
	
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
	
	public void print(Set<Integer> results) {
		StringBuilder sb = new StringBuilder();
		
		for(Integer r : results) {
			List<String> word = words.get(r);
			
			sb.append(word.get(0)).append(' ');
		}
		
		System.out.println(sb);		
	}
}
