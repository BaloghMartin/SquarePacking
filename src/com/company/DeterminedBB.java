package com.company;

import java.util.*;

public class DeterminedBB {
        private Map<Integer, List<Integer>> bestGenes;
        private Map<Integer, Integer> bestFitnessValues;
        private int N;
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
            if (fitness<BestFittness){BestFittness=fitness; Bestgene=gene;}
        }
        }

        public void DETERMINE(){


            List<List<Integer>> arrays = generateDescendingLists( LEFT.stream().mapToInt(Integer::intValue).toArray());
            for (List<Integer> element : arrays) {

                element.addAll(0, DONE);
                //System.out.println(element.toString());
                //System.out.println();
                new Gene(element, element.size());
            }
            DONE.add(Bestgene.get(i));
            LEFT.remove(Bestgene.get(i));

            i++;

            if(i==10){return;}
            if(i!=N){DETERMINE();}

        }
    public static List<List<Integer>> generateDescendingLists(int[] numbers) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> sortedList = new ArrayList<>();

        // Convert array to list and sort in descending order
        for (int num : numbers) {
            sortedList.add(num);
        }
        Collections.sort(sortedList, Collections.reverseOrder());

        // Generate sublists with the first two elements fixed
        for (int i = 0; i < sortedList.size(); i++) {
            for (int j = 0; j < sortedList.size(); j++) {
                if (i != j) {
                    List<Integer> list = new ArrayList<>();
                    list.add(sortedList.get(i)); // First fixed element
                    list.add(sortedList.get(j)); // Second fixed element

                    // Add remaining elements in descending order, skipping the first two chosen elements
                    for (int k = 0; k < sortedList.size(); k++) {
                        if (k != i && k != j) {
                            list.add(sortedList.get(k));
                        }
                    }
                    result.add(list);
                }
            }
        }
        return result;
    }


    public static void main(String[] args) {
            for(int i=3; i<101; i++){
        DeterminedBB determinedBB = new DeterminedBB(i);
                System.out.println(determinedBB.Bestgene.toString()+" "+determinedBB.BestFittness.toString());
            }

    }
}





