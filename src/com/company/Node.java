package com.company;

import java.util.ArrayList;
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

    public OrderTree(int N, int targetFitness) throws Exception {
        root = new Node(new ArrayList<>(), 0, N); // Initial fitness is set to 0
        bestGene = new ArrayList<>();
        bestFitness = Integer.MAX_VALUE; // Set to some initial maximum value
        this.targetFitness = targetFitness; // Set the target fitness
        generatePermutations(root, new boolean[N + 1], N);
        System.out.println("Best Gene: " + bestGene + ", Best Fitness: " + bestFitness);
    }

    private void generatePermutations(Node node, boolean[] used, int remaining) throws Exception {
        // Calculate fitness externally and set it here (replace 0 with your calculation)
        int fitness = calculateFitness(node.gene, node.N);
        node.fitness = fitness;

        // Print the gene and fitness of every node
        System.out.println("Gene: " + node.gene + ", Fitness: " + node.fitness);

        // Update bestGene and bestFitness if needed
        if (node.isLeaf() && node.fitness < bestFitness) {
            bestGene = new ArrayList<>(node.gene);
            bestFitness = node.fitness;
            // Check if the bestFitness has reached or is below the targetFitness
            if (bestFitness <= targetFitness) {
                System.out.println("Terminating. Best Fitness reached targetFitness or is below.");
                throw new Exception("opt found");
            }
        }



        if (remaining == 0) {
            // Clear children and check if the node should be deleted
            node.children.clear();
            if (!node.hasChildren()) {
                // If no more children, delete the node itself
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
                Node childNode = new Node(childGene, 0, node.N); // Initial fitness is set to 0
                node.addChild(childNode);
                generatePermutations(childNode, used, remaining - 1);
                used[i] = false;
            }
        }
    }

    private int calculateFitness(List<Integer> gene, int N) {
        if (gene.isEmpty()) {
            // Handle the case where gene is empty (return an appropriate value)
            return 0; // You should replace this with the appropriate default value
        }

        // Call your external fitness evaluation method here
        return Spiral.placeSquaresAndReturnSize(gene, N);
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

    public static void main(String[] args) throws Exception {
        int N = 16; // Change N to the desired value
        int targetFitness = 39; // Change this to your target fitness
        OrderTree orderTree = new OrderTree(N, targetFitness);
    }
}
