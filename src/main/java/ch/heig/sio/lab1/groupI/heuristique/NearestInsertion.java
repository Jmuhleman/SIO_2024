package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.Edge;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.util.*;

public class NearestInsertion extends InsertionHeuristic {

    @Override
    public TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer) {
        int n = data.getNumberOfCities();
        List<Integer> tourList = new ArrayList<>();
        boolean[] inTour = new boolean[n];
        long length = 0;

        // Initialisation
        tourList.add(startCityIndex);
        inTour[startCityIndex] = true;

        // Initialiser minDistances
        long[] minDistances = new long[n];
        Arrays.fill(minDistances, Long.MAX_VALUE);
        for (int city = 0; city < n; city++) {
            if (!inTour[city]) {
                minDistances[city] = data.getDistance(startCityIndex, city);
            }
        }

        while (tourList.size() < n) {
            // Trouver la ville non visitée la plus proche de la tournée
            int bestCity = -1;
            long minDistance = Long.MAX_VALUE;
            for (int city = 0; city < n; city++) {
                if (!inTour[city] && minDistances[city] < minDistance) {
                    minDistance = minDistances[city];
                    bestCity = city;
                }
            }

            // Trouver le meilleur endroit pour insérer bestCity
            int bestPosition = -1;
            long minIncrease = Long.MAX_VALUE;
            for (int i = 0; i < tourList.size(); i++) {
                int from = tourList.get(i);
                int to = tourList.get((i + 1) % tourList.size());
                long increase = data.getDistance(from, bestCity) + data.getDistance(bestCity, to) - data.getDistance(from, to);
                if (increase < minIncrease) {
                    minIncrease = increase;
                    bestPosition = i + 1;
                }
            }

            // Insérer bestCity dans la tournée
            tourList.add(bestPosition, bestCity);
            inTour[bestCity] = true;
            length += minIncrease;

            // Mettre à jour minDistances
            for (int city = 0; city < n; city++) {
                if (!inTour[city]) {
                    long distance = data.getDistance(bestCity, city);
                    if (distance < minDistances[city]) {
                        minDistances[city] = distance;
                    }
                }
            }

            observer.update(new NearestInsertion.Traversal(tourList));
        }

        // Construire le tableau de la tournée finale
        int[] tour = new int[n];
        for (int i = 0; i < n; i++) {
            tour[i] = tourList.get(i);
        }

        // Ajouter la distance pour fermer la boucle
        length += data.getDistance(tourList.get(n - 1), tourList.get(0));

        return new TspTour(data, tour, length);
    }
}
