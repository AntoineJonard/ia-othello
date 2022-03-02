package ia;

import java.util.ArrayList;
import java.util.List;

import othello.Frame;

public class Node {

	private int value;
	private int depth;
	private List<Node> childs;
	private Frame f;
	private boolean treated;
	
	public Node(Frame f, int depth) {
		super();
		this.f = f;
		this.depth = depth;
		childs = new ArrayList<>();
		treated = false;
	}

	public int getDepth() {
		return depth;
	}

	public List<Node> getChilds() {
		return childs;
	}

	public void setValue(int value) {
		treated = true;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public Frame getF() {
		return f;
	}

	public boolean isTreated() {
		return treated;
	}
	
	
		
}
