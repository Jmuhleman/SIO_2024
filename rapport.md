# Comparaison des Performances des Heuristiques d'Insertion

Les résultats ci-dessous présentent les performances des différentes heuristiques d'insertion appliquées au problème du voyageur de commerce (TSP), mesurées en pourcentage par rapport aux valeurs optimales pour chaque jeu de données.

**Légende :**

- **Min** : Longueur minimale de la tournée obtenue par l'heuristique.
- **% de Opt (Min)** : Longueur minimale en pourcentage de la longueur optimale.
- **Max** : Longueur maximale de la tournée obtenue.
- **% de Opt (Max)** : Longueur maximale en pourcentage de la longueur optimale.
- **Moyenne** : Longueur moyenne des tournées sur toutes les villes de départ.
- **% de Opt (Moyenne)** : Longueur moyenne en pourcentage de la longueur optimale.
- **Écart Type** : Indique la variabilité des longueurs de tournées.

---

## Dataset: att532 (Optimal : 86 729)

| Heuristique          | Min     | % de Opt | Max     | % de Opt | Moyenne    | % de Opt | Écart Type |
|----------------------|---------|----------|---------|----------|------------|----------|------------|
| Farthest Insertion   | 91 611  | 105,63%  | 98 244  | 113,28%  | 94 714,07  | 109,21%  | 1 117,83   |
| Nearest Insertion    | 106 504 | 122,80%  | 110 663 | 127,60%  | 107 646,98 | 124,12%  | 477,99     |
| Random Insertion     | 94 551  | 109,02%  | 99 329  | 114,53%  | 96 967,84  | 111,81%  | 725,24     |

**Analyse :**

- **Farthest Insertion** atteint une moyenne de 109,21% de l'optimal, avec une variabilité modérée. Cette heuristique est relativement efficace, dépassant l’optimal de 9% en moyenne.
- **Nearest Insertion** affiche des performances moins bonnes, avec une moyenne de 124,12% de l'optimal, indiquant des tournées souvent plus longues. Mais la meilleure stabilité grâce à un écart type plus faible.
- **Random Insertion** a une performance moyenne similaire à Farthest Insertion, à 111,81%, avec une stabilité en seconde place.

---

## Dataset: u574 (Optimal : 36 905)

| Heuristique          | Min    | % de Opt | Max    | % de Opt | Moyenne   | % de Opt | Écart Type |
|----------------------|--------|----------|--------|----------|-----------|----------|------------|
| Farthest Insertion   | 39 447 | 106,89%  | 42 064 | 113,98%  | 40 877,51 | 110,76%  | 461,64     |
| Nearest Insertion    | 44 756 | 121,27%  | 46 302 | 125,46%  | 45 581,59 | 123,51%  | 315,95     |
| Random Insertion     | 40 790 | 110,53%  | 42 462 | 115,06%  | 41 477,01 | 112,39%  | 315,01     |

**Analyse :**

- **Farthest Insertion** maintient de bonnes performances avec une moyenne de 110,76% de l'optimal et une faible variabilité.
- **Nearest Insertion** est plus éloignée de l'optimal, avec une moyenne de 123,51%, indiquant des tournées plus longues.
- **Random Insertion** est légèrement moins performante que Farthest Insertion mais meilleure que Nearest Insertion, avec une moyenne de 112,39%.

---

## Dataset: pcb442 (Optimal : 50 778)

| Heuristique          | Min    | % de Opt | Max    | % de Opt | Moyenne   | % de Opt | Écart Type |
|----------------------|--------|----------|--------|----------|-----------|----------|------------|
| Farthest Insertion   | 55 580 | 109,46%  | 59 975 | 118,11%  | 57 538,61 | 113,31%  | 729,75     |
| Nearest Insertion    | 59 312 | 116,81%  | 62 142 | 122,38%  | 60 462,62 | 119,07%  | 548,87     |
| Random Insertion     | 57 083 | 112,42%  | 59 966 | 118,09%  | 58 38,86  | 114,99%  | 501,59     |

**Analyse :**

- **Farthest Insertion** fournit des résultats raisonnables avec une moyenne de 113,31% de l'optimal.
- **Nearest Insertion** montre des tournées plus longues, avec une moyenne de 119,07% de l'optimal.
- **Random Insertion** se situe entre les deux, avec une moyenne de 114,99%.

---

## Dataset: pcb1173 (Optimal : 56 892)

