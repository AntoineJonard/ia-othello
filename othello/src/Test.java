
import othello.Game;
import players.Human;

public class Test {

	public static void main(String[] args) {
			
		Human p1 = new Human();
		Human p2 = new Human();
		
		Game game = new Game(p1, p2);
		game.start();

	}

}