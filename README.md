# PW3_Gruppo-2-Backend

Questo è un progetto Java Spring Boot che utilizza Maven come strumento di gestione del progetto.  
Prerequisiti

Per eseguire questo progetto, avrai bisogno di:
-JDK 11 o superiore
-Maven 3.6.0 o superiore

Installazione
1) Clona il repository GitHub sul tuo computer locale. Puoi farlo utilizzando il seguente comando git:
`git clone https://github.com/FabioGiannico/PW3_Gruppo-2-Backend/commits/develop/`

2) Naviga nella directory del progetto
  `cd PW3_Gruppo-2-Backend`

3) Esegui il progetto utilizzando Maven

4) l'email e la password del admin sono:
email: admin.one@example.com
password: Admin01!

## Documentazione del codice

* Il codice è organizzato in vari pacchetti, ognuno dei quali ha una specifica responsabilità nel progetto. Ecco una breve descrizione di ciascuno:  
* it.itsincom.webdev2023.persistence.repository: Contiene le classi repository che gestiscono l'interazione con il database.
* it.itsincom.webdev2023.persistence.model: Contiene le classi modello che rappresentano le entità del database.
* it.itsincom.webdev2023.rest.model: Contiene le classi modello utilizzate per la comunicazione REST.
* it.itsincom.webdev2023.rest: Contiene le classi resource che gestiscono le richieste HTTP.
* it.itsincom.webdev2023.service: Contiene le classi di servizio che implementano la logica tra mondo del database e il REST.

# pw-back-end

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/pw-back-end-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST JSON-B ([guide](https://quarkus.io/guides/rest#json-serialisation)): JSON-B serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Agroal - Database connection pool ([guide](https://quarkus.io/guides/datasource)): Pool JDBC database connections (included in Hibernate ORM)
- JDBC Driver - MySQL ([guide](https://quarkus.io/guides/datasource)): Connect to the MySQL database via JDBC

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
