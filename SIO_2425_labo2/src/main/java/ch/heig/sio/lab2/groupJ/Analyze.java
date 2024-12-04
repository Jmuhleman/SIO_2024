package ch.heig.sio.lab2.groupJ;

import ch.heig.sio.lab2.groupJ.heuristique.FarthestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.NearestInsertion;
import ch.heig.sio.lab2.groupJ.heuristique.TwoOpt;
import ch.heig.sio.lab2.tsp.RandomTour;
import ch.heig.sio.lab2.tsp.TspData;
import ch.heig.sio.lab2.display.HeuristicComboItem;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public final class Analyze {
    public static void main(String[] args) throws FileNotFoundException {
        // TODO
        //  - Renommer le package ;
        //  - Intégrer (et corriger si nécessaire) les heuristiques constructives du labo 1 dans ce package ;
        //  - Implémenter l'heuristique 2-opt utilisant la stratégie "meilleure amélioration" ;
        //  - Documentation soignée comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.

        // Longueurs optimales :
        // pcb442  : 50778
        // att532  : 86729
        // u574    : 36905
        // pcb1173 : 56892
        // nrw1379 : 56638
        // u1817   : 57201

        // Exemple de lecture d'un jeu de données :
        // TspData data = TspData.fromFile("data/att532.dat");

        String[] filePaths = {
                "data/att532.dat",
                "data/u574.dat",
                "data/pcb442.dat",
                "data/pcb1173.dat",
                "data/u1817.dat",
                "data/nrw1379.dat"
        };


        // Définition des valeurs optimales pour chaque dataset
        Map<String, Integer> optimalValues = new HashMap<>();
        optimalValues.put("att532", 86729);
        optimalValues.put("u574", 36905);
        optimalValues.put("pcb442", 50778);
        optimalValues.put("pcb1173", 56892);
        optimalValues.put("nrw1379", 56638);
        optimalValues.put("u1817", 57201);

        TspData[] data_sets = new TspData[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            data_sets[i] = TspData.fromFile(filePaths[i]);
        }
        var Fa = new FarthestInsertion();
        var Ni = new NearestInsertion();
        var Ri = new RandomTour();

        //création des tournées
        var tourneeFa = Fa.computeTour(data_sets[0], 0);
        var tourneeNi = Ni.computeTour(data_sets[0], 0);
        var tourneeRi = Ri.computeTour(data_sets[0], 0);

        var twoOpt = new TwoOpt();

        //partir d'une tournée aléatoire
        var tourneeTwoOptAl = twoOpt.computeTour(tourneeFa);
        var tourneeTwoOptNi = twoOpt.computeTour(tourneeNi);
        var tourneeTwoOptFi = twoOpt.computeTour(tourneeRi);
        //affichage des résultats
        System.out.println("Farthest Insertion : " + tourneeFa.length());
    }
}
