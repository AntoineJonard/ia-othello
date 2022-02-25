package players;

import othello.Frame;
import othello.Game;

public abstract class Player {
	
	private Game game;
	private Side side;
	protected String name;

	public void setGame(Game game, Side side) {
		this.game = game;
		this.side = side;
	}
	
	public abstract Frame play();

	public String getName() {
		return name;
	}

	public Game getGame() {
		return game;
	}

	public Side getSide() {
		return side;
	}
	
	
}
