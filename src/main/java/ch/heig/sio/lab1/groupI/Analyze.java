package ch.heig.sio.lab1.groupI;

import ch.heig.sio.lab1.display.HeuristicComboItem;
import ch.heig.sio.lab1.groupI.heuristique.FarthestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.NearestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.RandomInsertion;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
                //"data/u574.dat",
                //"data/pcb442.dat",
                //"data/pcb1173.dat",
                //"data/u1817.dat",
                //"data/nrw1379.dat",
        };

        String[] heuristicNames = {
                "Farthest Insertion",
                "Nearest Insertion",
                "Random Insertion"
        };

        TspData[] data = new TspData[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            data[i] = TspData.fromFile(filePaths[i]);
        }
        HeuristicComboItem[] heuristics = {
                new HeuristicComboItem("Farthest Insertion", new FarthestInsertion()),
                new HeuristicComboItem("Nearest Insertion", new NearestInsertion()),
                new HeuristicComboItem("Random Insertion", new RandomInsertion())
        };

        // Pour chaque dataset
        for (int i = 0; i < data.length; i++) {
            TspData tspData = data[i];
            int totalCities = tspData.getNumberOfCities();
            String csvFileName = "statistics_" + filePaths[i].substring(filePaths[i].lastIndexOf('/') + 1).replace(".dat", ".csv");

            try (PrintWriter writer = new PrintWriter(new FileWriter(csvFileName))) {
                // Write the CSV header
                writer.println("Dataset,CityCount,Heuristic,TourLength");

                //System.out.printf("Dataset: %s (Total Cities: %d)%n", filePaths[i], totalCities);

                // Pour chaque nombre de villes
                for (int cityCount = 1; cityCount < totalCities; cityCount+=10) {
                    //System.out.printf("  Running heuristics for %d cities:%n", cityCount);

                    // Pour chaque heuristique
                    for (int j = 0; j < heuristics.length; j++) {

                        TspTour tour = heuristics[j].computeTour(tspData, cityCount);
                        int tourLength = (int) tour.length();
                        // Écriture en csv
                        writer.printf("%s,%d,%s,%d%n", filePaths[i], cityCount, heuristicNames[j], tourLength);
                    }
                }
                System.out.printf("Results saved to %s%n", csvFileName);


            } catch (IOException e) {
                System.err.printf("Error writing to file %s: %s%n", csvFileName, e.getMessage());
            }
        }
    }
}