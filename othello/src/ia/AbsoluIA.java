package ia;

import othello.Game;
import players.IA;
import players.Side;

public class AbsoluIA  extends IA{

	public AbsoluIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.ABSOLU;
	}

	@Override
	public float computeHeuristique(Game game) {
		return getSide() == Side.BLACK ? game.getNbBlackFrame() : game.getNbRedFrame();
	}

}
