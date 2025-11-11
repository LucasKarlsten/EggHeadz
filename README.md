# The PingURL application

Tagline: Where URL goes to get pinged.

## Läs detta först!

* Skapa ett repository på ditt GitHub-konto.
* Sätt ssh-URLen till detta GitHub-repo som `remote origin` här.
* Pusha upp detta till main-branchen.

När detta är på plats behöver du lägga till mig som Collaborator för detta repository på GitHub
för att jag ska kunna se hur det går.

I GitHub, när du har repot öppet, under *Settings*, till vänster under Access finns Collaborators. Klicka där.

Under "Manage access", klicka på knapped "Add people".

Skriv "jimpa" och tryck på knappen "Add jimpa".

När detta är gjort, skicka mig ett email med länken till ditt nya repository.

## Bygga applikationen
Se till att ha Java 25 installerat.

```
./mvnw clean package
```

## Köra applikationen

```
java -jar target/pingurl-0.0.1-SNAPSHOT.jar
```
