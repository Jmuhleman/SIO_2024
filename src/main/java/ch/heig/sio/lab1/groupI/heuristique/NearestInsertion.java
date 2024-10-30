package ch.heig.sio.lab1.groupI.heuristique;

public final class NearestInsertion extends AbstractInsertionHeuristic {
    @Override
    protected int selectNextCity(long[] distances, boolean[] inTour) {
        int nearestCity = -1;
        long minDistance = Long.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            if (!inTour[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                nearestCity = i;
            }
        }
        return nearestCity;
    }
}
