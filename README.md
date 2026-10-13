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
- `invoice` produit une facture au format LaTeX (`.tex`) : le tableau reprend toutes les colonnes du CSV, la colonne `Prix` est additionnée, la TVA de 8,1 % est ajoutée et le total est calculé.

## Prérequis

- JDK 21 ou plus récent (le projet est compilé pour la version 21 de Java)
- Git

Maven n'a pas besoin d'être installé : le dépôt contient le Maven wrapper.

## Installation

```bash
git clone https://github.com/loueyyyy/project_louis_Jeremy_Loris.git
cd project_louis_Jeremy_Loris/invoicer
./mvnw clean package
```



Le build exécute les tests, puis produit le JAR exécutable `target/invoicer-1.0-SNAPSHOT.jar`, qui contient ses dépendances. Il doit être lancé depuis le dossier invoicer, car le modèle de facture est lu avec le chemin relatif ./src/main/java/ch/heigvd/model/invoice_model.tex. Le fichier `target/original-invoicer-1.0-SNAPSHOT.jar` est une étape intermédiaire du build et peut être ignoré.

## Utilisation

```text
java -jar target/invoicer-1.0-SNAPSHOT.jar COMMANDE [OPTIONS]
```

`--help` affiche l'aide de l'outil, et `COMMANDE --help` celle d'une commande. Une commande qui réussit retourne le code 0, une commande en erreur retourne 1.

### Format du fichier CSV

- encodage UTF-8, séparateur point-virgule, première ligne = en-têtes ;
- toutes les lignes doivent avoir autant de colonnes que l’en-tête (ce n’est pas vérifié par l’outil) ;
- les cellules entre guillemets ne sont pas gérées ;
- pour la commande `invoice`, une colonne s'appelle `Prix` (majuscules ou minuscules indifférentes), avec un point comme séparateur décimal.

Le dossier `invoicer/src/test/java/ch/heigvd/IOExemples/` contient un fichier d'essai, `data_file.csv` :

```text
Article;Categorie;Conditionnement;Quantite;Prix
Coca-Cola Original;Soda;Canette 33 cl;24;28.80
Coca-Cola Zero;Soda;Canette 33 cl;24;28.80
Fanta Orange;Soda;Canette 33 cl;24;27.60
Sprite;Soda;Canette 33 cl;24;27.60
Eau minerale plate;Eau;Bouteille 50 cl;24;18.00
Eau minerale gazeuse;Eau;Bouteille 50 cl;24;19.20
Jus d'orange;Jus;Bouteille 1 L;6;21.00
Jus de pomme;Jus;Bouteille 1 L;6;19.80
The froid peche;The froid;Bouteille 50 cl;12;22.80
The froid citron;The froid;Bouteille 50 cl;12;22.80
Limonade artisanale;Limonade;Bouteille 33 cl;12;30.00
Tonic Water;Tonic;Bouteille 20 cl;24;26.40
Ginger Beer;Soda;Bouteille 20 cl;24;31.20
```

### Commande invoice

| Option | Description |
|---|---|
| `-i`, `--input` | Fichier CSV d'entrée, extension `.csv` (obligatoire) |
| `-o`, `--output` | Fichier de facture, extension `.tex` (obligatoire) |

```text
$ java -jar target/invoicer-1.0-SNAPSHOT.jar invoice -i src/test/java/ch/heigvd/IOExemples/data_file.csv -o facture.tex
Checking input file: src/test/java/ch/heigvd/IOExemples/data_file.csv
Checking output file: facture.tex
Creating invoices from CSV file: src/test/java/ch/heigvd/IOExemples/data_file.csv
Output will be saved to: facture.tex
Success. Created invoice latex file: facture.tex from input CSV file: src/test/java/ch/heigvd/IOExemples/data_file.csv
```

Dans `facture.tex`, chaque ligne du CSV devient une ligne du tableau, et les totaux sont calculés avec une précision de deux décimales :

```text
% --- SUBTOTAL ---
324.00 CHF
% --- VAT ---
26.24 CHF
% --- TOTAL ---
350.24 CHF
```

