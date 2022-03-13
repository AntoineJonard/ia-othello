import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ia.IaBuilder;
import ia.Type;
import othello.Frame;
import othello.Game;
import players.IA;
import players.Player;
import players.Random;
import players.Side;

public class RandomAnalyzer {
	
	private int nbGames;
	private int depth;
	
	private long[] averageTimeSpent;
	private double[] nodesGenerated;
	private int[] iaTypeWins;
	private int nbGamesPlayed;

	public RandomAnalyzer(int nbGames, int depth) {
		this.nbGames = nbGames;
		this.depth = depth;
		averageTimeSpent = new long[Type.values().length];
		nodesGenerated = new double[Type.values().length];
		iaTypeWins = new int[Type.values().length];
		nbGamesPlayed = 0;
	}
	
	public void analyze() {
		System.out.println("Analyzing ... ");
		
		Timer timer = new Timer(true);
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				float percent = ((float) nbGamesPlayed)/(nbGames*Type.values().length)*100;

				if (percent < 100f)
					System.out.println(String.format("%.2f",percent)+"% done");
				else
					cancel();
				
			}
		}, 20000, 20000);
		
		for (Type iaType : Type.values()) {
			for (int i = 0 ; i < nbGames ; i++) {
				IA ia = IaBuilder.getIA(iaType, depth);
				Player random = new Random();
				
				Game game = new Game();
				
				ia.setGame(game, Side.BLACK);
				random.setGame(game, Side.RED);
				
				Side winner = null;
				
				winner = playGame(ia, random, game);	
				
				averageTimeSpent[iaType.ordinal()] += ia.getAverageTimeSpent();
				nodesGenerated[iaType.ordinal()] += ia.getNbNodesGenerated();
				if (winner == Side.BLACK)
					iaTypeWins[iaType.ordinal()]++;
				nbGamesPlayed++;
				
			}
		}
		
		for (Type iaType : Type.values()) {
			averageTimeSpent[iaType.ordinal()] /= nbGames;
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
		displayWinsPercent();
		System.out.println("_________\n");
		displayAverageTimeSpent();
		System.out.println("_________\n");
		displayNodesGenerated();
		
	}

	private void displayNodesGenerated() {
		
		System.out.println("Average nodes generated for each type of IA\n");
		
		System.out.print(fixedLengthString("IA type", 20));
		System.out.print("Average Nodes Generated");
		
		System.out.println();
		
		for (Type type : Type.values()) {
			
			System.out.print(fixedLengthString(type.toString(), 20));
			
			
			System.out.print(fixedLengthString(Integer.toString((int)Math.floor(nodesGenerated[type.ordinal()]/nbGames)), 20));
			
			
			System.out.println();
		}
		
	}

	private void displayAverageTimeSpent() {
		System.out.println("Average time spent (secondes) for a choice for each type of IA\n");
		
		System.out.print(fixedLengthString("IA type", 20));
		System.out.print("Average time spent");
		
		System.out.println();
		
		for (Type type : Type.values()) {
			
			System.out.print(fixedLengthString(type.toString(), 20));
			
				
			String min = String.format("%02d:%02d",
				     TimeUnit.MILLISECONDS.toSeconds(averageTimeSpent[type.ordinal()]),
				     averageTimeSpent[type.ordinal()] -
				     TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(averageTimeSpent[type.ordinal()])));
			
			System.out.print(fixedLengthString(min, 20));
		
			
			System.out.println();
		}
		
	}

	private void displayWinsPercent() {
		System.out.println("IA Wins percent\n");
		
		for (Type type : Type.values()) {
			System.out.print(fixedLengthString(type.toString(), 15));
			float winPercent = ((float)iaTypeWins[type.ordinal()])/nbGames*100;
			System.out.print("|");
			for (int i = 0 ; i < winPercent ; i++) {
				System.out.print("-");
			}
			System.out.println("> "+winPercent+"%");
		}
		
	}
	
	private static String fixedLengthString(String string, int length) {
	    return String.format("%-" + length + "." + length + "s", string);
	}

}
