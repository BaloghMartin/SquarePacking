package com.company;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class algoIranyito2 {
    private static int optV2;
    private static int length;
    private static final int populationSize = 10;
    private static final int numGenerations = 10;
    private static final double initialMutationRate = 0.5;
    private static final double finalMutationRate = 0.1;
    private static final int elitePercentage = 10;
    private static final Random random = new Random();
    private static final Map<List<Integer>, Integer> codeToIndexMap = new HashMap<>();
    private static List<List<Integer>> population = new ArrayList<>();
    private static List<List<Integer>> eliteGenes = new ArrayList<>();
    private static double eliteFitness = Double.MAX_VALUE;
    private static int generation = 1;
    private static double previousBestFitness = Double.MAX_VALUE;
    private static int n;

    public static List<Integer> generateGeneticCodeWithN(int len, int n) {
        List<Integer> geneticCode = new ArrayList<>();
        List<Integer> availableNumbers = new ArrayList<>();

        // Initialize available numbers with all numbers from 1 to len
        for (int i = 1; i <= len; i++) {
            availableNumbers.add(i);
        }

        for (int i = 0; i < len; i++) {
            if (i < n && !availableNumbers.isEmpty()) {
                // Add a random number from available numbers to the gene
                int randomIndex = random.nextInt(availableNumbers.size());
                int randomNumber = availableNumbers.remove(randomIndex);
                geneticCode.add(randomNumber);
            } else {
                // If 'n' elements have already been added, fill the rest with random numbers
                int randomGene = generateRandomGene(availableNumbers);
                geneticCode.add(randomGene);
            }
        }

        // Shuffle the genetic code to create permutations
        Collections.shuffle(geneticCode);

        // If len is greater than n, add the additional numbers from n+1 to len
        if (len > n) {
            for (int i = n + 1; i <= len; i++) {
                int randomIndex = random.nextInt(len); // Add the number at a random place
                geneticCode.add(randomIndex, i);
            }
        }

        return geneticCode;
    }

    public static List<Double> evaluatePopulation(List<List<Integer>> population) {
        List<Double> fitnessScores = new ArrayList<>();
        codeToIndexMap.clear(); // Clear the map

        for (int i = 0; i < population.size(); i++) {
            List<Integer> geneticCode = population.get(i);
            codeToIndexMap.put(geneticCode, i); // Map genetic code to its index
            System.out.println(geneticCode.toString());
            int[][] solution = Spiral.placeSquaresAndReturnArray(geneticCode);
            double score = (double) solution.length / optV2;
            fitnessScores.add(score);
        }
        return fitnessScores;
    }



    public static List<Integer> main(int len) {
        algoIranyito2.n = len;
        if (len <= 0 || populationSize <= 0) {
            return null;
        }
        if (generation == 1) {
            // If it's the first generation, initialize the population
            optV2 = (int) Math.ceil(Math.sqrt(len * (len + 1) * (2 * len + 1) / 6));
            length = len; // Set the length of the gene

            // Generate the initial population
            for (int i = population.size(); i < populationSize; i++) {
                List<Integer> geneticCode = generateGeneticCodeWithN(len, len); // Add 'len' to each gene
                population.add(geneticCode);
            }

            // Debug: Print initial population
            System.out.println("Initial Population:");
            for (List<Integer> geneticCode : population) {
                //  System.out.println(geneticCode);
            }
        }

        List<Double> fitnessScores = evaluatePopulation(population);
        List<Double> generationAverages = new ArrayList<>();

        while (generation <= numGenerations) {
            // Your evolution logic here

            // Debug: Print population for this generation
            System.out.println("Generation " + generation + " - Population:");
            for (List<Integer> geneticCode : population) {
                System.out.println(geneticCode);
            }

            generation++;
        }

        if (!eliteGenes.isEmpty() && eliteGenes.get(0).size() == length) {
            // Debug: Print the best gene (elite)
            System.out.println("Best Gene (Elite): " + eliteGenes.get(0));
            return eliteGenes.get(0);
        } else {
            return null;
        }
    }




    // Function to perform mutation by swapping two random numbers in the gene
    public static void mutate(List<Integer> gene) {
        int index1 = random.nextInt(gene.size());
        int index2 = random.nextInt(gene.size());
        Collections.swap(gene, index1, index2);
    }

    // Updated evolvePopulation method
    // Updated evolvePopulation method
    public static List<List<Integer>> evolvePopulation(List<List<Integer>> population, List<Double> fitnessScores) {
        // Create a new population to store the modified genes
        List<List<Integer>> newPopulation = new ArrayList<>();

        for (List<Integer> gene : population) {
            if (gene.size() < length) {
                // If the gene size is less than the target length, add 'n' sized elements to it
                List<Integer> newGene = new ArrayList<>(gene);
                for (int i = 0; i < n; i++) {
                    int randomGene = generateRandomGene(newGene);
                    newGene.add(randomGene);
                }
                newPopulation.add(newGene);
            } else {
                // If the gene size is equal to or greater than the target length, keep the gene as is
                newPopulation.add(gene);
            }
        }

        // Replace the old population with the new one
        population = newPopulation;

        return population;
    }

    public static int selectParentByFitness(List<Double> fitnessScores, double totalFitness) {
        double randomValue = random.nextDouble() * totalFitness;
        double cumulativeFitness = 0;

        for (int i = 0; i < fitnessScores.size(); i++) {
            cumulativeFitness += fitnessScores.get(i);

            if (cumulativeFitness >= randomValue) {
                return i;
            }
        }
        return fitnessScores.size() - 1;
    }

    public static List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (length <= 0) {
            throw new IllegalStateException("Invalid length");
        }

        List<Integer> offspring = new ArrayList<>(length);

        // Randomly choose a crossover point
        int crossoverPoint = random.nextInt(length);

        // Include genes from the elite parents
        for (int i = 0; i < crossoverPoint; i++) {
            offspring.add(parent1.get(i));
        }

        // Include genes from parent2 that are not already in the offspring
        for (int i = 0; i < length; i++) {
            int gene = parent2.get(i);
            if (!offspring.contains(gene)) {
                offspring.add(gene);
            }
        }

        // Fill the remaining slots with random genes
        while (offspring.size() < length) {
            int randomGene = generateRandomGene(offspring);
            offspring.add(randomGene);
        }

        return offspring;
    }

    private static int generateRandomGene(List<Integer> excludeList) {
        int gene;
        do {
            gene = random.nextInt(length) + 1;
        } while (excludeList.contains(gene));
        return gene;
    }

    public static void printGenerationInfo(int generation, double bestFitness, double averageFitness, List<Double> fitnessScores) {
        System.out.println("Generation " + generation + " - Best Fitness: " + bestFitness + " - Average Fitness: " + averageFitness);
    }
}
