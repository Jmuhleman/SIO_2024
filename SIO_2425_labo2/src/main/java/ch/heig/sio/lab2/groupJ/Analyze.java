package ch.heig.sio.lab2.groupJ;

import ch.heig.sio.lab2.groupJ.heuristique.FarthestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.NearestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.TwoOpt;
import ch.heig.sio.lab2.tsp.RandomTour;
import ch.heig.sio.lab2.tsp.TspConstructiveHeuristic;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * La classe {@code Analyze} permet d'évaluer la qualité et les performances de différentes heuristiques constructives
 * et de l'algorithme d'optimisation 2-opt appliqué au problème du voyageur de commerce (TSP).
 *
 * <p>Elle utilise les heuristiques suivantes pour générer des tournées initiales :
 * <ul>
 *     <li>Séquence aléatoire de villes sans remise</li>
 *     <li>Insertion la plus proche</li>
 *     <li>Insertion la plus éloignée</li>
 * </ul>
 * Chaque heuristique est testée sur six jeux de données TSP. Les performances sont mesurées et comparées
 * à l'aide de métriques telles que la longueur des tournées (minimum, moyenne, et maximum), les performances
 * relatives aux solutions optimales et le temps moyen d'exécution.
 *
 * <p>Les résultats sont calculés en effectuant N exécutions indépendantes pour chaque jeu de données et heuristique,
 * suivies de l'amélioration avec l'algorithme 2-opt utilisant la stratégie de "meilleure amélioration".
 *
 * <p>Les statistiques d'exécution sont affichées sous forme de tableau pour chaque jeu de données.
 *
 * <p><strong>Utilisation :</strong>
 * Ce programme suppose que les jeux de données se trouvent dans le répertoire {@code data/}.
 *
 * <p>Auteurs :
 * <li>Julien Mühlemann</li>
 * <li>Cristhian Ronquillo</li>
 * <li>Dr. Ing. Julien Billeter</li>
 */
public final class Analyze {
    /**
     * Point d'entrée principal du programme. Charge les jeux de données TSP, initialise les heuristiques
     * et évalue leurs performances avec l'optimisation 2-opt.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     * si un fichier de jeu de données est manquant, le programme termine avec le code d'erreur 1
     */
    public static void main(String[] args) {
        // TODO
        //  - Renommer le package ;
        //  - Intégrer (et corriger si nécessaire) les heuristiques constructives du labo 1 dans ce package ;
        //  - Implémenter l'heuristique 2-opt utilisant la stratégie "meilleure amélioration" ;
        //  - Documentation soignée comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        // Chemins vers les fichiers de jeux de données TSP
        String[] filePaths = {
                "data/att532.dat",
                "data/u574.dat",
                "data/pcb442.dat",
                "data/pcb1173.dat",
                "data/u1817.dat",
                "data/nrw1379.dat"
        };

        // Valeurs optimales pour les jeux de données (pour les calculs de performances relatives)
        Map<String, Integer> optimalValues = new HashMap<>();
        optimalValues.put("att532",  86729);
        optimalValues.put("u574",    36905);
        optimalValues.put("pcb442",  50778);
        optimalValues.put("pcb1173", 56892);
        optimalValues.put("nrw1379", 56638);
        optimalValues.put("u1817",   57201);

        // Charger les jeux de données dans des objets TspData
        TspData[] data = new TspData[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            try {
                data[i] = TspData.fromFile(filePaths[i]);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + filePaths[i]);
                System.exit(1);
            }
        }

        // Initialiser les générateurs aléatoires avec la même graine pour la reproductibilité
        long seed           = 0x134DAE9;
        RandomGenerator rnd = RandomGeneratorFactory.of("Random").create(seed);

        // Initialiser les heuristiques constructives
        FarthestInsertion farthestInsertion = new FarthestInsertion();
        NearestInsertion nearestInsertion   = new NearestInsertion();
        RandomTour randomInsertion          = new RandomTour(rnd);
        RandomTour randomInitialCities      = new RandomTour(rnd);

        final int NB_TOUR = 50; // Nombre d'exécutions par heuristique

        // Afficher l'en-tête du tableau
        System.out.printf("%-20s %8s %10s %8s   %10s  %8s %10s %8s %8s%n",
                "Heuristic", "Min", "% of Opt", "Max", "% of Opt", "Mean", "% of Opt", "StdDev", "Time");

