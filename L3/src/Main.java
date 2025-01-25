import montecarlo.*;
import statistics.*;

import java.util.Random;

/*
// Juste pour l'exemple
class FairCoinTossExperiment implements Experiment {
    public double execute(Random rnd) {
        return rnd.nextDouble() < 0.5 ? 1.0 : 0.0;
    }
}
*/
public class Main {

    public static void main(String[] args) {

        // Première simulation: Estimation de la probabilité p23
        final int K = 23; // Taille du groupe
        final int Y = 365; // Nombre de jours dans une année
        int M = 2; // Nombre minimum de personnes avec le même anniversaire
        final double LEVEL = 0.95; // Niveau de confiance (1 - alpha, alpha = 0.05)
        final double INITIAL_HALF_WIDTH = 1e-4; // Demi-largeur initiale
        final long INITIAL_RUNS = 1_000_000; // Nombre initial de réalisations
        final long ADDITIONAL_RUNS = 100_000; // Incrément pour les réalisations supplémentaires
        final long SEED = 0x134D6EE; // Graine pour la reproductibilité

        Random rnd = new Random(SEED);
        BirthdayaradoxExperiment bdayExperiment = new BirthdayaradoxExperiment(K, Y, M);
        StatCollector stat = new StatCollector();

        double maxHalfWidth = INITIAL_HALF_WIDTH;
/*
        for (int iteration = 1; iteration < 2; iteration++) {
            System.out.printf("Iteration %d:%n", iteration);

            long startTime = System.nanoTime();

            MonteCarloSimulation.simulateTillGivenCIHalfWidth(
                    bdayExperiment, LEVEL, maxHalfWidth, INITIAL_RUNS, ADDITIONAL_RUNS, rnd, stat);

            long endTime = System.nanoTime();
            double elapsedTimeInSeconds = (endTime - startTime) / 1e9;

            double average = stat.getAverage();
            double halfWidth = stat.getConfidenceIntervalHalfWidth(LEVEL);
            long totalRuns = stat.getNumberOfObs();

            System.out.printf("  Estimated probability (p̂): %.8f%n", average);
            System.out.printf("  Confidence interval (95%%): [%.8f, %.8f]%n",
                    average - halfWidth, average + halfWidth);
            System.out.printf("  Confidence interval half-width: %.8f%n", halfWidth);
            System.out.printf("  Total realizations generated: %d%n", totalRuns);
            System.out.printf("  Execution time: %.3f seconds%n%n", elapsedTimeInSeconds);

            maxHalfWidth /= 2;
        }

*/

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Deuxième simulation: Seuil de couverture des intervalles de confiance
        long N = 1_000_0; // Nombre de réalisations pour chaque simulation
/*
        final double TRUE_P23 = 0.5072972343; // Valeur théorique de p23
        rnd.setSeed(SEED); // Réinitialisation de la graine

        final long NUM_SIMULATIONS = 1000; // Nombre total de simulations
        int countContainingTrueP = 0; // Nombre d'intervalles contenant la vraie valeur de p23

        for (int i = 0; i < NUM_SIMULATIONS; i++) {
            //TODO peut etre faire un init pour aller plus vite hehe...
            stat.init();
            // pour garantir l'indépendance des simulations
            rnd.setSeed(SEED + i);
            MonteCarloSimulation.simulateNRuns(bdayExperiment, N, rnd, stat);

            // Calculer l'intervalle de confiance
            double estimatedP = stat.getAverage(); // Moyenne estimée
            double halfWidth = stat.getConfidenceIntervalHalfWidth(LEVEL); // Demi-largeur
            double lowerBound = estimatedP - halfWidth; // Borne inférieure
            double upperBound = estimatedP + halfWidth; // Borne supérieure

            // Vérifier si la vraie valeur p23 est dans l'intervalle
            if (TRUE_P23 >= lowerBound && TRUE_P23 <= upperBound) {
                countContainingTrueP++;
            }
        }

        // Calcul du seuil empirique de couverture
        double empiricalCoverage = (double) countContainingTrueP / NUM_SIMULATIONS;

        // Calcul de l'intervalle de confiance associé (toujours à 95%)
        double stdError = Math.sqrt((empiricalCoverage * (1 - empiricalCoverage)) / NUM_SIMULATIONS);
        double zValue = 1.96; // Valeur critique pour un seuil de confiance de 95%
        double coverageLowerBound = empiricalCoverage - zValue * stdError; // Borne inférieure
        double coverageUpperBound = empiricalCoverage + zValue * stdError; // Borne supérieure

        // Afficher les résultats
        System.out.printf("Seuil empirique de couverture: %.5f%n", empiricalCoverage);
        System.out.printf("Intervalle de confiance pour la couverture (95%%): [%.5f, %.5f]%n",
                coverageLowerBound, coverageUpperBound);

*/

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Troisième tâche : Déterminer le nombre minimal K
        final int MIN_K = 80; // Taille minimale du groupe
        final int MAX_K = 100; // Taille maximale du groupe
        M = 3; // Nombre minimum de personnes partageant un anniversaire
        final double THRESHOLD = 0.5; // Seuil de probabilité
        N = 1_000_00; // Nombre de réalisations pour chaque simulation

        int minimalK = -1; // Variable pour stocker le K trouvé
        double estimatedP = 0.0; // Probabilité estimée pour le K trouvé

        // Parcourir les valeurs de K de MIN_K à MAX_K
        for (int actual_k = MIN_K; actual_k <= MAX_K; actual_k++) {
            // Réinitialiser le générateur pseudo-aléatoire
            rnd.setSeed(SEED);

            // Créer un nouvel objet StatCollector
            StatCollector stat_3 = new StatCollector();

            bdayExperiment.set(actual_k, Y, M);

            // Générer N réalisations pour ce K
            MonteCarloSimulation.simulateNRuns(bdayExperiment, N, rnd, stat_3);

            // Calculer la probabilité estimée
            estimatedP = stat_3.getAverage();

            // Vérifier si la probabilité dépasse le seuil
            if (estimatedP > THRESHOLD) {
                minimalK = actual_k; // Stocker le premier K satisfaisant la condition
                break; // Arrêter la recherche une fois que la condition est remplie
            }
        }

        if (minimalK != -1) {
            System.out.printf("Nombre minimal K : %d%n", minimalK);
            System.out.printf("Probabilité estimée pour K = %d : %.8f%n", minimalK, estimatedP);
        } else {
            System.out.println("Aucun K trouvé entre " + MIN_K + " et " + MAX_K + " satisfaisant la condition.");
        }


    }

}
