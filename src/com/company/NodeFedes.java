package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

class NodeFedes {
    List<Integer> gene;
    List<Node> children;
    int N;
    int fitness;

    public NodeFedes(List<Integer> gene, int fitness, int N) {
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

class BestGeneTrackerFedes {
    private Map<Integer, List<Integer>> bestGenes;
    private Map<Integer, Integer> bestFitnessValues;

    public BestGeneTrackerFedes(Map<Integer, List<Integer>> existingSolutions) {
        this.bestGenes = new HashMap<>(existingSolutions);
        this.bestFitnessValues = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : existingSolutions.entrySet()) {
            int N = entry.getKey();
            List<Integer> gene = entry.getValue();
            int fitness = Spiral_fedes.placeSquaresAndReturnArray(gene).length;
            //System.out.println("N:" + N +" fitness:" + fitness +" gene: " + gene.toString());
            bestFitnessValues.put(N, fitness);
        }
    }

    public void updateBestGene(int N, List<Integer> gene, int fitness) {
        bestGenes.put(N, new ArrayList<>(gene));
        bestFitnessValues.put(N, fitness);
    }

    public List<Integer> getBestGene(int N) {
        return bestGenes.getOrDefault(N, new ArrayList<>());
    }

    public int getBestFitness(int N) {
        return bestFitnessValues.getOrDefault(N, Integer.MAX_VALUE);
    }

    public boolean containsBestGene(int N) {
        return bestGenes.containsKey(N);
    }

    public Set<Integer> getNs() {
        return bestGenes.keySet();
    }
}

class LastGeneTrackerFedes {
    private Map<Integer, List<Integer>> lastGenes;
    private Map<Integer, Integer> amountChecked;

    public LastGeneTrackerFedes(Map<Integer, List<Integer>> existingLastGenes) {
        this.lastGenes = new HashMap<>(existingLastGenes);
        this.amountChecked = new HashMap<>();
    }

    public void updateLastGene(int N, List<Integer> gene, int Amount) {
        lastGenes.put(N, new ArrayList<>(gene));
        amountChecked.put(N, Amount);

    }

    public List<Integer> getBestGene(int N) {
        return lastGenes.getOrDefault(N, new ArrayList<>());
    }

    public int getAmountChecked(int N) {
        return amountChecked.getOrDefault(N, Integer.MIN_VALUE);
    }

    public boolean containsLastGene(int N) {
        return lastGenes.containsKey(N);
    }

    public Set<Integer> getNs() {
        return lastGenes.keySet();
    }
}


class OrderTreeFedes {
    private BestGeneTrackerFedes bestGeneTracker;
    private LastGeneTrackerFedes lastGeneTracker;
    public Node root;
    private int targetFitness;
    private boolean terminate = false;
    private long startTime;
    String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
    String fileName = "solutions2.txt";
    String filePath = desktopPath + File.separator + fileName;
    String fileName2 = "checked2.txt";
    String filePath2 = desktopPath + File.separator + fileName2;

    public OrderTreeFedes(int N, int targetFitness) {
        this.bestGeneTracker = new BestGeneTrackerFedes(loadExistingSolutionsFromFile(filePath));
        //System.out.println("Loaded existing solutions from file: " + filePath);

        this.lastGeneTracker = new LastGeneTrackerFedes(loadExistingCheckedFromFile(filePath2));
        //System.out.println("Loaded existing checked genes from file: " + filePath2);

        this.root = new Node(new ArrayList<>(), 0, N);
        //System.out.println("Created root node with problem size N=" + N);

        this.targetFitness = targetFitness;
        //System.out.println("Set target fitness to: " + targetFitness);

        this.startTime = System.currentTimeMillis();
        //System.out.println("Started the search process at time: " + startTime);

        List<Integer> lastCheckedGene = lastGeneTracker.getBestGene(N);
        if (!lastCheckedGene.isEmpty()) {
            //System.out.println("Resuming search from last checked gene: " + lastCheckedGene);
            //Collections.reverse(lastCheckedGene);
            FOUND=false;
            findLastCheckedNode(root, root, lastCheckedGene, new boolean[N + 1], N);
            ////System.out.println("Last checked node: " + lastCheckedNode);

        } else {
            //System.out.println("No last checked gene found. Starting new search from root.");
            generatePermutations(root, new boolean[N + 1], N);
        }

        //System.out.println("Best Gene: " + bestGeneTracker.getBestGene(N) + ", Best Fitness: " + bestGeneTracker.getBestFitness(N));
    }

