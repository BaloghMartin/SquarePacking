package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class Node {
    List<Integer> gene;
    List<Node> children;
    int N;
    int fitness;

    public Node(List<Integer> gene, int fitness, int N) {
        this.N = N;
        this.gene = gene;
        this.children = new ArrayList<>();
        this.fitness = fitness;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isLeaf() {
        return gene.size() == N;
    }
}

class OrderTree {
    public Node root;
    private List<Integer> bestGene;
    private int bestFitness;
    private int targetFitness;
    private boolean terminate = false; // Flag to indicate termination
    private boolean timerTermination = false; // Flag to indicate timer termination
    private long startTime; // Start time of the OrderTree

    public OrderTree(int N, int targetFitness) {
        root = new Node(new ArrayList<>(), 0, N);
        bestGene = new ArrayList<>();
        bestFitness = Integer.MAX_VALUE;
        this.targetFitness = targetFitness;
        startTime = System.currentTimeMillis(); // Record the start time
        generatePermutations(root, new boolean[N + 1], N);
        System.out.println("Best Gene: " + bestGene + ", Best Fitness: " + bestFitness);
    }

    public List<Integer> getBestGene() {
        return bestGene;
    }

    public int getBestFitness() {
        return bestFitness;
    }

    private void generatePermutations(Node node, boolean[] used, int remaining) {
        long elapsedTime = System.currentTimeMillis() - startTime;

        if (node.isLeaf()) {
            int fitness = calculateFitness(node.gene, node.N);
            node.fitness = fitness;

            List<Integer> reversedGene = new ArrayList<>(node.gene);
            Collections.reverse(reversedGene);

            if (node.fitness < bestFitness) {
                bestGene = new ArrayList<>(node.gene);
                bestFitness = node.fitness;
                System.out.println("Reversed Gene: " + reversedGene + ", Fitness: " + node.fitness);
                if (bestFitness <= targetFitness) {
                    System.out.println("Terminating. Best Fitness reached targetFitness or is below.");
                    terminate = true;
                    return;
                }
            }
        }

        if (remaining == 0) {
            node.children.clear();
            if (!node.hasChildren()) {
                if (node != root) {
                    Node parent = findParent(root, node);
                    if (parent != null) {
                        parent.children.remove(node);
                    }
                }
            }
            return;
        }

        for (int i = 1; i <= used.length - 1; i++) {
            if (!used[i]) {
                used[i] = true;
                List<Integer> childGene = new ArrayList<>(node.gene);
                childGene.add(i);
                Node childNode = new Node(childGene, 0, node.N);
                node.addChild(childNode);
                generatePermutations(childNode, used, remaining - 1);

                if (terminate) {
                    return;
                }

                used[i] = false;
            }
        }


        // Check the elapsed time and terminate if the time limit is reached

        if (elapsedTime >= 60 * 60 * 1000) {
            System.out.println("Terminating. Time limit reached.");
            terminate = true;
            timerTermination = true;
        }
    }


    private int calculateFitness(List<Integer> gene, int N) {
        if (gene.isEmpty()) {
            return 0;
        }

        List<Integer> reversedGene = new ArrayList<>(gene);

        Collections.reverse(reversedGene);
        //System.out.println(reversedGene.toString());
        return Spiral.placeSquaresAndReturnSize(reversedGene, N, bestFitness);
    }

    private Node findParent(Node currentNode, Node targetNode) {
        for (Node child : currentNode.children) {
            if (child == targetNode) {
                return currentNode;
            } else {
                Node parent = findParent(child, targetNode);
                if (parent != null) {
                    return parent;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        int[] targetFitnessValues = {
                1, 3, 5, 7, 9, 11, 13, 15, 18, 21, 24, 27, 30, 33, 36, 39,
                43, 47, 50, 54, 58, 62, 66, 71, 75, 80, 84, 89, 93, 98,
                103, 108, 113, 118, 123, 128, 133, 139, 144, 150, 155, 161,
                166, 172, 178, 184, 190, 196, 202, 208, 214, 221, 227, 233, 240, 246
        };
        PrintWriter writer = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateTime = dateFormat.format(new Date());

        // Define the path to the desktop
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String fileName = "output_" + currentDateTime + ".txt";
        String filePath = desktopPath + File.separator + fileName;




        int countdown = 0;
        Visualizer arrayVisualization = new Visualizer(new int[0][0]);
        arrayVisualization.setVisible(true);
        writer = new PrintWriter(new FileWriter(filePath));
        for (int i = 65; i < 1000; i++) {

            int n = i + 1;
            int targetFitness= (int) Math.ceil(Math.sqrt(((n *( n+ 1)) * ((2 * n) + 1)) / 6));
            //int targetFitness= (int) Math.ceil(Math.sqrt((((n+1) *(( n+1 )+ 1)) * ((2 * (n+1)) + 1)) / 6));
            OrderTree orderTree = new OrderTree(n, targetFitness+countdown);

            List<Integer> bestGene = orderTree.getBestGene();
            int bestFitness = orderTree.getBestFitness();

            List<Integer> reversedBestGene = new ArrayList<>(bestGene);
            Collections.reverse(reversedBestGene);
            System.out.println("Final Best Gene for N=" + n + ": " + reversedBestGene + ", Final Best Fitness: " + bestFitness);

            int[][] solution = Spiral.placeSquaresAndReturnArray(reversedBestGene);
            System.out.println(solution.length + " ennyi az annyi");
            arrayVisualization.updateVisualization(solution);
            String output = String.format("n = %d, lbTher: %d, Sol: %d", n, targetFitness, solution.length);
            writer.println(output);

            writer.flush();
            if (orderTree.timerTermination) {
                //countdown += 1;
            }
        }
    }
}
