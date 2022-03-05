package ia;

import players.IA;

public class IaBuilder {

	public static IA getIA(Type iaType, int depth) {
		switch(iaType){
		case ABSOLU:
			return new AbsoluIA(depth);
		case MIXTE:
			return new MixteIA(depth);
		case MOBILITE:
			return new MobiliteIA(depth);
		case POSITIONNEL:
			return new PositionnelIA(depth);
		default:
			return null;	
		}
	}
	
}
