# Bibliotekshanteraren (CLI)

Laboration 1 i Javaprogrammering. Ett konsolprogram för att hantera ett litet bibliotek med böcker, medlemmar och utlåning.

## Funktioner

1. Lägg till bok
2. Registrera medlem
3. Låna bok
4. Lämna tillbaka bok
5. Sök bok på (del av) titel eller författare, skiftlägesokänsligt
6. Visa alla böcker och status, sorterade i bokstavsordning på titel
7. Visa alla medlemmar
8. Visa medlem med flest aktiva lån
9. Visa lån för en medlem

När man lånar en bok visas först alla lediga böcker, så att man slipper komma ihåg ISBN-numret. När man lägger till en bok måste ISBN vara 10 eller 13 siffror.

`e` avslutar programmet. När programmet startar finns redan några böcker och två medlemmar inlagda, så att man kan testa direkt.

## Köra programmet

Öppna projektet i IntelliJ och kör `main` i `Main.java`, eller bygg med Maven:

```
mvn package
java -cp target/classes se.amir.Main
```

Kräver Java 21 eller senare.

## Struktur

| Klass | Ansvar |
|---|---|
| `Book` | Record med isbn, titel och författare |
| `Member` | Medlem med medlemsnummer, namn och antal aktiva lån. Har `canBorrowMore()` som kollar om medlemmen får låna fler böcker (max 3) |
| `Library` | Håller arrayerna med böcker och medlemmar och all logik för lån, sökning, sortering och statistik |
| `Main` | Menyn, inläsning från användaren och utskrifter |
