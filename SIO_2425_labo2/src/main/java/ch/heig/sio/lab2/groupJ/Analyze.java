package ch.heig.sio.lab2.groupJ;

import ch.heig.sio.lab2.display.HeuristicComboItem;
import ch.heig.sio.lab2.groupJ.heuristique.FarthestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.NearestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.TwoOpt;
import ch.heig.sio.lab2.tsp.RandomTour;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Analyze {
    public static void main(String[] args) throws FileNotFoundException {
        // TODO
        //  - Renommer le package ;
        //  - Intégrer (et corriger si nécessaire) les heuristiques constructives du labo 1 dans ce package ;
        //  - Implémenter l'heuristique 2-opt utilisant la stratégie "meilleure amélioration" ;
        //  - Documentation soignée comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        // Longueurs optimales :
        // pcb442  : 50778
        // att532  : 86729
        // u574    : 36905
        // pcb1173 : 56892
        // nrw1379 : 56638
        // u1817   : 57201
        String[] filePaths = {
                "data/att532.dat",
                "data/u574.dat",
                "data/pcb442.dat",
                "data/pcb1173.dat",
                "data/u1817.dat",
                "data/nrw1379.dat"
        };


        // Définition des valeurs optimales pour chaque dataset
        Map<String, Integer> optimalValues = new HashMap<>();
        optimalValues.put("att532", 86729);
        optimalValues.put("u574", 36905);
        optimalValues.put("pcb442", 50778);
        optimalValues.put("pcb1173", 56892);
        optimalValues.put("nrw1379", 56638);
        optimalValues.put("u1817", 57201);

        // Charger les données des datasets
        TspData[] data = new TspData[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            data[i] = TspData.fromFile(filePaths[i]);
        }

        // Instancier les heuristiques de départ
        FarthestInsertion farthestInsertion = new FarthestInsertion();
        NearestInsertion nearestInsertion = new NearestInsertion();
        RandomTour randomInsertion = new RandomTour();
        TwoOpt twoOpt = new TwoOpt();

        // En-tête du tableau de statistiques
        System.out.printf("%-20s %8s %10s %8s %10s %8s %10s %8s%n",
                "Heuristic", "Min", "% of Opt", "Max", "% of Opt", "Mean", "% of Opt", "StdDev");

        for (int i = 0; i < data.length; i++) {
            TspData tspData = data[i];
            String datasetName = filePaths[i].substring(filePaths[i].lastIndexOf('/') + 1).replace(".dat", "");
            int optimalValue = optimalValues.getOrDefault(datasetName, -1);
            // affichage du nom du dataset et de la valeur optimale
            System.out.println("\nDataset: " + datasetName + " (Optimal: " + optimalValue + ")");
            // Analyser chaque heuristique
            analyzeHeuristic("Farthest Insertion", farthestInsertion, tspData, twoOpt, optimalValue);
            analyzeHeuristic("Nearest Insertion", nearestInsertion, tspData, twoOpt, optimalValue);
            analyzeHeuristic("Random Insertion", randomInsertion, tspData, twoOpt, optimalValue);
        }
    }

    private static void analyzeHeuristic(String heuristicName, Object heuristic, TspData tspData, TwoOpt twoOpt, int optimalValue) {
        ArrayList<Integer> tourLengths = new ArrayList<>();
        int totalCities = tspData.getNumberOfCities();
        // pour chaque ville lancer l'heuristique correspondante
        for (int cityCount = 0; cityCount < totalCities; cityCount++) {
            TspTour initialTour;
            if (heuristic instanceof FarthestInsertion) {
                initialTour = ((FarthestInsertion) heuristic).computeTour(tspData, cityCount);
            } else if (heuristic instanceof NearestInsertion) {
                initialTour = ((NearestInsertion) heuristic).computeTour(tspData, cityCount);
            } else if (heuristic instanceof RandomTour) {
                initialTour = ((RandomTour) heuristic).computeTour(tspData, cityCount);
            } else {
                throw new IllegalArgumentException("Unsupported heuristic");
            }

            // Optimiser le tour de départ avec 2-opt
            TspTour optimizedTour = twoOpt.computeTour(initialTour);
            tourLengths.add((int) optimizedTour.length());
        }
        // Extraire les statistiques
        int min = tourLengths.stream().mapToInt(Integer::intValue).min().orElse(0);
        int max = tourLengths.stream().mapToInt(Integer::intValue).max().orElse(0);
        double mean = tourLengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double stdDev = calculateStdDev(tourLengths, mean);

        // Calculer les pourcentages par rapport à la valeur optimale
        double minPercentOptimal = optimalValue > 0 ? (min / (double) optimalValue) * 100 : 0.0;
        double maxPercentOptimal = optimalValue > 0 ? (max / (double) optimalValue) * 100 : 0.0;
        double meanPercentOptimal = optimalValue > 0 ? (mean / (double) optimalValue) * 100 : 0.0;

        // Afficher les statistiques
        System.out.printf("%-20s %8d %10.2f%% %8d %10.2f%% %8.2f %10.2f%% %8.2f%n",
                heuristicName, min, minPercentOptimal, max, maxPercentOptimal, mean, meanPercentOptimal, stdDev);
    }

    private static double calculateStdDev(ArrayList<Integer> lengths, double mean) {
        double sumSquaredDiffs = 0.0;
        for (int length : lengths) {
            sumSquaredDiffs += Math.pow(length - mean, 2);
        }
        return Math.sqrt(sumSquaredDiffs / lengths.size());
    }
}
