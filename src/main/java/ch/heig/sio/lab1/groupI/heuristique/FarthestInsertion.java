package ch.heig.sio.lab1.groupI.heuristique;

public final class FarthestInsertion extends AbstractInsertionHeuristic {
    @Override
    protected int selectNextCity(long[] distances, boolean[] inTour) {
        int farthestCity = -1;
        long maxDistance = Long.MIN_VALUE;
        for (int i = 0; i < distances.length; i++) {
            if (!inTour[i] && distances[i] > maxDistance) {
                maxDistance = distances[i];
                farthestCity = i;
            }
        }
        return farthestCity;
    }
}
