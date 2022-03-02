package ia;

import othello.Game;
import othello.State;
import players.IA;

public class AbosuluIA  extends IA{

	public AbosuluIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.ABSOLU;
	}

	@Override
	public int computeHeuristique(Node current, Game game) {
		return current.getF().getState()== State.BLACK ? game.getNbBlackFrame() : game.getNbRedFrame();
	}

}
