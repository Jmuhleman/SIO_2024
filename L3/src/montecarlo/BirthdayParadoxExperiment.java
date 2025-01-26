package montecarlo;

import java.util.Random;

public class BirthdayParadoxExperiment implements Experiment {
    private int k;
    private int y;
    private int m;

    /**
     * Constructeur de la classe BirthdayParadoxExperiment avec passage de paramètres
     *
     * @param k
     * @param y
     * @param m
     */
    public BirthdayParadoxExperiment(int k, int y, int m) {
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
        /*
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
        */

        // Cas particulier : M = 2
        // -> un simple tableau de booléens suffit pour détecter la première collision
        if (m == 2) {
            boolean[] used = new boolean[y];
            for (int i = 0; i < k; i++) {
                int chosenDate = rnd.nextInt(y);
                if (used[chosenDate]) {
                    // On a trouvé une date déjà utilisée -> collision
                    return 1.0;
                }
                used[chosenDate] = true;
            }
            return 0.0; // aucune collision
        }
        /*
        else { // Cas général : m >= 2 (mais != 2)  -> tableau de compteurs
            int[] counts = new int[y];
            for (int i = 0; i < k; i++) {
                int chosenDate = rnd.nextInt(y);
                counts[chosenDate]++;
                if (counts[chosenDate] >= m) {
                    // Dès qu'on atteint m pour une date, on a un succès
                    return 1.0;
                }
            }
            // Aucune date n'a atteint m
            return 0.0;
        }
        */
        System.out.println("Erreur");
        return 0.0;
    }
}
