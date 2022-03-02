package ia;

import othello.Game;
import players.IA;

public class PositionnelIA extends IA {

	public PositionnelIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.POSITIONNEL;
	}

	@Override
	public int computeHeuristique(Node current, Game game) {
		return tacticalValues[current.getF().getI()][current.getF().getP()];
	}

}
