package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int maxMeret = 5;
        List<Integer> guideSequence = new ArrayList<>();
        guideSequence.add(1);
        guideSequence.add(5);
        guideSequence.add(8);
        guideSequence.add(2);
        guideSequence.add(1);

        try {
            int[][] result = FirstFit.placeSquares(maxMeret, guideSequence);

            // Print the result to check the placement of squares
            for (int[] row : result) {
                for (int value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        } catch (Except.Tulkicsi e) {
            e.printStackTrace();
        }
    }
}