    public List<Integer> getBestGene(int N) {
        return bestGeneTracker.getBestGene(N);
    }

    public int getBestFitness(int N) {
        return bestGeneTracker.getBestFitness(N);
    }

    private void generatePermutations(Node node, boolean[] used, int remaining) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        //System.out.println("Node: " + node.gene + ", Remaining: " + remaining + ", Elapsed Time: " + elapsedTime);

        if (node.isLeaf()) {
            int fitness = calculateFitness(node.gene, node.N);
            node.fitness = fitness;
            lastGeneTracker.updateLastGene(node.N, new ArrayList<>(node.gene), node.fitness);
            updateCheckedFile(lastGeneTracker, filePath2);

            List<Integer> reversedGene = new ArrayList<>(node.gene);
            Collections.reverse(reversedGene);
            //System.out.println("Reversed Gene: " + reversedGene + ", Fitness: " + node.fitness);

            if (node.fitness > bestGeneTracker.getBestFitness(node.N)) {
                bestGeneTracker.updateBestGene(node.N, new ArrayList<>(node.gene), node.fitness);
                //System.out.println("New Best Gene: " + node.gene + ", Fitness: " + node.fitness);
                if (node.fitness >= targetFitness) {
                    //System.out.println("Terminating. Best Fitness reached targetFitness or is below.");
                    terminate = true;
                    return;
                }
            }
        }

        if (remaining == 0) {
            Node parent = findParent(root, node);
            node.children.clear();
            if (!node.hasChildren() && node != root) {
                parent.children.remove(node);
            }
            for (Integer gene : node.gene) {
                used[gene] = true;
            }
            generatePermutations(parent, used, remaining + 1);
            return;
        }

        for (int i = 1; i <= used.length - 1; i++) {
            if (!used[i]) {
                used[i] = true;
                List<Integer> childGene = new ArrayList<>(node.gene);
                childGene.add(i);
                Node childNode = new Node(childGene, 0, node.N);
                node.addChild(childNode);
                //System.out.println("Exploring Child Gene: " + childGene);
                generatePermutations(childNode, used, remaining - 1);

                if (terminate) {
                    return;
                }

                used[i] = false;
            }
        }
    }

    private int calculateFitness(List<Integer> gene, int N) {
        if (gene.isEmpty()) {
            return 0;
        }

        List<Integer> reversedGene = new ArrayList<>(gene);
        Collections.reverse(reversedGene);
        return Spiral_fedes.placeSquaresAndReturnArray(reversedGene).length;
    }

