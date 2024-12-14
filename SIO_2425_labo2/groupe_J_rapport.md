# Rapport

<strong>Auteurs : Julien Mühlemann, Cristhian Ronquillo, Dr. Ing. Julien Billeter</strong>

Ci-dessous se trouve une analyse des résultats obtenus. 
Les statistiques incluent trois heuristiques (Random Insertion, Farthest Insertion et Nearest Insertion) appliquées à plusieurs instances de TSP, 
avec leurs résultats en pourcentage par rapport à la valeur optimale connue et le temps moyen d’exécution.

Généralités :
* Random Insertion : Génère aléatoirement une solution initiale puis l’améliore.
* Farthest Insertion (Insertion la plus éloignée) : Ajoute à chaque étape la ville la plus distante de la tournée courante.
* Nearest Insertion (Insertion la plus proche) : Ajoute à chaque étape la ville la plus proche de la tournée courante.

## Dataset: att532 (Optimal: 86729)

| Heuristic          | Min   | % of Opt | Max   | % of Opt | Mean     | % of Opt | StdDev  | Time (s) |
|---------------------|-------|----------|-------|----------|----------|----------|---------|----------|
| Random Insertion    | 93231 | 107.50%  | 95589 | 110.22%  | 93907.75 | 108.28%  | 973.39  | 0.385    |
| Farthest Insertion  | 92323 | 106.45%  | 94807 | 109.31%  | 93517.75 | 107.83%  | 1076.34 | 0.019    |
| Nearest Insertion   | 96853 | 111.67%  | 98368 | 113.42%  | 97446.75 | 112.36%  | 561.66  | 0.061    |

Pour att532, la Farthest Insertion fournit la meilleure qualité finale après l’application du 2-opt. 
Le point de départ donné par cette heuristique semble offrir à 2-opt un voisinage plus propice aux améliorations, 
surpassant ainsi les solutions initiales Random et Nearest.

Farthest Insertion est la plus rapide (0.019 s), largement devant Random (0.385 s) et Nearest (0.061 s).


---

## Dataset: u574 (Optimal: 36905)

| Heuristic          | Min   | % of Opt | Max   | % of Opt | Mean     | % of Opt | StdDev  | Time (s) |
|---------------------|-------|----------|-------|----------|----------|----------|---------|----------|
| Random Insertion    | 39916 | 108.16%  | 41325 | 111.98%  | 40376.75 | 109.41%  | 557.26  | 0.426    |
| Farthest Insertion  | 40736 | 110.38%  | 41365 | 112.09%  | 41017.25 | 111.14%  | 266.39  | 0.013    |
| Nearest Insertion   | 40772 | 110.48%  | 40974 | 111.03%  | 40853.50 | 110.70%  | 80.03   | 0.065    |

Sur u574, la meilleure solution finale est obtenue en partant d’une Random Insertion, ce qui est contre-intuitif. Cette solution aléatoire fournit un point de départ que 2-opt a pu aisément améliorer, dépassant même les heuristiques plus structurées en termes de résultat final.

Farthest Insertion est la plus rapide (0.013 s), Random étant la plus lente (0.426 s).

---

## Dataset: pcb442 (Optimal: 50778)

| Heuristic          | Min   | % of Opt | Max   | % of Opt | Mean     | % of Opt | StdDev  | Time (s) |
|---------------------|-------|----------|-------|----------|----------|----------|---------|----------|
| Random Insertion    | 55998 | 110.28%  | 56384 | 111.04%  | 56172.50 | 110.62%  | 175.57  | 0.174    |
| Farthest Insertion  | 55276 | 108.86%  | 57636 | 113.51%  | 56394.25 | 111.06%  | 917.20  | 0.004    |
| Nearest Insertion   | 54862 | 108.04%  | 55528 | 109.35%  | 55183.25 | 108.68%  | 273.30  | 0.021    |

Pour pcb442, la Nearest Insertion mène à la meilleure solution après 2-opt. La structure initiale plus « locale » de Nearest Insertion offre un point de départ qui facilite l’amélioration par 2-opt, permettant d’obtenir une solution plus proche de l’optimal que les autres heuristiques.

Farthest Insertion est la plus rapide (0.004 s), Nearest (0.021 s) et Random (0.174 s) sont plus lentes.

---

## Dataset: pcb1173 (Optimal: 56892)

