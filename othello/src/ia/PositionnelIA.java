package ia;

import java.util.List;

import othello.Frame;
import othello.Game;
import players.IA;

public class PositionnelIA extends IA {

	public PositionnelIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.POSITIONNEL;
	}

	@Override
	public float computeHeuristique(Game game) {
		
		List<Frame> played = game.getSidePlayed(getSide());
		
		int tacticalValue = 0;
		
		for (Frame f : played)
			 tacticalValue += tacticalValues[f.getI()][f.getP()];
		
		return tacticalValue;
	}

}
