package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.util.*;

public class FarthestInsertion extends InsertionHeuristic {

    @Override
    public TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer) {
        int n = data.getNumberOfCities();
        List<Integer> tourList = new ArrayList<>();
        boolean[] inTour = new boolean[n];
        long length = 0;

        // *** Étape 1: Initialisation avec deux villes ***
        // Trouver la ville la plus éloignée de la ville de départ
        int farthestCity = -1;
        long maxDistance = Long.MIN_VALUE;

        for (int city = 0; city < n; city++) {
            long distance = data.getDistance(startCityIndex, city);
            if (city != startCityIndex && distance > maxDistance) {
                maxDistance = distance;
                farthestCity = city;
            }
        }

        // Initialiser la tournée avec la ville de départ et la ville la plus éloignée
        tourList.add(startCityIndex);
        tourList.add(farthestCity);
        inTour[startCityIndex] = true;
        inTour[farthestCity] = true;
        length += data.getDistance(startCityIndex, farthestCity);
        length += data.getDistance(farthestCity, startCityIndex); // Fermeture du cycle

        // *** Étape 2: Initialiser minDistances ***
        long[] minDistances = new long[n];
        Arrays.fill(minDistances, Long.MAX_VALUE);
        for (int city = 0; city < n; city++) {
            if (!inTour[city]) {
                minDistances[city] = Math.min(
                        data.getDistance(city, startCityIndex),
                        data.getDistance(city, farthestCity)
                );
            }
        }

        observer.update(new FarthestInsertion.Traversal(tourList));

        // *** Étape 3: Construction de la tournée ***
        while (tourList.size() < n) {
            // Trouver la ville non visitée la plus éloignée de la tournée
            int bestCity = -1;
            long maxMinDistance = Long.MIN_VALUE;
            for (int city = 0; city < n; city++) {
                if (!inTour[city] && minDistances[city] > maxMinDistance) {
                    maxMinDistance = minDistances[city];
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
                    long distance = data.getDistance(city, bestCity);
                    if (distance < minDistances[city]) {
                        minDistances[city] = distance;
                    }
                }
            }

            observer.update(new FarthestInsertion.Traversal(tourList));
        }

        // Construire le tableau de la tournée finale
        int[] tour = new int[n];
        for (int i = 0; i < n; i++) {
            tour[i] = tourList.get(i);
        }

        // La longueur totale du tour a déjà été calculée lors des insertions

        return new TspTour(data, tour, length);
    }
}
