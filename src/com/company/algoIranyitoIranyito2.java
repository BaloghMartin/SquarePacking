package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class algoIranyitoIranyito2 {
    public static void main(String[] args) {
        int n = 5;
        PrintWriter writer = null;

        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateTime = dateFormat.format(new Date());

        // Define the path to the desktop
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String fileName = "output_" + currentDateTime + ".txt";
        String filePath = desktopPath + File.separator + fileName;

        Visualizer arrayVisualization = new Visualizer(new int[0][0]); // Initialize with an empty array
        arrayVisualization.setVisible(true);  // Set the window to visible outside the loop

        try {
            writer = new PrintWriter(new FileWriter(filePath));

            while (n <= 40) {
                double opt = 0;
                int optV2 = (int) Math.ceil(Math.sqrt(n * (n + 1) * (2 * n + 1) / 6));
                int m = n + 1;
                List<Integer> result;
                int[][] solution;
                int giveUP = 0;
                List<Integer> resultTEMP;
                int[][] solutionTEMP;

                result = algoIranyito2MULTI.main(n);
                System.out.println(result.toString());
                solution = Spiral.placeSquaresAndReturnArray(result);

                arrayVisualization.updateVisualization(solution);

                String output = String.format("n = %d, lb: %d, Sol: %d, ar: %.5f", n, optV2, solution.length, (double) solution.length / optV2);
                System.out.println(output);
                output = String.format("n = %d, lb: %d, Sol: %d, genetic code: %s", n, optV2, solution.length, result.toString());
                writer.println(output);

                writer.flush(); // Flush the writer to save the content immediately
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
