package ia;

import players.IA;

public class PositionnelIA extends IA {

	public PositionnelIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.POSITIONNEL;
	}

	@Override
	public int computeHeuristique(Node current, Node minChild) {
		return tacticalValues[current.getF().getI()][current.getF().getP()] + (minChild != null ? minChild.getValue():0);
	}

}
