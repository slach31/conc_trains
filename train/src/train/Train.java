package train;

/**
 * Représentation d'un train. Un train est caractérisé par deux valeurs :
 * <ol>
 *   <li>
 *     Son nom pour l'affichage.
 *   </li>
 *   <li>
 *     La position qu'il occupe dans le circuit (un élément avec une direction) : classe {@link Position}.
 *   </li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Mayte segarra <mt.segarra@imt-atlantique.fr>
 * Test if the first element of a train is a station
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @version 0.3
 */
public class Train extends Thread {
	private final String name;
	private final Position pos;

	public Train(String name, Position p) throws BadPositionForTrainException {
		
		super(name);
		
		if (name == null || p == null)
			throw new NullPointerException();

		// A train should be first be in a station
		if (!(p.getPos() instanceof Station))
			throw new BadPositionForTrainException(name);

		
		this.name = name;
		this.pos = p.clone();
		this.pos.occupy();
		
	}
	
	@Override
	public void run() {
		while (true) {
			this.advance();
			this.turnAround();
			System.out.println(this);
		}
	}
	
	public void advance() {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Direction d = this.pos.getDirection();
		Element p = this.pos.getPos();
		
		Element[] adjacents = p.getAdjacent();
		
		if (d == Direction.LR) {
			this.pos.updatePosition(adjacents[1]);
		}
		else {
			this.pos.updatePosition(adjacents[0]);
		}
	}
	
	public void turnAround() {
		Element p = this.pos.getPos();
		if (p.getClass() == Station.class) {
			this.pos.turnAround();
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Train[");
		result.append(this.name);
		result.append("]");
		result.append(" is on ");
		result.append(this.pos);
		return result.toString();
	}
}
