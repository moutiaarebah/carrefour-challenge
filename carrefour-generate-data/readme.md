# Carrefour Test
> ce test contient le developpement des points 1 et 7

## Compilation

windows X & Linux:

```mvn clean install 
   
la compilation cree deux archive jar carrefour-challenge-0.0.1.jar et 
carrefour-challenge-0.0.1-jar-with-dependencies.jar

utiliser le deuxieme jar par ce qu'il contient les dependances
 
```

##requirement

```
	JDK 1.8
		
```

## parametres

* [-p-,--path  : number of product per magasin ]
* [-ns,--numberOfStore :  number of store]
* [-nt,--numberofTransaction :  nombre de transaction]
* [-np,--numberOfProduct <arg>   number of product per magasin]
 
 
## Usage example

> java -jar carrefour-generate-data-0.0.1-SNAPSHOT-jar-with-dependencies.jar -ns 100 -np 100 
-nt 100 -p /home/lansrod/data

	
OR
	
> java -jar carrefour-generate-data-0.0.1-SNAPSHOT-jar-with-dependencies.jar --numberOfStore 100
 --numberOfProduct 100 --numberofTransaction 100 --path /home/lansrod/data

