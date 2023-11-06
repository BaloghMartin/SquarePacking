package com.company;

import java.util.*;

public class algoIranyito2 {
    static int optV2;
    static int length;
    static int populationSize = 100;
    static int numGenerations = 1000;
    static double mutationRate = 0.3;
    static List<List<Integer>> eliteGenes = new ArrayList<>();
    static double eliteFitness = Double.MAX_VALUE;
    static int generation = 1;
    static double previousBestFitness = Double.MAX_VALUE;

    public static List<Integer> generateGeneticCode() {
        List<Integer> geneticCode = new ArrayList<>();

        for (int i = 1; i <= length; i++) {
            geneticCode.add(i);
        }

        // Shuffle the genetic code to create permutations
        Collections.shuffle(geneticCode);

        return geneticCode;
    }

    public static List<Double> evaluatePopulation(List<List<Integer>> population) {
        List<Double> fitnessScores = new ArrayList<>();
        for (List<Integer> geneticCode : population) {
            //double score = Spiral.placeSquares(geneticCode);
            int[][] solution;

            solution = Spiral.placeSquaresAndReturnArray(geneticCode);
            double score=(double)solution.length/optV2;
            fitnessScores.add(score);

        }
        return fitnessScores;
    }

    public static List<Integer> main(int len) {
        if (len <= 0 || populationSize <= 0) {
            return null;
        }
        int generation = 1; // Reset the generation counter
        eliteFitness = Double.MAX_VALUE; // Reset eliteFitness
        eliteGenes.clear(); // Clear eliteGenes
        previousBestFitness = Double.MAX_VALUE; // Reset previousBestFitness
        optV2 = (int) Math.ceil(Math.sqrt(len * (len + 1) * (2 * len + 1) / 6));
        //System.out.println(len);
        //System.out.println(optV2);
        algoIranyito2.length = len;

        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> geneticCode = generateGeneticCode();
            if (geneticCode.size() != length) {
                continue;
            }
            population.add(geneticCode);
        }

        List<Double> fitnessScores = evaluatePopulation(population);
        List<Double> generationAverages = new ArrayList<>();

        while (generation <= numGenerations) {
            // After evolving the population
            population = evolvePopulation(population, fitnessScores);

            // Recalculate fitness scores for the updated population
            fitnessScores = evaluatePopulation(population);

            // Rest of your code remains the same...
            double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();
            double averageFitness = totalFitness / populationSize;
            generationAverages.add(averageFitness);

            for (List<Integer> geneticCode : population) {
                double score = fitnessScores.get(population.indexOf(geneticCode));

                if (score < eliteFitness) {
                    eliteFitness = score;
                    eliteGenes.clear();
                    eliteGenes.add(new ArrayList<>(geneticCode));

                    if (eliteGenes.get(0).size() != length) {
                        eliteGenes.set(0, generateGeneticCode());
                    }
                }

                if (score == 1.0) {
                    return eliteGenes.get(0);
                }
            }

            if (generation % 1 == 0) {
                printGenerationInfo(generation, eliteFitness, averageFitness, fitnessScores);

            }
            generation++;
        }

        if (eliteGenes.get(0).size() != length) {
            eliteGenes.set(0, generateGeneticCode());
        }

