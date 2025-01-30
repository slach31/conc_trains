package train;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Station A = new Station("GareA", 3);
        Station D = new Station("GareD", 3);
        Station G = new Station("GareG", 3);
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
        try {
            Train t1 = new Train("1", p);
            t1.start();
            Train t2 = new Train("2", p);
            t2.start();
            Train t3 = new Train("3", p);
            t3.start();
            
            

            // Update train positions in the visualization
            new Thread(() -> {
                while (true) {
                    for (Train train : new Train[] { t1, t2, t3 }) {
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