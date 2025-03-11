package com.company;

import java.util.*;

public class DeterminedBB {

    Visualizer arrayVisualization = new Visualizer(new int[0][0]);
        private Map<Integer, List<Integer>> bestGenes;
        private Map<Integer, Integer> bestFitnessValues;
        private int N;
        private int Counter=0;
    public DeterminedBB(int N) {
        this.N=N;
        LEFT = new ArrayList<>(); // Create a new ArrayList for LEFT
        DONE = new ArrayList<>();  // Create a new ArrayList for DONE
        for (int i = 0; i < N; i++) {
            //System.out.println(i);
            this.LEFT.add(N - i);
        }
        DETERMINE();

    }

    private List<Integer> LEFT;
        private List<Integer> DONE;
        int i=0;
        public Integer getBestFittness() {
            return BestFittness;
        }

        private Integer BestFittness=Integer.MAX_VALUE;
        List<Integer> Bestgene;
        class Gene {
        List<Integer> gene;
        int N;
        int fitness;

        public Gene(List<Integer> gene, int N) {
            this.N = N;
            this.gene = gene;
            this.fitness = Spiral.placeSquaresAndReturnSize(gene, N, BestFittness);
            Counter++;
            //System.out.println(Counter);
            if (fitness<BestFittness){BestFittness=fitness; Bestgene=gene;System.out.println(fitness);
                arrayVisualization.updateVisualization(Spiral.placeSquaresAndReturnArray(Bestgene));
                arrayVisualization.saveVisualizationAsImage(System.getProperty("user.home") + "/Desktop/array_visualization.png");



            }

            //arrayVisualization.updateVisualization(Spiral.placeSquaresAndReturnArray(Bestgene));
            //arrayVisualization.saveVisualizationAsImage(System.getProperty("user.home") + "/Desktop/array_visualization.png");
        }
        }

        public void DETERMINE(){


            List<List<Integer>> arrays = generateDescendingLists( LEFT.stream().mapToInt(Integer::intValue).toArray(),1);
            for (List<Integer> element : arrays) {

                element.addAll(0, DONE);
                //System.out.println(element.toString());
                //System.out.println();
                new Gene(element, element.size());
            }
            DONE.add(Bestgene.get(i));
            LEFT.remove(Bestgene.get(i));
            //helloooo
            i++;
            System.out.println("branchinganboundin");
            if(i>10){
                return;}
            if(i!=N){DETERMINE();}

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

    private static void generateCombinations(List<List<Integer>> result, List<Integer> current, List<Integer> remaining, int fixedCount) {
        if (current.size() == fixedCount) {
            // Add the current fixed elements and remaining elements in descending order
            List<Integer> fullList = new ArrayList<>(current);
            fullList.addAll(remaining);
            result.add(fullList);
            return;
        }

        // Generate combinations by choosing each element in remaining as the next fixed element
        for (int i = 0; i < remaining.size(); i++) {
            List<Integer> newCurrent = new ArrayList<>(current);
            newCurrent.add(remaining.get(i));

            List<Integer> newRemaining = new ArrayList<>(remaining);
            newRemaining.remove(i);

            generateCombinations(result, newCurrent, newRemaining, fixedCount);
        }
    }


    public static void main(String[] args) {
            for(int i=78; i<101; i++) {
                DeterminedBB determinedBB = new DeterminedBB(i);

                System.out.println(determinedBB.Bestgene.toString() + " " + determinedBB.BestFittness.toString());
                //arrayVisualization.saveVisualizationAsImage(System.getProperty("user.home") + "/Desktop/array_visualization.png");

            }
        }
}





