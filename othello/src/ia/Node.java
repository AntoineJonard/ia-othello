package ia;

import java.util.ArrayList;
import java.util.List;

import othello.Frame;

public class Node {

	private int value;
	private int depth;
	private List<Node> childs;
	private Frame f;
	
	public Node(Frame f, int depth) {
		super();
		this.f = f;
		this.depth = depth;
		childs = new ArrayList<>();
	}

	public int getDepth() {
		return depth;
	}

	public List<Node> getChilds() {
		return childs;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public Frame getF() {
		return f;
	}
		
}
