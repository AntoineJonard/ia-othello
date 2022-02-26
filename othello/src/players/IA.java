package players;

import java.util.ArrayList;
import java.util.List;

import ia.Node;
import ia.Type;
import othello.Frame;
import othello.Game;

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

		List<Frame> playables = getGame().displayPlayables(getSide());
		List<Node> nodes = new ArrayList<>();
		
		if (playables.isEmpty()) {
			System.out.println("Aucun coup n'est possible ! Tour passé");
			return null;
		}
		
		for (Frame f : playables) {
			nodes.add(new Node(f, 0));
		}
		
		Node max = max(nodes,getGame());
		
		return max.getF();
	}
	
	private Node min(List<Node> nodes, Game game) {
		
		for (Node n : nodes) {

			if (n.getDepth() >= maxDepth) {
				n.setValue(computeHeuristique(n, null));
			}else {
				Game fictive = new Game(getGame());
				
				fictive.playSide(getOpponentSide(), n.getF());
								
				for (Frame f : fictive.getSidePlayable(getSide())) {
					n.getChilds().add(new Node(f,n.getDepth()+1));
				}
				
				if (n.getChilds().isEmpty()) {
					n.setValue(computeHeuristique(n, null));
				}else {
					Node max = max(n.getChilds(),fictive);
					
					n.setValue(computeHeuristique(n, max));
				}
			}

		}
		
		return nodes.stream().min((n1, n2) -> n1.getValue() - n2.getValue()).get();
	}
	
	private Node max(List<Node> nodes, Game game) {
		
		for (Node n : nodes) {
				
			if (n.getDepth() >= maxDepth) {
				n.setValue(computeHeuristique(n, null));
			}else {
				Game fictive = new Game(getGame());
				
				fictive.playSide(getSide(), n.getF());
								
				for (Frame f : fictive.getSidePlayable(getOpponentSide())) {
					n.getChilds().add(new Node(f,n.getDepth()+1));
				}
				
				if (n.getChilds().isEmpty()) {
					n.setValue(computeHeuristique(n, null));
				}else {
					Node min = min(n.getChilds(),fictive);
					
					n.setValue(computeHeuristique(n, min));
				}
			}
				
		}
		
		return nodes.stream().max((n1, n2) -> n1.getValue() - n2.getValue()).get();
	}
	
	public abstract int computeHeuristique(Node current, Node child);
}
