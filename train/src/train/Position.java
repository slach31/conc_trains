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
    private SubRailway currentSubrailWay;

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

    public Element getPos() {
        return pos;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public synchronized void updatePosition(Element p) {
        p.enter(pos, this);
        this.pos.leave();
        this.pos = p;
        this.updateSubRailway();
        System.out.println(Thread.currentThread().getName() + " at: " + this.pos.toString());
    }

    public synchronized void updateSubRailway() {
		this.currentSubrailWay = direction==Direction.RL ? pos.subRailways.get(pos.subRailways.size() - 1)  : pos.subRailways.get(0);
	}



    public synchronized void updateDirection(Direction d) {
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
            Direction newDirection = (this.direction == Direction.LR) ? Direction.RL : Direction.LR;
            this.updateDirection(newDirection);
            SubRailway subRailway = railway.getSubRailway(this.pos);
            subRailway.switchDirection();
            System.out.println("Train is now going " + this.direction);
        }
    }

    public void occupy() {
        if (this.pos.getClass() == Section.class) {
            this.pos.setIsOccupied(true);
        }   
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(this.pos.toString());
        result.append(" going ");
        result.append(this.direction);
        return result.toString();
    }
}