/**
 * Classe BirthdayParadoxExperiment
 * <p>
 * Cette classe simule une expérience en rapport avec le paradoxe des anniversaires.
 * L'expérience consiste à choisir aléatoirement K dates parmi Y dates possibles, et
 * retourner 1 (succès) si au moins une date est choisie M fois ou plus, sinon 0 (échec).
 * <p>
 * Le cas particulier où M = 2 est traité de manière optimisée avec un tableau de booléens,
 * tandis que pour les cas généraux (M > 2), un tableau de compteurs est utilisé pour suivre
 * la fréquence des dates choisies.
 * <p>
 * Cette classe implémente l'interface Experiment et la méthode `execute` qui réalise l'expérience.
 *
 * @author Julien Mühlemann, Cristhian Ronquillo, Dr. Ing. Julien Billeter
 * @since 2025-01-26
 */

package montecarlo;

import java.util.Random;

public class BirthdayParadoxExperiment implements Experiment {
    private int k;
    private int y;
    private int m;

    /**
     * Constructeur de la classe BirthdayParadoxExperiment avec passage de paramètres
     *
     * @param k Nombre de dates à choisir
     * @param y Nombre total de dates possibles
     * @param m Seuil de répétition d'une date pour succès
     */
    public BirthdayParadoxExperiment(int k, int y, int m) {
        this.k = k;
        this.y = y;
        this.m = m;
    }

    /**
     * Permet de modifier les paramètres de l'expérience.
     *
     * @param k Nouveau nombre de dates à choisir
     * @param y Nouveau nombre de dates possibles
     * @param m Nouveau seuil pour un succès
     */
    public void set(int k, int y, int m) {
        this.k = k;
        this.y = y;
        this.m = m;

    }

    /**
     * Réalise l'expérience de Bernoulli : choisir K dates parmi Y dates possibles,
     * et retourne 1 si au moins une date est choisie M fois ou plus, sinon 0.
     *
     * @param rnd Source de nombres aléatoires utilisée pour simuler l'expérience
     * @return 1 si l'expérience réussit (au moins une date choisie M fois), sinon 0
     */
    @Override
    public double execute(Random rnd) {

        // Cas particulier : M = 2
        // Un simple tableau de booléens suffit pour détecter la première collision
        // Car une seule occurrence multiple (au moins 2 personnes partageant une date)
        // suffit pour réussir l'expérience
        if (m == 2) {
            // Tableau booléen pour suivre les dates déjà utilisées
            boolean[] used = new boolean[y];

            for (int i = 0; i < k; i++) {
                // Tirage d'une date aléatoire parmi Y dates
                int chosenDate = rnd.nextInt(y);

                if (used[chosenDate]) {
                    // On a trouvé une date déjà utilisée -> collision
                    return 1.0;
                }
                used[chosenDate] = true; // Marquer la date comme utilisée
            }

            return 0.0; // aucune collision

        } else { // Cas général : m > 2 -> tableau de compteurs

            // Tableau pour compter le nombre de fois qu'une date est choisie
            int[] counts = new int[y];

            for (int i = 0; i < k; i++) {
                // Tirage d'une date aléatoire parmi Y dates
                int chosenDate = rnd.nextInt(y);
                counts[chosenDate]++; // Incrémenter le compteur pour cette date

                if (counts[chosenDate] >= m) {
                    // Dès qu'on atteint m pour une date, on a un succès
                    return 1.0;
                }
            }
            // Aucune date n'a atteint m
            return 0.0;
        }
    }
}
