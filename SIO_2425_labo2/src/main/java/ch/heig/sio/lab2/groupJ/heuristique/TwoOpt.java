package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.Edge;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TwoOpt implements ObservableTspImprovementHeuristic {

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




    private int[] twoOptSwap(int[] tour, int i, int j) {
        int[] newTour = tour.clone();
        while (i < j) {
            int temp = newTour[i];
            newTour[i] = newTour[j];
            newTour[j] = temp;
            i++;
            j--;
        }
        return newTour;
    }

    private long computeDistance(int[] tour, TspData data) {
        long distance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            distance += data.getDistance(tour[i], tour[i + 1]);
        }
        // Add distance back to the starting city to complete the cycle
        distance += data.getDistance(tour[tour.length - 1], tour[0]);
        return distance;
    }

    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {
        int[] currentTour = initialTour.tour().copy();
        TspData data = initialTour.data();
        long bestDistance = initialTour.length();
        boolean improvement = true;

        while (improvement) {
            improvement = false;
            for (int i = 1; i < currentTour.length - 1; i++) {
                for (int j = i + 1; j < currentTour.length; j++) {
                    int[] newTour = twoOptSwap(currentTour, i, j);
                    long newDistance = computeDistance(newTour, data);

                    if (newDistance < bestDistance) {
                        currentTour = newTour;
                        bestDistance = newDistance;
                        improvement = true;

                        List<Integer> tourList = Arrays.stream(currentTour).boxed().toList();
                        observer.update(new Traversal(tourList));
                    }
                }
            }
        }

        return new TspTour(data, currentTour, bestDistance);
    }


}
