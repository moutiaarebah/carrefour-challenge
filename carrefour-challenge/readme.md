# Carrefour Test
> ce test contient le développement des points 1 et 7.

## Compilation

windows & Linux: (JDK 1.8 est pré requis)

>mvn clean install 
 
>mvn clean install -DskipTests=true (pour eviter le test)
 

deux archives jar sont créer aprés la compilation <<carrefour-challenge-0.0.1.jar>> et 
<<carrefour-challenge-0.0.1-jar-with-dependencies.jar>>
utiliser le deuxième  jar par ce qu'il contient les dépendances (log4j,common-cli ...)

		 
## parametres

l'odre des paramètres n'est pas important

-d,--date  : date of the transaction file à traiter au format yyyyMMdd (obligatoire)

-i,--input : path des fichiers (obligatoire)

-o,--output: path de resultat  (optionnelle) valeur par defaut $HOME/out)

-t,--task  : numero de tache a executer 1 ou 7
	1 => top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data , 
	7 =>top_100_ca_<ID_MAGASIN>_YYYYMMDD-J7.data (obligatoire);
 

## Usage example

>java -jar carrefour-challenge-0.0.1-jar-with-dependencies.jar --task 7 --input /home/lansrod/data 
	--date  20190625 --output /home/lansrod/out
	
OR
	
>java -jar carrefour-challenge-0.0.1-jar-with-dependencies.jar -i /home/lansrod/data 
	-d 20190625 -o /home/lansrod/out -t 7
