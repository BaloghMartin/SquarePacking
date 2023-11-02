package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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

            while (n <= 200) {
                double opt = 0;
                int optSeg = n;
                while (true) {
                    opt = opt + (optSeg * optSeg);
                    optSeg--;
                    if (optSeg == 0) {
                        break;
                    }
                }
                double optV = Math.sqrt(opt);
                int optV2 = (int) Math.round(optV);
                double result = algoIranyito.main(n);
                int optV2Result = (int) Math.round(optV2 * result);

                // Write to the file
                writer.println(n + " " + result + " " + optV2 + " " + optV2Result);
                writer.flush(); // Flush the writer to save the content immediately

                // Print to the console
                System.out.println(n + " " + result + " " + optV2 + " " + optV2Result);
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
