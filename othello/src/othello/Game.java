package othello;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import players.Player;
import players.Side;

public class Game {
	
    public static final String RESET = "\033[0m";  		// Text Reset
    public static final String BLACK = "\033[0;30m";   	// BLACK
    public static final String RED = "\033[0;31m";     	// RED
    public static final String WHITE = "\033[0;37m";   	// WHITE
	
	private final Frame[][] frames = new  Frame[8][8];
	
	private Player black;
	private Player red;
	
	private int round = 0;
	
	private HashSet<Frame> blackPlayables = new HashSet<>();
	private HashSet<Frame> redPlayables = new HashSet<>();
	
	public Game(Player player1, Player player2) {
		super();
		
		for (int i = 0 ; i < 8 ; i++) {
			for (int p = 0 ; p < 8 ; p++) {
				frames[i][p] = new Frame(i,p);
			}
		}
		
		frames[3][3].setRed();
		frames[4][4].setRed();
		frames[3][4].setBlack();
		frames[4][3].setBlack();
		
		this.black = player1;
		player1.setGame(this,Side.BLACK);
		this.red = player2;
		player2.setGame(this, Side.WHITE);
	}
	
	public HashSet<Frame> getBlackPlayables() {
		return blackPlayables;
	}

	public HashSet<Frame> getRedPlayables() {
		return redPlayables;
	}

	public void display() {	
		System.out.println("round "+round);
		for (int i = 0 ; i < 8 ; i++) {
			for (int p = 0 ; p < 8 ; p++) {
				String color = null;
				String symbol = null;
				switch (frames[i][p].getState()) {
				case BLACK:
					color = BLACK;
					symbol = "X";
					break;
				case RED:
					color = RED;
					symbol = "O";
					break;
				case EMPTY:
					color = WHITE;
					symbol = " ";
					break;
				}
				System.out.print("|");
				System.out.print(color +symbol+RESET);
				System.out.flush();
				if (p >= 7)
					System.out.println("|");
			}
		}	
	}
	
	public void start() {
		
		redPlayables.add(frames[4][2]);
		redPlayables.add(frames[5][3]);
		redPlayables.add(frames[4][2]);
		redPlayables.add(frames[3][5]);
		
		blackPlayables.add(frames[2][3]);
		blackPlayables.add(frames[3][2]);
		blackPlayables.add(frames[4][5]);
		blackPlayables.add(frames[5][4]);
				
		while(true) {
			Frame blackPlayed = black.play();
			
			redPlayables.remove(blackPlayed);
			blackPlayables.remove(blackPlayed);
			
			blackPlayed.setBlack();
			
			updateFramesWith(blackPlayed);
			
			reverse(blackPlayed);
			
			for (Frame f : getFreeNeighbors(blackPlayed))
				addToRedPlayables(f);
						
			display();
			
			Frame redPlayed = red.play();
			
			redPlayables.remove(redPlayed);
			blackPlayables.remove(redPlayed);
			
			redPlayed.setRed();
			
			updateFramesWith(redPlayed);
						
			reverse(redPlayed);

			for (Frame f : getFreeNeighbors(redPlayed))
				addToBackPlayables(f);
						
			display();
			round++;
		}
	}

	private void addToBackPlayables(Frame f) {
		if (isPlayable(f, State.BLACK))
			blackPlayables.add(f);
	}

	private void addToRedPlayables(Frame f) {
		if (isPlayable(f,State.RED))
			redPlayables.add(f);
	}
	
	private void addToPlayables(Frame f, HashSet<Frame> playables) {
		State endpoint = playables == blackPlayables ? State.BLACK:State.RED;
		if (isPlayable(f,endpoint))
			playables.add(f);
	}
	
	public List<Frame> getFreeNeighbors(Frame centralFrame){
		ArrayList<Frame> neighbors = new ArrayList<>();
		int i = centralFrame.getI();
		int p = centralFrame.getP();
		
		Frame f = frames[i-1][p];
		if (i-1 >= 0 && f.isEmpty())
			neighbors.add(f);
		f = frames[i+1][p];
		if (i+1 < 8 && f.isEmpty())
			neighbors.add(f);
		f = frames[i][p-1];
		if (p-1 >= 0 && f.isEmpty())
			neighbors.add(f);
		f = frames[i][p+1];
		if (p+1 < 8 && f.isEmpty())
			neighbors.add(f);
		
		return neighbors;
	}
	
	public boolean isPlayable(Frame frame, State endpoint) {
		
		State tmp;
		
		// lines
		
		for (int i = frame.getI()+2 ; i < 8 ; i ++) {
			tmp = frames[i][frame.getP()].getState();
			if (tmp == endpoint && i > frame.getI()+1)
				return true;
			if (tmp == State.EMPTY)
				break;
			
		}
		for (int i = frame.getI()-2 ; i >= 0 ; i --) {
			tmp = frames[i][frame.getP()].getState();
			if (tmp == endpoint && i < frame.getI()-1)
				return true;
			if (tmp == State.EMPTY)
				break;		
		}
		for (int p = frame.getP()+2; p < 8 ; p ++) {
			tmp = frames[frame.getI()][p].getState();
			if (tmp == endpoint && p > frame.getP()+1)
				return true;
			if (tmp == State.EMPTY)
				break;	
		}
		for (int p = frame.getP()-2 ; p >= 0 ; p --) {
			tmp = frames[frame.getI()][p].getState();
			if (tmp == endpoint && p < frame.getP()-1)
				return true;
			if (tmp == State.EMPTY)
				break;
		}
		
		// diags
		
		boolean iinfp = frame.getI() < frame.getP();
		int limiting = iinfp ? frame.getI() : frame.getP();
		int excess = iinfp ? frame.getP(): frame.getI();
		
		for (int ip = 2 ; excess + ip < 8; ip ++) {
			tmp = frames[frame.getI()+ip][frame.getP()+ip].getState();
			if (tmp == endpoint)
				return true;
			if (tmp == State.EMPTY)
				break;
		}
		for (int ip = 2 ; limiting -ip >= 0 ; ip ++) {
			tmp = frames[frame.getI()-ip][frame.getP()-ip].getState();
			if (tmp == endpoint)
				return true;
			if (tmp == State.EMPTY)
				break;
		}
		for (int ip = 2 ; frame.getI() +ip< 8 && frame.getP()-ip >= 0; ip ++) {
			tmp = frames[frame.getI()+ip][frame.getP()-ip].getState();
			if (tmp == endpoint)
				return true;
			if (tmp == State.EMPTY)
				break;
		}
		for (int ip = 2 ; frame.getI()-ip >= 0 && frame.getP()+ip < 8 ; ip ++) {
			tmp = frames[frame.getI()-ip][frame.getP()+ip].getState();
			if (tmp == endpoint)
				return true;
			if (tmp == State.EMPTY)
				break;
		}
		return false;
	}
	
