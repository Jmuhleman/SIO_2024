/**
 * Classe MonteCarloSimulation
 * <p>
 * Cette classe fournit des méthodes pour réaliser des simulations de Monte Carlo simples. Elle permet
 * de simuler un certain nombre de répétitions d'une expérience, d'estimer un intervalle de confiance
 * à un niveau donné, et de déterminer le nombre de répétitions nécessaires pour atteindre une précision
 * donnée pour cet intervalle.
 * <p>
 * La classe utilise un générateur de nombres pseudo-aléatoires pour réaliser les simulations et collecte
 * les résultats dans un collecteur statistique afin de calculer des métriques comme la variance et l'intervalle de confiance.
 *
 * @author Julien Mühlemann, Cristhian Ronquillo, Dr. Ing. Julien Billeter
 * @since 2025-01-26
 */
package montecarlo;

import statistics.InverseStdNormalCDF;
import statistics.StatCollector;

import java.util.Random;

/**
 * This class provides methods for simple Monte Carlo simulations.
 */
public class MonteCarloSimulation {
    /**
     * Private constructor. Makes it impossible to instantiate.
     */
    private MonteCarloSimulation() {
    }

    /**
     * Simulates experiment exp n times, using rnd as a source of pseudo-random numbers and collect
     * the results in stat.
     *
     * @param exp  experiment to be run each time
     * @param n    number of runs to be performed
     * @param rnd  random source to be used to simulate the experiment
     * @param stat collector to be used to collect the results of each experiment
     */
    public static void simulateNRuns(Experiment exp,
                                     long n,
                                     Random rnd,
                                     StatCollector stat) {
        for (long run = 0; run < n; ++run) {
            stat.add(exp.execute(rnd));
        }
    }

    /**
     * First simulates experiment exp initialNumberOfRuns times, then estimates the number of runs
     * needed for a 95% confidence interval half width no more than maxHalfWidth. If final C.I. is
     * too wide, simulates additionalNumberOfRuns before recalculating the C.I. and repeats the process
     * as many times as needed.
     * <p>
     * Uses rnd as a source of pseudo-random numbers and collects the results in stat.
     *
     * @param exp                    experiment to be run each time
     * @param level                  confidence level of the confidence interval
     * @param maxHalfWidth           maximal half width of the confidence interval
     * @param initialNumberOfRuns    initial number of runs to be performed
     * @param additionalNumberOfRuns additional number of runs to be performed if C.I. is too wide
     * @param rnd                    random source to be used to simulate the experiment
     * @param stat                   collector to be used to collect the results of each experiment
     */
    public static void simulateTillGivenCIHalfWidth(Experiment exp,
                                                    double level,
                                                    double maxHalfWidth,
                                                    long initialNumberOfRuns,
                                                    long additionalNumberOfRuns,
                                                    Random rnd,
                                                    StatCollector stat) {
        // Étape 1 : Exécuter un premier lot de simulations (nombre initial donné)
        simulateNRuns(exp, initialNumberOfRuns, rnd, stat);

        // Étape 2 : Calculer une estimation du nombre total de simulations nécessaires
        long firstAdditionalNumberOfRuns =
                (long) Math.ceil(Math.pow(InverseStdNormalCDF.getQuantile((1 + level) / 2)
                        * Math.sqrt(stat.getVariance()) / stat.getConfidenceIntervalHalfWidth(level), 2));

        // Étape 3 : Effectuer le nombre supplémentaire de simulations estimées
        simulateNRuns(exp, firstAdditionalNumberOfRuns, rnd, stat);

        // Étape 4 : Vérifier si la demi-largeur est toujours trop grande
        if (stat.getConfidenceIntervalHalfWidth(level) > maxHalfWidth) {
            // Tant que la demi-largeur de l'IC dépasse la valeur cible, continuer à simuler
            while (stat.getConfidenceIntervalHalfWidth(level) > maxHalfWidth) {
                // Ajouter des simulations supplémentaires en lots fixes (additionalNumberOfRuns)
                simulateNRuns(exp, additionalNumberOfRuns, rnd, stat);
            }
        }
    }
}
