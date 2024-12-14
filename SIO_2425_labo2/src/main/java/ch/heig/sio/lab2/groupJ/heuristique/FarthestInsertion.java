package ch.heig.sio.lab2.groupJ.heuristique;

import java.util.Arrays;
import java.util.List;

import ch.heig.sio.lab2.tsp.TspData;


/**
 * <p>The {@code FarthestInsertion} class implements the farthest insertion heuristic
 * for solving the Traveling Salesman Problem (TSP).</p>
 *
 * <p>This heuristic starts by selecting the city farthest from the starting city
 * and builds the tour by repeatedly inserting the city that is farthest from any
 * city already in the tour.</p>
 *
 * <p>Complexity (space and time): O(n) and O(n^2)</p>
 * Author: Julien Mühlemann, Cristhian Ronquillo
 */
public class FarthestInsertion extends InsertionHeuristic {

    /**
     * Initializes the tour and heuristic-specific data structures for the farthest insertion heuristic.
     *
     * @param data           The TSP data.
     * @param startCityIndex The index of the starting city.
     * @param tourList       The list representing the current tour.
     * @param inTour         Array indicating whether a city is already in the tour.
     */
    @Override
    protected void initialize(TspData data, int startCityIndex, List<Integer> tourList, boolean[] inTour) {
        // Initialize the minDistances array
        minDistances = new long[n];
        Arrays.fill(minDistances, Long.MAX_VALUE);

        // Initialize the tour with the starting city only (comme NearestInsertion)
        tourList.add(startCityIndex);
        inTour[startCityIndex] = true;
        length = 0; // No distance yet, one city only

        // Initialize minDistances for the remaining cities
        for (int city = 0; city < n; city++) {
            if (!inTour[city]) {
                // Each distance of the remaining cities is the distance to the only city in the tour.
                minDistances[city] = data.getDistance(startCityIndex, city);
            }
        }
    }

    /**
     * Selects the next city to insert into the tour.
     * The city selected is the one with the maximum minimum distance to the current tour.
     *
     * @param data   The TSP data.
     * @param inTour Array indicating whether a city is already in the tour.
     * @return The index of the next city to insert.
     */
    @Override
    protected int selectNextCity(TspData data, boolean[] inTour) {
        int bestCity = -1;
        long maxMinDistance = Long.MIN_VALUE;

        for (int city = 0; city < n; city++) {
            if (!inTour[city] && minDistances[city] > maxMinDistance) {
                maxMinDistance = minDistances[city];
                bestCity = city;
            }
        }
        return bestCity;
    }
}
