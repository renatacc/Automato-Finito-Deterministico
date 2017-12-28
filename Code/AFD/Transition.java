package AFD;

public class Transition {

	private String letra;
	private int from;
	private int to;
	
	public Transition() {
		
	}

	public Transition(int from, int to, String letra) {
		this.letra = letra;
		this.from = from;
		this.to = to;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
}
