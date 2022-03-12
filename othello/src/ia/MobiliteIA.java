package ia;

import java.util.ArrayList;import java.util.Comparator;
import java.util.List;

import othello.Frame;
import othello.Game;
import players.IA;

public class MobiliteIA extends IA{

	public MobiliteIA(int maxDepth) {
		super(maxDepth);
		this.type = Type.MOBILITE;
	}

	@Override
	public float computeHeuristique(Game game) {
		
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
		
		if (played.isEmpty()) {
			moyMinDist = 7;
		}else {
			moyMinDist /= played.size();
		}

		return possibilities-moyMinDist;
	}

}
