package train;

/**
 * Représentation d'une gare. C'est une sous-classe de la classe {@link Element}.
 * Une gare est caractérisée par un nom et un nombre de quais (donc de trains
 * qu'elle est susceptible d'accueillir à un instant donné).
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Station extends Element {
    private final int size;
    private volatile int trainCount = 0;
    private volatile int trainsHeadedHere = 0;  // New field to track incoming trains

    

    public Station(String name, int size) {
        super(name);
        if(size <= 0)
            throw new IllegalArgumentException();
        this.size = size;
    }

    
    public synchronized void incrementTrainCount() {
        trainCount++;
        trainsHeadedHere--; // Decrement when train arrives
        //System.out.println("Station " + this + " now has " + trainCount + "/" + size + " trains");
        notifyAll();
    }

    public synchronized void decrementTrainCount() {
        if (trainCount > 0) {
            trainCount--;
            //System.out.println("Station " + this + " now has " + trainCount + "/" + size + " trains");
            notifyAll();
        }
    }

    public boolean canAcceptMoreTrains() {
        return (trainCount + trainsHeadedHere) < size;
    }

    public synchronized void registerIncomingTrain() {
        trainsHeadedHere++;
        //System.out.println("Station " + this + " expecting " + trainsHeadedHere + " trains");
        notifyAll();
    }

    public synchronized void unregisterIncomingTrain() {
        if (trainsHeadedHere > 0) {
            trainsHeadedHere--;
            //System.out.println("Station " + this + " now expecting " + trainsHeadedHere + " trains");
            notifyAll();
        }
    }

    public int getTrainCount() {
        return trainCount;
    }

    public boolean isFull() {
        return trainCount >= size;
    }

    public int getAvailableSpace() {
        return Math.max(0, size - trainCount);
    }

    public int getSize() {
        return size;
    }
}	