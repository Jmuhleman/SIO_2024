package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.tsp.TspData;

import java.util.Arrays;
import java.util.List;

/**
 * <p>The {@code NearestInsertion} class implements the nearest insertion heuristic
 * for solving the Traveling Salesman Problem (TSP).</p>
 *
 * <p>This heuristic starts with a single city and iteratively inserts the nearest
 * city not yet in the tour to the existing tour, aiming to minimize the increase
 * in the total tour length at each step.</p>
 *
 * <p>Complexity (space and time): O(n^2)</p>
 * Author: Dr. Ing. Julien Billeter, Julien Muhlëmann, Crishtian Ronquillo
 */
public class NearestInsertion extends InsertionHeuristic {

    /** An array storing the minimum distances from each city to the current tour. */
    private long[] minDistances;

    /**
     * Initializes the tour and heuristic-specific data structures for the nearest insertion heuristic.
     *
     * @param data            The TSP data.
     * @param startCityIndex  The index of the starting city.
     * @param tourList        The list representing the current tour.
     * @param inTour          Array indicating whether a city is already in the tour.
     */
    @Override
    protected void initialize(TspData data, int startCityIndex, List<Integer> tourList, boolean[] inTour) {
        // Initialize the minDistances array to store the minimum distance from each city to the tour
        minDistances = new long[n]; // Use n from the base class
        Arrays.fill(minDistances, Long.MAX_VALUE);

        // Initialize the tour with the starting city
        tourList.add(startCityIndex);
        inTour[startCityIndex] = true;

        // Initialize minDistances for the remaining cities
        for (int city = 0; city < n; city++) {
            if (!inTour[city]) {
                minDistances[city] = data.getDistance(startCityIndex, city);
            }
        }
    }

    /**
     * Selects the next city to insert into the tour.
     * The city selected is the one with the smallest minimum distance to the current tour.
     *
     * @param data    The TSP data.
     * @param inTour  Array indicating whether a city is already in the tour.
     * @return        The index of the next city to insert.
     */
    @Override
    protected int selectNextCity(TspData data, boolean[] inTour) {
        int bestCity = -1;
        long minDistance = Long.MAX_VALUE;

        // Iterate over all cities to find the one with the smallest minimum distance to the tour
        for (int city = 0; city < n; city++) { // Use n from the base class
            if (!inTour[city] && minDistances[city] < minDistance) {
                minDistance = minDistances[city];
                bestCity = city;
            }
        }
        return bestCity;
    }

    /**
     * Updates the minDistances array after inserting a new city into the tour.
     *
     * @param data         The TSP data.
     * @param insertedCity The index of the city that has just been inserted.
     * @param inTour       Array indicating whether a city is already in the tour.
     */
    @Override
    protected void updateAfterInsertion(TspData data, int insertedCity, boolean[] inTour) {
        // Update the minimum distances for cities not yet in the tour
        for (int city = 0; city < n; city++) { // Use n from the base class
            if (!inTour[city]) {
                long distance = data.getDistance(insertedCity, city);
                if (distance < minDistances[city]) {
                    minDistances[city] = distance;
                }
            }
        }
    }
}
