package nl.novadoc.challenges.jrwer.bronkerbosch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import nl.novadoc.challenges.jrwer.Sentence;

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
	private Set<WordBK> result = new HashSet<>();
	private boolean found = false;
	
	public void start() throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		this.words = WordLoaderBK.loadWords();

		findSentenceWitouthPivoting();
		print("findSentenceWitouthPivoting", result);
		long end = System.currentTimeMillis();
		
		System.out.println("Process took: " + (end - start) + " ms\n");
	}

	/**
		algorithm BronKerbosch1(R, P, X) is
		    if P and X are both empty then
		        report R as a maximal clique
		    for each vertex v in P do
		        BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
		        P := P \ {v}
		        X := X ⋃ {v}	
	 */
	public void findSentenceWitouthPivoting() {
		HashSet<WordBK> candidateSet = new HashSet<>();
		candidateSet.addAll(words);
		
//		System.out.println(words.size());
		test(new HashSet<>(), candidateSet, new HashSet<>());
	}
	
	public void test(Set<WordBK> currentClique, Set<WordBK> candidateSet, Set<WordBK> exclusionSet) {
		if(candidateSet.isEmpty() && exclusionSet.isEmpty()) {
			if(currentClique.size() == 5) {
				result.addAll(currentClique);
				found = true;
				return;
			}
		} else if(!found) {
			Iterator<WordBK> it = candidateSet.iterator();
			while(it.hasNext()) {
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
				
				test(newClique, newCandidateSet, newExclusionSet);
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

	/**
		algorithm BronKerbosch2(R, P, X) is
		    if P and X are both empty then
		        report R as a maximal clique
		    choose a pivot vertex u in P ⋃ X
		    for each vertex v in P \ N(u) do
		        BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
		        P := P \ {v}
		        X := X ⋃ {v}
	 */
	public Sentence findSentencePivoting() {
		
		return new Sentence();	
	}
	
	/**
		algorithm BronKerbosch3(G) is
		    P = V(G)
		    R = X = empty
		    for each vertex v in a degeneracy ordering of G do
		        BronKerbosch2({v}, P ⋂ N(v), X ⋂ N(v))
		        P := P \ {v}
		        X := X ⋃ {v}
	 */
	public Sentence findSentenceVertexOrdering() {

		return new Sentence();	
	}
}
