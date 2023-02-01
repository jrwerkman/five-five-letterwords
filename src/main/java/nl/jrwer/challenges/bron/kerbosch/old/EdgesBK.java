package nl.jrwer.challenges.bron.kerbosch.old;

public class EdgesBK {
	final WordBK a, b;
	final int fingerprint;
	
	public EdgesBK(WordBK a, WordBK b) {
		this.a = a;
		this.b = b;
		this.fingerprint = a.fingerprint | b.fingerprint;
	}
	
	@Override
	public String toString() {
		return a + " - " + b;
	}
	
	@Override
	public int hashCode() {
		return fingerprint;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EdgesBK) {
			EdgesBK w = (EdgesBK) obj;

			return w.fingerprint == fingerprint;
		}

		return false;
	}	
}
