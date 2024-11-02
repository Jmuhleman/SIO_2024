Heuristic                 Min   % of Opt      Max   % of Opt     Mean   % of Opt   StdDev

Dataset: att532 (Optimal: 86729)
Farthest Insertion      91611     105.63%    98244     113.28% 94714.07     109.21%  1117.83
Nearest Insertion      106504     122.80%   110663     127.60% 107646.98     124.12%   477.99
Random Insertion        93013     107.25%    98151     113.17% 95319.01     109.90%   984.45

Dataset: u574 (Optimal: 36905)
Farthest Insertion      39447     106.89%    42064     113.98% 40877.51     110.76%   461.64
Nearest Insertion       44756     121.27%    46302     125.46% 45581.59     123.51%   315.95
Random Insertion        40701     110.29%    42205     114.36% 41426.26     112.25%   279.82

Dataset: pcb442 (Optimal: 50778)
Farthest Insertion      55580     109.46%    59975     118.11% 57538.61     113.31%   729.75
Nearest Insertion       59312     116.81%    62142     122.38% 60462.62     119.07%   548.87
Random Insertion        57349     112.94%    60490     119.13% 58648.03     115.50%   608.30

Dataset: pcb1173 (Optimal: 56892)
Farthest Insertion      63658     111.89%    67277     118.25% 65761.98     115.59%   500.09
Nearest Insertion       70804     124.45%    72986     128.29% 72268.45     127.03%   449.77
Random Insertion        65148     114.51%    67284     118.27% 66276.07     116.49%   372.78

Dataset: u1817 (Optimal: 57201)
Farthest Insertion      67216     117.51%    71307     124.66% 69039.88     120.70%   554.81
Nearest Insertion       70164     122.66%    71669     125.29% 70936.74     124.01%   240.39
Random Insertion        67428     117.88%    69381     121.29% 68228.76     119.28%   296.39

Dataset: nrw1379 (Optimal: 56638)
Farthest Insertion      61548     108.67%    64311     113.55% 62925.02     111.10%   398.50
Nearest Insertion       69025     121.87%    69838     123.31% 69503.40     122.72%   143.97
Random Insertion        62925     111.10%    64849     114.50% 63936.98     112.89%   251.36


Les résultats montrent les performances des différentes heuristiques d'insertion appliquées au problème du voyageur de commerce (TSP), mesurées en pourcentage par rapport aux valeurs optimales pour chaque dataset.





## Dataset: att532
- Farthest Insertion : Atteint un score minimum de 105.63 % de l’optimal, avec une moyenne de 109.21 % et un écart type modéré. Cette heuristique est relativement efficace mais dépasse l’optimal de 9 % en moyenne.

- Nearest Insertion : Performances moins bonnes, atteignant en moyenne 124.12 % de l’optimal, avec une faible variabilité (écart type de 477.99), fournissant des solutions souvent au-dessus de l'optimal.

- Random Insertion : Moyenne de 109.90 % de l'optimal, proche de Farthest Insertion, légèrement plus stable avec un écart type plus réduit.


## Dataset: u574
- Farthest Insertion : Moyenne de 110.76 %, proche de l'optimal, avec une faible variabilité. Cette heuristique semble fournir des solutions raisonnablement bonnes et constantes.

- Nearest Insertion : Plus éloignée de l'optimal, avec une moyenne de 123.51 %, indiquant une tendance à générer des parcours plus longs.

- Random Insertion : Moyenne de 112.25 %, assez proche de Farthest Insertion, suggérant une bonne efficacité pour une insertion aléatoire.


## Dataset: pcb442
- Farthest Insertion : Moyenne de 113.31 % de l'optimal, montrant une performance moyenne avec un léger écart de 13 % par rapport à l'optimal.

- Nearest Insertion : En moyenne 119.07 % de l'optimal, avec des solutions globalement moins efficaces et moins stables.

- Random Insertion : Moyenne de 115.50 %, entre Farthest et Nearest Insertion, mais toujours sensiblement au-dessus de l’optimal.


## Dataset: pcb1173
- Farthest Insertion : Moyenne de 115.59 % avec un écart type modéré, indiquant une bonne stabilité.

- Nearest Insertion : Moyenne élevée à 127.03 %, avec une faible variabilité, ce qui montre une tendance à donner des parcours plus longs.

- Random Insertion : Moyenne de 116.49 %, similaire à Farthest Insertion.


## Dataset: u1817
- Farthest Insertion : Performances moyennes avec une moyenne de 120.70 %, indiquant un écart de 20 % par rapport à l'optimal.

- Nearest Insertion : Moyenne élevée de 124.01 %, ce qui montre une tendance à générer des solutions très souvent sous-optimales.

- Random Insertion : Moyenne de 119.28 %, légèrement meilleure que Farthest Insertion.


## Dataset: nrw1379
- Farthest Insertion : Moyenne de 111.10 %, assez proche de l’optimal avec une faible variabilité.

- Nearest Insertion : Moyenne de 122.72 %, avec des solutions moins optimales.

- Random Insertion : Moyenne de 112.89 %, proche de Farthest Insertion, mais plus stable.


## Conclusion
Dans la plupart des cas, Farthest Insertion tend à fournir les solutions les plus proches de l’optimal, avec une bonne stabilité. Nearest Insertion donne souvent des parcours plus longs, tandis que Random Insertion offre une performance intermédiaire. Le choix de l'heuristique dépend donc de la disposition des villes dans le problème de départ. 
Le dataset u1817 (pcd avec grande quantité de villes alignées sur un axe) est un cas particulier où les heuristiques nearest et farthest sont les moins efficace et de manière intéressante le choix aléatoire est le plus efficace.










