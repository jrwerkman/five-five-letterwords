package nl.jrwer.challenges;

import java.util.Arrays;

public class Data {
	final int fingerprint;
	final int[] words;
	private boolean correct = true;
	
	public Data() {
		this.fingerprint = 0;
		this.words = new int[0];
	}
	
	public Data(Data d, int newWord) {
		this.words = Arrays.copyOf(d.words, d.words.length + 1);
		this.words[d.words.length] = newWord;
		
		this.fingerprint = correct(d.fingerprint, newWord);
	}
	
	private int correct(int fp, int w) {
		if ((fp & w) != 0)
			this.correct = false;
			
		return fp = fp | w;
	}
	
	public boolean correct() {
		return correct;
	}
	
	public int length() {
		return words.length;
	}
	
	public boolean complete() {
		return words.length == 5;
	}
	
	@Override
	public int hashCode() {
		return fingerprint;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if(obj instanceof Data)
			return ((Data) obj).fingerprint == fingerprint;
		
		return false;
	}
}