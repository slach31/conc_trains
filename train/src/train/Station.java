package train;

/**
 * Représentation d'une gare. C'est une sous-classe de la classe {@link Element}.
 * Une gare est caractérisée par un nom et un nombre de quais (donc de trains
 * qu'elle est susceptible d'accueillir à un instant donné).
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */
public class Station extends Element {
	
	// Size of the train station
    private final int size;
    
    // The number of trains present in the station
    private volatile int trainCount = 0;
    
    // New field to track incoming trains
    private volatile int trainsHeadedHere = 0; 

    
    /**
	 * 
	 * Constructor for creating a station with a specified name and size
	 * 
	 * @param name The name of the station
	 * @param size The size of the station
	 */
    public Station(String name, int size) {
        super(name);
        if(size <= 0)
            throw new IllegalArgumentException();
        this.size = size;
    }

    /**
     * Method that increments the train count of a station, and notifies all trains of the system.
     */
    public synchronized void incrementTrainCount() {
        trainCount++;
        trainsHeadedHere--; // Decrement when train arrives
        //System.out.println("Station " + this + " now has " + trainCount + "/" + size + " trains");
        notifyAll();
    }

    /**
     * Method that decrements the train count of a station, and notifies all trains of the system.
     */
    public synchronized void decrementTrainCount() {
        if (trainCount > 0) {
            trainCount--;
            //System.out.println("Station " + this + " now has " + trainCount + "/" + size + " trains");
            notifyAll();
        }
    }

    /**
     * Method that check whether a station can accept more trains or not, 
     * by seeing both the trains that are on the station and that are heading 
     * towards it.
     * 
     * @return A boolean that encodes whether a station can accept more trains or not. 
     */
    public boolean canAcceptMoreTrains() {
        return (trainCount + trainsHeadedHere) < size;
    }

    /**
     * Adds a new train to the trainsHeadingHere count
     * (Used to manage how many can be present on a given subrailway)
     * 
     */
    public synchronized void registerIncomingTrain() {
        trainsHeadedHere++;
        //System.out.println("Station " + this + " expecting " + trainsHeadedHere + " trains");
        notifyAll();
    }

    /**
     * Removes a train from the trainsHeadingHere count
     * (Used to manage how many can be present on a given subrailway)
     * 
     */
    public synchronized void unregisterIncomingTrain() {
        if (trainsHeadedHere > 0) {
            trainsHeadedHere--;
            //System.out.println("Station " + this + " now expecting " + trainsHeadedHere + " trains");
            notifyAll();
        }
    }


    /**
     * Method that check whether a station is full (at max capacity) or not
     * 
     * @return A boolean that encodes whether a station is full or not
     */
    public boolean isFull() {
        return trainCount >= size;
    }

    /**
     * Returns the available space on a station
     * 
     * @return The available space on a station
     */
    public int getAvailableSpace() {
        return Math.max(0, size - trainCount);
    }

}	