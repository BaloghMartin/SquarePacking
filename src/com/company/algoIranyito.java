package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



class algoIranyito {

    static int length;
    static int populationSize = 500;
    static int numGenerations = 20;
    static double mutationRate = 0.8;
    static List<Integer> eliteGeneticCode = null;
    static double eliteFitness = Double.MAX_VALUE;


    public static List<Integer> generateGeneticCode() {
        List<Integer> geneticCode = new ArrayList<>();
        Random random = new Random();

        geneticCode.add(1);  // The first element is always 1

        for (int i = 1; i < length; i++) {
            // Generate random numbers between 1 and 8 (inclusive) for the remaining elements
            int randomNumber = random.nextInt(8) + 1;
            geneticCode.add(randomNumber);
        }

        return geneticCode;
    }


    public static double evaluateGeneticCode(List<Integer> geneticCode) {
        // You can implement your own criteria to evaluate the genetic code here.
        // For example, you can calculate a score based on how well it matches certain conditions.
        if (geneticCode.size() != length) {
            // Ignore genetic codes with incorrect lengths
            return Double.POSITIVE_INFINITY; // or any other suitable value
        }


        double score = FirstFit.placeSquares(length,geneticCode);

        // Lower score is considered better
        return score;
    }

    public static List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (length <= 0) {
            throw new IllegalStateException("Invalid length");
        }

        List<Integer> offspring = new ArrayList<>(length);

        // Copy genes from both parents in pairs
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0 && i < parent1.size()) {
                offspring.add(parent1.get(i));
            } else if (i % 2 != 0 && i < parent2.size()) {
                offspring.add(parent2.get(i));
            } else {
                offspring.add(new Random().nextInt(8) + 1);
            }
        }

        return offspring;
    }

    public static void mutate(List<Integer> geneticCode) {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < mutationRate) {
                geneticCode.set(i, random.nextInt(8) + 1);
            }
        }
    }



    public static List<Integer> main(int len) {
        if (len <= 0 || populationSize <= 0) {
            // Handle invalid input, e.g., return an error code or throw an exception
            return null; // or any other suitable value
        }

        algoIranyito.length = len; // Set the length here

        // Initialize the population with random genetic codes
        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> geneticCode = generateGeneticCode();
            if (geneticCode.size() != length) {
                // Handle the case where the generated genetic code has an incorrect length
                continue; // Skip the genetic code and do not add it to the population
            }
            population.add(geneticCode);
        }

        for (int generation = 1; generation <= numGenerations; generation++) {
            // Evaluate the fitness of each individual in the population
            List<Double> fitnessScores = new ArrayList<>();
            for (List<Integer> geneticCode : population) {
                double score = evaluateGeneticCode(geneticCode);
                fitnessScores.add(score);

                // Check for a perfect score
                if (score == 1.0) {
                    return geneticCode; // Return the genetic code with a perfect score
                }
            }

            // Preserve the elite individual from the current generation
            for (List<Integer> geneticCode : population) {
                double score = evaluateGeneticCode(geneticCode);
                if (score < eliteFitness) {
                    eliteFitness = score;
                    eliteGeneticCode = new ArrayList<>(geneticCode);

                    // Ensure eliteGeneticCode has the correct length
                    if (eliteGeneticCode.size() != length) {
                        eliteGeneticCode = generateGeneticCode(); // Generate a new random elite genetic code
                    }
                }
            }

            // Select parents based on fitness
            List<List<Integer>> parents = new ArrayList<>();
            for (int i = 0; i < populationSize / 2; i++) {
                int parent1 = selectParent(fitnessScores);
                int parent2 = selectParent(fitnessScores);
                parents.add(population.get(parent1));
                parents.add(population.get(parent2));
            }

            // Create the next generation
            List<List<Integer>> newPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i += 2) {
                List<Integer> parent1 = parents.get(i);
                List<Integer> parent2 = parents.get(i + 1);
                List<Integer> offspring = crossover(parent1, parent2);
                mutate(offspring);
                newPopulation.add(offspring);
            }

            // Replace the old population with the new generation, preserving the elite individual
            population = newPopulation;
            population.set(0, eliteGeneticCode);

            // Check for a perfect score
            if (eliteFitness == 1.0) {
                // Ensure eliteGeneticCode has the correct length
                if (eliteGeneticCode.size() != length) {
                    eliteGeneticCode = generateGeneticCode(); // Generate a new random elite genetic code
                }
                return eliteGeneticCode; // Return the genetic code with a perfect score
            }
        }

        // Ensure eliteGeneticCode has the correct length
        if (eliteGeneticCode.size() != length) {
            eliteGeneticCode = generateGeneticCode(); // Generate a new random elite genetic code
        }

        return eliteGeneticCode; // Return the best genetic code found
    }






    public static int selectParent(List<Double> fitnessScores) {
        // Select a parent based on roulette wheel selection
        double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = new Random().nextDouble() * totalFitness;
        double cumulativeFitness = 0;
        for (int i = 0; i < fitnessScores.size(); i++) {
            cumulativeFitness += fitnessScores.get(i);
            if (cumulativeFitness >= randomValue) {
                return i;
            }
        }
        return fitnessScores.size() - 1;
    }
}
