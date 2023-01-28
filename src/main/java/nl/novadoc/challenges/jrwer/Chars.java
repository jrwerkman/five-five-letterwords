package nl.novadoc.challenges.jrwer;

public enum Chars {
	A('a',1),
	B('b',2),
	C('c',4),
	D('d',8),
	E('e',16),
	F('f',32),
	G('g',64),
	H('h',128),
	I('i',256),
	J('j',512),
	K('k',1024),
	L('l',2048),
	M('m',4096),
	N('n',8192),
	O('o',16384),
	P('p',32768),
	Q('q',65536),
	R('r',131072),
	S('s',262144),
	T('t',524288),
	U('u',1048576),
	V('v',2097152),
	W('w',4194304),
	X('x',8388608),
	Y('y',16777216),
	Z('z',33554432);
	
	public final char character;
	public final int bit;
	
	private Chars(char character, int bit) {
		this.character = character;
		this.bit = bit;
	}
	
	public static Chars get(char character) {
		for(Chars c : values())
			if(c.character == character)
				return c;
		
		throw new RuntimeException();
	}
	
	public static Chars get(int bit) {
		for(Chars c : values())
			if(c.bit == bit)
				return c;
		
		throw new RuntimeException();
	}
}
