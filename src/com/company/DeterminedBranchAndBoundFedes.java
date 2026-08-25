package com.company;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeterminedBranchAndBoundFedes {

    static Visualizer arrayVisualization = new Visualizer(new int[0][0]);
    private Map<Integer, List<Integer>> bestGenes;
    private Map<Integer, Integer> bestFitnessValues;
    private int N;
    private int Counter = 0;

    public DeterminedBranchAndBoundFedes(int N) {
        this.N = N;
        LEFT = new ArrayList<>(); // Create a new ArrayList for LEFT
        DONE = new ArrayList<>(); // Create a new ArrayList for DONE
        for (int i = 0; i < N; i++) {
            // System.out.println(i);
            this.LEFT.add(N - i);
        }
        // ez ide azért kell hogy legyen egy baseline, hogy első körbe ne számolja ki
        // mindennek az értékét
        Gene elso = new Gene(LEFT, N, Integer.MIN_VALUE);
        elso.calculateFitness();
        BestFittness = elso.getFitness();

        Bestgene = elso.gene;
        // System.out.println(Bestgene.toString()+" "+BestFittness.toString());
        DETERMINE();

    }

    private List<Integer> LEFT;
    private List<Integer> DONE;
    int i = 0;

    public Integer getBestFittness() {
        return BestFittness;
    }

    private Integer BestFittness = Integer.MIN_VALUE;
    static List<Integer> Bestgene;

    class Gene {

        private List<Integer> gene; // The list of integers representing the gene.
        private int N; // Gene size.
        private int fitness; // Fitness value.
        private static int bestFitness; // Shared "BestFittness".

        // Constructor without fitness calculation.
        public Gene(List<Integer> gene, int N, int BestFittness) {
            this.gene = new ArrayList<>(gene); // Create a defensive copy.
            this.N = N;
            Gene.bestFitness = BestFittness; // Assume a static/shared best fitness value.
            this.fitness = -1; // Default fitness value indicating "not calculated".
        }

        // Method to calculate fitness (can be parallelized).
        public void calculateFitness() {
            this.fitness = Spiral_fedes.placeSquaresAndReturnArray(gene, bestFitness).length;
            // System.out.println(this.fitness);
        }

        // Getter for fitness.
        public int getFitness() {
            if (fitness == -1) {
                throw new IllegalStateException("Fitness not calculated yet!");
            }
            return fitness;
        }
    }

    public void DETERMINE() {

        double result = 200.0 / (40 + N); // Converts to floating-point division
        int roundedResult = (int) Math.ceil(result); // Rounds up
        // int roundedResult = 4;
        // System.out.println(roundedResult);

        List<List<Integer>> arrays = generateDescendingLists(LEFT.stream().mapToInt(Integer::intValue).toArray(),
                roundedResult);
        // Create a thread-safe list for storing Gene objects
        List<Gene> geneList = new CopyOnWriteArrayList<>();

        // Part 1: Generate Gene objects sequentially
        arrays.forEach(element -> {
            element.addAll(0, DONE); // Add elements from DONE
            // System.out.println(element.toString());
            Gene gene = new Gene(element, element.size(), BestFittness); // Create Gene without calculating fitness.
            // System.out.println(gene.gene);
            geneList.add(gene); // Add to thread-safe list
        });

        // Part 2: Parallelize fitness calculation
        geneList.parallelStream().forEach(Gene::calculateFitness);

        for (Gene gene : geneList) {
            // System.out.println(gene.gene.toString() + " " + gene.fitness);
            int fitness = gene.getFitness(); // Assuming Gene has a getFitness() method
            if (fitness > BestFittness) {
                // System.out.println( gene.gene.toString() + " "+BestFittness+ "-rol " +
                // fitness+"-ra");
                BestFittness = fitness;
                Bestgene = gene.gene;
                // System.out.println(fitness);
                arrayVisualization.updateVisualization(Spiral_fedes.placeSquaresAndReturnArray(Bestgene, BestFittness));
                arrayVisualization
                        .saveVisualizationAsImage(System.getProperty("user.home") + "/Desktop/array_visualization.png");
            }
        }

        DONE.add(Bestgene.get(i));
        LEFT.remove(Bestgene.get(i));
        // helloooo
        i++;
        // System.out.println("branchinganboundin");
        // ITT KELL ÁTÍRNI HOGY MENNYI ELEM MÉLYRE MENJEN
        // if(i>500/N){return;}
        if (i == 1) {
            return;
        }
        if (i != N) {
            DETERMINE();
        }

    }

    public static List<List<Integer>> generateDescendingLists(int[] numbers, int fixedCount) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> sortedList = new ArrayList<>();

        // Convert array to list and sort in descending order
        for (int num : numbers) {
            sortedList.add(num);
        }
        Collections.sort(sortedList, Collections.reverseOrder());

        // Start recursive generation with fixedCount
        generateCombinations(result, new ArrayList<>(), sortedList, fixedCount);
        return result;
    }

    private static void generateCombinations(List<List<Integer>> result, List<Integer> current, List<Integer> remaining,
            int fixedCount) {
        if (current.size() == fixedCount) {
            // Add the current fixed elements and remaining elements in descending order
            List<Integer> fullList = new ArrayList<>(current);
            fullList.addAll(remaining);
            result.add(fullList);
            return;
        }

        // Generate combinations by choosing each element in remaining as the next fixed
        // element
        for (int i = 0; i < remaining.size(); i++) {
            List<Integer> newCurrent = new ArrayList<>(current);
            newCurrent.add(remaining.get(i));

            List<Integer> newRemaining = new ArrayList<>(remaining);
            newRemaining.remove(i);

            generateCombinations(result, newCurrent, newRemaining, fixedCount);
        }
    }

    public static void main(String[] args) {
        for (int i = 10; i < 20; i++) {
            DeterminedBranchAndBoundFedes determinedBB = new DeterminedBranchAndBoundFedes(i);

            System.out.println(determinedBB.Bestgene.size() + " " + determinedBB.BestFittness.toString() + " "
                    + determinedBB.Bestgene.toString());
            arrayVisualization.updateVisualization(Spiral_fedes.placeSquaresAndReturnArray(Bestgene));
            arrayVisualization
                    .saveVisualizationAsImage(System.getProperty("user.home") + "/Desktop/array_visualization.png");

        }
    }
}
 