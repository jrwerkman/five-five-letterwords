package nl.novadoc.challenges.jrwer.loop;

class Word {
	final String word;
	final int fingerprint;

	public Word(String word, int fingerprint) {
		this.word = word;
		this.fingerprint = fingerprint;
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
		if (obj instanceof Word) {
			Word w = (Word) obj;

			return w.fingerprint == fingerprint;
		}

		return false;
	}	
}