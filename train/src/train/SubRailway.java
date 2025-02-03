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
    private Direction direction = Direction.LR;
    private final Element[] elements;
    private final Set<Position> trainsInSubRailway = Collections.synchronizedSet(new HashSet<>());
    private final AtomicInteger trainCount = new AtomicInteger(0);

    public SubRailway(Element[] elements) {
        if(elements == null) throw new NullPointerException();
        this.elements = elements;
        for (Element e : elements) {
            e.setSubRailway(this);
            System.out.println(e + " set to " +this.toString());
        }
        
        System.out.println(this.toString());
    }

    public synchronized Direction getCurrentDirection() {
        return direction;
    }

    public synchronized void switchDirection() {
        if (this.allTrainsInStations()) {  // Only switch if no trains in sections
            direction = (direction == Direction.LR) ? Direction.RL : Direction.LR;
            System.out.println("Changed direction to " + direction);
            notifyAll();
        }
    }
    
    public synchronized void setDirection(Direction d) {
    	System.out.println(" i am being reached");
        if (this.allTrainsInStations()) {  // Only switch if no trains in sections
            direction = d;
            System.out.println("Changed direction to " + direction);
            notifyAll();
        }
    }
    // checks if the last station in the current subrailway has the capacity to house one more train
    public synchronized boolean canAcceptMoreTrains() {
        Station nextStation = (Station) getNextStation();
        return trainCount.get() < nextStation.getAvailableSpace();
    }

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

    public synchronized void removeTrain(Position trainPosition) {
        if (trainPosition != null && trainsInSubRailway.remove(trainPosition)) {
            trainCount.decrementAndGet();
            notifyAll();
        }
    }

    private boolean allTrainsInStations() {
        for (Element element : elements) {
            if (element instanceof Section && element.getIsOccupied()) {
            	System.out.println(element + " is the stinky loser");
                return false;
            }
        }
        return true;
    }

    public Element getNextStation() {
		Element e = this.direction == Direction.LR ? this.elements[this.elements.length - 1] : this.elements[0];
		//Station s = (Station) e;
		return e;
	}

    public Element[] getElements() {
        return this.elements;
    }

    
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
