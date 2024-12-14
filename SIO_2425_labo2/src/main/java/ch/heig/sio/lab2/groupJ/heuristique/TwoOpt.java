package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.Edge;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implémentation de l’heuristique 2-opt utilisant une approche de « meilleure amélioration ».
 * <p>
 * Cette heuristique part d’une tournée initiale et explore l'ensemble des 2-échanges possibles
 * pour identifier celui qui offre la plus grande réduction de distance. À chaque itération,
 * le meilleur échange est appliqué jusqu’à ce qu’aucune amélioration ne soit possible,
 * garantissant un optimum local dans l’espace des solutions définies par les 2-échanges.
 * </p>
 *
 * <p>
 * Auteurs :
 * <ul>
 * <li>Julien Mühlemann</li>
 * <li>Crishtian Ronquillo</li>
 * <li>Dr. Ing. Julien Billeter</li>
 * </ul>
 * </p>
 */
public class TwoOpt implements ObservableTspImprovementHeuristic {

    /**
     * Classe interne permettant de parcourir les arêtes d'une tournée.
     * <p>
     * Elle est utilisée pour transmettre la structure d’une solution à un observateur,
     * facilitant la visualisation ou l'analyse de l’évolution de l'algorithme.
     * </p>
     */
    private static final class Traversal implements Iterator<Edge> {
        private final int[] tour;
        private int index;

        /**
         * Initialise le parcours des arêtes d'une tournée.
         *
         * @param tour Tableau représentant la tournée.
         */
        Traversal(int[] tour) {
            this.tour  = tour;
            this.index = 0;
        }

        /**
         * Vérifie s'il reste des arêtes à parcourir.
         *
         * @return true s'il existe une arête suivante, false sinon.
         */
        @Override
        public boolean hasNext() {
            return index < tour.length;
        }

        /**
         * Renvoie l’arête suivante dans la tournée.
         *
         * @return L’arête suivante sous forme d’objet {@link Edge}.
         * @throws NoSuchElementException si aucune arête n'est disponible.
         */
        @Override
        public Edge next() {
            if (!hasNext()) throw new NoSuchElementException();
            int from = tour[index];
            int to   = tour[(index + 1) % tour.length];
            index++;
            return new Edge(from, to);
        }
    }

    /**
     * Inverse l’ordre des villes dans le tableau 'tour' entre les indices i et j (inclus).
     * Cette opération correspond à l’application du 2-échange sur le segment concerné.
     *
     * @param tour Le tableau représentant la tournée.
     * @param i    Indice de début.
     * @param j    Indice de fin.
     */
    private void twoOptSwap(int[] tour, int i, int j) {
        while (i < j) {
            int temp = tour[i];
            tour[i]  = tour[j];
            tour[j]  = temp;
            i++;
            j--;
        }
    }

    /**
     * Calcule le gain de distance obtenu par un 2-échange entre deux arêtes.
     * <p>
     * Le gain correspond à la différence entre la distance des arêtes supprimées
     * et celle des nouvelles arêtes introduites par le 2-échange.
     * </p>
     *
     * @param data Les données du TSP (permettent d’accéder aux distances).
     * @param i    Indice i dans la tournée.
     * @param j    Indice j dans la tournée, avec j > i.
     * @param tour Le tableau représentant la tournée.
     * @return Le gain de distance (positif si améliorant).
     */
    private long computeDistanceGain(TspData data, int i, int j, int[] tour) {
        int n   = tour.length;
        int ip1 = (i + 1) % n;
        int jp1 = (j + 1) % n;

        long distOriginale = data.getDistance(tour[i], tour[ip1]) + data.getDistance(tour[j], tour[jp1]);
        long distNouvelle  = data.getDistance(tour[i], tour[j]) + data.getDistance(tour[ip1], tour[jp1]);

        return distOriginale - distNouvelle;
    }

    /**
     * Identifie le 2-échange offrant la meilleure amélioration possible.
     * <p>
     * Parcourt toutes les paires possibles (i, j) dans la tournée pour trouver
     * l'échange avec le gain de distance le plus élevé.
     * </p>
     *
     * @param data Le problème TSP
     * @param tour Le tableau représentant la tournée actuelle.
     * @return Un tableau [bestI, bestJ, bestGain] donnant les indices i, j du meilleur échange trouvé
     * ainsi que son gain. Si aucun échange n’est améliorant, bestGain <= 0.
     */
    private long[] searchBest2OptMove(TspData data, int[] tour) {
        long bestGain = 0;
        int bestI     = -1;
        int bestJ     = -1;
        int n         = tour.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                if ((i == 0 && j == n - 1)) {
                    continue;
                }

                long gain = computeDistanceGain(data, i, j, tour);
                if (gain > bestGain) {
                    bestGain = gain;
                    bestI    = i;
                    bestJ    = j;
                }
            }
        }

        return new long[]{bestI, bestJ, bestGain};
    }

    /**
     * Applique l’algorithme 2-opt à une tournée initiale pour l'optimiser localement.
     *
     * @param initialTour La tournée initiale.
     * @param observer    Observateur permettant de visualiser l'évolution du tour.
     * @return Une nouvelle tournée (de type TspTour) améliorée localement par 2-opt.
     */
    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {
        int[] currentTour = initialTour.tour().copy();
        TspData data      = initialTour.data();
        long bestDistance = initialTour.length();

        boolean improvement = true;
        while (improvement) {
            improvement = false;

            // Recherche du meilleur 2-échange
            long[] bestMove = searchBest2OptMove(data, currentTour);
            int bestI       = (int) bestMove[0];
            int bestJ       = (int) bestMove[1];
            long bestGain   = bestMove[2];

            // Si le gain est positif, on applique le 2-échange
            if (bestGain > 0) {
                twoOptSwap(currentTour, bestI + 1, bestJ);

                // Mise à jour de la distance totale
                bestDistance -= bestGain;
                improvement   = true;

                if (observer != null) {
                    observer.update(new Traversal(currentTour));
                }
            }
        }

        return new TspTour(data, currentTour, bestDistance);
    }
}
