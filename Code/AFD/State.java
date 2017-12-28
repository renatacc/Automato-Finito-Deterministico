package AFD;

public class State {
	
	private int id;
	private int name;
	private boolean stateInitial;
	private boolean stateFinal;
	
	public State() {
		
	}
	
	public State(int id, boolean stateInitial, boolean stateFinal) {
		this.id = id;
		this.name = id;
		this.stateInitial = stateInitial;
		this.stateFinal = stateFinal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public boolean isStateInitial() {
		return stateInitial;
	}

	public void setStateInitial(boolean stateInitial) {
		this.stateInitial = stateInitial;
	}

	public boolean isStateFinal() {
		return stateFinal;
	}

	public void setStateFinal(boolean stateFinal) {
		this.stateFinal = stateFinal;
	}
}
