package train;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * The SubRailway class represents a railway segment with a directional flow 
 * that can be switched when conditions are met. 
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */

public class SubRailway {
	
	// The direction of the subrailway, set left to right initially
    private Direction direction = Direction.LR;
    
    // The list of elements in a subrailway
    private final Element[] elements;
    
    // Set that tracks the trains in a subrailway
    private final Set<Position> trainsInSubRailway = Collections.synchronizedSet(new HashSet<>());
    
    // Count of the trains in a subrailway
    private final AtomicInteger trainCount = new AtomicInteger(0);

    /**
	 * 
	 * Constructor that initializes a subrailway with an array of elements 
	 * 
	 * @param elements Elements of the railway (must not be null)
	 * @throws NullPointerException If the entered array is null 
	 */
    public SubRailway(Element[] elements) {
        if(elements == null) throw new NullPointerException();
        this.elements = elements;
        for (Element e : elements) {
            e.setSubRailway(this);
            System.out.println(e + " set to " +this.toString());
        }
        
        System.out.println(this.toString());
    }

    /**
	 * 
	 * Return the current direction of a subrailway.
	 * 
	 * @return The current direction of a subrailway.
	 */
    public synchronized Direction getCurrentDirection() {
        return direction;
    }

    /**
	 * Method that switches the direction of the railway (if all trains are in a station),
	 * and informs all trains of the switch
	 * 
	 */
    public synchronized void switchDirection() {
        if (this.allTrainsInStations()) {  // Only switch if no trains in sections
            direction = (direction == Direction.LR) ? Direction.RL : Direction.LR;
            System.out.println("Changed direction to " + direction);
            notifyAll();
        }
    }
    
    /**
     * TO DO LATER 
     * 
     * @return
     */
    public synchronized void setDirection(Direction d) {
    	System.out.println(" i am being reached");
        if (this.allTrainsInStations()) {  // Only switch if no trains in sections
            direction = d;
            System.out.println("Changed direction to " + direction);
            notifyAll();
        }
    }

    /**
     * Method that check whether a subrailway can accept more trains or not, 
     * 
     * @return A boolean that encodes whether a subrailway can accept more trains or not. 
     */
    public synchronized boolean canAcceptMoreTrains() {
        Station nextStation = (Station) getNextStation();
        return trainCount.get() < nextStation.getAvailableSpace();
    }

    /**
     * Method that adds a train to the subrailway (if there's space in the subrailway),
     * and informs all trains of the change.
     * 
     * @param trainPosition The train's position on the subrailway
     */
    public synchronized void addTrain(Position trainPosition) {
        if (trainPosition != null && !trainsInSubRailway.contains(trainPosition)) {
            Station destinationStation = (Station)getNextStation();
            // Only add if destination station has space
            if (trainCount.get() < destinationStation.getAvailableSpace()) {
                trainsInSubRailway.add(trainPosition);
                trainCount.incrementAndGet();
                System.out.println("SubRailway added train, count=" + trainCount.get() + 
                    ", destination=" + destinationStation + 
                    ", available space=" + destinationStation.getAvailableSpace());
                notifyAll();
            }
        }
    }

    /**
     * TO DO LATER 
     * 
     * 
     * @param trainPosition
     */
    public synchronized void removeTrain(Position trainPosition) {
        if (trainPosition != null && trainsInSubRailway.remove(trainPosition)) {
            trainCount.decrementAndGet();
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
            	System.out.println(element + " is the stinky loser");
                return false;
            }
        }
        return true;
    }
    
    /**
     * TO DO LATER 
     * 
     * @return
     */
    public Element getNextStation() {
		Element e = this.direction == Direction.LR ? this.elements[this.elements.length - 1] : this.elements[0];
		//Station s = (Station) e;
		return e;
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

    /**
	 * 
	 * Return the train count of a subrailway.
	 * 
	 * @return The train count of a subrailway.
	 */
    public synchronized int getTrainCount() {
        return trainCount.get();
    }
    
    /**
     * Method that encodes the elements of the subrailway on a String
     * 
     * @return String that lists the elements of the subrailway
     */
    @Override
    public String toString() {
    	StringBuilder result = new StringBuilder("Subrail[");
    	result.append(this.elements[0]);
    	result.append("-");
    	result.append(this.elements[this.elements.length - 1]);
        result.append("]");
    	return result.toString();
    }
}
