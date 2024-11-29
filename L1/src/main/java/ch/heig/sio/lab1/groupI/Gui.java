package ch.heig.sio.lab1.groupI;

import ch.heig.sio.lab1.display.HeuristicComboItem;
import ch.heig.sio.lab1.display.TspSolverGui;
import ch.heig.sio.lab1.groupI.heuristique.FarthestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.NearestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.RandomInsertion;
import ch.heig.sio.lab1.sample.CanonicalTour;
import com.formdev.flatlaf.FlatLightLaf;

public final class Gui {
  public static void main(String[] args) {
    HeuristicComboItem[] heuristics = {
        new HeuristicComboItem("Canonical tour", new CanonicalTour()),
        new HeuristicComboItem("Farthest insertion", new FarthestInsertion()),
        new HeuristicComboItem("Random insertion", new RandomInsertion()),
        new HeuristicComboItem("Nearest insertion", new NearestInsertion())};
        // TODO: Add your heuristics here


    // May not work on all platforms, comment out if necessary
    System.setProperty("sun.java2d.opengl", "true");

    FlatLightLaf.setup();
    new TspSolverGui(1400, 800, "TSP solver", heuristics);
  }
}
