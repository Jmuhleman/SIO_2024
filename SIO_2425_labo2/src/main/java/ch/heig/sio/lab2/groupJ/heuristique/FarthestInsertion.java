/**
 * À chaque étape, la classe identifie la ville non visitée la plus éloignée
 * en fonction des distances calculées, et insère cette ville à la position
 * optimale dans la tournée afin de minimiser l'augmentation de la distance totale.
 * La méthode {@link #computeTour(TspData, int, TspHeuristicObserver)}
 * gère l'itération et l'insertion, tandis que la méthode
 * {@link #selectNextCity(long[], boolean[])} est utilisée pour déterminer
 * la prochaine ville à ajouter à la tournée.
 * <p>
 * @auteurs Ronquillo Cristhian, Muhlemann Julien
 */
package ch.heig.sio.lab2.groupJ.heuristique;

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
