package ch.heig.sio.lab1.groupI;

import ch.heig.sio.lab1.display.HeuristicComboItem;
import ch.heig.sio.lab1.groupI.heuristique.FarthestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.NearestInsertion;
import ch.heig.sio.lab1.groupI.heuristique.RandomInsertion;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Analyze {
    public static void main(String[] args) throws IOException {
        // TODO
        //  - Renommer le package ;
        //  - Implémenter les différentes heuristiques en écrivant le code dans ce package, et uniquement celui-ci
        //    (sous-packages et packages de tests ok) ;
        //  - Factoriser le code commun entre les différentes heuristiques ;
        //  - Documentation soignée comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        String[] filePaths = {
                "data/att532.dat",
                "data/u574.dat",
                "data/pcb442.dat",
                "data/pcb1173.dat",
                "data/u1817.dat",
                "data/nrw1379.dat"
        };

        String[] heuristicNames = {
                "Farthest Insertion",
                "Nearest Insertion",
                "Random Insertion"
        };

        // Définition des valeurs optimales pour chaque dataset
        Map<String, Integer> optimalValues = new HashMap<>();
        optimalValues.put("att532", 86729);
        optimalValues.put("u574", 36905);
        optimalValues.put("pcb442", 50778);
        optimalValues.put("pcb1173", 56892);
        optimalValues.put("nrw1379", 56638);
        optimalValues.put("u1817", 57201);

        // Charger les données des fichiers
        TspData[] data = new TspData[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            data[i] = TspData.fromFile(filePaths[i]);
        }

        // Définition des heuristiques à tester
        HeuristicComboItem[] heuristics = {
                new HeuristicComboItem("Farthest Insertion", new FarthestInsertion()),
                new HeuristicComboItem("Nearest Insertion", new NearestInsertion()),
                new HeuristicComboItem("Random Insertion", new RandomInsertion())
        };

        // Affichage du header pour les résultats
        System.out.printf("%-20s %8s %10s %8s %10s %8s %10s %8s%n",
                "Heuristic", "Min", "% of Opt", "Max", "% of Opt", "Mean", "% of Opt", "StdDev");

        // Pour chaque dataset
        for (int i = 0; i < data.length; i++) {
            TspData tspData = data[i];
            // récuperer le nom du dataset
            String datasetName = filePaths[i].substring(filePaths[i].lastIndexOf('/') + 1).replace(".dat", "");
            int optimalValue = optimalValues.getOrDefault(datasetName, -1);

            System.out.println("\nDataset: " + datasetName + " (Optimal: " + optimalValue + ")");
            // Pour chaque heuristique
            for (int j = 0; j < heuristics.length; j++) {
                // Calculer les longueurs des tours pour chaque ville comme point de départ
                ArrayList<Integer> tourLengths = new ArrayList<>();
                int totalCities = tspData.getNumberOfCities();
                for (int cityCount = 0; cityCount < totalCities; cityCount++) {
                    TspTour tour = heuristics[j].computeTour(tspData, cityCount);
                    int tourLength = (int) tour.length();
                    tourLengths.add(tourLength);
                }

                // calcul des statistiques min max mean et écart type
                String heuristicName = heuristicNames[j];
                int min = tourLengths.stream().mapToInt(Integer::intValue).min().orElse(0);
                int max = tourLengths.stream().mapToInt(Integer::intValue).max().orElse(0);
                double mean = tourLengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                double stdDev = calculateStdDev(tourLengths, mean);

                // Calcul des pourcentages par rapport à la valeur optimale
                double minPercentOptimal = optimalValue > 0 ? (min / (double) optimalValue) * 100 : 0.0;
                double maxPercentOptimal = optimalValue > 0 ? (max / (double) optimalValue) * 100 : 0.0;
                double meanPercentOptimal = optimalValue > 0 ? (mean / (double) optimalValue) * 100 : 0.0;

                // On affiche les résultats pour chaque heuristique
                System.out.printf("%-20s %8d %10.2f%% %8d %10.2f%% %8.2f %10.2f%% %8.2f%n",
                        heuristicName, min, minPercentOptimal, max, maxPercentOptimal, mean, meanPercentOptimal, stdDev);
            }
        }
    }

    // Calcul de l'écart type
    private static double calculateStdDev(ArrayList<Integer> lengths, double mean) {
        double sumSquaredDiffs = 0.0;
        for (int length : lengths) {
            sumSquaredDiffs += Math.pow(length - mean, 2);
        }
        return Math.sqrt(sumSquaredDiffs / lengths.size());
    }
}