| Heuristique          | Min    | % de Opt | Max    | % de Opt | Moyenne   | % de Opt | Écart Type |
|----------------------|--------|----------|--------|----------|-----------|----------|------------|
| Farthest Insertion   | 63 658 | 111,89%  | 67 277 | 118,25%  | 65 761,98 | 115,59%  | 500,09     |
| Nearest Insertion    | 70 804 | 124,45%  | 72 986 | 128,29%  | 72 268,45 | 127,03%  | 449,77     |
| Random Insertion     | 65 253 | 114,70%  | 67 162 | 118,05%  | 66 092,28 | 116,17%  | 351,14     |

**Analyse :**

- **Farthest Insertion** et **Random Insertion** ont des performances similaires, avec des moyennes autour de 115% à 116% de l'optimal.
- **Nearest Insertion** génère des tournées plus longues, avec une moyenne de 127,03% de l'optimal.

---

## Dataset: u1817 (Optimal : 57 201)

| Heuristique          | Min    | % de Opt | Max    | % de Opt | Moyenne   | % de Opt | Écart Type |
|----------------------|--------|----------|--------|----------|-----------|----------|---------|
| Farthest Insertion   | 67 216 | 117,51%  | 71 307 | 124,66%  | 69 039,88 | 120,70%  | 554,81  |
| Nearest Insertion    | 70 164 | 122,66%  | 71 669 | 125,29%  | 70 936,74 | 124,01%  | 240,39  |
| Random Insertion     | 67 645 | 118,26%  | 69 614 | 121,70%  | 68 682,75 | 120,07%  | 393,77  |

**Analyse :**

- **Random Insertion** performe légèrement mieux que **Farthest Insertion** sur ce dataset, avec une moyenne de 120,07% de l'optimal.
- **Nearest Insertion** est la moins efficace, avec une moyenne de 124,01% de l'optimal.
- Ce dataset est particulier, avec probablement une disposition des villes alignées, où les heuristiques classiques sont moins performantes.

---

## Dataset: nrw1379 (Optimal : 56 638)

| Heuristique          | Min    | % de Opt | Max    | % de Opt | Moyenne   | % de Opt | Écart Type |
|----------------------|--------|----------|--------|----------|-----------|----------|------------|
| Farthest Insertion   | 61 548 | 108,67%  | 64 311 | 113,55%  | 62 925,02 | 111,10%  | 398,50     |
| Nearest Insertion    | 69 025 | 121,87%  | 69 838 | 123,31%  | 69 503,40 | 122,72%  | 143,97     |
| Random Insertion     | 62 800 | 110,88%  | 64 439 | 113,77%  | 63 706,42 | 112,48%  | 252,52     |

**Analyse :**

- **Farthest Insertion** offre les meilleures performances avec une moyenne de 111,10% de l'optimal.
- **Nearest Insertion** est significativement moins performante, avec une moyenne de 122,72%.
- **Random Insertion** est légèrement moins efficace que Farthest Insertion, avec une moyenne de 112,48%.

---

## Conclusion

- **Farthest Insertion** tend à fournir les solutions les plus proches de l’optimal, avec des moyennes variant de 109% à 120% de l'optimal, et une bonne stabilité à travers les datasets.
- **Nearest Insertion** génère systématiquement des tournées plus longues, avec des moyennes entre 119% et 127% de l'optimal, indiquant qu'elle peut ne pas être la plus efficace pour ces jeux de données.
- **Random Insertion** offre des performances comparables à Farthest Insertion, parfois légèrement inférieures, mais sur le dataset **u1817**, Random Insertion surpasse les deux autres heuristiques.

### Observation Spécifique sur le Dataset u1817 :

Le dataset **u1817** semble être un cas particulier où les villes sont alignées sur un axe (formant une grille ou une ligne). Dans ce scénario, les heuristiques **Nearest Insertion** et **Farthest Insertion** sont moins efficaces.

Fait intéressant, **Random Insertion** performe mieux que les autres heuristiques sur ce dataset, 
suggérant que la sélection aléatoire peut éviter certains biais inhérents aux autres heuristiques lorsque l'agencement des villes présente des particularités.

---

Ces résultats soulignent l'importance de choisir l'heuristique appropriée en fonction des caractéristiques du jeu de données. 
Bien que **Farthest Insertion** tende à fournir les meilleures performances globales, 
**Random Insertion** peut parfois la surpasser, notamment dans des configurations spécifiques comme celle du dataset u1817. 
**Nearest Insertion**, bien que conceptuellement intuitive, 
ne produit pas toujours les meilleurs résultats et tend à générer des tournées plus longues dans les datasets testés.
