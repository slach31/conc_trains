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
        setPreferredSize(new Dimension(800, 200)); // Set the size of the panel
    }
    
    /**
     * Method that paint the components on the screen 
     * 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawRailway(g);
        drawTrains(g);
    }

    /**
     * 	
     * Method that draws the railway and its elements
     * 
     * @param g used graphics
     */
    private void drawRailway(Graphics g) {
        int y = 100; // Vertical position of the railway
        int xStart = 50;
        int xEnd = getWidth() - 50;
        int elementWidth = (xEnd - xStart) / railway.getSize();

        // Draw the railway line
        g.setColor(Color.BLACK);
        g.drawLine(xStart, y, xEnd, y);

        // Draw the elements (stations and sections)
        g.setColor(Color.BLUE);
        for (int i = 0; i < railway.getSize(); i++) {
            int x = xStart + i * elementWidth;
            g.drawString(railway.getElement(i).toString(), x, y - 10); // Element name
        }
    }

    /**
     * 	
     * Method that draws the train
     * 
     * @param g used graphics
     */
    private void drawTrains(Graphics g) {
        int y = 100; // Vertical position of the railway
        int xStart = 50;
        int xEnd = getWidth() - 50;
        int elementWidth = (xEnd - xStart) / railway.getSize();

        for (Map.Entry<String, TrainInfo> entry : trainPositions.entrySet()) {
            String trainName = entry.getKey();
            TrainInfo trainInfo = entry.getValue();

            int x = xStart + trainInfo.position * elementWidth;
            if (trainInfo.direction == Direction.LR) {
                g.setColor(Color.RED); // Train going left to right
                g.fillPolygon(new int[] { x, x + 10, x }, new int[] { y - 5, y, y + 5 }, 3); // Right arrow
            } else {
                g.setColor(Color.GREEN); // Train going right to left
                g.fillPolygon(new int[] { x + 10, x, x + 10 }, new int[] { y - 5, y, y + 5 }, 3); // Left arrow
            }
            g.drawString(trainName, x, y - 20); // Train name
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
        trainPositions.put(trainName, new TrainInfo(elementIndex, direction)); // Update train position and direction
        repaint(); // Redraw the panel
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