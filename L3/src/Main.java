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
        final int K = 23; // Taille du groupe
        final int Y = 365; // Nombre de jours dans une année
        final int M = 2; // Nombre minimum de personnes avec le même anniversaire
        final double LEVEL = 0.95; // Niveau de confiance (1 - alpha, alpha = 0.05)
        final double INITIAL_HALF_WIDTH = 1e-4; // Demi-largeur initiale
        final long INITIAL_RUNS = 1_000_000; // Nombre initial de réalisations
        final long ADDITIONAL_RUNS = 100_000; // Incrément pour les réalisations supplémentaires
        final long SEED = 0x134D6EE; // Graine pour la reproductibilité

        Random rnd = new Random(SEED);
        Experiment bdayExperiment = new BirthdayaradoxExperiment(K, Y, M);
        StatCollector stat = new StatCollector();

        double maxHalfWidth = INITIAL_HALF_WIDTH;

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




        // Deuxième simulation: Seuil de couverture des intervalles de confiances du théorème central limite
        final double TRUE_P23 = 0.5072972343;
        //reinitialisation du random
        rnd.setSeed(SEED);
        rnd.setSeed(SEED); // Réinitialisation du générateur pseudo-aléatoire

        int countContainingTrueP = 0; // Nombre d'intervalles contenant la vraie valeur de p23
        final long NUM_SIMULATIONS = 1000;
        // Effectuer 1000 simulations
        for (int i = 0; i < NUM_SIMULATIONS; i++) {
            StatCollector stat = new StatCollector();
            Experiment bdayExperiment_ = new BirthdayaradoxExperiment(K, Y, M);

            // Générer N réalisations pour cette simulation
            MonteCarloSimulation.simulateNRuns(bdayExperiment_, N, rnd, stat);

            // Calcul de l'intervalle de confiance
            double estimatedP = stat.getAverage();
            double halfWidth = stat.getConfidenceIntervalHalfWidth(LEVEL);
            double lowerBound = estimatedP - halfWidth;
            double upperBound = estimatedP + halfWidth;

            // Vérifier si la vraie valeur p23 se trouve dans l'intervalle
            if (TRUE_P23 >= lowerBound && TRUE_P23 <= upperBound) {
                countContainingTrueP++;
            }
        }

        // Calcul du seuil empirique de couverture
        double empiricalCoverage = (double) countContainingTrueP / NUM_SIMULATIONS;

        // Calcul de l'intervalle de confiance associé (toujours à 95%)
        double stdError = Math.sqrt((empiricalCoverage * (1 - empiricalCoverage)) / NUM_SIMULATIONS);
        double zValue = 1.96; // Pour un seuil de confiance à 95%
        double coverageLowerBound = empiricalCoverage - zValue * stdError;
        double coverageUpperBound = empiricalCoverage + zValue * stdError;

        // Afficher les résultats
        System.out.printf("Seuil empirique de couverture: %.5f%n", empiricalCoverage);
        System.out.printf("Intervalle de confiance pour la couverture (95%%): [%.5f, %.5f]%n",
                coverageLowerBound, coverageUpperBound);





    }

}
