package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.tsp.TspData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <p>The {@code RandomInsertion} class implements the random insertion heuristic
 * for solving the Traveling Salesman Problem (TSP).</p>
 *
 * <p>This heuristic starts with the starting city and randomly selects cities to insert
 * into the tour. At each step, it finds the best position to insert the selected city
 * to minimize the increase in the total tour length.</p>
 *
 * <p>Complexity (space and time): O(^2)</p>
 *
 */
public class RandomInsertion extends InsertionHeuristic {

    /** A list of cities that have not yet been inserted into the tour. */
    private List<Integer> remainingCities;

    /** A Random instance with a fixed seed for reproducibility. */
    private Random random;

    /**
     * Initializes the tour and heuristic-specific data structures for the random insertion heuristic.
     *
     * @param data            The TSP data.
     * @param startCityIndex  The index of the starting city.
     * @param tourList        The list representing the current tour.
     * @param inTour          Array indicating whether a city is already in the tour.
     */
    @Override
    protected void initialize(TspData data, int startCityIndex, List<Integer> tourList, boolean[] inTour) {
        // Initialize the list of remaining cities
        remainingCities = new ArrayList<>();
        // Initialize the random number generator with a fixed seed for consistent results
        random = new Random(0x134DA73);

        // Initialize the tour with the starting city
        tourList.add(startCityIndex);
        inTour[startCityIndex] = true;

        // Build the list of remaining cities
        for (int city = 0; city < n; city++) {
            if (!inTour[city]) {
                remainingCities.add(city);
            }
        }

        // Shuffle the remaining cities to randomize the insertion order
        Collections.shuffle(remainingCities, random);

        // Add the first random city to the tour to start the process
        if (!remainingCities.isEmpty()) {
            int firstCity = remainingCities.removeFirst(); // remainingCities.remove(0);
            tourList.add(firstCity);
            inTour[firstCity] = true;

            // Add the initial length of the tour (distance from starting city to first random city)
            length = data.getDistance(startCityIndex, firstCity);
            length += data.getDistance(firstCity, startCityIndex); // Add the return trip
        }
    }

    /**
     * Selects the next city to insert into the tour.
     * The city selected is the next city in the shuffled list of remaining cities.
     *
     * @param data    The TSP data.
     * @param inTour  Array indicating whether a city is already in the tour.
     * @return        The index of the next city to insert.
     */
    @Override
    protected int selectNextCity(TspData data, boolean[] inTour) {
        if (remainingCities.isEmpty()) {
            throw new IllegalStateException("No more cities to insert");
        }
        return remainingCities.removeFirst();
    }

    /**
     * Updates data structures after inserting a new city into the tour.
     * For RandomInsertion, no specific updates are necessary.
     *
     * @param data         The TSP data.
     * @param insertedCity The index of the city that has just been inserted.
     * @param inTour       Array indicating whether a city is already in the tour.
     */
    @Override
    protected void updateAfterInsertion(TspData data, int insertedCity, boolean[] inTour) {
        // No updates required for RandomInsertion
    }
}
