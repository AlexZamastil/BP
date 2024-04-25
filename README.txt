spuštění projektu

BE
Kód backenodové části ne nachází v podadresáři:

                src/main/java

testovací třídy se nachází v:

                src/test

Pro spuštění Backendové části projektu spustit spustitenou třídu v podadresáři:

                src/main/java/cz/uhk/fim/project/bakalarka/BakalarkaJavaApplication.java


FE
Pro spuštění Webové části v terminálu otevřít podadresář:

                src\main\frontend

                v tomto podadresáři zadat příkazy nejprve npm init při prvním spuštění, dále jen npm start

DOCKER

Pro spuštění containerizované verze otevřít podadresář:

            docker

            v něm zadat příkaz docker compose build a následně docker compose up

            Pro spuštění aplikace v dockeru je nutné mít běžící program Docker desktop

DB
Pro správný chod databáze je potřeba mít v databází data.

soubor db_backup.sql umožňuje nahrát celou strukturu databáze včetně dat.

Na zařízení je potřeba vytvořit postgreSQL databázi, nahrát do ni db_backup.sql (funkce restore) a přizpůsobit
soubor application.properties názvu a portu databáze. Konkrétně údaj spring.datasource.url
Lze spustit i bez testovacích dat, ale např. generování tréninku bez těchto dat není možné.
V testovacích datech je vytvořený uživatel s rolí administrátora. email je : a@admin.cz a heslo 12345678


