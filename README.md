# TransitMate

Aplicatie web pentru transportul public urban, dezvoltata cu Scrum in cadrul
proiectului de laborator MOPS (FMI, Universitatea din Bucuresti).

## Cerinte

- JDK 17 sau mai nou
- Maven 3.9

## Rulare

```
mvn spring-boot:run
```

Aplicatia porneste pe http://localhost:8080. Baza de date este H2 in memorie,
deci datele se reinitializeaza la fiecare pornire.

## Conturi de test

Conturile de personal sunt create direct in baza de date pana la US-32.
Parola implicita este `Staff12345` (se poate schimba cu variabila de mediu `STAFF_PASSWORD`).

| Rol | Email |
| --- | --- |
| Sofer | sofer@transitmate.ro |
| Controlor | controlor@transitmate.ro |
| Admin operator | admin@transitmate.ro |

Contul de calator se creeaza din pagina "Creeaza cont".

## Teste si code coverage

```
mvn verify
```

Raportul de coverage se genereaza in `target/site/jacoco/index.html`.
Build-ul pica daca acoperirea liniilor scade sub pragul din Definition of Done (60%).
Pipeline-ul CI (GitHub Actions) ruleaza aceeasi comanda la fiecare push si pull request.

## Reguli de lucru

- `main` este protejat: orice schimbare intra printr-un pull request revizuit, cu CI verde.
- Un branch pe User Story: `feature/US-01-inregistrare`.
- Mesajele de commit incep cu tipul si povestea: `feat(US-01): ...`, `test(US-01): ...`.
- Pentru povestile facute cu TDD, pasii Red, Green si Refactor sunt commit-uri separate.
- Pull request-urile se integreaza cu "Create a merge commit", nu cu "Squash", ca sa
  ramana vizibil istoricul TDD.
