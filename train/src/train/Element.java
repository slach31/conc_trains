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
 */
public abstract class Element {
<<<<<<< Updated upstream
	private final String name;
	protected Railway railway;
	private boolean isOccupied = false;

	protected Element(String name) {
		if(name == null)
			throw new NullPointerException();
		
		this.name = name;
	}

	public void setRailway(Railway r) {
		if(r == null)
			throw new NullPointerException();
		
		this.railway = r;
	}
	
	public boolean getIsOccupied() {
		return isOccupied;
	}
	
	public synchronized void setIsOccupied(boolean b) {
		this.isOccupied = b;
		notifyAll();
	}
	
	public Railway getRailway() {
		return this.railway;
	}
	
	public synchronized void leave() {
		this.isOccupied = false;
		notifyAll();
	}
	
	public synchronized void enter(Direction d) {
		while (!this.invariant(d)) {
			System.out.println(Thread.currentThread().getName() + " waiting");
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		
		this.isOccupied = true;
		notifyAll();
	}
	
	
	public boolean invariant(Direction d) {
	    if (this.isOccupied) {
	        return false;
	    }

	    if (!this.railway.getCurrentDirection().equals(d)) {
	        return false;
	    }

	    return true;
	}
	
	
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
		Element [] adjacents = {left, right};
		return adjacents;
	}
	

	@Override
	public String toString() {
		return this.name;
	}
=======
    private final String name;
    protected Railway railway;
    protected ArrayList<SubRailway> subRailways = new ArrayList<>();
    protected volatile boolean isOccupied = false;

    protected Element(String name) {
        if(name == null) throw new NullPointerException();
        this.name = name;
    }

    public void setRailway(Railway r) {
        if(r == null) throw new NullPointerException();
        this.railway = r;
    }

    public void setSubRailway(SubRailway r) {
        if(r == null) throw new NullPointerException();
        this.subRailways.add(r);
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public synchronized void setIsOccupied(boolean b) {
        this.isOccupied = b;
        notifyAll();
    }

    public Railway getRailway() {
        return this.railway;
    }

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

    

    public synchronized void enter(Element p, Position pos) {
    	SubRailway subRailway = railway.getSubRailway(p);
    	if (!subRailway.getCurrentDirection().equals(pos.getDirection())) {
    		subRailway.switchDirection();
    	}
        while (!this.invariant(p, pos)) {
            try {
                wait(1000);
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
    
    
    public boolean invariant(Element p, Position pos) {
        // Check if element is occupied
        if (this.isOccupied) {
            //System.out.println(Thread.currentThread().getName() + " waiting: element occupied");
            return false;
        }

        SubRailway subRailway = railway.getSubRailway(p);

                
        // Check direction match
        if (!subRailway.getCurrentDirection().equals(pos.getDirection())) {
            //System.out.println(Thread.currentThread().getName() + " waiting: direction mismatch");
            return false;
        }
        
        // If starting from the first station, check destination capacity
        if (pos.getPos() instanceof Station && this instanceof Section) {
            Station destinationStation = (Station)subRailway.getNextStation();
            
            // Only allow train to leave if destination can accept it
            if (!destinationStation.canAcceptMoreTrains()) {
                //System.out.println(Thread.currentThread().getName() + " waiting: destination station " + destinationStation +  " cannot accept more trains");
                return false;
            }
            
            // Register this train as headed to the destination
            destinationStation.registerIncomingTrain();
        }

        return true;
    }

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

    @Override
    public String toString() {
        return name;
    }
>>>>>>> Stashed changes
}
