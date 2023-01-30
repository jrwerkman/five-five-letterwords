package nl.novadoc.challenges.jrwer.loop.old;

public class Sentence {
	final int fingerprint;
	final Word[] words;
	boolean correct = true;

	public Sentence() {
		this.words = new Word[0];
		this.fingerprint = 0;
	}

	public Sentence(Sentence s) {
		this.words = s.words;
		this.fingerprint = s.fingerprint;
	}

	public Sentence(Sentence s, Word newWord) {
		this.words = new Word[s.words.length + 1];
		this.fingerprint = addWords(s.words, newWord);
	}

	public Sentence(Word w) {
		this.words = new Word[] {w};
		this.fingerprint = w.fingerprint;
	}

	public Sentence(Word[] newWords) {
		this.words = new Word[newWords.length];
		this.fingerprint = addWords(newWords);
	}

	private int addWords(Word[] newWords, Word newWord) {
		int fp = addWords(newWords);
		fp = addWord(fp, newWords.length, newWord);
		
		return fp;
	}
	
	private int addWords(Word[] newWords) {
		int fp = 0;

		for (int i = 0; i < newWords.length; i++)
			fp = addWord(fp, i, newWords[i]);
		
		return fp;
	}
	
	private int addWord(int fp, int i, Word newWord) {
		this.words[i] = newWord;

		if ((newWord.fingerprint & fp) == 0)
			return fp | newWord.fingerprint;
		else
			correct = false;

		return 0;
	}

	public boolean correct() {
		return correct;
	}

	/**
	 * true if sentence is complete
	 * 
	 * @return
	 */
	public boolean complete() {
		return correct && words.length == 5;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Word w : words)
			if (w != null)
				sb.append(w).append(' ');

		return sb.toString();
	}

	@Override
	public int hashCode() {
		return fingerprint;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sentence) {
			Sentence s = (Sentence) obj;

			return s.fingerprint == fingerprint;
		}

		return false;
	}
}