    private static Node findParent(Node currentNode, Node targetNode) {
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

    private void exploreTree(Node currentNode, boolean[] used) {
        //System.out.println("HELOOOOO");
        // Calculate fitness for the current node
        currentNode.fitness = calculateFitness(currentNode.gene, currentNode.N);

        // Update best gene if applicable
        if (currentNode.fitness < bestGeneTracker.getBestFitness(currentNode.N)) {
            bestGeneTracker.updateBestGene(currentNode.N, new ArrayList<>(currentNode.gene), currentNode.fitness);
            //System.out.println("New Best Gene: " + currentNode.gene + ", Fitness: " + currentNode.fitness);
            if (currentNode.fitness <= targetFitness) {
                //System.out.println("Terminating. Best Fitness reached targetFitness or is below.");
                terminate = true;
                return;
            }
        }

        // Explore children recursively
        for (Node child : currentNode.children) {
            List<Integer> childGene = new ArrayList<>(currentNode.gene);
            childGene.add(child.gene.get(child.gene.size() - 1)); // Add the last gene of the child node
            exploreTree(child, used);

            // Terminate if necessary
            if (terminate) {
                return;
            }
        }
    }
    boolean FOUND;
    private void findLastCheckedNode(Node currentNode, Node root, List<Integer> lastCheckedGene, boolean[] used, int remaining) {
        // Check if the current node's gene matches the last checked gene
        if (currentNode.gene.equals(lastCheckedGene)) {
            //System.out.println("HERE");
            // After finding the node, continue exploring the tree
            //System.out.println(currentNode.gene.toString()+" v "+ lastCheckedGene);

            FOUND = true;

            //exploreTree(currentNode, used);
            //return currentNode;
        }
        if (currentNode.isLeaf()&&FOUND) {

            Node node = currentNode;
            int fitness = calculateFitness(node.gene, node.N);
            node.fitness = fitness;
            lastGeneTracker.updateLastGene(node.N, new ArrayList<>(node.gene), node.fitness);
            //System.out.println("asdasd");
            updateCheckedFile(lastGeneTracker, filePath2);

            List<Integer> reversedGene = new ArrayList<>(node.gene);
            Collections.reverse(reversedGene);
            //System.out.println("Reversed Gene: " + reversedGene + ", Fitness: " + node.fitness);

            if (node.fitness < bestGeneTracker.getBestFitness(node.N)) {
                bestGeneTracker.updateBestGene(node.N, new ArrayList<>(node.gene), node.fitness);
                //System.out.println("New Best Gene: " + node.gene + ", Fitness: " + node.fitness);
                if (node.fitness <= targetFitness) {
                    //System.out.println("Terminating. Best Fitness reached targetFitness or is below.");
                    terminate = true;
                    //System.out.println("ITT");
                    return;
                }
            }
        }
        // If remaining is 0, delete all children of the current node and return null
        if (remaining <= 0) {
            currentNode.children.clear();
            return;
        }

        // Flag to check if any child node matches the last checked gene
        boolean foundInChildren = false;

        // Traverse through the children of the current node recursively
        for (int i = 1; i <= used.length - 1; i++) {

            if (!used[i]) {
                used[i] = true;
                List<Integer> childGene = new ArrayList<>(currentNode.gene);
                childGene.add(i);
                Node childNode = new Node(childGene, 0, lastCheckedGene.size());
                currentNode.addChild(childNode);
                ////System.out.println(childNode.gene.toString()+" v "+lastCheckedGene.toString());
                ////System.out.println(FOUND);
                findLastCheckedNode(childNode, root, lastCheckedGene, used, remaining - 1);
                if (terminate) {
                    return;
                }

                used[i] = false;

                // If any child node is found, update the flag
                if (foundInChildren) {
                    foundInChildren = true;
                }
            }
        }

        // If none of the child nodes matched and all children are checked, clear the children
        if (!foundInChildren && currentNode.hasChildren()) {
            currentNode.children.clear();
        }

        // Return null as the node is not found
        return;
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

        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String fileName = "solutions2.txt";
        String filePath = desktopPath + File.separator + fileName;
        String fileName2 = "checked2.txt";
        String filePath2 = desktopPath + File.separator + fileName2;
        Map<Integer, List<Integer>> existingSolutions = loadExistingSolutionsFromFile(filePath);
        Map<Integer, List<Integer>> existingChecked = loadExistingCheckedFromFile(filePath2);

        int countdown = 0;
        Visualizer arrayVisualization = new Visualizer(new int[0][0]);
        arrayVisualization.setVisible(true);
        writer = new PrintWriter(new FileWriter(filePath));

        for (int i = 100; i < 101; i++) {
            int targetFitness;
            int n = i;
            //System.out.println(n+1);
            int k = n;
            if (k <= 56) {
                targetFitness = targetFitnessValues[k - 1];
            } else {
                targetFitness = (int) Math.ceil(Math.sqrt(((k * (k + 1)) * ((2 * k) + 1)) / 6));
            }
            //if(n==16){targetFitness++;}
            //targetFitness=(int)Math.floor((targetFitness*1.019));
            System.out.println(targetFitness);

            //targetFitness=99999999;
            //targetFitness++;
            System.out.println(n);
            //targetFitness=targetFitness;
            OrderTreeFedes orderTree = new OrderTreeFedes(n, targetFitness + countdown);

            List<Integer> bestGene = orderTree.getBestGene(n);
            int bestFitness = orderTree.getBestFitness(n);

            List<Integer> reversedBestGene = new ArrayList<>(bestGene);
            Collections.reverse(reversedBestGene);
            //System.out.println("Final Best Gene for N=" + n + ": " + reversedBestGene + ", Final Best Fitness: " + bestFitness);

            int[][] solution = Spiral.placeSquaresAndReturnArray(reversedBestGene);
            //System.out.println(solution.length + " ennyi az annyi");
            arrayVisualization.updateVisualization(solution);

            updateSolutionsFile(n, targetFitness, solution.length, reversedBestGene.toString(), filePath);




        }

        //updateSolutionsFile(existingSolutions, filePath);
        writer.close();
    }

    private static Map<Integer, List<Integer>> loadExistingSolutionsFromFile(String filePath) {
        Map<Integer, List<Integer>> existingSolutions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Find the index of the first opening square bracket
                int indexOfFirstOpeningBracket = line.indexOf('[');
                if (indexOfFirstOpeningBracket != -1) {
                    // Extract the gene string including the square brackets
                    String geneString = line.substring(indexOfFirstOpeningBracket);
                    // Remove the gene string from the line
                    line = line.substring(0, indexOfFirstOpeningBracket).trim();
                    // Split the remaining line by commas
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        int n = Integer.parseInt(parts[0].trim());
                        int targetFitness = Integer.parseInt(parts[1].trim());
                        int solutionLength = Integer.parseInt(parts[2].trim());
                        //System.out.println("Gene String from File: " + geneString);
                        List<Integer> gene = parseGene(geneString);
                        existingSolutions.put(n, gene);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingSolutions;
    }


    private static Map<Integer, List<Integer>> loadExistingCheckedFromFile(String filePath) {
        Map<Integer, List<Integer>> existingChecked = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":"); // Split by colon
                if (parts.length >= 2) {
                    int n = Integer.parseInt(parts[0]);
                    List<Integer> gene = parseGene(parts[1]); // Use parts[1] for the gene list
                    existingChecked.put(n, gene);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingChecked;
    }


    private static List<Integer> parseGene(String geneString) {
        List<Integer> gene = new ArrayList<>();
        //System.out.println(geneString);
        String[] parts = geneString.substring(1, geneString.length() - 1).split(", ");// Remove square brackets and split

        for (String part : parts) {
            gene.add(Integer.parseInt(part));
        }
        return gene;
    }

    private static void updateCheckedFile(LastGeneTrackerFedes lastGeneTracker, String filePath) {
        try {
            // Read existing content of the file
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

            // Iterate over the N values in the LastGeneTracker
            for (Integer N : lastGeneTracker.getNs()) {
                List<Integer> gene = lastGeneTracker.getBestGene(N);
                String newLine = N + ":" + gene.toString();

                // Find the line corresponding to the current N value
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.startsWith(N + ":")) {
                        // Replace the line with the new information
                        lines.set(i, newLine);
                        break; // Exit the loop since the line has been found and replaced
                    }
                }
            }

            // Write the updated content back to the file
            Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateSolutionsFile(Map<Integer, List<Integer>> existingSolutions, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Map.Entry<Integer, List<Integer>> entry : existingSolutions.entrySet()) {
                int N = entry.getKey();
                List<Integer> gene = entry.getValue();
                writer.println(N + ", " + gene.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateSolutionsFile(int n, int targetFitness, int solutionLength, String reversedBestGene, String filePath) {
        try {
            // Open the file writer
            FileWriter fileWriter = new FileWriter(filePath, true); // Open in append mode
            PrintWriter writer = new PrintWriter(new BufferedWriter(fileWriter));

            // Read existing solutions from the file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder fileContent = new StringBuilder();
            String line;
            boolean foundN = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 3) {
                    int currentN = Integer.parseInt(parts[0]);
                    if (currentN == n) {
                        // Found existing data for this N, check and update if necessary
                        foundN = true;
                        int existingSolutionLength = Integer.parseInt(parts[2]);
                        if (solutionLength < existingSolutionLength) {
                            // Override the line with the new data
                            line = String.format("%d, %d, %d, %s", n, targetFitness, solutionLength, reversedBestGene);
                        }
                    }
                }
                fileContent.append(line).append("\n");
            }
            reader.close();

            // If no existing data for this N, append the new data
            if (!foundN) {
                writer.println(String.format("%d, %d, %d, %s", n, targetFitness, solutionLength, reversedBestGene));
            } else {
                // Rewrite the file with updated data
                FileWriter fileWriter2 = new FileWriter(filePath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter2);
                bufferedWriter.write(fileContent.toString());
                bufferedWriter.close();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
