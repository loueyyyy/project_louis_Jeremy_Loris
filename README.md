# Invoicer

Outil en ligne de commande pour préparer un fichier CSV et en tirer une facture LaTeX.



## Auteurs

- Louis Garcia
- Jeremy Bortone
- Loris Voce Torquemada

## Objectif de l'application

Une petite entreprise tient la liste de ses ventes ou de ses prestations dans un tableur exporté en CSV. Pour en faire une facture, il faut d'abord corriger ce fichier (une erreur de ville, des lignes à retirer, un tri), puis recopier les lignes et calculer les totaux à la main.

Invoicer automatise ces deux étapes avec deux commandes :

- `modify` produit un nouveau fichier CSV après remplacement d'une cellule, suppression de lignes ou de colonnes, ajout d'une ligne ou tri ;
- `invoice` produit une facture au format LaTeX (`.tex`) : le tableau reprend toutes les colonnes du CSV, la colonne `Prix` est additionnée, la TVA de 8,1 % est ajoutée et le total est calculé..

## Prérequis

- JDK 21 ou plus récent (le projet est compilé pour la version 21 de Java)
- Git

Maven n'a pas besoin d'être installé : le dépôt contient le Maven wrapper.

## Installation

```bash 
git clone https://github.com/loueyyyy/project_louis_Jeremy_Loris.git
cd ./invoicer
./mvnw clean package
```



Le build exécute les tests, puis produit le JAR exécutable `target/invoicer-1.0-SNAPSHOT.jar`, qui contient ses dépendances et le modèle de facture. Il peut être lancé depuis n'importe quel dossier. Le fichier `target/original-invoicer-1.0-SNAPSHOT.jar` est une étape intermédiaire du build et peut être ignoré.

## Utilisation

```text
java -jar target/invoicer-1.0-SNAPSHOT.jar COMMANDE [OPTIONS]
```

`--help` affiche l'aide de l'outil, et `COMMANDE --help` celle d'une commande. Une commande qui réussit retourne le code 0, une commande en erreur retourne 1.

### Format du fichier CSV

- encodage UTF-8, séparateur point-virgule, première ligne = en-têtes ;
- toutes les lignes ont le même nombre de colonnes ;
- les cellules entre guillemets ne sont pas gérées ;
- pour la commande `invoice`, une colonne s'appelle `Prix` (majuscules ou minuscules indifférentes), avec un point ou une virgule comme séparateur décimal.

Le dossier `io/` contient un fichier d'essai, `data_file.csv` :

```text
Nom;Prenom;Age;Ville;Email;Telephone;Categorie;Prix
Dupont;Jean;25;Lausanne;jean.dupont@email.ch;+41791234567;Soda;12.50
Martin;Julie;30;Geneve;julie.martin@email.ch;+41782345678;Eau;8.90
...
```

### Commande invoice

| Option | Description |
|---|---|
| `-i`, `--input` | Fichier CSV d'entrée, extension `.csv` (obligatoire) |
| `-o`, `--output` | Fichier de facture, extension `.tex` (obligatoire) |

```text
$ java -jar target/invoicer-1.0-SNAPSHOT.jar invoice -i ../io/data_file.csv -o facture.tex
Creating invoice from CSV file: ../io/data_file.csv
Success. Created LaTeX invoice: facture.tex
```

Dans `facture.tex`, chaque ligne du CSV devient une ligne du tableau, et les totaux sont calculés avec une précision de deux décimales :

```text
% --- SUBTOTAL ---
106.65 CHF
% --- VAT ---
8.64 CHF
% --- TOTAL ---
115.29 CHF
```

Le fichier `io/facture_exemple.tex` est le résultat de cette commande.

L'en-tête de la facture (entreprise, client, numéro, date) est fixe dans le modèle `invoicer/src/main/resources/invoice_model.tex`. Il se modifie avant de lancer le build.

### Commande modify

| Option | Description |
|---|---|
| `-i`, `--input` | Fichier CSV d'entrée (obligatoire) |
| `-o`, `--output` | Fichier CSV de sortie (obligatoire) |
| `-r`, `--replace` | Remplace une cellule : `colonne,ligne,valeur`, par exemple `B,3,Fribourg` |
| `-dr`, `--delete-rows` | Supprime une ligne ou une plage : `2` ou `2-4` |
| `-dc`, `--delete-columns` | Supprime une colonne ou une plage : `C` ou `C-E` |
| `-a`, `--add` | Ajoute une ligne à la fin, cellules séparées par `;` (ou par des espaces) |
| `-s`, `--sort` | Trie les lignes par ordre alphabétique selon l'en-tête de colonne indiqué |

Les lignes sont numérotées comme dans un tableur : la ligne 1 est l'en-tête, la colonne A est la première. Une seule option peut être appliquée à la fois. Les numéros de lignes et de colonnes se rapportent toujours au fichier d'origine.

```text
$ java -jar target/invoicer-1.0-SNAPSHOT.jar modify -i ../io/data_file.csv \
    -o ../io/data_file_trie.csv --sort nom
Modifying CSV file: ../io/data_file.csv
Success. Created modified CSV file: ../io/data_file_trie.csv

$ head -3 ../io/data_file_trie.csv
Nom;Prenom;Age;Ville;Email;Telephone;Categorie;Prix
Bernard;Lucas;22;Yverdon;lucas.bernard@email.ch;+41763456789;Jus;15.20
Dupont;Jean;25;Lausanne;jean.dupont@email.ch;+41791234567;Soda;12.50
```




## Tests

```bash
cd invoicer
./mvnw clean test
```

Les tests JUnit 5 portent sur le traitement. Ils utilisent des `StringReader` et des `StringWriter` : aucun fichier sur disque, aucun chemin, donc les mêmes résultats sur toutes les machines. Ils couvrent :

- la lecture du CSV : cellules vides, fins de ligne Windows, fichier sans saut de ligne final, fichier vide, accents, lignes de longueurs différentes ;
- la lecture du modèle LaTeX et la recherche des marqueurs, y compris un marqueur absent ;
- la facture : structure du fichier généré, sous-total, TVA et total, absence d'erreur d'arrondi, virgule décimale, en-tête sans lignes, échappement de LaTeX, colonne `Prix` absente ou illisible ;
- les modifications du CSV : chaque option seule et combinée, plages, cellule hors du fichier, formats invalides, tri de plus de dix lignes, tri stable, en-tête conservé.

## Dépendances

| Dépendance | Usage |
|---|---|
| picocli 4.7.7 | Commandes, options et messages d'aide |
| JUnit Jupiter 5.10.2 | Tests unitaires (portée `test`) |
| maven-shade-plugin | Intégration de picocli dans le JAR exécutable |


## Utilisation de l'intelligence artificielle

Nous avons utilisé des outils d'intelligence artificielle pour nous aider dans certaines tâches du projet, notamment :

- **Rédaction et mise en forme :** correction de l'orthographe et aide à l'utilisation de Markdown pour structurer et formater ce document.
- **Tests :** génération de donnée aléatoire et proposition d'idée de cas à tester

Le code, les tests et la documentation ont été vérifiés et sont compris par les membres du groupe.