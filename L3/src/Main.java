import montecarlo.BirthdayParadoxExperiment;
import montecarlo.MonteCarloSimulation;
import statistics.StatCollector;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        System.out.println("Simulation du paradoxe des anniversaires");
        System.out.println("=========================================");
        System.out.println("Permière expérience : Estimation de la probabilité p23");
        // Première simulation : Estimation de la probabilité p23
        final int    K                  = 23; // Taille du groupe
        final int    Y                  = 365; // Nombre de jours dans une année
        int          M                  = 2; // Nombre minimum de personnes avec le même anniversaire
        final double LEVEL              = 0.95; // Niveau de confiance (1 - alpha, alpha = 0.05)
        final double INITIAL_HALF_WIDTH = 1e-4; // Demi-largeur initiale
        final long   INITIAL_RUNS       = 1_000_000; // Nombre initial de réalisations
        final long   ADDITIONAL_RUNS    = 100_000; // Incrément pour les réalisations supplémentaires
        final long   SEED               = 0x134D6EE; // Graine pour la reproductibilité
        int          NB_ITERATIONS      = 3; // Nombre de simulations

        Random rnd = new Random(SEED);
        BirthdayParadoxExperiment bDayExperiment = new BirthdayParadoxExperiment(K, Y, M);
        StatCollector stat  = new StatCollector();
        double maxHalfWidth = INITIAL_HALF_WIDTH;

        for (int iteration = 1 ; iteration <= NB_ITERATIONS ; iteration++) {
            System.out.printf("Itération %d:%n", iteration);

            long startTime = System.nanoTime();

            MonteCarloSimulation.simulateTillGivenCIHalfWidth(
                    bDayExperiment, LEVEL, maxHalfWidth, INITIAL_RUNS, ADDITIONAL_RUNS, rnd, stat);

            long endTime = System.nanoTime();
            double elapsedTimeInSeconds = (endTime - startTime) / 1e9;

            // calcul des estimateurs selon la simulation actuelle
            double average   = stat.getAverage();
            double halfWidth = stat.getConfidenceIntervalHalfWidth(LEVEL);
            long totalRuns   = stat.getNumberOfObs();

            System.out.printf("  Probabilité estimée (p̂): %.5f%n", average);
            System.out.printf("  Intervalle de confiance (95%%): [%.5f, %.5f]%n",
                    average - halfWidth, average + halfWidth);
            System.out.printf("  Demi-largeur de l'intervalle de confiance: %.6f%n", halfWidth);
            System.out.printf("  Nombre total de réalisations générées: %d%n", totalRuns);
            System.out.printf("  Temps d'exécution: %.1f secondes%n%n", elapsedTimeInSeconds);

            maxHalfWidth /= 2;
        }

        // Deuxième simulation : Seuil de couverture des intervalles de confiance
        System.out.println("=========================================");
        System.out.println("Deuxième expérience : Seuil de couverture des intervalles de confiance");
        long         N                    = 1_000_000; // Nombre de réalisations pour chaque simulation
        final double TRUE_P23             = 0.5072972343; // Valeur théorique de p23
        final long   NUM_SIMULATIONS      = 1000; // Nombre total de simulations
        int          countContainingTrueP = 0; // Nombre d'intervalles contenant la vraie valeur de p23
        rnd.setSeed(SEED); // réinitialisation de la seed pour la nouvelle expérience

        for (int i = 0; i < NUM_SIMULATIONS; i++) {
            stat.init();
            MonteCarloSimulation.simulateNRuns(bDayExperiment, N, rnd, stat);

            // Calculer l'intervalle de confiance
            double estimatedP = stat.getAverage();
            double halfWidth  = stat.getConfidenceIntervalHalfWidth(LEVEL);
            double lowerBound = estimatedP - halfWidth;
            double upperBound = estimatedP + halfWidth;

            // Vérifier si la vraie valeur p23 est dans l'intervalle
            if (TRUE_P23 >= lowerBound && TRUE_P23 <= upperBound) {
                countContainingTrueP++;
            }
        }

        // Calcul du seuil empirique de couverture
        double empiricalCoverage = (double) countContainingTrueP / NUM_SIMULATIONS;

        // Calcul de l'intervalle de confiance associé 95%, valeur Z, bornes, variance
        double stdError           = Math.sqrt((empiricalCoverage * (1 - empiricalCoverage)) / NUM_SIMULATIONS);
        double zValue             = 1.96;
        double coverageLowerBound = empiricalCoverage - zValue * stdError;
        double coverageUpperBound = empiricalCoverage + zValue * stdError;

        System.out.printf("Seuil empirique de couverture: %.3f%n", empiricalCoverage);
        System.out.printf("Intervalle de confiance pour la couverture (95%%): [%.3f, %.3f]%n",
                coverageLowerBound, coverageUpperBound);

        System.out.println("=========================================");
        System.out.println("Troisième expérience : Détermination du nombre minimal K");

        // Troisième simulation : Déterminer le nombre minimal K
        final int    MIN_K     = 80; // Taille minimale du groupe
        final int    MAX_K     = 100; // Taille maximale du groupe
                     M         = 3; // Nombre minimum de personnes partageant un anniversaire
        final double THRESHOLD = 0.5; // Seuil de probabilité

        int    minimalK   = -1; // Variable pour stocker le K trouvé
        double estimatedP = 0.0; // Probabilité estimée pour le K trouvé
        rnd.setSeed(SEED);

        for (int actual_k = MIN_K; actual_k <= MAX_K; actual_k++) {
            stat.init();
            bDayExperiment.set(actual_k, Y, M);
            // Générer N réalisations pour ce K
            MonteCarloSimulation.simulateNRuns(bDayExperiment, N, rnd, stat);
            // Calculer la probabilité estimée
            estimatedP = stat.getAverage();
            // Vérifier si la probabilité dépasse le seuil
            if (estimatedP > THRESHOLD) {
                minimalK = actual_k; // Stocker le premier K satisfaisant la condition
                break;
            }
        }

        if (minimalK != -1) {
            System.out.printf("Nombre minimal K : %d%n", minimalK);
            System.out.printf("Probabilité estimée pour K = %d : %.4f%n", minimalK, estimatedP);
        } else {
            System.out.println("Aucun K trouvé entre " + MIN_K + " et " + MAX_K + " satisfaisant la condition.");
        }
    }
}
