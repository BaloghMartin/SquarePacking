package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



class algoIranyito {

    static int length;
    static int populationSize = 200;
    static int numGenerations = 200;
    static double mutationRate = 0.8;
    static List<Integer> eliteGeneticCode = null;
    static double eliteFitness = Double.MAX_VALUE;


    public static List<Integer> generateGeneticCode() {
        List<Integer> geneticCode = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            // Generate random numbers between 1 and 4
            int randomNumber = random.nextInt(8) + 1;
            geneticCode.add(randomNumber);
        }

        // Ensure the generated genetic code has the correct length
        if (geneticCode.size() != length) {
            throw new IllegalStateException("Generated genetic code length is incorrect");
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
        //System.out.println(geneticCode.toString());
        double score = FirstFit.placeSquares(length,geneticCode);

        // Lower score is considered better
        return score;
    }

    public static List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (length <= 0) {
            throw new IllegalStateException("Invalid length");
        }

        int crossoverPoint = Math.min(length / 2, Math.min(parent1.size(), parent2.size()));
        List<Integer> offspring = new ArrayList<>(length);

        // Copy the first part of parent1
        offspring.addAll(parent1.subList(0, crossoverPoint));

        // Copy the remaining part from parent2
        if (crossoverPoint < length) {
            offspring.addAll(parent2.subList(crossoverPoint, Math.min(length, parent2.size())));
        }

        // If offspring size is less than length, pad it with random values
        while (offspring.size() < length) {
            offspring.add(new Random().nextInt(4) + 1);
        }

        // If offspring size is greater than length, truncate it
        if (offspring.size() > length) {
            offspring = offspring.subList(0, length);
        }

        return offspring;
    }
    /*
    public static List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (length <= 0) {
            throw new IllegalStateException("Invalid length");
        }

        int crossoverPoint = Math.min(length / 2, Math.min(parent1.size(), parent2.size()));
        List<Integer> offspring = new ArrayList<>(length);

        // Copy genes from both parents in pairs
        for (int i = 0; i < length; i++) {
            if (i % 4 < 2) {
                offspring.add(parent1.get(i));
            } else {
                offspring.add(parent2.get(i));
            }
        }

        return offspring;
    }
*/



    public static void mutate(List<Integer> geneticCode) {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < mutationRate) {
                // Mutate the gene with a small probability
                geneticCode.set(i, random.nextInt(4) + 1);
            }
        }
    }

    public static double main(int len) {
        if (len <= 0 || populationSize <= 0) {
            // Handle invalid input, e.g., return an error code or throw an exception
            return -1; // or any other suitable value
        }

        algoIranyito.length = len; // Set the length here

        // Initialize the population with random genetic codes
        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> geneticCode = generateGeneticCode();
            if (geneticCode.size() != length) {
                // Handle the case where generated genetic code has an incorrect length
                continue; // Skip the genetic code and do not add it to the population
            }
            population.add(geneticCode);
        }

        double bestFitness = 0;
        for (int generation = 1; generation <= numGenerations; generation++) {
            // Evaluate the fitness of each individual in the population
            List<Double> fitnessScores = new ArrayList<>();
            for (List<Integer> geneticCode : population) {
                double score = evaluateGeneticCode(geneticCode);
                fitnessScores.add(score);

                // Check for a perfect score
                if (score == 1.0) {
                    bestFitness = 1.0;
                    return bestFitness; // Return immediately
                }
            }

            // Preserve the elite individual from the current generation
            for (List<Integer> geneticCode : population) {
                double score = evaluateGeneticCode(geneticCode);
                if (score < eliteFitness) {
                    eliteFitness = score;
                    eliteGeneticCode = new ArrayList<>(geneticCode);
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

            // Print information about the current generation
            bestFitness = fitnessScores.stream().min(Double::compare).get();

            double averageFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            //System.out.println("Generation " + generation + " - Best Fitness: " + bestFitness + " - Average Fitness: " + averageFitness);
            int mutatedOffspringCount = 0;
            for (List<Integer> offspring : newPopulation) {
                if (!offspring.equals(population.get(0))) {
                    mutatedOffspringCount++;
                }
            }
            //System.out.println("Generation " + generation + " - Mutated Offspring Count: " + mutatedOffspringCount);
            // Check for a perfect score
            if (bestFitness == 1.0) {
                return bestFitness; // Return immediately
            }
        }
        return bestFitness;
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
