package players;

import java.util.List;
import java.util.Scanner;

import othello.Frame;

public class Human extends Player{
	
	private final Scanner sc = new Scanner(System.in);
	
	public Human() {
		System.out.println("Veuillez entrer votre nom :");
		this.name = sc.nextLine();
		System.out.println("réussitre de la création du joueur");
	}

	@Override
	public Frame play() {
		System.out.println("Tour de "+getName());
		System.out.println("Possibilités :");
		List<Frame> playables = getGame().displayPlayables(getSide());
		if (playables.isEmpty()) {
			System.out.println("Aucun coup n'est possible ! Tour passé");
			return null;
		}
		System.out.println("Tour de "+name);
		System.out.println("Veuillez entrer le coup que vous voulez jouer :");
		String p = sc.nextLine();
		int choice = 0;
		while (choice == 0) {
			try {
				int tmpChoice = Integer.parseInt(p);
				if (tmpChoice < 1 || tmpChoice > playables.size() )
					throw new Exception();
				choice = tmpChoice;
			} catch(Exception e) {
				System.out.println("Veuillez entrer un choix valide");
				p = sc.nextLine();
			}
		}
		return playables.get(choice-1);
	}
}