        return eliteGenes.get(0);
    }

    // Function to perform mutation by swapping two random numbers in the gene
    public static void mutate(List<Integer> gene) {
        int index1 = new Random().nextInt(gene.size());
        int index2 = new Random().nextInt(gene.size());
        Collections.swap(gene, index1, index2);
    }

    // Updated evolvePopulation method
    // Updated evolvePopulation method
    public static List<List<Integer>> evolvePopulation(List<List<Integer>> population, List<Double> fitnessScores) {
        if (populationSize < 10) {
            // Handle the case when the population size is too small
            System.out.println("Population size is too small for evolution.");
            return population;
        }

        double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();

        // Create a copy of the population for sorting
        List<List<Integer>> sortedPopulation = new ArrayList<>(population);

        // Sort the sortedPopulation based on fitness scores using a custom Comparator
        sortedPopulation.sort((code1, code2) -> {
            int index1 = population.indexOf(code1);
            int index2 = population.indexOf(code2);
            double fitness1 = fitnessScores.get(index1);
            double fitness2 = fitnessScores.get(index2);
            return Double.compare(fitness1, fitness2);
        });

        int bestCount = populationSize / 30; // Best 10%
        bestCount = Math.min(bestCount, populationSize); // Ensure bestCount doesn't exceed population size
        int worstCount = populationSize / 30; // Worst 10%
        worstCount = Math.min(worstCount, populationSize); // Ensure worstCount doesn't exceed population size
        int middleCount = populationSize - bestCount - worstCount; // Middle 80%

        List<List<Integer>> newPopulation = new ArrayList<>();

        // Include the best gene directly
        if (!eliteGenes.isEmpty()) {
            newPopulation.add(new ArrayList<>(eliteGenes.get(0)));
            bestCount--; // Decrement the count of best genes to account for the elite gene
        }

        // Include parents from the best 10% for crossover
        for (int i = 0; i < bestCount; i++) {
            int parentIndex1 = selectParentByFitness(fitnessScores, totalFitness);
            int parentIndex2 = selectParentByFitness(fitnessScores, totalFitness);

            if (parentIndex1 < fitnessScores.size() && parentIndex2 < fitnessScores.size()) {
                List<Integer> parent1 = new ArrayList<>(sortedPopulation.get(parentIndex1));
                List<Integer> parent2 = new ArrayList<>(sortedPopulation.get(parentIndex2));

                // Perform crossover
                List<Integer> offspring1 = crossover(parent1, parent2);
                List<Integer> offspring2 = crossover(parent2, parent1);

                newPopulation.add(offspring1);
                newPopulation.add(offspring2);
            } else {
                // Handle the case where the index is out of bounds
                // You can choose to add a random genetic code or handle it differently.
                newPopulation.add(generateGeneticCode());
                newPopulation.add(generateGeneticCode());
            }
        }

        // Perform crossover for the middle 80%
        for (int i = 0; i < middleCount; i += 2) {
            if (i + 1 < bestCount + middleCount) {
                int parentIndex1 = selectParentByFitness(fitnessScores, totalFitness);
                int parentIndex2 = selectParentByFitness(fitnessScores, totalFitness);

                if (parentIndex1 < fitnessScores.size() && parentIndex2 < fitnessScores.size()) {
                    List<Integer> parent1 = new ArrayList<>(sortedPopulation.get(parentIndex1));
                    List<Integer> parent2 = new ArrayList<>(sortedPopulation.get(parentIndex2));

                    // Perform crossover
                    List<Integer> offspring1 = crossover(parent1, parent2);
                    List<Integer> offspring2 = crossover(parent2, parent1);

                    newPopulation.add(offspring1);
                    newPopulation.add(offspring2);
                } else {
                    // Handle the case where the index is out of bounds
                    // You can choose to add a random genetic code or handle it differently.
                    newPopulation.add(generateGeneticCode());
                    newPopulation.add(generateGeneticCode());
                }
            } else {
                // If there's an odd number of parents, add one of them directly
                int parentIndex = i + bestCount;
                if (parentIndex < sortedPopulation.size()) {
                    newPopulation.add(new ArrayList<>(sortedPopulation.get(parentIndex)));
                } else {
                    // If parentIndex is out of bounds, add a new random genetic code
                    newPopulation.add(generateGeneticCode());
                }
            }
        }

        // Introduce mutation
        for (int i = 0; i < populationSize-1; i++) {
            if (Math.random() < mutationRate) {
                mutate(newPopulation.get(i)); // Call the mutate function
            }
        }

        // Randomly generate new individuals for the remaining slots
        while (newPopulation.size() < populationSize) {
            newPopulation.add(generateGeneticCode());
        }

        return newPopulation;
    }

    public static int selectParentByFitness(List<Double> fitnessScores, double totalFitness) {
        double randomValue = Math.random() * totalFitness;
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
        int crossoverPoint = new Random().nextInt(length);

        // Swap genes from the end of parent1 with the beginning of parent2
        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                offspring.add(parent1.get(i));
            } else {
                // If we've reached the end of parent1's genes, start adding from parent2
                int gene = parent2.get(i);
                if (!offspring.contains(gene)) {
                    offspring.add(gene);
                } else {
                    // If the gene is already in the offspring, find the first available gene
                    for (int j = 1; j <= length; j++) {
                        if (!offspring.contains(j)) {
                            offspring.add(j);
                            break;
                        }
                    }
                }
            }
        }

        return offspring;
    }

    public static void printGenerationInfo(int generation, double bestFitness, double averageFitness, List<Double> fitnessScores) {
        //System.out.println("Generation " + generation + " - Best Fitness: " + bestFitness + " - Average Fitness: " + averageFitness);

        //System.out.println("Fitness Values:");
        for (int i = 0; i < populationSize; i++) {
            //System.out.println("Individual " + i + ": " + fitnessScores.get(i));
        }
    }
}