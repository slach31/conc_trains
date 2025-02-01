package train;

/**
 * 
 * The SubRailway class represents a railway segment with a directional flow 
 * that can be switched when conditions are met. 
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */

public class SubRailway{
	
	// The direction of the subrailway, set left to right initially
	private Direction direction = Direction.LR;
	
	// The list of elements in a subrailway
	private final Element[] elements;

	/**
	 * 
	 * Constructor that initializes a subrailway with an array of elements 
	 * 
	 * @param elements Elements of the railway (must not be null)
	 * @throws NullPointerException If the entered array is null 
	 */
    public SubRailway(Element[] elements) {
        if (elements == null)
            throw new NullPointerException();
        this.elements = elements;
    }
    
    
    /**
	 * 
	 * Return the current direction of a subrailway.
	 * 
	 * @return The current direction of a subrailway.
	 */
	public Direction getCurrentDirection() {
        return direction;
    }

	/**
	 * Method that switches the direction of the railway (if all trains are in a station),
	 * and informs all trains of the switch
	 * 
	 */
    public synchronized void switchDirection() {
        if (allTrainsInStations()) {
            direction = direction.equals(Direction.LR) ? Direction.RL : Direction.LR;
            System.out.println("changed direction to " + direction);
            notifyAll();
        }
    }

    /**
	 * 
	 * Method that checks whether all trains are in a station
	 * (via seeing if a section is occupied or not).
	 * 
	 * @return a Boolean that checks whether all trains are in a station or not 
	 */
    private boolean allTrainsInStations() {
        for (Element element : elements) {
            if (element instanceof Section && element.getIsOccupied()) {
                return false;
            }
        }
        return true;
    }

    /**
	 * 
	 * Return the list of elements of a subrailway.
	 * 
	 * @return The list of elements of a subrailway.
	 */
	public Element[] getElements() {
		return this.elements;
	}


}
