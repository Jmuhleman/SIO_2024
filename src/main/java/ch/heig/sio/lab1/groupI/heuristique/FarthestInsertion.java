package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.Edge;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.util.Arrays;
import java.util.LinkedList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class FarthestInsertion implements ObservableTspConstructiveHeuristic {

    private static final class Traversal implements Iterator<Edge> {
        private final LinkedList<Integer> tour;  // List representing the current tour
        private int i = 0;  // Iterator index

        Traversal(LinkedList<Integer> tour) {
            this.tour = tour;
        }

        @Override
        public boolean hasNext() {
            return i < tour.size();
        }

        @Override
        public Edge next() {
            if (!hasNext())
                throw new NoSuchElementException();

            // Get the edge between two consecutive cities, handle wrap-around (last city -> first city)
            int from = tour.get(i);
            int to = tour.get((i + 1) % tour.size());
            i++;
            return new Edge(from, to);
        }
    }

    @Override
    public TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer) {
        int n = data.getNumberOfCities();
        // tableau pour suivre les villes insérées dans le tour
        LinkedList<Integer> tour = new LinkedList<>();
        long fullPathLength = 0;
        // tableau d'indexation de chaque ville 1 insérée 0 non insérée
        boolean[] insertedCities = new boolean[n];

        // tableau pour suivre les villes insérées dans le tour
        long[] distances = new long[n];

        // Initialiser le tour avec la ville de départ
        tour.add(startCityIndex);
        insertedCities[startCityIndex] = true;

        // construire le tableau des distances
        Arrays.fill(distances, Long.MAX_VALUE);
        for (int i = 0; i < n; ++i) {
            if (!insertedCities[i]) {
                distances[i] = data.getDistance(startCityIndex, i);
            }
        }


        // Continuer à insérer des villes jusqu'à ce que toutes les villes soient insérées
        while (tour.size() < n) {
            // Trouver la ville la plus éloignée de toutes les villes insérées
            int farthestCity = -1;
            long maxDistance = Long.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                if (!insertedCities[i] && distances[i] > maxDistance) {
                    maxDistance = distances[i];
                    farthestCity = i;
                }
            }

            // Chercher la meilleure position pour insérer la ville la plus éloignée
            int bestInsertPosition = -1;
            long bestInsertCost = Long.MAX_VALUE;
            for (int i = 0; i < tour.size(); ++i) {
                int from = tour.get(i);
                int to = tour.get((i + 1) % tour.size());
                long insertCost = data.getDistance(from, farthestCity) +
                        data.getDistance(farthestCity, to) -
                        data.getDistance(from, to);
                if (insertCost < bestInsertCost) {
                    bestInsertCost = insertCost;
                    bestInsertPosition = i + 1;
                }
            }

            // Insérer la ville la plus éloignée à la meilleure position dans le tour
            tour.add(bestInsertPosition, farthestCity);
            fullPathLength += bestInsertCost;
            insertedCities[farthestCity] = true;

            // Parcourir toutes les villes non insérées et mettre à jour les distances
            // Puisque les distances ne peuvent que diminuer (augmentation de la tournée),
            // nous n'avons besoin de comparer uniquement les distances par rapport à la ville insérée (farthestCity)
            for (int i = 0; i < n; i++) {
                if (!insertedCities[i]) {
                    long distance = data.getDistance(farthestCity, i);
                    if (distance < distances[i]) {
                        distances[i] = distance;
                    }
                }
            }
            observer.update(new FarthestInsertion.Traversal(tour));
        }


        // Construire le tableau de la tournée finale
        int[] tourFinal = new int[n];
        for (int i = 0; i < n; i++) {
            tourFinal[i] = tour.get(i);
        }

        // Ajouter la distance pour fermer la boucle
        fullPathLength += data.getDistance(tour.getLast(), tour.getFirst());

        return new TspTour(data, tourFinal, fullPathLength);
    }
}