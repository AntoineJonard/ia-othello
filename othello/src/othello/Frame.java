package othello;

import java.util.Objects;

public class Frame {
	
	private State state;
	private int i;
	private int p;

	public Frame(int i, int p) {
		super();
		this.state = State.EMPTY;
		this.i=i;
		this.p=p;
	}
	
	public boolean isEmpty() {
		return state == State.EMPTY;
	}
	
	public void setRed() {
		this.state = State.RED;
	}
	
	public void setBlack() {
		this.state = State.BLACK;
	}
	
	public void reverse() {
		if (state==State.BLACK)
			setRed();
		else if (state==State.RED)
			setBlack();
	}

	public State getState() {
		return state;
	}

	public int getI() {
		return i;
	}

	public int getP() {
		return p;
	}

	@Override
	public int hashCode() {
		return Objects.hash(i, p, state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frame other = (Frame) obj;
		return i == other.i && p == other.p && state == other.state;
	}

	@Override
	public String toString() {
		return "("+p+","+i+") : "+state;
	}
	
	
}
