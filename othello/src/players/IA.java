package players;

import othello.Frame;

public class IA extends Player {

	private final static int[][] tacticalValues = {
			{500,-150,30,10,10,30,-150,500},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{500, -150, 30, 10, 10, 30, -150, 500},
	};

	@Override
	public Frame play() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
