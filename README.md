# Square Packing Optimizer

A Java-based algorithmic project designed to solve complex 2D square packing optimization problems. The core engine utilizes a Branch and Bound approach to explore and identify the most optimal placement sequences for squares of varying sizes.

## Features

- **Branch and Bound Algorithms**: Two distinct, highly optimized controllers (`DeterminedBB` and `DeterminedBranchAndBoundFedes`) to intelligently search the permutation tree.
- **Spiral Placement Logic**: Advanced placement strategies (`Spiral` and `Spiral_fedes`) that calculate the tightest bounding box for a given sequence of squares.
- **Real-Time Visualization**: A built-in Java Swing UI (`Visualizer`) that renders the current best square layout dynamically.
- **Auto-Snapshot**: Automatically saves a screenshot of the best layout to your Desktop as `array_visualization.png`.

## Project Structure

- `src/squarepacking/controller/`: The "brains" of the operation. Contains the multi-threaded Branch and Bound algorithms.
- `src/squarepacking/algorithm/`: The geometric placement logic. Converts a 1D sequence of squares into a 2D optimized spiral arrangement.
- `src/squarepacking/ui/`: Contains the visualizer components.
- `archive/`: A graveyard of legacy scripts, previous algorithm iterations, and deprecated models for historical reference.

## Getting Started

1. Clone the repository.
2. Open the project in your favorite Java IDE (IntelliJ IDEA, Eclipse, VS Code).
3. Open `src/squarepacking/Main.java`.
4. Uncomment the specific algorithm you wish to run.
5. Click **Run**! 

*Note: You can tweak the search depth and branch width by adjusting the parameters in the controller's constructor.*
