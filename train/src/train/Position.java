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
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA othmane.el-akraba@imt-atlantique.net
 * @author Soufiane LACHGUER soufiane.lachguer@imt-atlantique.net
 */
public class Position implements Cloneable {
	
	// Direction that is taken by a train
    private Direction direction;
    
    // Element that is occupied by a train
    private Element pos;
    
    // The current SubRailway on which the train sits
    private SubRailway currentSubrailWay;

    /**
	 * 
	 * Constructor that initializes a position with a direction and an element 
	 * If either are null, then a NullPointerException is thrown
	 * 
	 * @param elt Element occupied by a train
	 * @param d direction of the train
	 * @throws NullPointerException if either entries are null
	 */
    public Position(Element elt, Direction d) {
        if (elt == null || d == null)
            throw new NullPointerException();

        this.pos = elt;
        this.direction = d;
    }

    /**
	 * Method that clones the position so that it can be assigned to a train. 
	 * If the position can't be cloned, it returns a null position.
	 * 
	 * @return A position to be assigned to a train.
	 */
    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    /**
	 * Returns the element where the train is positioned
	 * 
	 * @return Element where the train is positioned
	 */
    public Element getPos() {
        return pos;
    }

    /**
	 * Returns the direction of the train
	 * 
	 * @return the direction of the train 
	 */
    public Direction getDirection() {
        return this.direction;
    }

    /**
	 * Method that updates the position of a train 
	 * (quits the old position and enters the next one).
	 * 
	 * 
	 * @param p The element that the train will enter
	 */
    public synchronized void updatePosition(Element p) {
        p.enter(pos, this);
        this.pos.leave();
        this.pos = p;
        this.updateSubRailway();
        System.out.println(Thread.currentThread().getName() + " at: " + this.pos.toString());
    }

    /**
	 * TO DO LATER
	 * 
	 */
    public synchronized void updateSubRailway() {
		this.currentSubrailWay = direction==Direction.RL ? pos.subRailways.get(pos.subRailways.size() - 1)  : pos.subRailways.get(0);
	}

    /**
	 * Returns the current SubRailway that the train traverses 
	 * 
	 * @return the current SubRailway that the train traverses 
	 */
    public SubRailway getSubRailway() {
        return this.currentSubrailWay;
    }

    /**
   	 * Returns the count of the train that exist in the subrailway
   	 * 
   	 * @return the count of the train that exist in the subrailway
   	 */
    public int getSubRailwayCount() {
        return this.currentSubrailWay.getTrainCount();
    }

    /**
	 * Method that updates the train's direction and notifies all trains on the same railway
	 * 
	 * @param d New updated direction
	 */
    public synchronized void updateDirection(Direction d) {
        this.direction = d;
        if (this.pos.getClass() == Station.class) {
            Railway railway = this.pos.getRailway();
            synchronized (railway) {
                railway.notifyAll();
            }
        }
    }

    /**
	 * Method that makes a train turn around.
	 * Firstly, it looks up the direction used by the railway, 
	 * and calls the updateDirection() method to change the train's direction   
	 * as well as the switchDirection() method to change the subrailway's direction
	 * 
	 */
    public void turnAround() {
        Railway railway = this.pos.getRailway();
        synchronized (railway) {
            Direction newDirection = (this.direction == Direction.LR) ? Direction.RL : Direction.LR;
            this.updateDirection(newDirection);
            SubRailway subRailway = railway.getSubRailway(this.pos);
            subRailway.switchDirection();
            System.out.println("Train is now going " + this.direction);
        }
    }

    /**
	 * Method that sets the element occupied to be occupied
	 * 
	 */
    public void occupy() {
        if (this.pos.getClass() == Section.class) {
            this.pos.setIsOccupied(true);
        }   
    }

    /**
	 * 
	 * Returns a string containing information about the train's position
	 * 
	 * @return String containing information about the train's position (element occupied and direction)
	 */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(this.pos.toString());
        result.append(" going ");
        result.append(this.direction);
        return result.toString();
    }
}