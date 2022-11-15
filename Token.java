package sourcery;

public class Token{
	public String value;
	public int start;
	public int stop;

	public Token(String v, int a, int b){
		value = v;
		start = a;
		stop = b;
	}
}