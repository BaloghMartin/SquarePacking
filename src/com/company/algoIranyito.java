package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class algoIranyito {
    static int length;
    static int populationSize = 10000;
    static int numGenerations = 1000;
    static double mutationRate = 0.8;
    static List<Integer> eliteGeneticCode = null;
    static double eliteFitness = Double.MAX_VALUE;

    public static List<Integer> generateGeneticCode() {
        List<Integer> geneticCode = new ArrayList<>();
        Random random = new Random();

        geneticCode.add(1);  // The first element is always 1

        for (int i = 1; i < length; i++) {
            int randomNumber = random.nextInt(8) + 1;
            geneticCode.add(randomNumber);
        }

        return geneticCode;
    }

    public static List<Double> evaluatePopulation(List<List<Integer>> population) {
        List<Double> fitnessScores = new ArrayList<>();
        for (List<Integer> geneticCode : population) {
            double score = FirstFit.placeSquares(length, geneticCode);
            fitnessScores.add(score);
        }
        return fitnessScores;
    }

    public static List<Integer> main(int len) {
        if (len <= 0 || populationSize <= 0) {
            return null;
        }

        algoIranyito.length = len;

        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> geneticCode = generateGeneticCode();
            if (geneticCode.size() != length) {
                continue;
            }
            population.add(geneticCode);
        }

        List<Double> fitnessScores = evaluatePopulation(population);

        for (int generation = 1; generation <= numGenerations; generation++) {
            population = evolvePopulation(population, fitnessScores);

            for (List<Integer> geneticCode : population) {
                double score = fitnessScores.get(population.indexOf(geneticCode));

                if (score < eliteFitness) {
                    eliteFitness = score;
                    eliteGeneticCode = new ArrayList<>(geneticCode);

                    if (eliteGeneticCode.size() != length) {
                        eliteGeneticCode = generateGeneticCode();
                    }
                }

                if (score == 1.0) {
                    return eliteGeneticCode;
                }
                printAverageFitness(fitnessScores);
            }
        }

        if (eliteGeneticCode.size() != length) {
            eliteGeneticCode = generateGeneticCode();
        }

        return eliteGeneticCode;
    }

    public static List<List<Integer>> evolvePopulation(List<List<Integer>> population, List<Double> fitnessScores) {
        double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();

        // Create a copy of the population for sorting
        List<List<Integer>> sortedPopulation = new ArrayList<>(population);

        // Sort the sortedPopulation based on fitness scores
        sortedPopulation.sort((code1, code2) -> Double.compare(fitnessScores.get(population.indexOf(code1)), fitnessScores.get(population.indexOf(code2))));

        int bestCount = populationSize / 10; // Best 10%
        int worstCount = populationSize / 10; // Worst 10%
        int middleCount = populationSize - bestCount - worstCount; // Middle 80%

        List<List<Integer>> newPopulation = new ArrayList<>();

        for (int i = 0; i < bestCount; i++) {
            double selectionProbability = fitnessScores.get(population.indexOf(sortedPopulation.get(i))) / totalFitness;
            if (Math.random() < selectionProbability) {
                newPopulation.add(sortedPopulation.get(i));
            }
        }

        for (int i = bestCount; i < bestCount + middleCount; i += 2) {
            List<Integer> parent1 = sortedPopulation.get(i);
            List<Integer> parent2 = sortedPopulation.get(i + 1);
            List<Integer> offspring = crossover(parent1, parent2);
            mutate(offspring);
            newPopulation.add(offspring);
        }

        for (int i = bestCount + middleCount; i < populationSize; i++) {
            population.set(i, generateGeneticCode());
        }

        for (int i = 0; i < newPopulation.size(); i++) {
            population.set(i + bestCount, newPopulation.get(i));
        }

        return population;
    }

    public static int selectParent(List<Double> fitnessScores) {
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

    public static List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (length <= 0) {
            throw new IllegalStateException("Invalid length");
        }

        List<Integer> offspring = new ArrayList<>(length);

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
    public static void printAverageFitness(List<Double> fitnessScores) {
        double sum = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / fitnessScores.size();
        System.out.println("Generation Fitness Average: " + average);
    }
}
