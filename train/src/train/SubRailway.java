package train;

public class SubRailway{
	private Direction direction = Direction.LR;
	private final Element[] elements;

    public SubRailway(Element[] elements) {
        if (elements == null)
            throw new NullPointerException();
        this.elements = elements;
    }
	
	public Direction getCurrentDirection() {
        return direction;
    }

    public synchronized void switchDirection() {
        if (allTrainsInStations()) {
            direction = direction.equals(Direction.LR) ? Direction.RL : Direction.LR;
            System.out.println("changed direction to " + direction);
            notifyAll();
        }
    }

    private boolean allTrainsInStations() {
        for (Element element : elements) {
            if (element instanceof Section && element.getIsOccupied()) {
                return false;
            }
        }
        return true;
    }

	public Element[] getElements() {
		return this.elements;
	}


}
