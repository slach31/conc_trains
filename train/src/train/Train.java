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
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */

/**
 *  Class representing a Train in the concurrent train model.
 */

public class Train extends Thread {
	
	// Name of the train 
	private final String name;
	
	// Position of the train in the railway
	private final Position pos;

	
	/**
     * Constructor for creating a Train with a specified name and position.
     * 
     * @param name The name of the train (cannot be null).
     * @param p The position of the train within the railway  (cannot be null).
     * @throws BadPositionForTrainException If either the name or the position of the train are entered null.
     */
	public Train(String name, Position p) throws BadPositionForTrainException {
		if (name == null || p == null)
			throw new NullPointerException();

		// A train should be first be in a station
		if (!(p.getPos() instanceof Station))
			throw new BadPositionForTrainException(name);

		this.name = name;
		this.pos = p.clone();
		this.pos.occupy();
	}
	
	/**
	 * Method to run the Train thread.
	 * Each time the thread is run, the train advances.
	 */
	public void run() {
		while (true) {
			this.advance();
			this.turnAround();
		}
	}
	
	/**
	 * Method that makes the train advance.
	 * If the train wasn't made to stop (InterruptedException), the train searches the adjacent 
	 * sections to the section it occupies and chooses the section to advance to depending on the 
	 * direction that it's facing (left to right or right to left).
	 */
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
	
	
	/**
	 * Method that makes the train turn around (change direction) if it reaches a station.
	 */
	public void turnAround() {
		Element p = this.pos.getPos();
		if (p.getClass() == Station.class) {
			this.pos.turnAround();
		}
	}

	/**
	 * Returns  a description of the train's situation (position)
	 * 
	 * @return The Place object linked to this arc.
	 */
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
