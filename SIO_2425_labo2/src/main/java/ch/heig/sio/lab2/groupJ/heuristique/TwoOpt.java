package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.Edge;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;


import javax.security.sasl.SaslServer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TwoOpt implements ObservableTspImprovementHeuristic {

    private static final class Traversal implements Iterator<Edge> {
        private final int[] tour; // int[] tour
        private int index;

        Traversal(int[] tour) {
            this.tour = tour;
            this.index = 0;
        }


        @Override
        public boolean hasNext() {
            return index < tour.length;
        }


        @Override
        public Edge next() {
            if (!hasNext()) throw new NoSuchElementException();
            int from = tour[index];
            int to = tour[(index + 1) % tour.length];
            index++;
            return new Edge(from, to);
        }


    }

    private void twoOpt(int[] tour, int i, int j) {
        while (i < j) {
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
            i++;
            j--;
        }
    }

    private long computeDistance(TspData data, int i, int j, int ip1, int jp1, int[] tour) {
        long current_distance = data.getDistance(tour[i], tour[ip1]) + data.getDistance(tour[j], tour[jp1]);
        long new_distance = data.getDistance(tour[i], tour[j]) + data.getDistance(tour[ip1], tour[jp1]);
        return current_distance - new_distance;
    }

    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {
        int[] currentTour = initialTour.tour().copy();
        TspData data = initialTour.data();
        long bestDistance = initialTour.length();
        boolean improvement = true;

        while (improvement) {
            improvement = false;
            int bestI = -1, bestJ = -1;
            long saving = 0;
            long maxSaving = 0;

            for (int i = 0; i < currentTour.length; i++) {
                for (int j = i + 1; j < currentTour.length; j++) {
                    saving = computeDistance(data, i, j, (i + 1), (j + 1) % currentTour.length, currentTour);
                    if (saving > maxSaving) {
                        maxSaving = saving;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }
            if (maxSaving > 0) {
                twoOpt(currentTour, bestI + 1, bestJ);
                bestDistance -= maxSaving;
                improvement = true;

                observer.update(new Traversal(currentTour));
            }
        }
        return new TspTour(data, currentTour, bestDistance);
    }
}