        // Évaluer chaque heuristique sur tous les jeux de données
        for (int i = 0; i < data.length; i++) {
            TspData tspData = data[i];

            // Tournée utilisée pour tirer une ville aléatoire de départ pour chaque exécution (non utilisé pour RandomTour)
            TspTour initialCities = randomInitialCities.computeTour(data[i], i);

            // Affichage des valeurs optimales avant les statistiques pour chaque heuristique
            String datasetName = filePaths[i].substring(filePaths[i].lastIndexOf('/') + 1).replace(".dat", "");
            int optimalValue   = optimalValues.getOrDefault(datasetName, -1);
            System.out.println("\nDataset: " + datasetName + " (Optimal: " + optimalValue + ")");

            // Évaluer les heuristiques
            analyzeHeuristic("Random Insertion", randomInsertion, tspData, optimalValue, initialCities, NB_TOUR);
            analyzeHeuristic("Farthest Insertion", farthestInsertion, tspData, optimalValue, initialCities, NB_TOUR);
            analyzeHeuristic("Nearest Insertion", nearestInsertion, tspData, optimalValue, initialCities, NB_TOUR);
        }
    }

    /**
     * Analyse les performances d'une heuristique donnée sur un jeu de données TSP spécifique.
     *
     * @param heuristicName nom de l'heuristique
     * @param heuristic l'implémentation de l'heuristique
     * @param tspData le jeu de données TSP
     * @param optimalValue valeur optimale de la solution
     * @param initialCities tournée utilisée pour tirer une ville aléatoire de départ pour chaque exécution
     * @param NB_TOUR nombre d'exécutions à réaliser
     */
    private static void analyzeHeuristic(String heuristicName,
                                         TspConstructiveHeuristic heuristic,
                                         TspData tspData,
                                         int optimalValue,
                                         TspTour initialCities,
                                         final int NB_TOUR) {
        TwoOpt twoOpt = new TwoOpt();
        ArrayList<Integer> tourLengths = new ArrayList<>();

        long time = 0, startTime, endTime;

        for (int i = 0; i < NB_TOUR; ++i) {
            startTime = System.nanoTime();
            /* Une bonne pratique serait de tester que tspData et initialCities ne sont pas null,
               mais pour éviter un ralentissement inutile, on ne le fait pas
               initialCities.tour().get(i) non utilisé pour RandomTour
            */
            TspTour currentTour   = heuristic.computeTour(tspData, initialCities.tour().get(i));
            TspTour optimizedTour = twoOpt.computeTour(currentTour);

            endTime = System.nanoTime();
            time   += endTime - startTime;
            tourLengths.add((int) optimizedTour.length());

        }
        // Calculer les statistiques
        int min       = tourLengths.stream().mapToInt(Integer::intValue).min().orElse(0);
        int max       = tourLengths.stream().mapToInt(Integer::intValue).max().orElse(0);
        double mean   = tourLengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double stdDev = calculateStdDev(tourLengths, mean);

        // Calculer les pourcentages par rapport à la valeur optimale
        double minPercentOptimal  = optimalValue > 0 ? (min  / (double) optimalValue) * 100 : 0.0;
        double maxPercentOptimal  = optimalValue > 0 ? (max  / (double) optimalValue) * 100 : 0.0;
        double meanPercentOptimal = optimalValue > 0 ? (mean / (double) optimalValue) * 100 : 0.0;
        double averageTime        = (double) time / (NB_TOUR * 1e9);

        // Afficher les résultats
        System.out.printf("%-20s %8d %10.2f%% %8d %10.2f%% %8.2f %10.2f%% %8.2f %8.3f%n",
                heuristicName, min, minPercentOptimal, max, maxPercentOptimal, mean, meanPercentOptimal, stdDev, averageTime);
    }

    /**
     * Calcule l'écart-type des longueurs de tournées.
     *
     * @param lengths liste des longueurs de tournées
     * @param mean moyenne des longueurs
     * @return écart-type des longueurs de tournées
     */
    private static double calculateStdDev(ArrayList<Integer> lengths, double mean) {
        double sumSquaredDiffs = 0.0;
        for (int length : lengths) {
            sumSquaredDiffs += Math.pow(length - mean, 2);
        }
        return Math.sqrt(sumSquaredDiffs / lengths.size());
    }
}
