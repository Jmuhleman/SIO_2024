package ch.heig.sio.lab1.groupI.heuristique;

import ch.heig.sio.lab1.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.Edge;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class InsertionHeuristic implements ObservableTspConstructiveHeuristic {

    protected static final class Traversal implements Iterator<Edge> {
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
            int from = tour.get(index % tour.size());
            int to = tour.get((index + 1) % tour.size());
            index++;
            return new Edge(from, to);
        }
    }

    public abstract TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer);

}
