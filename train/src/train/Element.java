package train;

import java.util.ArrayList;

/**
 * Cette classe abstraite est la représentation générique d'un élément de base d'un
 * circuit, elle factorise les fonctionnalitÃ©s communes des deux sous-classes :
 * l'entrée d'un train, sa sortie et l'appartenance au circuit.<br/>
 * Les deux sous-classes sont :
 * <ol>
 *   <li>La représentation d'une gare : classe {@link Station}</li>
 *   <li>La représentation d'une section de voie ferrée : classe {@link Section}</li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * 
 *  * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */
public abstract class Element {
	
	// Name of the element
    private final String name;
    
    // Railway that the element belongs to
    protected Railway railway;
    
    // SubRailway List
    protected ArrayList<SubRailway> subRailways = new ArrayList<>();
    
    // boolean to see if an element is occupied by a train 
    protected boolean isOccupied = false;

    /**
	 * 
	 * Constructor that initializes an element with a name.
	 * If a name isn't given, it will throw a NullPointerException.
	 * 
	 * @param name Name of the Element
	 */
    protected Element(String name) {
        if(name == null)
            throw new NullPointerException();
        this.name = name;
    }

    /**
	 * Method that appoints an element to a specific railway.
	 * If the railway isn't given, it will throw a NullPointerException.
	 * 
	 * @param r The railway to be assigned to
	 * @throws NullPointerException if a railway isn't given
	 */
    public void setRailway(Railway r) {
        if(r == null)
            throw new NullPointerException();
        this.railway = r;
    }

    /**
	 * Method that appoints an element to a specific subrailway.
	 * If the subrailway isn't given, it will throw a NullPointerException.
	 * 
	 * @param r The subrailway to be assigned to
	 * @throws NullPointerException if a subrailway isn't given
	 */
    public void setSubRailway(SubRailway r) {
        if(r == null)
            throw new NullPointerException();
        this.subRailways.add(r);
    }

    /**
	 * Returns whether the element is occupied of not
	 * @return Boolean informing whether the element is occupied or not
	 */
    public boolean getIsOccupied() {
        return isOccupied;
    }

    /**
	 * Changes the status of the element (the boolean isOccupied)
	 * and notifies all trains on the railway 
	 * 
	 * @param b The new value of the boolean isOccupied
	 */
    public synchronized void setIsOccupied(boolean b) {
        this.isOccupied = b;
        notifyAll();
    }

    /**
	 * Returns the railway that the element belongs to 
	 * @return The railway that the element belongs to 
	 */
    public Railway getRailway() {
        return this.railway;
    }

    /**
	 * Method that changes the status of the element to be free 
	 * and notifies all the trains of the change
	 */
    public synchronized void leave() {
        if (this instanceof Station) {
            Station station = (Station)this;
            station.decrementTrainCount();
            this.isOccupied = station.isFull();
        } else {
            this.isOccupied = false;
        }
        notifyAll();
    }

    /**
	 * The enter method, used to enter a new element
	 *  
	 * @param p the element that the train is attempting to enter
	 * @param pos The position of the entering train
	 */
    public synchronized void enter(Element p, Position pos) {
    	pos.updateSubRailway();
    	
    	SubRailway subRailway = pos.getDirection()==Direction.LR ? p.subRailways.get(p.subRailways.size() - 1)  : p.subRailways.get(0);
    	subRailway.setDirection(pos.getDirection());
    	while (!this.invariant(p, pos)) {
            try {
                wait(2000);
            	subRailway.setDirection(pos.getDirection()); //tries to change the subrailway direction after a while
            } catch (InterruptedException e) {}
        }
        
        // Handle entering station
        if (this instanceof Station) {
            Station thisStation = (Station)this;
            thisStation.incrementTrainCount();
            this.isOccupied = thisStation.isFull();
        } else {
            this.isOccupied = true;
        }
    }
    
    /**
     * 
     * the security invarient
     * Ensures that at a any given moment, no 2 trains can be on the same element,
     * no 2 trains on the same subrailway can move in different directions,
     * and that a train does'nt leave a station if there are N trains on the given subrailway
     * where N is the size of said station.
     * This ensures that trains don't interblock and operate as intended
     * @param p
     * @param pos
     * @return
     */
    public boolean invariant(Element p, Position pos) {
        // Check if element is occupied
        if (this.isOccupied) {
            //System.out.println(Thread.currentThread().getName() + " waiting: element occupied");
            return false;
        }

        SubRailway subRailway = pos.getDirection()==Direction.LR ? p.subRailways.get(p.subRailways.size() - 1)  : p.subRailways.get(0);

                
        // Check direction match
        if (!subRailway.getCurrentDirection().equals(pos.getDirection())) {
            System.out.println(Thread.currentThread().getName() + " waiting: direction mismatch");
            return false;
        }
        
        // If starting from the first station, check destination capacity
        if (pos.getPos() instanceof Station && this instanceof Section) {
            Station destinationStation = (Station)subRailway.getNextStation();
            
            // Only allow train to leave if destination can accept it
            if (!destinationStation.canAcceptMoreTrains()) {
                System.out.println(Thread.currentThread().getName() + "  " + p + "  " + subRailway + " waiting: destination station " + destinationStation +  " cannot accept more trains");
                return false;
            }
            
            // Register this train as headed to the destination
            destinationStation.registerIncomingTrain();
        }

        return true;
    }


    /**
	 * 
	 * Returns an array that contains the adjacent elements (sections or railways)
	 * @return Array that contains the adjacent elements (sections or railways)
	 */
    public Element[] getAdjacent() {
        int index = this.railway.getIndex(this);
        int length = this.railway.getSize();
        
        Element left = null;
        Element right = null;
        
        if (0 < index && index < length - 1) {
            left = this.railway.getElement(index - 1);
            right = this.railway.getElement(index + 1);
        } else if (index == 0) {
            left = this;
            right = this.railway.getElement(index + 1);
        } else if(index == length - 1) {
            left = this.railway.getElement(index - 1);
            right = this;
        }
        return new Element[] {left, right};
    }

    /**
	 * Returns a String containing the name of the element
	 */
    @Override
    public String toString() {
        return this.name;
    }
}

