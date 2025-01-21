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
 */
public abstract class Element {
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
}
