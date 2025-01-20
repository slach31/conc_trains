package train;


/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	private final Element[] elements;
	private Direction direction = Direction.LR;

	public Railway(Element[] elements) {
		if(elements == null)
			throw new NullPointerException();
		
		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
	}
	
	public int getSize() {
		return this.elements.length;
	}
	
	public int getIndex(Element e) {
		for (int i = 0; i < this.elements.length; i++) {
            if (this.elements[i].equals(e)) {
                return i;
            }
        }
        return -1; // Return -1 if the element is not found
	}
	
	
	public Element getElement(int index) {
		try {
		return elements[index];
		} catch (Exception e) {
			return null;
		}
	}
	
	public synchronized Direction getCurrentDirection() {
        return direction;
    }

	public synchronized void switchDirection() {
	    if (allTrainsInStations()) {
		    direction = direction.equals(Direction.LR) ? Direction.RL : Direction.LR;
		    notifyAll();
	    }
	}

    private boolean allTrainsInStations() {
        for (Element element : elements) {
            if (element instanceof Section && element.getIsOccupied()) {
                return false;
            }
        }
        return true;
    }

    
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
