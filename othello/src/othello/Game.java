package othello;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import players.Player;
import players.Side;

public class Game {
	
    public static final String RESET = "\033[0m";  		// Text Reset
    public static final String BLACK = "\033[0;30m";   	// BLACK
    public static final String RED = "\033[0;31m";     	// RED
    public static final String WHITE = "\033[0;37m";   	// WHITE
    public static final String GREEN = "\033[32m";		// GREEN
	
	private final Frame[][] frames = new  Frame[8][8];
	
	private Player black;
	private Player red;
	
	private int round = 0;
	private int nbFreeFrame = 64;
	
	private HashSet<Frame> blackPlayablesPos = new HashSet<>();
	private HashSet<Frame> redPlayablesPos = new HashSet<>();
	
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
	
	public List<Frame> getBlackPlayables() {
		return  blackPlayablesPos.stream().filter(f -> isPlayable(f, State.BLACK)).collect(Collectors.toList());
	}

	public List<Frame> getRedPlayables() {
		return redPlayablesPos.stream().filter(f -> isPlayable(f, State.RED)).collect(Collectors.toList());
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
	
	public List<Frame> displayPlayables(Side side) {	
		
		List<Frame> playables = side == Side.BLACK ? getBlackPlayables() : getRedPlayables();
		List<Frame> orderedPlayables = new ArrayList();
		int cpt = 1;
		
		System.out.println("round "+round);
		for (int i = 0 ; i < 8 ; i++) {
			for (int p = 0 ; p < 8 ; p++) {
				String color = null;
				String symbol = null;
				Frame f = frames[i][p];
				switch (f.getState()) {
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
				
				if (playables.contains(f)) {
					System.out.print(GREEN + cpt++ + RESET);
					orderedPlayables.add(f);
				}else {
					System.out.print(color +symbol+RESET);
				}
				
				System.out.flush();
				
				if (p >= 7)
					System.out.println("|");
			}
		}	
		
		return orderedPlayables;
	}
	
	public void start() {
		
		redPlayablesPos.add(frames[4][2]);
		redPlayablesPos.add(frames[5][3]);
		redPlayablesPos.add(frames[2][4]);
		redPlayablesPos.add(frames[3][5]);
		
		blackPlayablesPos.add(frames[2][3]);
		blackPlayablesPos.add(frames[3][2]);
		blackPlayablesPos.add(frames[4][5]);
		blackPlayablesPos.add(frames[5][4]);
				
		while(!gameEnd()) {
			
			Frame blackPlayed = black.play();
			
			redPlayablesPos.remove(blackPlayed);
			blackPlayablesPos.remove(blackPlayed);
			
			blackPlayed.setBlack();
			
			updateFramesWith(blackPlayed);
			
			reverse(blackPlayed);
			
			for (Frame f : getFreeNeighbors(blackPlayed))
				addToRedPlayables(f);
						
			Frame redPlayed = red.play();
			
			redPlayablesPos.remove(redPlayed);
			blackPlayablesPos.remove(redPlayed);
			
			redPlayed.setRed();
			
			updateFramesWith(redPlayed);
						
			reverse(redPlayed);

			for (Frame f : getFreeNeighbors(redPlayed))
				addToBackPlayables(f);
						
			round++;
		}
	}

	private void addToBackPlayables(Frame f) {
		blackPlayablesPos.add(f);
	}

	private void addToRedPlayables(Frame f) {
		redPlayablesPos.add(f);
	}
	
	private void addToPlayables(Frame f, HashSet<Frame> playables) {
		playables.add(f);
	}
	
	public List<Frame> getFreeNeighbors(Frame centralFrame){
		ArrayList<Frame> neighbors = new ArrayList<>();
		int i = centralFrame.getI();
		int p = centralFrame.getP();
		
		Frame f = null;
		if (i-1 >= 0) {
			f = frames[i-1][p];
			if (f.isEmpty())
				neighbors.add(f);
		}
		
		if (i+1 < 8) {
			f = frames[i+1][p];
			if (f.isEmpty())
				neighbors.add(f);
		}
			
		if (p-1 >= 0) {
			f = frames[i][p-1];
			if (f.isEmpty())
				neighbors.add(f);
		}
			
		
		if (p+1 < 8) {
			f = frames[i][p+1];
			if (f.isEmpty())
				neighbors.add(f);
		}
			
		return neighbors;
	}
	
	public boolean isPlayable(Frame frame, State endpoint) {
		
		State tmp;
		State invState = endpoint == State.RED ? State.BLACK : State.RED;
		
		// lines
		
		if (frame.getI()+1<8 && frames[frame.getI()+1][frame.getP()].getState()==invState)
			for (int i = frame.getI()+2 ; i < 8 ; i ++) {
				tmp = frames[i][frame.getP()].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;
				
			}
		
		if (frame.getI()-1>=0 && frames[frame.getI()-1][frame.getP()].getState()==invState)
			for (int i = frame.getI()-2 ; i >= 0 ; i --) {
				tmp = frames[i][frame.getP()].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;		
			}
		
		if (frame.getP()+1 < 8 && frames[frame.getI()][frame.getP()+1].getState()==invState)
			for (int p = frame.getP()+2; p < 8 ; p ++) {
				tmp = frames[frame.getI()][p].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;	
			}
		
		if (frame.getP()-1 >= 0 && frames[frame.getI()][frame.getP()-1].getState()==invState)
			for (int p = frame.getP()-2 ; p >= 0 ; p --) {
				tmp = frames[frame.getI()][p].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;
			}
		
		// diags
		
		boolean iinfp = frame.getI() < frame.getP();
		int limiting = iinfp ? frame.getI() : frame.getP();
		int excess = iinfp ? frame.getP(): frame.getI();
		
		if (frame.getI()+1 < 8 && frame.getP() < 8 && frames[frame.getI()+1][frame.getP()+1].getState()==invState)
			for (int ip = 2 ; excess + ip < 8; ip ++) {
				tmp = frames[frame.getI()+ip][frame.getP()+ip].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;
			}
		
		if (frame.getI()-1 >= 0 && frame.getI()-1>=0 && frames[frame.getI()-1][frame.getP()-1].getState()==invState)
			for (int ip = 2 ; limiting -ip >= 0 ; ip ++) {
				tmp = frames[frame.getI()-ip][frame.getP()-ip].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;
			}
		
		if (frame.getI()+1 < 8 && frame.getP()-1 >= 0 && frames[frame.getI()+1][frame.getP()-1].getState()==invState)
			for (int ip = 2 ; frame.getI() +ip< 8 && frame.getP()-ip >= 0; ip ++) {
				tmp = frames[frame.getI()+ip][frame.getP()-ip].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;
			}
		
		if (frame.getI()-1 < 8 && frame.getP()+1 < 8 && frames[frame.getI()-1][frame.getP()+1].getState()==invState)
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
			myPlayables = blackPlayablesPos;
			opponentPlayables = redPlayablesPos;
		}else {
			myPlayables = redPlayablesPos;
			opponentPlayables = blackPlayablesPos;
		}
		
		// lines
		
		int fromi = from.getI();
		int fromp = from.getP();
		int toi = from.getI();
		int top = from.getP();
		
		State tmp;
		
		for (int i = from.getI()+1 ; i < 8 ; i ++) {
			tmp = frames[i][from.getP()].getState();
			if (tmp == trigger && i > from.getI()+1)
				toi = i;
			if (tmp == State.EMPTY)
				break;
			
		}
		for (int i = from.getI()-1 ; i >= 0 ; i --) {
			tmp = frames[i][from.getP()].getState();
			if (tmp == trigger && i < from.getI()-1)
				fromi = i;
			if (tmp == State.EMPTY)
				break;		
		}
		for (int p = from.getP()+1; p < 8 ; p ++) {
			tmp = frames[from.getI()][p].getState();
			if (tmp == trigger && p > from.getP()+1)
				top = p;
			if (tmp == State.EMPTY)
				break;	
		}
		for (int p = from.getP()-1 ; p >= 0 ; p --) {
			tmp = frames[from.getI()][p].getState();
			if (tmp == trigger && p < from.getP()-1)
				fromp = p;
			if (tmp == State.EMPTY)
				break;
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
		
		boolean iinfp = from.getI() < from.getP();
		int limiting = iinfp ? from.getI() : from.getP();
		int excess = iinfp ? from.getP(): from.getI();
		
		// to bottom right
		for (int ip = 1 ; excess + ip < 8; ip ++) {
			tmp = frames[from.getI()+ip][from.getP()+ip].getState();
			if (tmp == trigger && ip > 1)
				toip = frames[from.getI()+ip][from.getP()+ip];
			if (tmp == State.EMPTY)
				break;
		}
		// to top left
		for (int ip = 1 ; limiting -ip >= 0 ; ip ++) {
			tmp = frames[from.getI()-ip][from.getP()-ip].getState();
			if (tmp == trigger && ip > 1)
				fromip = frames[from.getI()-ip][from.getP()-ip];
			if (tmp == State.EMPTY)
				break;
		}
		// to bottom left
		for (int ip = 1 ; from.getI() +ip< 8 && from.getP()-ip >= 0; ip ++) {
			tmp = frames[from.getI()+ip][from.getP()-ip].getState();
			if (tmp == trigger && ip > 1)
				topi = frames[from.getI()+ip][from.getP()-ip];
			if (tmp == State.EMPTY)
				break;
		}
		// to top right
		for (int ip = 1 ; from.getI()-ip >= 0 && from.getP()+ip < 8 ; ip ++) {
			tmp = frames[from.getI()-ip][from.getP()+ip].getState();
			if (tmp == trigger && ip > 1)
				frompi = frames[from.getI()-ip][from.getP()+ip];
			if (tmp == State.EMPTY)
				break;
		}
		
		for (int it1 = fromip.getI(), it2 = fromip.getP() ; it1 < toip.getI() && fromip != toip; it1++, it2++) {
			if (frames[it1][it2].getState() != trigger) {
				frames[it1][it2].reverse();
				List<Frame> neighbors = getFreeNeighbors(frames[it1][it2]);
				neighbors.forEach(neighbor -> myPlayables.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, opponentPlayables));
			}
		}
		
		for (int it1 = frompi.getI(), it2 = frompi.getP() ; it1 < topi.getI() && frompi != topi; it1++, it2--) {
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
	
	public boolean gameEnd() {
		return getBlackPlayables().isEmpty() && getRedPlayables().isEmpty() || nbFreeFrame == 0;
	}
}