	public void reverse(Frame from) {
		
		State trigger = from.getState();
		
		HashSet<Frame> opponentPlayables;
		HashSet<Frame> myPlayables;
		
		if (trigger == State.BLACK) {
			myPlayables = blackPlayables;
			opponentPlayables = redPlayables;
		}else {
			myPlayables = redPlayables;
			opponentPlayables = blackPlayables;
		}
		
		// lines
		
		int fromi = from.getI();
		int fromp = from.getP();
		int toi = from.getI();
		int top = from.getP();
		
		for (int it = 0 ; it < from.getI(); it++) {
			if (frames[it][from.getP()].getState()==trigger) {
				fromi = it;
				break;
			}
				
		}
		
		for (int it = 7 ; it > from.getI(); it--) {
			if (frames[it][from.getP()].getState()==trigger) {
				toi = it;
				break;
			}
				
		}
		
		for (int it = 0 ; it < from.getP(); it++) {
			if (frames[from.getI()][it].getState()==trigger) {
				fromp = it;
				break;
			}
				
		}
		
		for (int it = 7 ; it > from.getP(); it--) {
			if (frames[from.getI()][it].getState()==trigger) {
				top = it;
				break;
			}
		}
		
		for (int it = fromi + 1 ; it < toi && toi-fromi>1 ; it ++) {
			if (frames[it][from.getP()].getState() != trigger) {
				frames[it][from.getP()].reverse();
				List<Frame> neighbors = getFreeNeighbors(frames[it][from.getP()]);
				neighbors.forEach(neighbor -> myPlayables.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, opponentPlayables));
			}
		}
		
		for (int it = fromp + 1 ; it < top && top-fromp>1 ; it ++) {
			if (frames[from.getI()][it].getState() != trigger) {
				frames[from.getI()][it].reverse();
				List<Frame> neighbors = getFreeNeighbors(frames[from.getI()][it]);
				neighbors.forEach(neighbor -> myPlayables.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, opponentPlayables));
			}
		}
		
		// diagos
		
		Frame fromip = from;
		Frame toip = from;
		Frame frompi = from;
		Frame topi = from;
		
		for (int it1 = from.getI()<from.getP()?0:from.getI()-from.getP(), it2 = from.getP()<from.getI()?0:from.getP()-from.getI() ; it1 < from.getI() ; it1++, it2++) {
			if (frames[it1][it2].getState() == trigger) {
				fromip = frames[it1][it2];
				break;
			}
		}
		
		for (int it1 = from.getI()<from.getP()?from.getI()+7-from.getP():7, it2 = from.getP()<from.getI()?from.getP()+7-from.getI():7 ; it1 > from.getI() ; it1--, it2--) {
			if (frames[it1][it2].getState() == trigger) {
				toip = frames[it1][it2];
				break;
			}
		}
		
		for (int it1 = from.getI()+from.getP()<7?from.getI()+from.getP():7, it2 = from.getI()+from.getP()<7?0:from.getP()-from.getI() ; it1 < from.getI() ; it1++, it2--) {
			if (frames[it1][it2].getState() == trigger) {
				frompi = frames[it1][it2];
				break;
			}
		}
		
		for (int it1 = from.getI()+from.getP()<7?0:-7+from.getI()+from.getP(), it2 = from.getI()+from.getP()<7?from.getI()+from.getP():7 ; it1 > from.getI() ; it1--, it2++) {
			if (frames[it1][it2].getState() == trigger) {
				topi = frames[it1][it2];
				break;
			}
		}
		
		for (int it1 = fromip.getI(), it2 = fromip.getP() ; it1 < toip.getI() && fromip != toip; it1++, it2++) {
			if (frames[it1][it2].getState() != trigger) {
				frames[it1][it2].reverse();
				List<Frame> neighbors = getFreeNeighbors(frames[it1][it2]);
				neighbors.forEach(neighbor -> myPlayables.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, opponentPlayables));
			}
		}
		
		for (int it1 = frompi.getI(), it2 = frompi.getP() ; it1 < topi.getI() && frompi != topi; it1--, it2--) {
			if (frames[it1][it2].getState() != trigger){
				frames[it1][it2].reverse();
				List<Frame> neighbors = getFreeNeighbors(frames[it1][it2]);
				neighbors.forEach(neighbor -> myPlayables.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, opponentPlayables));
			}	
		}
	}
	
	public void updateFramesWith(Frame from) {
		if (from.getState() == State.BLACK)
			frames[from.getI()][from.getP()].setBlack();
		else
			frames[from.getI()][from.getP()].setRed();
	}
}
