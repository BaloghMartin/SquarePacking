package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class algoIranyitoIranyito {
    public static void main(String[] args) {
        int n = 1;
        PrintWriter writer = null;

        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateTime = dateFormat.format(new Date());

        // Define the path to the desktop
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String fileName = "output_" + currentDateTime + ".txt";
        String filePath = desktopPath + File.separator + fileName;

        try {
            writer = new PrintWriter(new FileWriter(filePath));

            while (n <= 201) {
                double opt = 0;
                int optV2 = (int) Math.ceil(Math.sqrt(n * (n + 1) * (2 * n + 1) / 6));
                int m=n+1;
                List<Integer> result;
                int[][] solution;
                int giveUP=0;
                List<Integer> resultTEMP;
                int[][] solutionTEMP;
                while(true) {
                    giveUP++;
                    result = algoIranyito.main(n);
                    solution = FirstFit.placeSquaresAndReturnArray(n, result);

                   if (solution.length<=((int) Math.ceil(Math.sqrt(m * (m + 1) * (2 * m + 1) / 6)))){

                       break;}
                   if (giveUP==1){
                    //System.out.println("GIVEN UP");
                    break;}
                    }
                String output = String.format("n = %d, lb: %d, Sol: %d, ar: %.5f", n,optV2, solution.length, (double)solution.length/optV2);
                System.out.println(output);
                output = String.format("n = %d, lb: %d, Sol: %d, genetic code: %s", n,optV2, solution.length, result.toString());
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
