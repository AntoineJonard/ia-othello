import ia.IaBuilder;
import ia.Type;
import othello.Frame;
import othello.Game;
import players.Player;
import players.Side;

public class Analyzer {
	
	private int untilDepth;
	
	private int nbRedWins;
	private int nbBlackWins;
	
	private Side[][] wins;
	
	private int[][] fDepth;

	public Analyzer(int untilDepth) {
		super();
		this.untilDepth = untilDepth;
		this.nbRedWins = 0;
		this.nbBlackWins = 0;
		this.fDepth = new int[Type.values().length][untilDepth];
		this.wins = new Side[Type.values().length][Type.values().length];
	}

	public void analyze() {
		
		for (Type p1 : Type.values()) {
			for (Type p2 : Type.values()) {
				for (int depthP1 = 0; depthP1 < untilDepth ; depthP1++) {
					for (int depthP2 = 0; depthP2 < untilDepth ; depthP2++) {
						Player black = IaBuilder.getIA(p1, depthP1);
						Player red = IaBuilder.getIA(p2, depthP2);
						
						Game game = new Game();
						
						Side winner = null;
						
						try {
							winner = playGame(black, red, game);	
						}catch(OutOfMemoryError e) {
							System.out.println("Memory limitation for depth :" + depthP1 + "(black)" + depthP2 + "(red)");
						}
						
						if (winner == Side.RED)
							nbRedWins++;
						else 
							nbBlackWins++;
						
						if (depthP1 == depthP2) {
							wins[p1.ordinal()][p2.ordinal()] = winner;
						}
						
						fDepth[p1.ordinal()][depthP1]++;
						fDepth[p2.ordinal()][depthP2]++;
					}	
				}
			}	
		}
	}

	private Side playGame(Player black, Player red, Game game) {
		
		while(!game.gameEnd()) {
						
			Frame blackPlayed = black.play();
			
			game.playBlack(blackPlayed);
									
			Frame redPlayed = red.play();
			
			game.playRed(redPlayed);
			
		}
		
		return game.whoWin();
	}
	
	public void displayResult() {
		System.out.println("Red wins : "+nbRedWins+"\nBlack wins :"+nbBlackWins);
		
		for (Type type : Type.values()) {
			System.out.print(type+"\t");
		}
	
	}
}
