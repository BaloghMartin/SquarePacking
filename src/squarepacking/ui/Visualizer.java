package squarepacking.ui;


import squarepacking.algorithm.*;
import squarepacking.ui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Visualizer extends JFrame {

    private static Visualizer instance;

    private static final int CELL_SIZE = 20; // Size of each cell in pixels
    private int[][] dataArray;
    private double zoomFactor = 1.0;
    private double initialZoomFactor = zoomFactor;
    private static final double ZOOM_OUT_LIMIT = 0.1; // Zoom out limit threshold

    private ArrayPanel arrayPanel; // Reference to the ArrayPanel for updates

    public Visualizer(int[][] dataArray) {
        this.dataArray = dataArray;

        setTitle("Array Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JScrollPane and add ArrayPanel to it
        arrayPanel = new ArrayPanel();
        JScrollPane scrollPane = new JScrollPane(arrayPanel);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Add a mouse wheel listener for zooming
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    zoomIn();
                } else {
                    zoomOut();
                }
            }
        });

        pack(); // Adjust the size of the frame to fit the contents
        setLocationRelativeTo(null); // Center the frame on the screen
        initialZoomFactor = zoomFactor; // Store initial zoom factor
    }

    public static Visualizer getInstance(int[][] dataArray) {
        if (instance == null) {
            instance = new Visualizer(dataArray);
        } else {
            instance.updateVisualization(dataArray);
        }
        return instance;
    }

    private class ArrayPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.scale(zoomFactor, zoomFactor); // Apply zoom factor

            for (int i = 0; i < dataArray.length; i++) {
                for (int j = 0; j < dataArray[0].length; j++) {
                    int value = dataArray[i][j];
                    Color cellColor = getColorForValue(value);

                    g2d.setColor(cellColor);
                    g2d.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    // You can also add text inside the cells if needed
                    g2d.drawString(String.valueOf(value), j * CELL_SIZE + CELL_SIZE / 2, i * CELL_SIZE + CELL_SIZE / 2);
                }
            }
        }

        // Method to update the visualization with new data
        public void updateVisualization(int[][] newDataArray) {
            dataArray = newDataArray;
            repaint();
        }
    }

    private void zoomIn() {
        if (zoomFactor < 2.0) {
            zoomFactor += 0.1;
            adjustScrollPaneSize();
            arrayPanel.repaint(); // Repaint the ArrayPanel
        }
    }

    private void zoomOut() {
        if (zoomFactor > initialZoomFactor * ZOOM_OUT_LIMIT) { // Limit zoom out
            zoomFactor -= 0.1;
            adjustScrollPaneSize();
            arrayPanel.repaint(); // Repaint the ArrayPanel
        }
    }

    private void adjustScrollPaneSize() {
        revalidate();
        repaint();
    }

    // Method to update the visualization with new data
    public void updateVisualization(int[][] newDataArray) {
        arrayPanel.updateVisualization(newDataArray);
    }

    // Method to save the visualization as an image
    public void saveVisualizationAsImage(String filePath) {
        int width = dataArray[0].length * CELL_SIZE;
        int height = dataArray.length * CELL_SIZE;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        for (int i = 0; i < dataArray.length; i++) {
            for (int j = 0; j < dataArray[0].length; j++) {
                int value = dataArray[i][j];
                Color cellColor = getColorForValue(value);

                g2d.setColor(cellColor);
                g2d.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                // Optionally add text inside the cells
                g2d.drawString(String.valueOf(value), j * CELL_SIZE + CELL_SIZE / 2, i * CELL_SIZE + CELL_SIZE / 2);
            }
        }

        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Color getColorForValue(int value) {
        if (value == 0) {
            return Color.WHITE;
        }

        // Normalize the value to the range [0, 1]
        double normalizedValue = value / 100.0;

        // Adjusted RGB values for color variation
        int red = (int) (255 * Math.sin(2 * Math.PI * normalizedValue + 0.5) * 0.5 + 0.5);
        int green = (int) (255 * Math.sin(2 * Math.PI * normalizedValue) * 0.5 + 0.5);
        int blue = (int) (255 * Math.sin(2 * Math.PI * normalizedValue - 0.5) * 0.5 + 0.5);

        // Set HSB values to create distinct colors with varied hues and brightness
        float hue = (float) normalizedValue;
        float saturation = 0.8f;
        float brightness = (float) (1.0 - 0.2 * Math.abs(normalizedValue - 0.5));

        // Convert RGB to HSB to get a color with the desired hue and brightness
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        hsb[0] = hue;
        hsb[2] = brightness;

        // Convert HSB back to RGB
        return Color.getHSBColor(hsb[0], saturation, hsb[2]);
    }


}

