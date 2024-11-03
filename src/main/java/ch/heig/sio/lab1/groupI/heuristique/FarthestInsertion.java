package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.tsp.TspData;

import java.util.Arrays;
import java.util.List;

/**
 * <p>The {@code FarthestInsertion} class implements the farthest insertion heuristic
 * for solving the Traveling Salesman Problem (TSP).</p>
 *
 * <p>This heuristic starts by selecting the city farthest from the starting city
 * and builds the tour by repeatedly inserting the city that is farthest from any
 * city already in the tour.</p>
 *
 * <p>Complexity (space and time): O(n^2)</p>
 *
 */
public class FarthestInsertion extends InsertionHeuristic {

    /** An array storing the minimum distances from each city to the current tour. */
    private long[] minDistances;

    /**
     * Initializes the tour and heuristic-specific data structures for the farthest insertion heuristic.
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

        // Find the city farthest from the starting city
        int farthestCity = -1;
        long maxDistance = Long.MIN_VALUE;

        for (int city = 0; city < n; city++) {
            if (city != startCityIndex) {
                long distance = data.getDistance(startCityIndex, city);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestCity = city;
                }
            }
        }

        // Initialize the tour with the starting city and the farthest city
        tourList.add(startCityIndex);
        tourList.add(farthestCity);
        inTour[startCityIndex] = true;
        inTour[farthestCity] = true;

        // Add the initial length of the tour (round trip between the two cities)
        length += data.getDistance(startCityIndex, farthestCity);
        length += data.getDistance(farthestCity, startCityIndex); // Add the return trip

        // Initialize minDistances for the remaining cities
        for (int city = 0; city < n; city++) {
            if (!inTour[city]) {
                minDistances[city] = Math.min(
                        data.getDistance(city, startCityIndex),
                        data.getDistance(city, farthestCity)
                );
            }
        }
    }

    /**
     * Selects the next city to insert into the tour.
     * The city selected is the one with the maximum minimum distance to the current tour.
     *
     * @param data    The TSP data.
     * @param inTour  Array indicating whether a city is already in the tour.
     * @return        The index of the next city to insert.
     */
    @Override
    protected int selectNextCity(TspData data, boolean[] inTour) {
        int bestCity = -1;
        long maxMinDistance = Long.MIN_VALUE;

        // Iterate over all cities to find the one with the maximum minimum distance to the tour
        for (int city = 0; city < n; city++) { // Use n from the base class
            if (!inTour[city] && minDistances[city] > maxMinDistance) {
                maxMinDistance = minDistances[city];
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
                long distance = data.getDistance(city, insertedCity);
                if (distance < minDistances[city]) {
                    minDistances[city] = distance;
                }
            }
        }
    }
}
