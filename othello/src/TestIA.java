import ia.PositionnelIA;
import othello.Frame;
import othello.Game;
import players.Player;
import players.Side;

public class TestIA {

	public static void main(String[] args) {
		
		int round = 1;
		
		Player p1 = new PositionnelIA(3);
		Player p2 = new PositionnelIA(3);
		
		Game game = new Game();
		
		p1.setGame(game, Side.BLACK);
		p2.setGame(game, Side.RED);
		
		while(!game.gameEnd()) {
			
			System.out.println("Round "+round);
			
			Frame blackPlayed = p1.play();
			
			game.playBlack(blackPlayed);
			
			game.display();
						
			Frame redPlayed = p2.play();
			
			game.playRed(redPlayed);
			
			game.display();
			
			round++;
		}
		
		System.out.println("Nombre de jetons noirs :"+game.getNbBlackFrame());
		System.out.println("Nombre de jetons rouges :"+game.getNbRedFrame());
		System.out.println("Les "+(game.getNbBlackFrame()>game.getNbRedFrame()?Side.BLACK:Side.RED)+" gagnents !!!");
		
	}

}
