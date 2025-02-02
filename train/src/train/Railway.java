package train;

import java.util.ArrayList;

/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	
	// Array of elements of a railway
	protected final Element[] elements;
	
	// Array of stations of a railway
	private Element[] stations;
	
	// Array of subrailways of a railway
	private SubRailway[] subRailways;

	/**
	 * 
	 * Constructor that initializes a railway with an array of elements 
	 * subrailways and stations
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
		this.setSubRailways();
		this.initStations();
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
        return -1;
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
	 * Method that initializes the stations linked to a railway.
	 * It sees all the elements of a railway and extracts the stations in its own List
	 */
	public void initStations() {
		ArrayList<Element> stations = new ArrayList<>();
	    for (int i = 0; i < this.getSize(); i++) {
	        Element e = this.getElement(i);
	        if (e instanceof Station) {
	            stations.add(e);
	        }
	    }
	    this.stations = stations.toArray(new Element[0]);
	}
	
	
	/**
	 * 
	 * Method that creates new subrailways based on the elements of a railway,
	 * using stations as delimiters of each subrailway
	 */
	public void setSubRailways() {
		ArrayList<SubRailway> subRailways = new ArrayList<>();
		ArrayList<Element> subRailwayElements = new ArrayList<>();
	    
	    Station startStation = null;

	    for (int i = 0; i < this.getSize(); i++) {
	        Element currentElement = this.getElement(i);

	        if (currentElement instanceof Station) {
	            if (startStation == null) {
	                // First station found, mark it as start
	                startStation = (Station) currentElement;
	                subRailwayElements.add(startStation);
	            } else {
	                // End station found, create a SubRailway
	                subRailwayElements.add(currentElement);
	                SubRailway subRailway = new SubRailway(subRailwayElements.toArray(new Element[0]));
	                subRailways.add(subRailway);

	                // Reset for the next subRailway
	                startStation = (Station) currentElement;
	                subRailwayElements = new ArrayList<>();
	                subRailwayElements.add(startStation);
	            }
	        } else {
	            // Add section to the current subRailway
	            subRailwayElements.add(currentElement);
	        }
	    }

	    // Handle the last subRailway if it exists
	    if (!subRailwayElements.isEmpty()) {
	        SubRailway subRailway = new SubRailway(subRailwayElements.toArray(new Element[0]));
	        subRailways.add(subRailway);
	    }
	    
	    this.subRailways = subRailways.toArray(new SubRailway[0]);
	}
	
	/**
	 * 
	 * Return the list of subrailways of a railway.
	 * 
	 * @return The list of subrailways of a railway.
	 */
	public SubRailway[] getSubRailways() {
		return subRailways;
	}
	
	/**
	 * 
	 * Returns the subrailway that an element belongs to, else throws
	 * an IllegalStateException if it doesn't exist in the railway or in any subrailway
	 * 
	 * @param p the element that we want to get the subrailway of 
	 * @return The subrailway on which the element exist
	 * @throws IllegalStateException if the element doesn't exist in the railway or in any subrailway
	 */
	public SubRailway getSubRailway(Element p) {
	    Element currentElement = p;
	    int elementIndex = -1;

	    // Find the index of the current element in the railway
	    for (int i = 0; i < this.elements.length; i++) {
	        if (currentElement.equals(this.elements[i])) {
	            elementIndex = i;
	            break;
	        }
	    }

	    if (elementIndex == -1) {
	        throw new IllegalArgumentException("Element not found in the railway.");
	    }

	    // Determine the sub-railway based on the element's index and direction
	    for (SubRailway subRailway : this.subRailways) {
	        Element[] subRailwayElements = subRailway.getElements();

	        // Check if the current element is in this sub-railway
	        for (Element e : subRailwayElements) {
	            if (e.equals(currentElement)) {
	                return subRailway;
	            }
	        }
	    }

	    throw new IllegalStateException("SubRailway not found for the given position.");
	}

	/**
	 * 
	 * Return the list of stations of a railway.
	 * 
	 * @return The list of stations of a railway.
	 */
	public Element[] getStations() {
		return this.stations;
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