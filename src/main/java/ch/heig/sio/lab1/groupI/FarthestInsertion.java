package ch.heig.sio.lab1.groupI;

import ch.heig.sio.lab1.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.Edge;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;
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
        LinkedList<Integer> tour = new LinkedList<>();
        long fullPathLength = 0;

        // tableau pour suivre les villes insérées dans le tour
        boolean[] insertedCities = new boolean[n];
        insertedCities[startCityIndex] = true;

        // Initialiser le tour avec la ville de départ
        tour.add(startCityIndex);

        // Trouver la ville la plus éloignée de la ville de départ
        int farthestCity = -1;
        long farthestDistance = 0;
        for (int i = 0; i < n; i++) {
            if (i != startCityIndex) {
                long distance = data.getDistance(startCityIndex, i);
                if (distance > farthestDistance) {
                    farthestDistance = distance;
                    farthestCity = i;
                }
            }
        }
        // Insérer la ville la plus éloignée dans le tour et mettre à jour la longueur du chemin
        tour.add(farthestCity);
        insertedCities[farthestCity] = true;
        fullPathLength += farthestDistance;

        observer.update(new Traversal(new LinkedList<>(tour)));

        // Continuer à insérer des villes jusqu'à ce que toutes les villes soient insérées
        while (tour.size() < n) {
            // Trouver la ville la plus éloignée de toutes les villes insérées
            farthestCity = -1;
            farthestDistance = -1;
            for (int i = 0; i < n; i++) {
                if (!insertedCities[i]) {
                    for (int cityInTour : tour) {
                        long distance = data.getDistance(cityInTour, i);
                        if (distance > farthestDistance) {
                            farthestDistance = distance;
                            farthestCity = i;
                        }
                    }
                }
            }

            // Chercher la meilleure position pour insérer la ville la plus éloignée
            int bestInsertPosition = 0;
            long bestInsertCost = Long.MAX_VALUE;
            int prevCity = tour.getLast(); // Commencer par la dernière ville du tour
            // LinkedList est doublement chainée -> O(1) pour obtenir l'index
            for (int cityInTour : tour) {
                //calcul du coût d'insertion
                long insertCost = data.getDistance(prevCity, farthestCity)
                        + data.getDistance(farthestCity, cityInTour)
                        - data.getDistance(prevCity, cityInTour);

                if (insertCost < bestInsertCost) {
                    bestInsertCost = insertCost;
                    bestInsertPosition = tour.indexOf(cityInTour);
                }

                prevCity = cityInTour;
            }

            // Inserrer la ville la plus éloignée à la meilleure position dans le tour
            tour.add(bestInsertPosition, farthestCity);
            insertedCities[farthestCity] = true;
            fullPathLength += bestInsertCost;
            observer.update(new Traversal(new LinkedList<>(tour)));
        }
        //passer en tableau pour le dernier return
        int[] finalTour = tour.stream().mapToInt(Integer::intValue).toArray();
        return new TspTour(data, finalTour, fullPathLength);
    }
}