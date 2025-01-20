package train;

/**
 * Représentation de la position d'un train dans le circuit. Une position
 * est caractérisée par deux valeurs :
 * <ol>
 *   <li>
 *     L'élément où se positionne le train : une gare (classe  {@link Station})
 *     ou une section de voie ferrée (classe {@link Section}).
 *   </li>
 *   <li>
 *     La direction qu'il prend (enumération {@link Direction}) : de gauche à
 *     droite ou de droite à gauche.
 *   </li>
 * </ol>
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr> Modifié par Mayte
 *         Segarra 
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 *         
 * @version 0.3
 */
public class Position implements Cloneable {
	private Direction direction;
	private Element pos;

	public Position(Element elt, Direction d) {
		if (elt == null || d == null)
			throw new NullPointerException();

		this.pos = elt;
		this.direction = d;
	}

	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public  Element getPos() {
		return pos;
	}
	
	public  Direction getDirection() {
		return this.direction;
	}
	
	public void updatePosition(Element p) {
	    synchronized (p) {
	        while (!this.invariant(p)) {
	            System.out.println(Thread.currentThread().getName() + " waiting");
	            try {
	                p.wait();
	            } catch (InterruptedException e) {
	            }
	        }
	    }

	    synchronized (this.pos) {
	        this.pos.setIsOccupied(false);
	    }
	    this.pos = p;

	    if (this.pos.getClass() == Section.class) {
	        synchronized (p) {
	            this.pos.setIsOccupied(true);
	        }
	    }
	    
	    System.out.println(Thread.currentThread().getName() + " at: " + this.pos.toString());
	}
	
	public synchronized  void updateDirection(Direction d) {
		this.direction = d;
		if (this.pos.getClass() == Station.class) {
	        Railway railway = this.pos.getRailway();
	        synchronized (railway) {
	            railway.notifyAll();
	        }
	    }
	}
	
	public void turnAround() {
		Railway railway = this.pos.getRailway();
		synchronized (railway) {
		if (this.direction == Direction.LR) {
			this.updateDirection(Direction.RL);
			railway.switchDirection();
			//System.out.println("Train is going to switch directions");
		}
		else if (this.direction == Direction.RL){
			this.updateDirection(Direction.LR);
	        railway.switchDirection();
			//System.out.println("Train going to switch directions");
		}
		}
		
	}
	
	public void occupy() {
		if (this.pos.getClass() == Section.class) {
			this.pos.setIsOccupied(true);
		}	
	}
	
	public boolean invariant(Element e) {
	    if (e.getIsOccupied()) {
	        return false;
	    }

	    Railway railway = e.getRailway();
	    if (railway != null && !railway.getCurrentDirection().equals(this.direction)) {
	        return false;
	    }

	    return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(this.pos.toString());
		result.append(" going ");
		result.append(this.direction);
		return result.toString();
	}
}
