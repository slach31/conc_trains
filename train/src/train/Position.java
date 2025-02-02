package train;

/**
 * Représentation de la position d'un train dans le circuit. Une position
 * est caractérisée par deux valeurs :
 * <ol>
 *   <li>
 *     L'élément où se positionne le train : une gare (classe  {@link Station})
 *     ou une section de voie ferrée (classe {@link Section}).
 *   </li>
 *   <li>
 *     La direction qu'il prend (enumération {@link Direction}) : de gauche à
 *     droite ou de droite à gauche.
 *   </li>
 * </ol>
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr> Modifié par Mayte
 *         Segarra 
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 *         
 * @version 0.3
 */
public class Position implements Cloneable {
	private Direction direction;
	private Element pos;
<<<<<<< Updated upstream

=======
	
	private SubRailway currentSubrailWay;
	
	/**
	 * 
	 * Constructor that initializes a position with a direction and an element 
	 * If either are null, then a NullPointerException is thrown
	 * 
	 * @param elt Element occupied by a train
	 * @param d direction of the train
	 * @throws NullPointerException if either entries are null
	 */
>>>>>>> Stashed changes
	public Position(Element elt, Direction d) {
		if (elt == null || d == null)
			throw new NullPointerException();

		this.pos = elt;
		this.direction = d;
	}

	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public  Element getPos() {
		return pos;
	}
	
	public  Direction getDirection() {
		return this.direction;
	}
	
	public synchronized void updatePosition(Element p) {
<<<<<<< Updated upstream
		if (p.getClass() == Section.class) {
	    	p.enter(this.direction);
	    }
=======
		p.enter(p, this);
>>>>>>> Stashed changes
		
	    this.pos.leave();
	    
	    this.pos = p;
	    
	    this.updateSubRailway();
	    
	    System.out.println(Thread.currentThread().getName() + " at: " + this.pos.toString());
	}
	
<<<<<<< Updated upstream
=======
	
	public synchronized void updateSubRailway() {
		this.currentSubrailWay = direction==Direction.RL ? pos.subRailways.get(pos.subRailways.size() - 1)  : pos.subRailways.get(0);
	}
	
	
	public SubRailway getSubRailway() {
		return this.currentSubrailWay;
	}
	
	public int getSubRailwayCount() {
		return this.currentSubrailWay.getTrainCount();
	}
	
	/**
     * Get the next element in the train's direction.
     */
    private Element getNextElementInDirection(Direction d) {
        Element[] adjacents = this.pos.getAdjacent();
        return (d == Direction.LR) ? adjacents[1] : adjacents[0];
    }
	
	/**
	 * Method that updates the train's direction and notifies all trains on the same railway
	 * 
	 * @param d New updated direction
	 */
>>>>>>> Stashed changes
	public synchronized  void updateDirection(Direction d) {
		this.direction = d;
		if (this.pos.getClass() == Station.class) {
	        Railway railway = this.pos.getRailway();
	        synchronized (railway) {
	            railway.notifyAll();
	        }
	    }
	}
	
	public void turnAround() {
		Railway railway = this.pos.getRailway();
		synchronized (railway) {
		if (this.direction == Direction.LR) {
			this.updateDirection(Direction.RL);
			railway.switchDirection();
			//System.out.println("Train is going to switch directions");
		}
		else if (this.direction == Direction.RL){
			this.updateDirection(Direction.LR);
	        railway.switchDirection();
			//System.out.println("Train going to switch directions");
		}
		}
		
	}
	
	public void occupy() {
		if (this.pos.getClass() == Section.class) {
			this.pos.setIsOccupied(true);
		}	
	}
	
	

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(this.pos.toString());
		result.append(" going ");
		result.append(this.direction);
		return result.toString();
	}
}
