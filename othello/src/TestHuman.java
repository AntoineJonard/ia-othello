
import othello.Frame;
import othello.Game;
import players.Human;
import players.Player;
import players.Side;

public class TestHuman {

	public static void main(String[] args) {
		
		int round = 1;
			
		Player p1 = new Human();
		Player p2 = new Human();
		
		Game game = new Game();
		
		p1.setGame(game, Side.BLACK);
		p2.setGame(game, Side.RED);
		
		while(!game.gameEnd()) {
			
			System.out.println("Round "+round);
			
			Frame blackPlayed = p1.play();
			
			game.playBlack(blackPlayed);
						
			Frame redPlayed = p2.play();
			
			game.playRed(redPlayed);
			
			round++;
		}
		
		System.out.println("Nombre de jetons noirs :"+game.getNbBlackFrame());
		System.out.println("Nombre de jetons rouges :"+game.getNbRedFrame());
		System.out.println("Les"+(game.getNbBlackFrame()>game.getNbRedFrame()?Side.BLACK:Side.RED)+"gagnents !!!");

	}

}
