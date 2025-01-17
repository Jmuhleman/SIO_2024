package montecarlo;

import java.util.Random;

public class BirthdayaradoxExperiment implements Experiment {
    private int k;
    private int y;
    private int m;

    /**
     * Constructeur de la classe BirthdayaradoxExperiment avec passage de paramètres
     *
     * @param k
     * @param y
     * @param m
     */
    public BirthdayaradoxExperiment(int k, int y, int m) {
        this.k = k;
        this.y = y;
        this.m = m;
    }

    /**
     * Constructeur de la classe BirthdayaradoxExperiment avec paramètres par défaut
     *
     */
    public BirthdayaradoxExperiment() {
        this.k = 23;
        this.y = 365;
        this.m = 2;
    }

    /**
     * expérience de Bernoulli suivante : choisir K dates au hasard, parmi Y dates équiprobables,
     * et retourner 1 (succès) si au moins une date a été choisie M fois ou plus et 0
     * (échec) sinon ;
     *
     * @param rnd random source to be used to simulate the experiment
     * @return
     */
    @Override
    public double execute(Random rnd) {
        // Array to count occurrences of each "date"
        int[] dates = new int[y];

        // Randomly assign k people to dates
        for (int i = 0; i < k; i++) {
            dates[rnd.nextInt(y)]++;
        }

        // Check if any date is chosen m times or more
        for (int i = 0; i < y; i++) {
            if (dates[i] >= m) {
                return 1.0;
            }
        }

        return 0.0;
    }
}
