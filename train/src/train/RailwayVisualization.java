package train;

/**
 * 
 * Class that defines the UI used to visualize the railway
 * 
 * 
 * Authors of the new implementation 
 * @author Othmane EL AKRABA <othmane.el-akraba@imt-atlantique.net>
 * @author Soufiane LACHGUER <soufiane.lachguer@imt-atlantique.net>
 */

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RailwayVisualization extends JPanel {
	
	//Initialize the railway that we want to visualize
    private final Railway railway;
    
    // Stores train positions and directions
    private final Map<String, TrainInfo> trainPositions;

    
    /**
   	 * 
   	 * Constructor that initializes the elements used in the UI
   	 * 
   	 * @param railway The visualized railway 
   	 */
    public RailwayVisualization(Railway railway) {
        this.railway = railway;
        this.trainPositions = new HashMap<>();
        setPreferredSize(new Dimension(800, 200));
    }

    /**
     * Method that paint the components on the screen 
     * 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // First draw railway lines
        drawRailwayLines(g);
        // Then draw element names
        drawElementNames(g);
        // Finally draw trains on top
        drawTrains(g);
    }
    
    /**
     * 	
     * Method that draws the railway and its elements
     * 
     * @param g used graphics
     */
    private void drawRailwayLines(Graphics g) {
        int y = 100;
        int xStart = 50;
        int xEnd = getWidth() - 50;
        int elementWidth = (xEnd - xStart) / railway.getSize();

        // Draw subrailways first
        SubRailway[] subRailways = railway.getSubRailways();
        for (SubRailway subRailway : subRailways) {
            Element[] elements = subRailway.getElements();
            int firstIndex = railway.getIndex(elements[0]);
            int lastIndex = railway.getIndex(elements[elements.length - 1]);
            
            int subStart = xStart + firstIndex * elementWidth;
            int subEnd = xStart + (lastIndex + 1) * elementWidth;
            
            // Set color based on direction
            if (subRailway.getCurrentDirection() == Direction.LR) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            
            // Draw thicker line for subrailway
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(subStart, y, subEnd, y);
        }
    }

    /**
     * 	
     * Method that draws the elements' names 
     * 
     * @param g used graphics
     */
    private void drawElementNames(Graphics g) {
        int y = 100;
        int xStart = 50;
        int xEnd = getWidth() - 50;
        int elementWidth = (xEnd - xStart) / railway.getSize();

        g.setColor(Color.BLUE);
        for (int i = 0; i < railway.getSize(); i++) {
            int x = xStart + i * elementWidth;
            g.drawString(railway.getElement(i).toString(), x, y - 10);
        }
    }

    /**
     * 	
     * Method that draws the train
     * 
     * @param g used graphics
     */
    private void drawTrains(Graphics g) {
        int y = 100;
        int xStart = 50;
        int xEnd = getWidth() - 50;
        int elementWidth = (xEnd - xStart) / railway.getSize();

        for (Map.Entry<String, TrainInfo> entry : trainPositions.entrySet()) {
            String trainName = entry.getKey();
            TrainInfo trainInfo = entry.getValue();

            int x = xStart + trainInfo.position * elementWidth;
            if (trainInfo.direction == Direction.LR) {
                g.setColor(Color.RED);
                g.fillPolygon(new int[] { x, x + 10, x }, new int[] { y - 5, y, y + 5 }, 3);
            } else {
                g.setColor(Color.GREEN);
                g.fillPolygon(new int[] { x + 10, x, x + 10 }, new int[] { y - 5, y, y + 5 }, 3);
            }
            g.drawString(trainName, x, y - 20);
        }
    }

    /**
     * Method that updates the train's position on the screen 
     * 
     * @param trainName the train's name
     * @param elementIndex the new element's index
     * @param direction the train's direction
     */
    public void updateTrainPosition(String trainName, int elementIndex, Direction direction) {
        trainPositions.put(trainName, new TrainInfo(elementIndex, direction));
        repaint();
    }

    /**
     * Helper class to store train information
     */
    private static class TrainInfo {
        int position;
        Direction direction;

        TrainInfo(int position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }
    }
}