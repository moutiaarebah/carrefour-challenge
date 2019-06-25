# Carrefour Test
>ce projet permet la génération des données aléatoire pour simuler la volumétrie.

## Compilation

windows & Linux: (JDK 1.8 est pré requis)

>mvn clean install 
 
>mvn clean install -DskipTests=true (pour eviter le test)
 
 
deux archives jar sont creer aprés la compilation <<carrefour-generate-data-0.0.1.jar>> et 
<<carrefour-generate-data-0.0.1-jar-with-dependencies.jar>>
utiliser le deuxieme jar par ce qu'il contient les dependances.
 

## parametres

ordre n'est pas imporant

-p-,--path  : path ou stocker les données générer
-ns,--numberOfStore :  nombre des magasins
-nt,--numberofTransaction :  nombre des transactions par fichier
-np,--numberOfProduct : nombre des produits par magasin
 
 
## Usage example

> java -jar carrefour-generate-data-0.0.1-SNAPSHOT-jar-with-dependencies.jar -ns 100 -np 100 
-nt 100 -p /home/lansrod/data

OR
	
> java -jar carrefour-generate-data-0.0.1-SNAPSHOT-jar-with-dependencies.jar --numberOfStore 100
 --numberOfProduct 100 --numberofTransaction 100 --path /home/lansrod/data