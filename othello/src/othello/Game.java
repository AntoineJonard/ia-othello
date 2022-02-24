package othello;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import players.Player;

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
		player1.setGame(this);
		this.red = player2;
		player2.setGame(this);
	}
	
	public void display() {	
		System.out.println("round "+round);
		for (int i = 0 ; i < 8 ; i++) {
			for (int p = 0 ; p < 8 ; p++) {
				String color = null;
				switch (frames[i][p].getState()) {
				case BLACK:
					color = BLACK;
					break;
				case RED:
					color = RED;
					break;
				case EMPTY:
					color = WHITE;
					break;
				}
				System.out.print("|");
				System.out.print(color + "🟨"+RESET);
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
			
			for (Frame f : getNeighbors(blackPlayed))
				if (f.isEmpty())
					redPlayables.add(f);
			
			Frame redPlayed = red.play();
			
			redPlayables.remove(redPlayed);
			blackPlayables.remove(redPlayed);
			
			for (Frame f : getNeighbors(redPlayed))
				if (f.isEmpty())
					redPlayables.add(f);
			
			display();
			round++;
		}
	}
	
	public List<Frame> getNeighbors(Frame centralFrame){
		ArrayList<Frame> neighbors = new ArrayList<>();
		int i = centralFrame.getI();
		int p = centralFrame.getP();
		
		if (i-1 > 0)
			neighbors.add(frames[i-1][p]);
		if (i+1 < 8)
			neighbors.add(frames[i+1][p]);
		if (p-1 > 0)
			neighbors.add(frames[i][p-1]);
		if (p+1 < 8)
			neighbors.add(frames[i][p+1]);
		
		return neighbors;
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
		int fromp = from.getI();
		int toi = from.getI();
		int top = from.getI();
		
		for (int it = 0 ; it < from.getI(); it++) {
			if (frames[it][from.getP()].getState()==trigger) {
				fromi = it;
				break;
			}
				
		}
		
		for (int it = 8 ; it > from.getI(); it--) {
			if (frames[it][from.getP()].getState()==trigger) {
				toi = it;
				break;
			}
				
		}
		
		for (int it = 0 ; it < from.getI(); it++) {
			if (frames[it][from.getP()].getState()==trigger) {
				fromp = it;
				break;
			}
				
		}
		
		for (int it = 8 ; it > from.getI(); it--) {
			if (frames[it][from.getP()].getState()==trigger) {
				top = it;
				break;
			}
		}
		
		for (int it = fromi + 1 ; it < toi && fromi-toi>-1 ; it ++) {
			if (frames[it][from.getP()].getState() != trigger) {
				frames[it][from.getP()].reverse();
				List<Frame> neighbors = getNeighbors(frames[it][from.getP()]);
				myPlayables.remove(neighbors);
				neighbors.forEach(neighbor -> opponentPlayables.add(neighbor));
			}
		}
		
		for (int it = fromp + 1 ; it < top && fromp-top>-1 ; it ++) {
			if (frames[from.getI()][it].getState() != trigger) {
				frames[from.getI()][it].reverse();
				List<Frame> neighbors = getNeighbors(frames[from.getI()][it]);
				myPlayables.remove(neighbors);
				neighbors.forEach(neighbor -> opponentPlayables.add(neighbor));
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
		
		for (int it1 = from.getI()+from.getP()<7?from.getI()+from.getP():7, it2 = from.getI()+from.getP()<7?0:from.getP()-from.getI() ; it1 < from.getI() ; it1++, it2++) {
			if (frames[it1][it2].getState() == trigger) {
				frompi = frames[it1][it2];
				break;
			}
		}
		
		for (int it1 = from.getI()+from.getP()<7?0:-7+from.getI()+from.getP(), it2 = from.getI()+from.getP()<7?from.getI()+from.getP():7 ; it1 < from.getI() ; it1--, it2--) {
			if (frames[it1][it2].getState() == trigger) {
				topi = frames[it1][it2];
				break;
			}
		}
		
		for (int it1 = fromip.getI(), it2 = fromip.getP() ; it1 < toip.getI() && fromip != toip; it1++, it2++) {
			if (frames[it1][it2].getState() != trigger) {
				frames[it1][it2].reverse();
				List<Frame> neighbors = getNeighbors(frames[it1][it2]);
				myPlayables.remove(neighbors);
				neighbors.forEach(neighbor -> opponentPlayables.add(neighbor));
			}
		}
		
		for (int it1 = frompi.getI(), it2 = frompi.getP() ; it1 < topi.getI() && frompi != topi; it1--, it2--) {
			if (frames[it1][it2].getState() != trigger){
				frames[it1][it2].reverse();
				List<Frame> neighbors = getNeighbors(frames[it1][it2]);
				myPlayables.remove(neighbors);
				neighbors.forEach(neighbor -> opponentPlayables.add(neighbor));
			}	
		}
	}
}
