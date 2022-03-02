package ia;

import java.util.ArrayList;
import java.util.List;

import othello.Frame;
import othello.Game;
import othello.State;
import players.IA;

public class MixteIA extends IA{

	public MixteIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.MIXTE;
	}

	@Override
	public int computeHeuristique(Node current, Game game) {
		
		int nbPlays = game.getNbPlays();
		int heuristique = 0;
		
		if (nbPlays < 20) {
			heuristique = tacticalValues[current.getF().getI()][current.getF().getP()];
		}else if (nbPlays < 50) {
			int possibilities = game.getSidePlayable(getSide()).size();
			
			Frame f = current.getF();
			
			int toTopLeft = f.getI()+f.getP();
			int toBottomRight = 14-f.getI()-f.getP();
			int toTopRight = 7-f.getP()+f.getI();
			int toBottomLeft = 7-f.getI()+f.getP();
			
			List<Integer> distances = new ArrayList<>();
			distances.add(toTopLeft);
			distances.add(toBottomRight);
			distances.add(toTopRight);
			distances.add(toBottomLeft);
			
			int minDist = distances.stream().min((d1, d2) -> d1 - d2).get();
			
			heuristique = possibilities - minDist;
		}else {
			heuristique = current.getF().getState()== State.BLACK ? game.getNbBlackFrame() : game.getNbRedFrame();
		}
		
		return heuristique;
	}

}
