package ia;

import java.util.ArrayList;
import java.util.List;
import othello.Frame;
import othello.Game;
import players.IA;
import players.Side;

public class MixteIA extends IA{

	public MixteIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.MIXTE;
	}

	@Override
	public float computeHeuristique(Game game) {
		
		int nbPlays = game.getNbPlays();
		float heuristique = 0;
		
		if (nbPlays < 20) {
			List<Frame> played = game.getSidePlayed(getSide());
			
			int tacticalValue = 0;
			
			for (Frame f : played)
				 tacticalValue += tacticalValues[f.getI()][f.getP()];
			
			heuristique = tacticalValue;
		}else if (nbPlays < 50) {
			int possibilities = game.getSidePlayable(getSide()).size();
			
			List<Frame> played = game.getSidePlayed(getSide());
			
			float moyMinDist = 0;
			
			for (Frame f : played){
				int toTopLeft = f.getI()+f.getP();
				int toBottomRight = 14-f.getI()-f.getP();
				int toTopRight = 7-f.getP()+f.getI();
				int toBottomLeft = 7-f.getI()+f.getP();
				
				List<Integer> distances = new ArrayList<>();
				distances.add(toTopLeft);
				distances.add(toBottomRight);
				distances.add(toTopRight);
				distances.add(toBottomLeft);
				
				float minDist = distances.stream().min((d1, d2) -> d1 - d2).get();
				
				moyMinDist += minDist;
			}
			
			moyMinDist /= played.size();
			
			heuristique = possibilities - moyMinDist;
		}else {
			heuristique = getSide() == Side.BLACK ? game.getNbBlackFrame() : game.getNbRedFrame();
		}
		
		return heuristique;
	}

}
