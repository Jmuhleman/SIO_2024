package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.Edge;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;


import javax.security.sasl.SaslServer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implémentation de l’heuristique 2-opt « meilleure amélioration ».
 * Cette heuristique part d’une tournée initiale, puis recherche, à chaque itération,
 * le 2-échange offrant la plus grande réduction de la longueur du tour (c.-à-d. la "meilleure amélioration").
 * Elle applique ce 2-échange et répète le processus jusqu’à ce qu’aucune amélioration
 * ne soit trouvée, garantissant ainsi un minimum local par rapport à la structure
 * de voisinage définie par les 2-échanges.
 */
public class TwoOpt implements ObservableTspImprovementHeuristic {

    /**
     * Classe interne permettant de parcourir la tournée et retourner ses arêtes.
     * Utilisée pour informer l'observeur de l'évolution de la solution.
     */
    private static final class Traversal implements Iterator<Edge> {
        private final int[] tour;
        private int index;

        Traversal(int[] tour) {
            this.tour = tour;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < tour.length;
        }

        @Override
        public Edge next() {
            if (!hasNext()) throw new NoSuchElementException();
            int from = tour[index];
            int to = tour[(index + 1) % tour.length];
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
            tour[i] = tour[j];
            tour[j] = temp;
            i++;
            j--;
        }
    }

    /**
     * Calcule le gain en distance obtenu par un 2-échange (i, j).
     * On enlève les arêtes (i -> i+1) et (j -> j+1), puis on les remplace par (i -> j) et (i+1 -> j+1).
     * Le gain est : distanceAncienne - distanceNouvelle
     * Si ce gain est positif, le 2-échange est améliorant.
     *
     * @param data Les données du TSP (permettent d’accéder aux distances).
     * @param i    Indice i dans la tournée.
     * @param j    Indice j dans la tournée, avec j > i.
     * @param tour Le tableau représentant la tournée.
     * @return Le gain de distance (positif si améliorant).
     */
    private long computeDistanceGain(TspData data, int i, int j, int[] tour) {
        int n = tour.length;
        int ip1 = (i + 1) % n;
        int jp1 = (j + 1) % n;

        long distOriginale = data.getDistance(tour[i], tour[ip1]) + data.getDistance(tour[j], tour[jp1]);
        long distNouvelle = data.getDistance(tour[i], tour[j]) + data.getDistance(tour[ip1], tour[jp1]);

        return distOriginale - distNouvelle;
    }

    /**
     * Recherche le meilleur 2-échange améliorant, c’est-à-dire celui offrant le gain
     * de distance le plus élevé.
     *
     * @param data Le problème TSP
     * @param tour Le tableau représentant la tournée actuelle.
     * @return Un tableau [bestI, bestJ, bestGain] donnant les indices i, j du meilleur échange trouvé
     * ainsi que son gain. Si aucun échange n’est améliorant, bestGain <= 0.
     */
    private long[] searchBest2OptMove(TspData data, int[] tour) {
        long bestGain = 0;
        int bestI = -1;
        int bestJ = -1;
        int n = tour.length;

        // On parcours toutes les paires (i, j) avec i < j.
        // On ignore j = i+1 car cela ne modifie pas la tournée.
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                // Eviter le cas où j+1 = i (ce qui reviendrait simplement à un décalage).
                if ((i == 0 && j == n - 1)) {
                    // Ce 2-opt n'a aucun sens car il revient au même cycle non modifié.
                    continue;
                }

                long gain = computeDistanceGain(data, i, j, tour);
                if (gain > bestGain) {
                    bestGain = gain;
                    bestI = i;
                    bestJ = j;
                }
            }
        }

        return new long[]{bestI, bestJ, bestGain};
    }

    /**
     * Applique l’heuristique 2-opt « meilleure amélioration » sur la tournée initiale.
     *
     * @param initialTour La tournée initiale.
     * @param observer    Observateur permettant de visualiser l'évolution du tour.
     * @return Une nouvelle TspTour, améliorée localement par 2-opt.
     */
    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {
        int[] currentTour = initialTour.tour().copy();
        TspData data = initialTour.data();
        long bestDistance = initialTour.length();

        boolean improvement = true;
        while (improvement) {
            improvement = false;

            // Recherche du meilleur 2-échange
            long[] bestMove = searchBest2OptMove(data, currentTour);
            int bestI = (int) bestMove[0];
            int bestJ = (int) bestMove[1];
            long bestGain = bestMove[2];

            // Si on a trouvé une amélioration
            if (bestGain > 0) {
                // Appliquer le 2-échange en inversant le segment entre bestI+1 et bestJ
                twoOptSwap(currentTour, bestI + 1, bestJ);
                bestDistance -= bestGain;
                improvement = true;

                // Mise à jour de la solution pour observer l'évolution
                if (observer != null) {
                    observer.update(new Traversal(currentTour));
                }
            }
        }

        // Retourner la solution finale localement optimale
        return new TspTour(data, currentTour, bestDistance);
    }
}
