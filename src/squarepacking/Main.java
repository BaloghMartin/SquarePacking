package squarepacking;

import squarepacking.controller.DeterminedBB;
import squarepacking.controller.DeterminedBranchAndBoundFedes;

public class Main {
    public static void main(String[] args) {
        System.out.println("Indul a kalkuláció...");

        // Válaszd ki, melyik verziót szeretnéd futtatni az alábbiak közül,
        // és vedd ki a kommentelésből (a másikat hagyd kommentben)!

        // 1. Az eredeti Determined Branch and Bound (Spiral algoritmussal):
        // DeterminedBB.main(args);

        // 2. A Fedes verziójú Determined Branch and Bound (Spiral_fedes algoritmussal):
        DeterminedBranchAndBoundFedes.main(args);
    }
}
