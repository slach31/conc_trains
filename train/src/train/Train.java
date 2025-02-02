package train;

/**
 * Représentation d'un train. Un train est caractérisé par deux valeurs :
 * <ol>
 *   <li>
 *     Son nom pour l'affichage.
 *   </li>
 *   <li>
 *     La position qu'il occupe dans le circuit (un élément avec une direction) : classe {@link Position}.
 *   </li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Mayte segarra <mt.segarra@imt-atlantique.fr>
 * Test if the first element of a train is a station
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @version 0.3
 */
public class Train extends Thread {
    private final String name;
    private final Position pos;

    public Train(String name, Position p) throws BadPositionForTrainException {
        if (name == null || p == null)
            throw new NullPointerException();

        // A train should first be in a station
        if (!(p.getPos() instanceof Station))
            throw new BadPositionForTrainException(name);

        this.name = name;
        this.pos = p.clone();
        this.pos.occupy();
    }

    public void run() {
        while (true) {
            this.advance();
            this.turnAround();
        }
    }

    public void advance() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {}
        
        Direction d = this.pos.getDirection();
        Element p = this.pos.getPos();
        
        Element[] adjacents = p.getAdjacent();
        
        if (d == Direction.LR) {
            this.pos.updatePosition(adjacents[1]);
        }
        else {
            this.pos.updatePosition(adjacents[0]);
        }
    }

    public void turnAround() {
        Element p = this.pos.getPos();
        
        if (p instanceof Station) {
            Element[] adjacents = p.getAdjacent();
    
            if (this.pos.getDirection() == Direction.LR && adjacents[1] instanceof Section) {
                return;
            }
            if (this.pos.getDirection() == Direction.RL && adjacents[0] instanceof Section) {
                return;
            }
            this.pos.turnAround();
        }
    }

    public Position getPosition() {
        return this.pos;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Train[");
        result.append(this.name);
        result.append("]");
        return result.toString();
    }
}
