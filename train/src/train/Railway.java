package train;

import java.util.ArrayList;
import java.util.List;

/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	protected final Element[] elements;
	private Element[] stations;
	private SubRailway[] subRailways;

	public Railway(Element[] elements) {
		if(elements == null)
			throw new NullPointerException();
		
		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
		this.setSubRailways();
		this.initStations();
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
        return -1;
	}
	
	
	public Element getElement(int index) {
		try {
		return elements[index];
		} catch (Exception e) {
			return null;
		}
	}
	
	public void initStations() {
	    List<Element> stations = new ArrayList<>();
	    for (int i = 0; i < this.getSize(); i++) {
	        Element e = this.getElement(i);
	        if (e instanceof Station) {
	            stations.add(e);
	        }
	    }
	    this.stations = stations.toArray(new Element[0]);
	}
	
	public void setSubRailways() {
	    List<SubRailway> subRailways = new ArrayList<>();
	    List<Element> subRailwayElements = new ArrayList<>();
	    
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
	
	
	public SubRailway[] getSubRailways() {
		return subRailways;
	}
	
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

	
	public Element[] getStations() {
		return this.stations;
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
