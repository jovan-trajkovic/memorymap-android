# Uporabljena tehnologija: Room for Android
___

Room je del odprtokodne skupine knjižnic Android Jetpack, ki se uporablja za shranjevanje podatkov v lokalni podatkovni bazi SQLite.
Predstavlja abstrakcijsko plast nad SQLite podatkovno bazo, ki nam omogoča lažje upravljanje z bazo.

## Prednosti:
- Preverjanje poizvedbah ob prevajanju
- Anotacije, ki nam minimizirajo pisanje boilerplate kode (Z anotacijami: @Entity, @Insert, itd.)
- Dobro sodelovanje z drugimi Jetpack knjižnicami
## Slabosti:
- Potrebno je veliko dela ob začetku (Ustvarjanje entitet, DAO, podatkovne baze...)
- SQLite je lahko pomanjkljiv naprednejšim uporabnikom

## Statistike:
Na Jetpack androidx knjižnicami je delalo več kot 700 razvijalcev, z 9 glavnih lastnikov Android Jetpack-a.<br>
Skupaj so razvijalci naredili več kot 195000 commitov. Na GitHubu je zadnji commit bil pred enim tednom, in je trenutno aktivna verzija Room-a 2.6.1. [source](https://github.com/androidx/androidx)

Ocenjeno je da obstaja več kot 200 stackov, ki med drugimi tehnologijami uporabljajo tudi Room. [source](https://stackshare.io/android-room)<br>
Na GitHubu je še več kot tisoč repozitorijev, ki so forkali Jetpack androidx repozitorij.

## Licenca:
Jetpack uporablja Apache 2.0 licenco za odprto kodo, kar omogoča uporabnikom da modificirajo in delijo modificirano kodo brez plačanja licenc.

## Primeri uporabe:
- Prvo je potrebno vključiti androidx:room knjižnico in room compiler z kotlin anotacijo
- Nato je potrebno zgraditi podatkovno bazo in definirati entitete, ki predstavljajo tabele v bazi in DAO (Data Access Object), ki določajo, kako bo potekala komunikacija z bazo.
- Bazo definiramo in določimo imena tabel:<br>
![database](./README%20images/database.png "Definirana baza")
- Definiramo entitete, ki nam bodo predstavljale tabele v bazi in objekte v kodi: <br>
![entity](./README%20images/entity.png "Definirana entiteta")
- Definiramo DAO razrede in komunikacijo z bazo: <br>
![DAO](./README%20images/dao.png "Definiran DAO")
- V razredu MyApplication je kreiran objekt podatkovne baze, ki vsebuje objekt DAO. Lahko mu dostopamo iz katerega koli fragmenta in komuniciramo z bazo: <br>
![usage](./README%20images/usage.png "Uporaba")
- Vsaka komunikacija mora biti narejena asinhrono, v korutini na niti, ki ni glavna.
- Podatki so lokalno shranjeni na napravi in bazo lahko odpremo v upravljalniku na računalniku: <br>
![database_contents](./README%20images/database_contents.png "Vsebina baze")
