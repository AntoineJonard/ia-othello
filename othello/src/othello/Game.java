package othello;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import players.Side;

public class Game{
	
    public static final String RESET = "\033[0m";  		// Text Reset
    public static final String BLACK = "\033[0;30m";   	// BLACK
    public static final String RED = "\033[0;31m";     	// RED
    public static final String WHITE = "\033[0;37m";   	// WHITE
    public static final String GREEN = "\033[32m";		// GREEN
	
	private final Frame[][] frames = new  Frame[8][8];
	
	private int nbRedFrame = 0;
	private int nbBlackFrame = 0;
	
	private int nbPlays = 0;
	
	private List<Frame> blackPlayablesPos = new ArrayList();
	private List<Frame> redPlayablesPos = new ArrayList();
	
	public Game() {
		super();
		
		for (int i = 0 ; i < 8 ; i++) {
			for (int p = 0 ; p < 8 ; p++) {
				frames[i][p] = new Frame(i,p);
			}
		}
		
		initGame();
	}
	
	
	public Game(Game from) {
		
		from.blackPlayablesPos.forEach(f -> blackPlayablesPos.add(new Frame(f)));
		from.redPlayablesPos.forEach(f -> redPlayablesPos.add(new Frame(f)));
		
		nbPlays = from.nbPlays;
		nbRedFrame = from.nbRedFrame;
		nbBlackFrame = from.nbBlackFrame;
		
		for (int i = 0 ; i < 8 ; i++) {
			for (int p = 0 ; p < 8 ; p++) {
				frames[i][p] = new Frame(from.frames[i][p]);
			}
		}
	}
	
	
	public List<Frame> getBlackPlayables() {
		return  blackPlayablesPos.stream().filter(f -> isPlayable(f, State.BLACK)).collect(Collectors.toList());
	}

	public List<Frame> getRedPlayables() {
		return redPlayablesPos.stream().filter(f -> isPlayable(f, State.RED)).collect(Collectors.toList());
	}
	
	public List<Frame> getSidePlayable(Side side){
		return side == Side.BLACK ? getBlackPlayables():getRedPlayables();
	}

	public int getNbRedFrame() {
		return nbRedFrame;
	}

	public int getNbBlackFrame() {
		return nbBlackFrame;
	}

	public int getNbPlays() {
		return nbPlays;
	}

	public void display() {	
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

	private void initGame() {
		frames[3][3].setRed();
		frames[4][4].setRed();
		frames[3][4].setBlack();
		frames[4][3].setBlack();
		
		nbBlackFrame = 2;
		nbRedFrame = 2;
		
		redPlayablesPos.add(frames[4][2]);
		redPlayablesPos.add(frames[5][3]);
		redPlayablesPos.add(frames[2][4]);
		redPlayablesPos.add(frames[3][5]);
		
		blackPlayablesPos.add(frames[2][3]);
		blackPlayablesPos.add(frames[3][2]);
		blackPlayablesPos.add(frames[4][5]);
		blackPlayablesPos.add(frames[5][4]);
	}

	public void playRed(Frame framePlayed) {
		if (framePlayed != null) {
			nbPlays++;
			nbRedFrame++;
			
			redPlayablesPos.remove(framePlayed);
			blackPlayablesPos.remove(framePlayed);
			
			framePlayed.setRed();
			
			updateFramesWith(framePlayed);
						
			reverse(framePlayed);

			for (Frame f : getFreeNeighbors(framePlayed))
				addToBackPlayables(f); 			
		}
	}

	public void playBlack(Frame framePlayed) {
		if (framePlayed != null) {
			nbPlays++;
			nbBlackFrame++;
			
			redPlayablesPos.remove(framePlayed);
			blackPlayablesPos.remove(framePlayed);
			
			framePlayed.setBlack();
			
			updateFramesWith(framePlayed);
			
			reverse(framePlayed);
			
			for (Frame f : getFreeNeighbors(framePlayed))
				addToRedPlayables(f);
		}
	}
	
	public void playSide(Side s, Frame framePlayed) {
		if (s == Side.BLACK)
			playBlack(framePlayed);
		else
			playRed(framePlayed);
	}

	private void addToBackPlayables(Frame f) {
		if (!blackPlayablesPos.contains(f))
			blackPlayablesPos.add(f);
	}

	private void addToRedPlayables(Frame f) {
		if (!redPlayablesPos.contains(f))
			redPlayablesPos.add(f);
	}
	
	private void addToPlayables(Frame f, List<Frame> playables) {
		if (!playables.contains(f))
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
		
		if (frame.getI()+1 < 8 && frame.getP()+1 < 8 && frames[frame.getI()+1][frame.getP()+1].getState()==invState)
			for (int ip = 2 ; excess + ip < 8; ip ++) {
				tmp = frames[frame.getI()+ip][frame.getP()+ip].getState();
				if (tmp == endpoint)
					return true;
				if (tmp == State.EMPTY)
					break;
			}
		
		if (frame.getI()-1 >= 0 && frame.getP()-1>=0 && frames[frame.getI()-1][frame.getP()-1].getState()==invState)
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
		
		if (frame.getI()-1 >=0 && frame.getP()+1 < 8 && frames[frame.getI()-1][frame.getP()+1].getState()==invState)
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
		
		Function<Frame, Void> reverseFrame;
		
		if (trigger == State.BLACK) {
			reverseFrame = toReverse -> {
				toReverse.reverse();
				nbRedFrame--;
				nbBlackFrame++;
				List<Frame> neighbors = getFreeNeighbors(toReverse);
				neighbors.forEach(neighbor -> blackPlayablesPos.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, redPlayablesPos));
				return null;
			};
		}else {
			reverseFrame = toReverse -> {
				toReverse.reverse();
				nbRedFrame++;
				nbBlackFrame--;
				List<Frame> neighbors = getFreeNeighbors(toReverse);
				neighbors.forEach(neighbor -> redPlayablesPos.remove(neighbor));
				neighbors.forEach(neighbor -> addToPlayables(neighbor, blackPlayablesPos));
				return null;
			};
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
				reverseFrame.apply(frames[it][from.getP()]);
			}
		}
		
		for (int it = fromp + 1 ; it < top && top-fromp>1 ; it ++) {
			if (frames[from.getI()][it].getState() != trigger) {
				reverseFrame.apply(frames[from.getI()][it]);
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
				reverseFrame.apply(frames[it1][it2]);
			}
		}
		
		for (int it1 = frompi.getI(), it2 = frompi.getP() ; it1 < topi.getI() && frompi != topi; it1++, it2--) {
			if (frames[it1][it2].getState() != trigger){
				reverseFrame.apply(frames[it1][it2]);
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
		return getBlackPlayables().isEmpty() && getRedPlayables().isEmpty() || nbRedFrame+nbBlackFrame >= 64;
	}
}
