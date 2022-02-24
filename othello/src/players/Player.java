package players;

import othello.Frame;
import othello.Game;

public abstract class Player {
	
	private Game game;

	public void setGame(Game game) {
		this.game = game;
	}
	
	public Frame play() {
		return null;
	}
	
}
