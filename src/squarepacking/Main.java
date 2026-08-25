package squarepacking;

import squarepacking.controller.DeterminedBB;
import squarepacking.controller.DeterminedBBOptimized;
import squarepacking.controller.DeterminedBranchAndBoundFedes;

public class Main {
    public static void main(String[] args) {
        System.out.println("Indul a kalkuláció...");

        // Válaszd ki, melyik verziót szeretnéd futtatni az alábbiak közül,
        // és vedd ki a kommentelésből (a másikat hagyd kommentben)!

        System.out.println("=== EREDETI (Lassú) VERZIÓ ===");
        long startTime = System.currentTimeMillis();
        DeterminedBB.main(args);
        long endTime = System.currentTimeMillis();
        System.out.println("Eredeti verzió futási ideje: " + (endTime - startTime) + " ms\n");

        System.out.println("=== OPTIMALIZÁLT (O(1) ugrás) VERZIÓ ===");
        long startTimeOpt = System.currentTimeMillis();
        DeterminedBBOptimized.main(args);
        long endTimeOpt = System.currentTimeMillis();
        System.out.println("Optimalizált verzió futási ideje: " + (endTimeOpt - startTimeOpt) + " ms\n");

        // 3. A Fedes verziójú Determined Branch and Bound (Spiral_fedes algoritmussal):
        // DeterminedBranchAndBoundFedes.main(args);
    }
}