Le fichier `invoicer/src/test/java/ch/heigvd/IOExemples/new_invoice.tex` est un résultat-exemple de cette commande.

L'en-tête de la facture (entreprise, client, numéro, date) est fixe dans le modèle `invoicer/src/main/java/ch/heigvd/model/invoice_model.tex`. Il se modifie avant de lancer le build.

### Commande modify

| Option | Description |
|---|---|
| `-i`, `--input` | Fichier CSV d'entrée (obligatoire) |
| `-o`, `--output` | Fichier CSV de sortie (obligatoire) |
| `-r`, `--replace` | Remplace une cellule : `ColonneLigne:valeur`, par exemple `B3:Fribourg` |
| `-dr`, `--delete-rows` | Supprime une ligne ou une plage : `2` ou `2-4` |
| `-dc`, `--delete-columns` | Supprime une colonne ou une plage : `C` ou `C-E` |
| `-a`, `--add` | Ajoute une ligne à la fin, cellules séparées par `-` , par exemple  `SchweppesAgrumes-Soda-Canette 33 cl-24-26.40` |
| `-s`, `--sort` | Trie les lignes par ordre alphabétique selon l'en-tête de colonne indiqué |

Les lignes sont numérotées comme dans un tableur : la ligne 1 est l'en-tête, la colonne A est la première. Une seule option peut être appliquée à la fois. Si plusieurs options sont entrées , seulement la première est appliquée dans l'ordre -r, -dr, -dc, -a, -s

```text
$ java -jar target/invoicer-1.0-SNAPSHOT.jar modify -i src/test/java/ch/heigvd/IOExemples/data_file.csv -o sorted.csv --sort article
Checking input file: src/test/java/ch/heigvd/IOExemples/data_file.csv
Checking output file: sorted.csv
Modifying CSV file: src/test/java/ch/heigvd/IOExemples/data_file.csv
Output will be saved to: sorted.csv
Sorting by: article
Success. Created invoice file: sorted.csv from input CSV file: src/test/java/ch/heigvd/IOExemples/data_file.csv

$ head -5 sorted.csv
Article;Categorie;Conditionnement;Quantite;Prix
Coca-Cola Original;Soda;Canette 33 cl;24;28.80
Coca-Cola Zero;Soda;Canette 33 cl;24;28.80
Eau minerale gazeuse;Eau;Bouteille 50 cl;24;19.20
Eau minerale plate;Eau;Bouteille 50 cl;24;18.00
```




## Tests

```bash
cd invoicer
./mvnw clean test
```

Les tests JUnit 5 portent sur le traitement. Les tests de `CsvDataExtractor` et de `LatexFileReader` lisent des données écrites dans le test (`StringReader`). Ceux de `ModifyProcess`, `InvoiceProcess` et `IOhelper` passent par des fichiers temporaires, créés et supprimés par JUnit, car ces classes ouvrent elles-mêmes leurs fichiers. `InvoiceProcessTest` lit le modèle de facture avec un chemin relatif : les tests doivent donc être lancés depuis le dossier `invoicer`, ce que fait Maven. Ils couvrent :

- la lecture du CSV : cellules vides, fins de ligne Windows, fichier vide, accents, fichier terminé par un saut de ligne ;
- la lecture du modèle LaTeX et la recherche des marqueurs, y compris un marqueur absent ;
- la facture : position des en-têtes et des lignes, colonne `Prix` en première ou en dernière position, sous-total, TVA de 8,1 %, total, arrondi à deux décimales, CSV sans lignes de données, fins de ligne Windows, accents, modèle conservé ;
- les modifications du CSV : remplacement, suppression d'une ligne ou d'une plage de lignes ou de colonnes (y compris la dernière colonne), ajout d'une ligne, tri (sans tenir compte des majuscules, par une autre colonne, stable, avec l'en-tête conservé, au-delà de dix lignes) ;
- les vérifications de fichiers d'entrée et de sortie et des extensions `.csv` et `.tex`.

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