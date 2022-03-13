import java.util.concurrent.TimeUnit;

import ia.AbsoluIA;
import ia.IaBuilder;
import ia.Type;
import othello.Frame;
import othello.Game;
import players.IA;
import players.Player;
import players.Side;

public class Analyzer {
	
	private int untilDepth;
	private int fromDepth;
	
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
	
	private long[][] averageTimeSpentDepth;
	
	private int nbGamesPlayed;
	private int nbTotalGame;
	private double[][] nodesGeneratedDepth;
	
	private int range;

	public Analyzer(int fromDepth, int untilDepth) {
		super();
		range = untilDepth - fromDepth+1;
		this.untilDepth = untilDepth;
		this.fromDepth = fromDepth;
		this.nbRedWins = 0;
		nbGamesPlayed = 0;
		nbTotalGame = Type.values().length*Type.values().length*(range)*(range);
		this.nbBlackWins = 0;
		nbBlackDepthWins = new int[range];
		nbRedDepthWins = new int[range];
		nbBlackDepthPlayed = new int[range];
		nbRedDepthPlayed = new int[range];
		this.fDepth = new int[Type.values().length][range];
		this.winsEqualDepth = new Side[range][Type.values().length][Type.values().length];
		iaTypeWins = new int [Type.values().length];
		depthWins = new int[range];
		nodesGeneratedDepth = new double[Type.values().length][range];
		averageTimeSpentDepth = new long[Type.values().length][range];
	}

	public void analyze() {
		
		System.out.println("Analyse started.");
		
		for (Type p1 : Type.values()) {
			for (Type p2 : Type.values()) {
				for (int depthP1 = 0; depthP1 < range ; depthP1++) {
					for (int depthP2 = 0; depthP2 < range ; depthP2++) {
						
						IA black = IaBuilder.getIA(p1, depthP1+fromDepth);
						IA red = IaBuilder.getIA(p2, depthP2+fromDepth);
						
						Game game = new Game();
						
						black.setGame(game, Side.BLACK);
						red.setGame(game, Side.RED);
						
						Side winner = null;
						
						try {
							winner = playGame(black, red, game);	
						}catch(OutOfMemoryError e) {
							System.out.println("Memory limitation for depth :" + (depthP1+fromDepth) + "(black)" + (depthP2+fromDepth) + "(red)");
						}
						
						nbBlackDepthPlayed[depthP1]++;
						nbRedDepthPlayed[depthP2]++;
						
						nodesGeneratedDepth[p1.ordinal()][depthP1]+=black.getNbNodesGenerated();
						nodesGeneratedDepth[p2.ordinal()][depthP2]+=red.getNbNodesGenerated();
						averageTimeSpentDepth[p1.ordinal()][depthP1]+=black.getAverageTimeSpent();
						averageTimeSpentDepth[p2.ordinal()][depthP2]+=red.getAverageTimeSpent();
						
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
					
					System.out.println(String.format("Analyzing ... (%02d% done)",percent));
				}
			}
		}
		
		int nbIaInstanceGamesPlayed = Type.values().length * (range) * 2;
		
		for (Type type : Type.values()) {
			for (int depth = 0; depth < range ; depth++) {
				nodesGeneratedDepth[type.ordinal()][depth] /= nbIaInstanceGamesPlayed;
				averageTimeSpentDepth[type.ordinal()][depth] /= nbIaInstanceGamesPlayed;
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
		displayNodesGenerated();
		System.out.println("_________\n");	
		displayTimeSpent();
	}

	private void displayTimeSpent() {
		System.out.println("Average time spent (secondes) for a choice for each type of IA depending on depth\n");
		
		System.out.print(fixedLengthString("IA type \\ Depth", 20));
		
		for (int depth = 0 ; depth < range ; depth++) {
			System.out.print(fixedLengthString(Integer.toString((depth+fromDepth), 20),20));
		}
		
		System.out.println();
		
		for (Type type : Type.values()) {
			
			System.out.print(fixedLengthString(type.toString(), 20));
			
			for (int depth = 0 ; depth < range ; depth++) {
				
				String min = String.format("%02d:%02d",
					     TimeUnit.MILLISECONDS.toSeconds(averageTimeSpentDepth[type.ordinal()][depth]),
					     averageTimeSpentDepth[type.ordinal()][depth] -
					     TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(averageTimeSpentDepth[type.ordinal()][depth])));
				
				System.out.print(fixedLengthString(min, 20));
			}
			
			System.out.println();
		}
		
	}

	private void displayNodesGenerated() {
		System.out.println("Average nodes generated for each type of IA depending on depth\n");
		
		System.out.print(fixedLengthString("IA type \\ Depth", 20));
		
		for (int depth = 0 ; depth < range ; depth++) {
			System.out.print(fixedLengthString(Integer.toString((depth+fromDepth), 20),20));
		}
		
		System.out.println();
		
		for (Type type : Type.values()) {
			
			System.out.print(fixedLengthString(type.toString(), 20));
			
			for (int depth = 0 ; depth < range ; depth++) {
				System.out.print(fixedLengthString(Integer.toString((int)Math.floor(nodesGeneratedDepth[type.ordinal()][depth])), 20));
			}
			
			System.out.println();
		}
		
		
	}

	private void displayDepthWins() {
		
		System.out.println("Wins percent depending on depth\n");
		for (int depth = 0 ; depth < range ; depth++) {
			System.out.print(fixedLengthString("Depth "+(depth+fromDepth), 15));
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
		for (int depth = 0 ; depth < range ; depth++) {
			System.out.println("Depth "+(depth+fromDepth)+"\n");
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
		
		for (int depth = 0 ; depth < range ; depth++) {
			System.out.print(fixedLengthString(Integer.toString((depth+fromDepth), 20),20));
		}
		
		System.out.println();
		
		for (Type type : Type.values()) {
			
			System.out.print(fixedLengthString(type.toString(), 20));
			
			for (int depth = 0 ; depth < range ; depth++) {
				System.out.print(fixedLengthString(Integer.toString(fDepth[type.ordinal()][depth]), 20));
			}
			
			System.out.println();
		}
		
	}

	private void displayWinsEqualDepth() {
		
		for (int depth = 0 ; depth < range ; depth++) {
			System.out.println("Wins with equal depth for BLACK and RED : "+(depth+fromDepth)+"\n");
			
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
