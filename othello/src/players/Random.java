package players;

import java.util.List;

import othello.Frame;

public class Random extends Player{

	@Override
	public Frame play() {
		List<Frame> playables = getGame().getSidePlayable(getSide());
		if (playables.isEmpty()) {
			return null;
		}
		return playables.get(new java.util.Random().nextInt(playables.size()));
	}
}
