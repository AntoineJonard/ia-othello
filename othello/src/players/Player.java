package players;

import othello.Frame;
import othello.Game;

public abstract class Player{
	
	private Game game;
	private Side side;
	private Side opponentSide;
	protected String name;

	public void setGame(Game game, Side side) {
		this.game = game;
		this.side = side;
		this.opponentSide = side == Side.BLACK ? Side.RED : Side.BLACK;
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

	public Side getOpponentSide() {
		return opponentSide;
	}
}
