package nl.novadoc.challenges.jrwer.bronker.bosch;

import java.util.HashSet;
import java.util.Set;

class WordBK {
	private final Set<WordBK> neighbours = new HashSet<>();
	final String word;
	final int fingerprint;
	final Set<WordBK> allWords;

	public WordBK(String word, int fingerprint, Set<WordBK> allWords) {
		this.word = word;
		this.fingerprint = fingerprint;
		this.allWords = allWords;
	}
	
	public Set<WordBK> neighbours() {
		if(neighbours.isEmpty())
			setNeighbours();
		
		Set<WordBK> neightboursClone = new HashSet<>();
		neightboursClone.addAll(neighbours);
		
		return neightboursClone;
	}
	
	private void setNeighbours() {
		for(WordBK w: allWords) 
			if((w.fingerprint & fingerprint) == 0)
				neighbours.add(w);
	}

	@Override
	public String toString() {
		return word;
	}
	
	@Override
	public int hashCode() {
		return fingerprint;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WordBK) {
			WordBK w = (WordBK) obj;

			return w.fingerprint == fingerprint;
		}

		return false;
	}	
}