| Heuristic          | Min   | % of Opt | Max   | % of Opt | Mean     | % of Opt | StdDev  | Time (s) |
|---------------------|-------|----------|-------|----------|----------|----------|---------|----------|
| Random Insertion    | 62770 | 110.33%  | 64874 | 114.03%  | 63615.25 | 111.82%  | 821.40  | 4.221    |
| Farthest Insertion  | 63586 | 111.77%  | 64988 | 114.23%  | 64293.75 | 113.01%  | 497.45  | 0.143    |
| Nearest Insertion   | 62518 | 109.89%  | 63041 | 110.81%  | 62756.00 | 110.31%  | 197.31  | 0.548    |

Comme pour pcb442, Nearest Insertion produit ici la meilleure solution finale. Le point de départ fourni par Nearest Insertion semble permettre à 2-opt de mieux optimiser la tournée, offrant ainsi un résultat final relativement proche de l’optimal.

Farthest Insertion (0.143 s) est plus rapide que Nearest (0.548 s) et beaucoup plus que Random (4.221 s).

---

## Dataset: u1817 (Optimal: 57201)

| Heuristic          | Min   | % of Opt | Max   | % of Opt | Mean     | % of Opt | StdDev  | Time (s) |
|---------------------|-------|----------|-------|----------|----------|----------|---------|----------|
| Random Insertion    | 65240 | 114.05%  | 66130 | 115.61%  | 65598.50 | 114.68%  | 340.41  | 20.557   |
| Farthest Insertion  | 67040 | 117.20%  | 68110 | 119.07%  | 67631.75 | 118.24%  | 479.34  | 0.486    |
| Nearest Insertion   | 63996 | 111.88%  | 64258 | 112.34%  | 64117.00 | 112.09%  | 93.30   | 1.416    |

Pour u1817, la Nearest Insertion surpasse les autres méthodes après 2-opt. Les propriétés locales de cette heuristique initiale semblent plus adaptées à ce dataset, permettant au 2-opt d’atteindre une solution plus proche de l’optimal.

Farthest Insertion (0.486 s) est plus rapide que Nearest (1.416 s). Random est très lente (20.557 s) et moins bonne en qualité.

---

## Dataset: nrw1379 (Optimal: 56638)

| Heuristic          | Min   | % of Opt | Max   | % of Opt | Mean     | % of Opt | StdDev  | Time (s) |
|---------------------|-------|----------|-------|----------|----------|----------|---------|----------|
| Random Insertion    | 62428 | 110.22%  | 62755 | 110.80%  | 62579.25 | 110.49%  | 122.37  | 6.570    |
| Farthest Insertion  | 61614 | 108.79%  | 62706 | 110.71%  | 62315.50 | 110.02%  | 416.31  | 0.210    |
| Nearest Insertion   | 63779 | 112.61%  | 64172 | 113.30%  | 63980.75 | 112.96%  | 187.55  | 0.733    |

Sur nrw1379, Farthest Insertion aboutit à la meilleure solution après 2-opt, surpassant non seulement la Random Insertion mais surtout la Nearest. Ici, une base plus diversifiée facilite apparemment l’amélioration par 2-opt.

Farthest Insertion (0.210 s) est plus rapide que Nearest (0.733 s) et Random (6.570 s).

---

## Conclusion

Ces résultats montrent que le choix de la tournée initiale (Random, Nearest ou Farthest Insertion) a un impact significatif sur la performance finale obtenue après l’amélioration par 2-opt. Selon le dataset, l’une ou l’autre de ces méthodes fournit une base plus favorable à l’optimisation locale. En somme :

- Une solution initiale aléatoire (Random) peut, dans certains cas, donner un point de départ permettant de meilleures améliorations, même si elle est souvent plus éloignée de l’optimal et aussi très souvent l'amélioration la plus lente.
- Les solutions initiales construites avec Nearest Insertion, plus « locales », facilitent parfois le travail de 2-opt, permettant d’atteindre une bonne qualité finale mais n’est pas toujours la plus rapide. Sur pcb442, pcb1173 et u1817, elle donne les meilleurs résultats finaux.
- Les solutions issues de Farthest Insertion, plus « dispersées », peuvent également améliorer le potentiel d’optimisation du 2-opt selon la structure du problème. Elle est souvent très rapide.

La qualité de la solution de départ conditionne donc en grande partie l’efficacité du 2-opt et le gain possible. Il est important de choisir la méthode de construction initiale en fonction des caractéristiques du dataset et des objectifs (qualité, temps, robustesse) visés.