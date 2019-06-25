# Carrefour Test
> ce test contient le developpement des points 1 et 7

## Compilation

windows & Linux:

>mvn clean install 
 
>mvn clean install -DskipTests=true (pour eviter le test)
 
```   
deux archive jar sont creer apres la compilation <<carrefour-challenge-0.0.1.jar>> et 
<<carrefour-challenge-0.0.1-jar-with-dependencies.jar>>
utiliser le deuxieme jar par ce qu'il contient les dependances (log4j,junit ...)
```

##requirement

JDK 1.8
		 
## parametres

l'odre des parametre n'est pas important

-d,--date  : date of the transaction file a traiter au format yyyyMMdd (obligatoire))

-i,--input : path ou les donnee existe (obligatoire))

-o,--output: path ou les donnee seront stoker  (optionelle) valeur par defaut $HOME/out)

-t,--task  : numero de tache a executer 1 ou 7
			1 => top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data , 
			7 =>top_100_ca_<ID_MAGASIN>_YYYYMMDD-J7.data))]
 

## Usage example

>java -jar carrefour-challenge-0.0.1-jar-with-dependencies.jar --task 7 --input /home/lansrod/data 
	--date  20190625 --output /home/lansrod/out
	
OR
	
>java -jar carrefour-challenge-0.0.1-jar-with-dependencies.jar -i /home/lansrod/data 
	-d 20190625 -o /home/lansrod/out -t 7
