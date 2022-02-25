package players;

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
		for (Frame f : getSide() == Side.BLACK ? getGame().getBlackPlayables():getGame().getRedPlayables()) {
			System.out.print(f.toString()+" ; ");
		}
		System.out.println("Tour de "+name);
		System.out.println("Veuillez entrer l'abscisse du jeton :");
		int p = sc.nextInt();
		System.out.println("Veuillez entrer l'ordonnée du jeton :");
		int i = sc.nextInt();
		return new Frame(i, p);
	}

}
