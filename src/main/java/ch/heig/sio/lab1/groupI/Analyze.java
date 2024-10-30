package ch.heig.sio.lab1.groupI;

import ch.heig.sio.lab1.display.HeuristicComboItem;
import ch.heig.sio.lab1.groupI.heuristique.FarthestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.NearestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.RandomInsertion;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.io.FileNotFoundException;

public final class Analyze {
  public static void main(String[] args) throws FileNotFoundException {
    // TODO
    //  - Renommer le package ;
    //  - Implémenter les différentes heuristiques en écrivant le code dans ce package, et uniquement celui-ci
    //    (sous-packages et packages de tests ok) ;
    //  - Factoriser le code commun entre les différentes heuristiques ;
    //  - Documentation soignée comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.

    // Longueurs optimales :
    // pcb442 : 50778
    // att532 : 86729
    // u574 : 36905
    // pcb1173   : 56892
    // nrw1379  : 56638
    // u1817 : 57201
    String[] filePaths = {
            "data/att532.dat",
            "data/u574.dat",
            "data/pcb442.dat",
            "data/pcb1173.dat"
    };

    String[] heuristicNames = {
            "Farthest Insertion",
            "Nearest Insertion",
            "Random Insertion"
    };
    // Load TSP datasets
    TspData[] data = new TspData[filePaths.length];
    for (int i = 0; i < filePaths.length; i++) {
      data[i] = TspData.fromFile(filePaths[i]);
    }

    // Initialize heuristic algorithms
    HeuristicComboItem[] heuristics = {
            new HeuristicComboItem("Farthest Insertion", new FarthestInsertion()),
            new HeuristicComboItem("Nearest Insertion", new NearestInsertion()),
            new HeuristicComboItem("Random Insertion", new RandomInsertion())
    };

    // Run statistics for each dataset
    for (int i = 0; i < data.length; i++) {
      TspData tspData = data[i];
      int totalCities = tspData.getNumberOfCities();
      System.out.printf("Dataset: %s (Total Cities: %d)%n", filePaths[i], totalCities);

      // Perform runs for each number of cities up to the total number
      for (int cityCount = 1; cityCount < totalCities; cityCount++) {
        System.out.printf("  Running heuristics for %d cities:%n", cityCount);

        for (int j = 0 ; j < heuristics.length ; j++) {

          // Assuming computeTour accepts cityCount as a parameter
          TspTour tour = heuristics[j].computeTour(tspData, cityCount);
          System.out.printf("    Heuristic: %s, Tour Length: %d%n", heuristicNames[j], tour.length());
        }
        System.out.println();
      }
    }
  }
}
