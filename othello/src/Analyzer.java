import ia.AbsoluIA;
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
	
	private int[] nbRedDepthWins;
	private int[] nbBlackDepthWins;
	private int[] nbRedDepthPlayed;
	private int[] nbBlackDepthPlayed;
	
	private int[] depthWins;
	
	private Side[][][] winsEqualDepth;
	
	private int[] iaTypeWins;
	
	private int[][] fDepth;
	
	private long[][] timeDepth;
	
	private int nbGamesPlayed;
	private int nbTotalGame;
	private int[] nodesDepth;

	public Analyzer(int untilDepth) {
		super();
		this.untilDepth = untilDepth;
		this.nbRedWins = 0;
		nbGamesPlayed = 0;
		nbTotalGame = Type.values().length*Type.values().length*(untilDepth+1)*(untilDepth+1);
		this.nbBlackWins = 0;
		nbBlackDepthWins = new int[untilDepth+1];
		nbRedDepthWins = new int[untilDepth+1];
		nbBlackDepthPlayed = new int[untilDepth+1];
		nbRedDepthPlayed = new int[untilDepth+1];
		this.fDepth = new int[Type.values().length][untilDepth+1];
		this.winsEqualDepth = new Side[untilDepth+1][Type.values().length][Type.values().length];
		iaTypeWins = new int [Type.values().length];
		depthWins = new int[untilDepth+1];
		nodesDepth = new int[untilDepth+1];
		timeDepth = new long[Type.values().length][untilDepth+1];
	}

	public void analyze() {
		
		System.out.println("Analyse started.");
		
		for (Type p1 : Type.values()) {
			for (Type p2 : Type.values()) {
				for (int depthP1 = 0; depthP1 <= untilDepth ; depthP1++) {
					for (int depthP2 = 0; depthP2 <= untilDepth ; depthP2++) {
						
						Player black = IaBuilder.getIA(p1, depthP1);
						Player red = IaBuilder.getIA(p2, depthP2);
						
						Game game = new Game();
						
						black.setGame(game, Side.BLACK);
						red.setGame(game, Side.RED);
						
						Side winner = null;
						
						try {
							winner = playGame(black, red, game);	
						}catch(OutOfMemoryError e) {
							System.out.println("Memory limitation for depth :" + depthP1 + "(black)" + depthP2 + "(red)");
						}
						
						nbBlackDepthPlayed[depthP1]++;
						nbRedDepthPlayed[depthP2]++;
						
						if (winner == Side.RED) {
							nbRedWins++;
							fDepth[p2.ordinal()][depthP2]++;
							iaTypeWins[p2.ordinal()]++;
							nbRedDepthWins[depthP2]++;
							depthWins[depthP2]++;
						}
						else {
							iaTypeWins[p1.ordinal()]++;
							nbBlackWins++;
							fDepth[p1.ordinal()][depthP1]++;
							nbBlackDepthWins[depthP1]++;
							depthWins[depthP1]++;
						}

						if (depthP1 == depthP2) {
							winsEqualDepth[depthP1][p1.ordinal()][p2.ordinal()] = winner;
						}
							
						nbGamesPlayed++;
					}	
					
					float percent = ((float) nbGamesPlayed)/nbTotalGame*100;
					
					System.out.println("Analyzing ... ("+percent+"% done)");
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
		System.out.println("Results");
		System.out.println("_________\n");
		System.out.println("Red wins :\t"+nbRedWins+"\nBlack wins :\t"+nbBlackWins);
		System.out.println("_________\n");
		displayDepthWins();
		System.out.println("_________\n");
		displayDepthSideWins();
		System.out.println("_________\n");
		displayWinsEqualDepth();
		System.out.println("_________\n");
		displayWinsAccordingToDepth();
		System.out.println("_________\n");
		displayIaWinsGraphe();
		System.out.println("_________\n");	
	}

	private void displayDepthWins() {
		
		System.out.println("Wins percent depending on depth\n");
		for (int depth = 0 ; depth <= untilDepth ; depth++) {
			System.out.print(fixedLengthString("Depth "+depth, 15));
			float winPercent = ((float)depthWins[depth])/nbGamesPlayed*100;
			System.out.print("|");
			for (int i = 0 ; i < winPercent ; i++) {
				System.out.print("-");
			}
			System.out.println("> "+winPercent+"%");
		}
	}

	private void displayDepthSideWins() {
		System.out.println("Side Wins percent depending on depth\n");
		for (int depth = 0 ; depth <= untilDepth ; depth++) {
			System.out.println("Depth "+depth+"\n");
			System.out.print(fixedLengthString(Side.BLACK.toString(), 15));
			float winPercent = ((float)nbBlackDepthWins[depth])/nbBlackDepthPlayed[depth]*100;
			System.out.print("|");
			for (int i = 0 ; i < winPercent ; i++) {
				System.out.print("-");
			}
			System.out.println("> "+winPercent+"%");
			System.out.print(fixedLengthString(Side.RED.toString(), 15));
			winPercent = ((float)nbRedDepthWins[depth])/nbRedDepthPlayed[depth]*100;
			System.out.print("|");
			for (int i = 0 ; i < winPercent ; i++) {
				System.out.print("-");
			}
			System.out.println("> "+winPercent+"%");
			System.out.println();
		}
	}

	private void displayIaWinsGraphe() {
		System.out.println("IA Wins percent\n");
		
		for (Type type : Type.values()) {
			System.out.print(fixedLengthString(type.toString(), 15));
			float winPercent = ((float)iaTypeWins[type.ordinal()])/nbGamesPlayed*100;
			System.out.print("|");
			for (int i = 0 ; i < winPercent ; i++) {
				System.out.print("-");
			}
			System.out.println("> "+winPercent+"%");
		}
	}

	private void displayWinsAccordingToDepth() {
		System.out.println("IA Wins depending on Depth\n");
		
		System.out.print(fixedLengthString("IA type \\ Depth", 20));
		
		for (int depth = 0 ; depth <= untilDepth ; depth++) {
			System.out.print(fixedLengthString(Integer.toString(depth, 20),20));
		}
		
		System.out.println();
		
		for (Type type : Type.values()) {
			
			System.out.print(fixedLengthString(type.toString(), 20));
			
			for (int depth = 0 ; depth <= untilDepth ; depth++) {
				System.out.print(fixedLengthString(Integer.toString(fDepth[type.ordinal()][depth]), 20));
			}
			
			System.out.println();
		}
		
	}

	private void displayWinsEqualDepth() {
		
		for (int depth = 0 ; depth <= untilDepth ; depth++) {
			System.out.println("Wins with equal depth for BLACK and RED : "+depth+"\n");
			
			System.out.print(fixedLengthString("BLACK \\ RED", 15));
			
			for (Type type : Type.values()) {
				System.out.print(fixedLengthString(type.toString(), 15));
			}
			
			System.out.println();
			
			for (Type type : Type.values()) {
				
				System.out.print(fixedLengthString(type.toString(), 15));
				
				for (Type typeAgainst : Type.values()) {
					System.out.print(fixedLengthString(winsEqualDepth[depth][type.ordinal()][typeAgainst.ordinal()].toString(), 15));
				}
				
				System.out.println();
			}
			System.out.println();
		}

	}
	
	private static String fixedLengthString(String string, int length) {
	    return String.format("%-" + length + "." + length + "s", string);
	}
}
