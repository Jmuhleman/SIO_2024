/**
 * AbstractInsertionHeuristic fournit un modèle pour construire des solutions heuristiques
 * pour le Problème du Voyageur de Commerce (TSP).
 * Cette classe abstraite facilite la création de tournées en ajoutant des villes
 * de manière itérative, en fonction d'une heuristique spécifique.
 * <p>
 * Les sous-classes définissent la stratégie de sélection de la prochaine ville à insérer
 * en implémentant la méthode {@link #selectNextCity(long[], boolean[])}. La classe abstraite
 * gère l'insertion de la ville sélectionnée à la position optimale dans la tournée pour
 * minimiser la distance totale du parcours.
 * <p>
 * @authors Ronquillo Cristhian, Muhlemann Julien
 */


package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.Edge;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.util.*;

public abstract class AbstractInsertionHeuristic implements ObservableTspConstructiveHeuristic {

    private static final class Traversal implements Iterator<Edge> {
        private final List<Integer> tour;
        private int index;

        Traversal(List<Integer> tour) {
            this.tour = tour;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < tour.size();
        }

        @Override
        public Edge next() {
            if (!hasNext()) throw new NoSuchElementException();
            int from = tour.get(index);
            int to = tour.get((index + 1) % tour.size());
            index++;
            return new Edge(from, to);
        }
    }

    @Override
    public TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer) {
        int n = data.getNumberOfCities();
        List<Integer> tourList = new ArrayList<>();
        boolean[] inTour = new boolean[n];
        long length = 0;

        // Initialiser la ville de départ
        tourList.add(startCityIndex);
        inTour[startCityIndex] = true;

        // Construire la liste des distances aux villes non visitées
        long[] distances = new long[n];
        Arrays.fill(distances, Long.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            if (!inTour[i]) {
                distances[i] = data.getDistance(startCityIndex, i);
            }
        }

        // Rechercher la prochaine ville à insérer selon l'heuristique
        while (tourList.size() < n) {
            int nextCity = selectNextCity(distances, inTour);

            // Choisir la meilleure position pour insérer nextCity
            int bestPosition = -1;
            long minIncrease = Long.MAX_VALUE;
            for (int i = 0; i < tourList.size(); i++) {
                int from = tourList.get(i);
                int to = tourList.get((i + 1) % tourList.size());
                long increase = data.getDistance(from, nextCity) + data.getDistance(nextCity, to) - data.getDistance(from, to);
                if (increase < minIncrease) {
                    minIncrease = increase;
                    bestPosition = i + 1;
                }
            }

            // Insérer la ville dans la tournée
            tourList.add(bestPosition, nextCity);
            inTour[nextCity] = true;
            length += minIncrease;

            // Parcourir toutes les villes non insérées et mettre à jour les distances
            // Puisque les distances ne peuvent que diminuer (augmentation de la tournée),
            // nous n'avons besoin de comparer uniquement les distances par rapport à la ville insérée (nextCity)
            for (int i = 0; i < n; i++) {
                if (!inTour[i]) {
                    long distance = data.getDistance(nextCity, i);
                    if (distance < distances[i]) {
                        distances[i] = distance;
                    }
                }
            }

            observer.update(new Traversal(tourList));
        }

        // Construire le tableau de la tournée finale
        int[] tourArray = new int[n];
        for (int i = 0; i < n; i++) {
            tourArray[i] = tourList.get(i);
        }
        // Boucler la tournée avec la dernière distance
        length += data.getDistance(tourList.get(n - 1), tourList.getFirst());

        return new TspTour(data, tourArray, length);
    }
    // sélection de la prchaine ville selon la stratégie de l'heuristique
    protected abstract int selectNextCity(long[] distances, boolean[] inTour);
}
