package nl.novadoc.challenges.jrwer.bron.kerbosch.old;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nl.novadoc.challenges.jrwer.loop.old.Sentence;

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
public class WordPuzzleBK {
	public static void main(String[] args) {
		try {
			WordPuzzleBK p = new WordPuzzleBK();
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Set<WordBK> words;
	private Set<WordBK> result;
	private boolean found;
	private int i;
	
	public void start() throws FileNotFoundException, IOException {
		findSentenceWitouthPivoting();
		findSentencePivoting();
	}
	
	public void init() throws FileNotFoundException, IOException {
		this.words = WordLoaderBK.loadWords();
		this.result = new HashSet<>();
		this.i=0;
		this.found = false;
	}

	public void findSentenceWitouthPivoting() throws FileNotFoundException, IOException {
		init();
		System.out.println("findSentenceWitouthPivoting");

		HashSet<WordBK> candidateSet = new HashSet<>();
		candidateSet.addAll(words);

		long start = System.currentTimeMillis();
		findSentenceWitouthPivoting(new HashSet<>(), candidateSet, new HashSet<>());
		long end = System.currentTimeMillis();
		
		print("findSentenceWitouthPivoting", result);
		System.out.println("Iterations: " + i);
		System.out.println("Process took: " + (end - start) + " ms\n");
	}
	
	/**
	 *	algorithm BronKerbosch1(R, P, X) is
	 *	    if P and X are both empty then
	 *	        report R as a maximal clique
	 * 	    for each vertex v in P do
	 *	        BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
	 *	        P := P \ {v}
	 *	        X := X ⋃ {v}	
	 * 
	 * @param currentClique
	 * @param candidateSet
	 * @param exclusionSet
	 */
	public void findSentenceWitouthPivoting(Set<WordBK> currentClique, Set<WordBK> candidateSet, Set<WordBK> exclusionSet) {
		if(candidateSet.isEmpty() && exclusionSet.isEmpty()) {
			if(currentClique.size() == 5) {
				result.addAll(currentClique);
				found = true;
				return;
			}
		} 
		
		
		if(!found) {
			Iterator<WordBK> it = candidateSet.iterator();
			while(it.hasNext()) {
				i++;
				if(found)
					return;
				
				WordBK candidate = it.next();
				
				Set<WordBK> newClique = new HashSet<>();
				newClique.addAll(currentClique);
				newClique.add(candidate);

				Set<WordBK> newCandidateSet = new HashSet<>();
				newCandidateSet.addAll(candidateSet);
				newCandidateSet.retainAll(candidate.neighbours());
				
				Set<WordBK> newExclusionSet = new HashSet<>();
				newExclusionSet.addAll(exclusionSet);
				newExclusionSet.retainAll(candidate.neighbours());
				
				findSentenceWitouthPivoting(newClique, newCandidateSet, newExclusionSet);
				it.remove(); // remove candidate
				exclusionSet.add(candidate);
			}
		}
	}

	public void findSentencePivoting() throws FileNotFoundException, IOException {
		init();
		System.out.println("findSentencePivoting");

		HashSet<WordBK> candidateSet = new HashSet<>();
		candidateSet.addAll(words);

		long start = System.currentTimeMillis();
		findSentencePivoting(new HashSet<>(), candidateSet, new HashSet<>());
		long end = System.currentTimeMillis();
		
		print("findSentencePivoting", result);
		System.out.println("Iterations: " + i);
		System.out.println("Process took: " + (end - start) + " ms\n");
	}
	
	/**
	 *	algorithm BronKerbosch2(R, P, X) is
	 *	    if P and X are both empty then
	 *	        report R as a maximal clique
	 *	    choose a pivot vertex u in P ⋃ X
	 *	    for each vertex v in P \ N(u) do
	 *	        BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
	 *	        P := P \ {v}
	 *	        X := X ⋃ {v}
	 * 
	 * @param currentClique
	 * @param candidateSet
	 * @param exclusionSet
	 */
	public void findSentencePivoting(Set<WordBK> currentClique, Set<WordBK> candidateSet, Set<WordBK> exclusionSet) {
		if(candidateSet.isEmpty() && exclusionSet.isEmpty()) {
			if(currentClique.size() == 5) {
				result.addAll(currentClique);
				found = true;
				return;
			}
		} 

		if(!found) {
			Set<WordBK> pivotVertex = new HashSet<>();
			
			for(WordBK w : currentClique)
				if(w.neighbours().size() > pivotVertex.size())
					pivotVertex = w.neighbours();

			candidateSet.removeAll(pivotVertex);
			
			Iterator<WordBK> it = candidateSet.iterator();
			while(it.hasNext()) {
				i++;
				if(found)
					return;
				
				WordBK candidate = it.next();
				
				Set<WordBK> newClique = new HashSet<>();
				newClique.addAll(currentClique);
				newClique.add(candidate);

				Set<WordBK> newCandidateSet = new HashSet<>();
				newCandidateSet.addAll(candidateSet);
				newCandidateSet.retainAll(candidate.neighbours());
				
				Set<WordBK> newExclusionSet = new HashSet<>();
				newExclusionSet.addAll(exclusionSet);
				newExclusionSet.retainAll(candidate.neighbours());
				
				findSentenceWitouthPivoting(newClique, newCandidateSet, newExclusionSet);
				it.remove(); // remove candidate
				exclusionSet.add(candidate);
			}
		}
	}	
	
	public void print(String pre, Set<WordBK> currentClique) {
		System.out.print(pre + ": ");
		for(WordBK w: currentClique)
			System.out.print(w + " ");

		System.out.println();
	}	
}
