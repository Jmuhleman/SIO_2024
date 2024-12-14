package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.Edge;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.util.*;

/**
 * <p>An abstract class {@code InsertionHeuristic} that implements a constructive heuristic
 * for solving the Traveling Salesman Problem (TSP) using insertion methods.</p>
 *
 * <p>This class provides the skeleton of the insertion algorithm, allowing subclasses
 * to define how cities are selected for insertion and how data structures are updated
 * after each insertion.</p>
 *
 * <p>Complexity (space and time): O(n^2)</p>
 * Author: Julien Mühlemann, Cristhian Ronquillo
 */
abstract class InsertionHeuristic implements ObservableTspConstructiveHeuristic {

    /**
     * An iterator to traverse the edges of the current tour.
     */
    protected static final class Traversal implements Iterator<Edge> {
        /**
         * The list of cities in the current tour.
         */
        private final List<Integer> tour;
        /**
         * The index of the next edge to return.
         */
        private int index;

        /**
         * Constructs a new iterator to traverse the edges of the given tour.
         *
         * @param tour The list of cities forming the tour.
         */
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
            if (!hasNext())
                throw new NoSuchElementException();
            int from = tour.get(index);
            int to = tour.get((index + 1) % tour.size());
            index++;
            return new Edge(from, to);
        }
    }

    /**
     * The total length of the current tour.
     */
    protected long length;
    /**
     * The total number of cities in the TSP.
     */
    protected int n;
    /**
     * An array storing the minimum distances from each city to the current tour.
     */
    protected long[] minDistances;

    /**
     * Computes a tour for a given TSP instance using the insertion heuristic.
     *
     * @param data           The TSP data.
     * @param startCityIndex The index of the starting city.
     * @param observer       The observer to track the heuristic's progress.
     * @return The computed tour.
     */
    @Override
    public final TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer) {
        n = data.getNumberOfCities();
        minDistances = new long[n];
        Arrays.fill(minDistances, Long.MAX_VALUE);

        List<Integer> tourList = new ArrayList<>();
        boolean[] inTour = new boolean[n];
        length = 0; // Initialize total length

        // Heuristic-specific initialization
        initialize(data, startCityIndex, tourList, inTour);

        // Notify observer after initialization
        observer.update(new Traversal(tourList));

        while (tourList.size() < n) {
            // Select the next city to insert
            int nextCity = selectNextCity(data, inTour);
            inTour[nextCity] = true;

            // Find the best position to insert nextCity
            int bestPosition = -1;
            long minIncrease = Long.MAX_VALUE;
            int tourSize = tourList.size();
            for (int i = 0; i < tourSize; i++) {
                int from = tourList.get(i);
                int to = tourList.get((i + 1) % tourSize);
                long increase = data.getDistance(from, nextCity) + data.getDistance(nextCity, to)
                        - data.getDistance(from, to);
                if (increase < minIncrease) {
                    minIncrease = increase;
                    bestPosition = i + 1;
                }
            }

            // Insert nextCity into the tour
            tourList.add(bestPosition, nextCity);
            length += minIncrease; // Update total length

            // Heuristic-specific update after insertion
            updateAfterInsertion(data, nextCity, inTour);

            // Notify observer after each insertion
            observer.update(new Traversal(tourList));
        }

        // Build the final tour array
        int[] tour = tourList.stream().mapToInt(Integer::intValue).toArray();

        return new TspTour(data, tour, length);
    }

    /**
     * Abstract method to initialize the tour and heuristic-specific data structures.
     *
     * @param data           The TSP data.
     * @param startCityIndex The index of the starting city.
     * @param tourList       The list representing the current tour.
     * @param inTour         Array indicating whether a city is already in the tour.
     */
    protected abstract void initialize(TspData data, int startCityIndex, List<Integer> tourList, boolean[] inTour);

    /**
     * Abstract method to select the next city to insert into the tour.
     *
     * @param data   The TSP data.
     * @param inTour Array indicating whether a city is already in the tour.
     * @return The index of the next city to insert.
     */
    protected abstract int selectNextCity(TspData data, boolean[] inTour);

    /**
     * Updates the minDistances array after inserting a new city into the tour.
     *
     * @param data         The TSP data.
     * @param insertedCity The index of the city that has just been inserted.
     * @param inTour       Array indicating whether a city is already in the tour.
     */
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
