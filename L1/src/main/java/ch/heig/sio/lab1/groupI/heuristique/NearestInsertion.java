/**
 * À chaque étape, la classe identifie la ville non visitée la plus proche en fonction
 * des distances minimales calculées, et insère cette ville à la position optimale
 * dans la tournée afin de minimiser l'augmentation de la distance totale.
 * La méthode {@link #computeTour(TspData, int, TspHeuristicObserver)}
 * gère l'itération et l'insertion, tandis que la méthode
 * {@link #selectNextCity(long[], boolean[])} est utilisée pour déterminer
 * la prochaine ville à ajouter à la tournée.
 * <p>
 * @auteurs Ronquillo Cristhian, Muhlemann Julien
 */

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
