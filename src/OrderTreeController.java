/*import com.company.OrderTree;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OrderTreeController {
    private static final String BEST_SOLUTIONS_FILE_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "best_solutions.txt";
    private static final String LAST_TESTED_GENE_FILE_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "last_tested_gene.txt";

    private static final long TIME_LIMIT_PER_N = 1 * 60 * 1000; // 1 minute in milliseconds

    public static Map<Integer, OrderTree> loadState() {
        Map<Integer, OrderTree> orderTrees = new HashMap<>();
        // No state file to load, return empty map
        return orderTrees;
    }

    public static void saveBestSolutions(Map<Integer, List<Integer>> bestSolutions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BEST_SOLUTIONS_FILE_PATH))) {
            for (Map.Entry<Integer, List<Integer>> entry : bestSolutions.entrySet()) {
                writer.println("n=" + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("Best solutions saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLastTestedGene(List<Integer> gene) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LAST_TESTED_GENE_FILE_PATH))) {
            for (int i = 0; i < gene.size(); i++) {
                writer.print(gene.get(i));
                if (i < gene.size() - 1) {
                    writer.print(" ");
                }
            }
            System.out.println("Last tested gene saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> loadLastTestedGene() {
        File file = new File(LAST_TESTED_GENE_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Last tested gene file created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                System.out.println("Last tested gene file is empty.");
                return null;
            }
            String[] geneValues = line.split(" ");
            List<Integer> lastTestedGene = new ArrayList<>();
            for (String value : geneValues) {
                lastTestedGene.add(Integer.parseInt(value));
            }
            System.out.println("Last tested gene loaded successfully.");
            return lastTestedGene;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map<Integer, OrderTree> orderTrees;
        Map<Integer, List<Integer>> bestSolutions = new HashMap<>();
        List<Integer> lastTestedGene;

        // Load previous state if available (not used in this version)
        orderTrees = loadState();
        // Load last tested gene
        lastTestedGene = loadLastTestedGene();

        Timer timer = new Timer();

        // Initialize or update order trees for each n
        for (int n = 10; n <= 100; n++) {
            // No state to load, create new OrderTree instances
            int targetFitness;
            if (n <= 56) {
                int[] targetFitnessValues = {
                        1, 3, 5, 7, 9, 11, 13, 15, 18, 21, 24, 27, 30, 33, 36, 39,
                        43, 47, 50, 54, 58, 62, 66, 71, 75, 80, 84, 89, 93, 98,
                        103, 108, 113, 118, 123, 128, 133, 139, 144, 150, 155, 161,
                        166, 172, 178, 184, 190, 196, 202, 208, 214, 221, 227, 233, 240, 246
                };
                targetFitness = targetFitnessValues[n - 1];
            } else {
                targetFitness = (int) Math.ceil(Math.sqrt(((n * (n + 1)) * ((2 * n) + 1)) / 6));
            }

            OrderTree orderTree = new OrderTree(n, targetFitness);
            // Schedule a task to stop after 1 minute
            final int currentN = n;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Stopping for n=" + currentN + " after 1 minute.");
                    // No state to save
                    this.cancel(); // Stop the timer after 1 minute for this n
                }
            }, TIME_LIMIT_PER_N);
        }

        // Wait for the last task to finish before saving the state and best solutions
        try {
            Thread.sleep(TIME_LIMIT_PER_N + 1000); // Wait for 1 minute plus some additional time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Save last tested gene
        if (lastTestedGene != null) {
            saveLastTestedGene(lastTestedGene);
        }

        // Save best solutions
        saveBestSolutions(bestSolutions);
    }
}*/
