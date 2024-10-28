package ch.heig.sio.lab1.groupI.heuristique;
import ch.heig.sio.lab1.display.ObservableTspConstructiveHeuristic;
import ch.heig.sio.lab1.display.TspHeuristicObserver;
import ch.heig.sio.lab1.tsp.Edge;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;

import java.util.*;

public class RandomInsertion implements ObservableTspConstructiveHeuristic {

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
            int from = tour.get(index % tour.size());
            int to = tour.get((index + 1) % tour.size());
            index++;
            return new Edge(from, to);
        }
    }

    @Override
    public TspTour computeTour(TspData data, int startCityIndex, TspHeuristicObserver observer) {
        int n = data.getNumberOfCities();
        List<Integer> tourList = new ArrayList<>();
        List<Integer> remainingCities = new ArrayList<>();
        long length = 0;

        // Initialiser le générateur de nombres pseudo-aléatoires avec la graine spécifiée
        Random random = new Random(0x134DA73);

        // Initialiser la tournée avec la ville de départ
        tourList.add(startCityIndex);

        // Construire la liste des villes restantes
        for (int city = 0; city < n; city++) {
            if (city != startCityIndex) {
                remainingCities.add(city);
            }
        }

        // Si au moins une autre ville est disponible, on ajoute une deuxième ville pour commencer
        if (!remainingCities.isEmpty()) {
            // Mélanger les villes restantes
            Collections.shuffle(remainingCities, random);

            // Sélectionner la première ville aléatoire
            int firstCity = remainingCities.removeFirst();

            // Ajouter la ville à la tournée
            tourList.add(firstCity);

            // Calculer la longueur initiale de la tournée
            length += data.getDistance(startCityIndex, firstCity);
            length += data.getDistance(firstCity, startCityIndex); // Boucler la tournée

            // Notifier l'observateur de l'état actuel de la tournée
            observer.update(new Traversal(tourList));
        }

        // Boucle principale pour insérer les villes restantes
        while (!remainingCities.isEmpty()) {
            // Mélanger les villes restantes
            Collections.shuffle(remainingCities, random);

            // Choisir la première ville aléatoire
            int cityToInsert = remainingCities.removeFirst();

            // Trouver le meilleur emplacement pour insérer la ville
            int bestPosition = -1;
            long minIncrease = Long.MAX_VALUE;

            for (int i = 0; i < tourList.size(); i++) {
                int from = tourList.get(i);
                int to = tourList.get((i + 1) % tourList.size()); // Boucle vers le début si nécessaire

                // Calculer l'augmentation de la longueur si la ville est insérée ici
                long currentDistance = data.getDistance(from, to);
                long newDistance = data.getDistance(from, cityToInsert) + data.getDistance(cityToInsert, to);
                long increase = newDistance - currentDistance;

                if (increase < minIncrease) {
                    minIncrease = increase;
                    bestPosition = i + 1; // Position d'insertion après 'from'
                }
            }

            // Insérer la ville à la meilleure position trouvée
            tourList.add(bestPosition, cityToInsert);

            // Mettre à jour la longueur totale de la tournée
            length += minIncrease;

            // Notifier l'observateur de l'état actuel de la tournée
            observer.update(new Traversal(tourList));
        }

        // Construire le tableau final de la tournée
        int[] tour = new int[n];
        for (int i = 0; i < n; i++) {
            tour[i] = tourList.get(i);
        }

        // Ajouter la distance pour boucler la tournée si ce n'est pas déjà fait
        if (tourList.size() > 1) {
            length += data.getDistance(tourList.getLast(), tourList.getFirst());
        }

        // Retourner la tournée complète
        return new TspTour(data, tour, length);
    }
}
