import ia.AbosuluIA;
import ia.MixteIA;
import ia.MobiliteIA;
import ia.PositionnelIA;
import othello.Frame;
import othello.Game;
import players.Player;
import players.Side;

public class TestIA {

	public static void main(String[] args) {
		
		int round = 1;
		
		Player p1 = new PositionnelIA(7);
		Player p2 = new PositionnelIA(7);
		
		Game game = new Game();
		
		p1.setGame(game, Side.BLACK);
		p2.setGame(game, Side.RED);
		
		while(!game.gameEnd()) {
			
			System.out.println("Round "+round);
			
			Frame blackPlayed = p1.play();
			
			if (blackPlayed == null)
				System.out.println("Aucun coup n'est possible ! Tour passé");
			
			game.playBlack(blackPlayed);
			
			game.display();
						
			Frame redPlayed = p2.play();
			
			if (redPlayed == null) {
				System.out.println("Aucun coup n'est possible ! Tour passé");
			}
			
			game.playRed(redPlayed);
			
			game.display();
			
			round++;
		}
		
		System.out.println("Nombre de jetons noirs :"+game.getNbBlackFrame());
		System.out.println("Nombre de jetons rouges :"+game.getNbRedFrame());
		System.out.println("Les "+(game.getNbBlackFrame()>game.getNbRedFrame()?Side.BLACK:Side.RED)+" gagnents !!!");
		
	}

}
