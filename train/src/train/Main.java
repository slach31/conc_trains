package train;

import java.util.ArrayList;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */
public class Main {
<<<<<<< Updated upstream
	public static void main(String[] args) {
		Station A = new Station("GareA", 3);
		Station D = new Station("GareD", 3);
		Section AB = new Section("AB");
		Section BC = new Section("BC");
		Section CD = new Section("CD");
		Railway r = new Railway(new Element[] { A, AB, BC, CD, D });
		System.out.println("The railway is:");
		System.out.println("\t" + r);
		Position p = new Position(A, Direction.LR);
		try {
			Train t1 = new Train("1", p);
			t1.start();
			Train t2 = new Train("2", p);
			t2.start();
			Train t3 = new Train("3", p);
			t3.start();
			
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}
}
=======
    public static void main(String[] args) {
        Station A = new Station("GareA", 3);
        Station D = new Station("GareD", 2);
        Station G = new Station("GareG", 2);
        Section AB = new Section("AB");
        Section BC = new Section("BC");
        Section CD = new Section("CD");
        Section DE = new Section("DE");
        Section EF = new Section("EF");
        Section FG = new Section("FG");
        Railway r = new Railway(new Element[] { A, AB, BC, CD, D, DE, EF, FG, G });

        // Create the Swing GUI
        JFrame frame = new JFrame("Railway Visualization");
        RailwayVisualization visualization = new RailwayVisualization(r);
        frame.add(visualization);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
       

        // Start the trains
        Position p = new Position(A, Direction.LR);
        ArrayList<Train> trains = new ArrayList<>();
        try {
        	

            for (int i = 1; i <= 3; i++) {
                Train train = new Train(String.valueOf(i), p);
                trains.add(train);
                train.start();
            }
            
            

            // Update train positions in the visualization
            new Thread(() -> {
                while (true) {
                    for (Train train : trains) {
                        int index = r.getIndex(train.getPosition().getPos());
                        Direction direction = train.getPosition().getDirection();
                        visualization.updateTrainPosition(train.toString(), index, direction);
                    }
                    try {
                        Thread.sleep(500); // Update every 500ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (BadPositionForTrainException e) {
            System.out.println("Le train " + e.getMessage());
        }
    }
}
>>>>>>> Stashed changes
