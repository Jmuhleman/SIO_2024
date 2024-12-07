package ch.heig.sio.lab2.groupJ;

import ch.heig.sio.lab2.display.HeuristicComboItem;
import ch.heig.sio.lab2.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspSolverGui;
import ch.heig.sio.lab2.groupJ.heuristique.FarthestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.NearestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.TwoOpt;
import ch.heig.sio.lab2.tsp.RandomTour;
import com.formdev.flatlaf.FlatLightLaf;

public final class Gui {
  public static void main(String[] args) {
    ObservableTspConstructiveHeuristic[] constructiveHeuristics = {
            new HeuristicComboItem.Constructive("Nearest insertion", new NearestInsertion()),
            new HeuristicComboItem.Constructive("Farthest insertion", new FarthestInsertion()),
            new HeuristicComboItem.Constructive("Random insertion", new RandomTour())
        // Add the constructive heuristics
    };

    ObservableTspImprovementHeuristic[] improvementHeuristics = {
        new HeuristicComboItem.Improvement("2-opt", new TwoOpt())
        // Add the new improvement heuristic
    };

    // May not work on all platforms, comment out if necessary
    System.setProperty("sun.java2d.opengl", "true");
    FlatLightLaf.setup();

    new TspSolverGui(1400, 800, "TSP solver", constructiveHeuristics, improvementHeuristics);
  }
}
