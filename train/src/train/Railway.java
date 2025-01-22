package train;


/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */
public class Railway {
	
	// Array of elements of a railway
	private final Element[] elements;
	
	// Direction of the railway
	private Direction direction = Direction.LR;
	
	
	/**
	 * 
	 * Constructor that initializes a railway with an array of elements 
	 * 
	 * @param elements Elements of the railway (must not be null)
	 * @throws NullPointerException If the entered array is null 
	 */
	public Railway(Element[] elements) {
		if(elements == null)
			throw new NullPointerException();
		
		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
	}
	
	/**
	 * 
	 * Returns the length of the railway (via counting the elements that compose it)
	 * 
	 * @return length of the railway
	 */
	public int getSize() {
		return this.elements.length;
	}
	
	/**
	 * 
	 * Return the index (order) of an element in a railway.
	 * If the element doesn't exist in the railway, it returns -1 instead.
	 * 
	 * @param e Element in a railway
	 * @return The index of a railway (if it exists in the railway), else -1 
	 */
	
	public int getIndex(Element e) {
		for (int i = 0; i < this.elements.length; i++) {
            if (this.elements[i].equals(e)) {
                return i;
            }
        }
        return -1; // Return -1 if the element is not found
	}
	
	/**
	 * 
	 * Returns an element of the railway corresponding to the index given.
	 * If the index doesn't exist, it returns a null element.
	 * 
	 * @param index Index of an element in the element
	 * @return The element corresponding to the index (or null if no such index exist)
	 */
	public Element getElement(int index) {
		try {
		return elements[index];
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 
	 * Returns the current direction of the railway
	 * @return Current direction of the railway
	 */
	public Direction getCurrentDirection() {
        return direction;
    }

	/**
	 * Method that switches the direction of the railway (if all trains are in a station), and informs all trains of the switch
	 */
	public synchronized void switchDirection() {
	    if (allTrainsInStations()) {
		    direction = direction.equals(Direction.LR) ? Direction.RL : Direction.LR;
		    notifyAll();
	    }
	}
	
	/**
	 * Verifies if all trains are occupied in a station.
	 * If a Section is occupied by a train, it returns false
	 * 
	 * @return Boolean verifying whether or not all trains are in a station
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
     * Method that encodes the elements of the railway on a String
     * 
     * @return String that lists the elements of the railway
     */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Element e : this.elements) {
			if (first)
				first = false;
			else
				result.append("--");
			result.append(e);
		}
		return result.toString();
	}
}
