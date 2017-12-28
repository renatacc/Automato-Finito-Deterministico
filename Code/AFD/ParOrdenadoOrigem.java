package AFD;

public class ParOrdenadoOrigem {
	private int x;
	private int y;
	private AFD xOrigem;
	private AFD yOrigem;
	
	public ParOrdenadoOrigem() {
		
	}
	
	public ParOrdenadoOrigem(int x, int y, AFD xOrigem, AFD yOrigem) {
		this.x = x;
		this.y = y;
		this.xOrigem = xOrigem;
		this.yOrigem = yOrigem;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public AFD getxOrigem() {
		return xOrigem;
	}

	public void setxOrigem(AFD xOrigem) {
		this.xOrigem = xOrigem;
	}

	public AFD getyOrigem() {
		return yOrigem;
	}

	public void setyOrigem(AFD yOrigem) {
		this.yOrigem = yOrigem;
	}
}
