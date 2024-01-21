package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Visualizer extends JFrame {

    private static Visualizer instance;

    private static final int CELL_SIZE = 30; // Size of each cell in pixels
    private int[][] dataArray;
    private double zoomFactor = 1.0;

    private ArrayPanel arrayPanel; // Reference to the ArrayPanel for updates

    Visualizer(int[][] dataArray) {
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

        // Method to update the visualization with new data
        public void updateVisualization(int[][] newDataArray) {
            dataArray = newDataArray;
            repaint();
        }
    }

    private void zoomIn() {
        if (zoomFactor < 2.0) {
            zoomFactor += 0.1;
            arrayPanel.repaint(); // Repaint the ArrayPanel
        }
    }

    private void zoomOut() {
        if (zoomFactor > 0.1) {
            zoomFactor -= 0.1;
            arrayPanel.repaint(); // Repaint the ArrayPanel
        }
    }

    // Method to update the visualization with new data
    public void updateVisualization(int[][] newDataArray) {
        arrayPanel.updateVisualization(newDataArray);
    }


}
