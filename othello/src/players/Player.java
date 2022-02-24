package players;

import othello.Frame;
import othello.Game;

public abstract class Player {
	
	private Game game;

	protected String name;

	public void setGame(Game game) {
		this.game = game;
	}
	
	public abstract Frame play();

	public String getName() {
		return name;
	}
}
