package train;

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
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */
public abstract class Element {
	
	// Name of the element
	private final String name;
	
	// Railway that the element belongs to
	protected Railway railway;
	
	// boolean to see if an element is occupied by a train 
	private boolean isOccupied = false;
	
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
	 */
	
	public void setRailway(Railway r) {
		if(r == null)
			throw new NullPointerException();
		
		this.railway = r;
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
		this.isOccupied = false;
		notifyAll();
	}
	
	/**
	 * Method that changes the status of the element to be free 
	 * and notifies all the trains of the change.
	 * Note that the train can only enter if the invariant is verified (and that the invariant depends
	 * on the direction of the entering train) 
	 * If the train can't verify the invariant, the train is made to wait.
	 *  
	 * @param d The direction of the entering train
	 */
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
	
	/**
	 * Method that encodes the invariant conditions, i.e :
	 * - if the element isn't occupied, and
	 * - the train is entering from the same direction as the railway's direction
	 * 
	 * Returns the verification status of the invariant
	 * 
	 * @param d The direction of the entering train
	 * @return Boolean that encodes the verification status of the invariant
	 */
	public boolean invariant(Direction d) {
	    if (this.isOccupied) {
	        return false;
	    }

	    if (!this.railway.getCurrentDirection().equals(d)) {
	        return false;
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
		Element [] adjacents = {left, right};
		return adjacents;
	}
	
	/**
	 * Returns a String containing the name of the element
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
