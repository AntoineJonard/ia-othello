package players;

import java.util.ArrayList;
import java.util.List;

import ia.Node;
import ia.Type;
import othello.Frame;
import othello.Game;
import othello.State;

public abstract class IA extends Player {

	protected final static int[][] tacticalValues = {
			{500, -150, 30, 10, 10, 30, -150, 500},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{500, -150, 30, 10, 10, 30, -150, 500},
	};
	
	private int maxDepth;
	
	protected Type type;
	
	public IA(int maxDepth) {
		super();
		this.maxDepth = maxDepth;
	}
	
	public Type getType() {
		return type;
	}

	@Override
	public Frame play() {

		List<Frame> playables = getSide() == Side.BLACK ? getGame().getBlackPlayables() : getGame().getRedPlayables();;
		List<Node> nodes = new ArrayList<>();
		
		if (playables.isEmpty()) {
			return null;
		}
		
		for (Frame f : playables) {
			nodes.add(new Node(new Frame(f), 0));
		}
		
		Node max = max(nodes,Integer.MAX_VALUE,getGame());
		
		return new Frame(State.EMPTY,max.getF().getI(),max.getF().getP());
	}
	
	private Node min(List<Node> nodes, float maxValue, Game game) {
		
		float minValue = Integer.MAX_VALUE;
		Node minNode = null;
		
		for (Node n : nodes) {

			Game fictive = new Game(game);
			
			fictive.playSide(getOpponentSide(), n.getF());
							
			for (Frame f : fictive.getSidePlayable(getSide())) {
				n.getChilds().add(new Node(new Frame(f),n.getDepth()+1));
			}
			
			if (n.getDepth() >= maxDepth || n.getChilds().isEmpty()) 
				n.setValue(computeHeuristique(fictive));
			else {

				Node max = max(n.getChilds(), minValue ,fictive);
				
				n.setValue(max.getValue());

			}

			if (n.getValue() < minValue) {
				minValue = n.getValue();
				minNode = n;
				if (minValue < maxValue)
					return minNode;
			}
		}
		
		return minNode;
	}
	
	private Node max(List<Node> nodes, float minValue, Game game) {
		
		float maxValue = Integer.MIN_VALUE;
		Node maxNode = null;
		
		for (Node n : nodes) {
				
			Game fictive = new Game(game);
			
			fictive.playSide(getSide(), n.getF());
			
			for (Frame f : fictive.getSidePlayable(getOpponentSide())) {
				n.getChilds().add(new Node(new Frame(f),n.getDepth()+1));
			}
			
			if (n.getDepth() >= maxDepth || n.getChilds().isEmpty()) 
				n.setValue(computeHeuristique(fictive));
			else {
				
				Node min = min(n.getChilds(), maxValue ,fictive);
					
				n.setValue(min.getValue());
					
			}
			
			if (n.getValue()>maxValue) {
				maxValue = n.getValue();
				maxNode = n;
				if (maxValue > minValue)
					return maxNode;
			}
		}
		if (maxNode == null)
			System.out.println();
		
		return maxNode;
	}
	
	public abstract float computeHeuristique(Game game);
}
