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

    public void set(int k, int y, int m) {
        this.k = k;
        this.y = y;
        this.m = m;

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
        int[] dates = new int[y];
        int[] randomValues = new int[k];
        for (int i = 0; i < k; i++) {
            randomValues[i] = rnd.nextInt(y); // Pré-calculer les valeurs aléatoires
        }

        for (int i = 0; i < k; i++) {
            int chosenDate = randomValues[i];
            dates[chosenDate]++;
            if (dates[chosenDate] >= m) {
                return 1.0;
            }
        }
        return 0.0;
    }
}
