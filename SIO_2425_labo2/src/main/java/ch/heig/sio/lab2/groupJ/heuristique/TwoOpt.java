package ch.heig.sio.lab2.groupJ.heuristique;

import ch.heig.sio.lab2.display.ObservableTspImprovementHeuristic;
import ch.heig.sio.lab2.display.TspHeuristicObserver;
import ch.heig.sio.lab2.tsp.Edge;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.tsp.TspTour;

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

    private int[] twoOptSwap(int[] tour, int i, int j) {
        int[] newTour = tour.clone(); //TODO faire ne place -> pas de copie malheureux !
        while (i < j) {
            int temp = newTour[i];
            newTour[i] = newTour[j];
            newTour[j] = temp;
            i++;
            j--;
        }
        return newTour;
    }

    /**
     *
     * @param data
     * @param i
     * @param j
     * @param ip1
     * @param jp1
     * @return
     */
    private long computeDistance(TspData data, int i , int j, int ip1, int jp1) {
        long current_distance = data.getDistance(i, ip1) + data.getDistance(j, jp1);
        long new_distance = data.getDistance(i, j) + data.getDistance(ip1, jp1);
        //return number < 0 if saving is worse, otherwise return something positive
        return current_distance - new_distance;
    }

    @Override
    public TspTour computeTour(TspTour initialTour, TspHeuristicObserver observer) {

        int[] currentTour = initialTour.tour().copy();
        TspData data = initialTour.data();

        long bestDistance = initialTour.length();
        boolean improvement = true;

        System.out.println("avant de while");

        while (improvement) {
            improvement = false;
            for (int i = 1; i < currentTour.length - 1; i++) {
                for (int j = i + 1; j < currentTour.length - 1; j++) {

                    int[] newTour = twoOptSwap(currentTour, i, j);
                    long saving = computeDistance(data, i, j, i + 1, j + 1);

                    if (saving > 0) {
                        currentTour = newTour;
                        bestDistance -= saving;
                        improvement = true;
                        System.out.println("(" + i + " ," + j + ") : improvement = true");
                        observer.update(new Traversal(currentTour));
                    }
                }
            }
            System.out.println("fin de while, improvement = " + improvement);
        }

        return new TspTour(data, currentTour, bestDistance);
    }


}
