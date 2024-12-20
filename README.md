# Semesterarbeit 

**Gegenstand: Meta-Daten-Management-App (MAM)**

## Projektbeschreibung
Diese Applikation ist eine Semesterarbeit aus meinem Informatikstudium. Ziel des Projekts war es, eine vollständige Softwareanwendung von Anfang bis Ende zu planen, zu entwerfen und umzusetzen. Der Fokus lag auf drei zentralen Bereichen:

- **Projektmanagement**
- **Software Engineering**
- **Programmierung**

Das Projekt umfasst die Entwicklung einer **Meta-Daten-Management-App (MAM)** mit folgenden Zielen:

1. Implementierung von CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen) für Metadaten.
2. Entwurf und Umsetzung einer grafischen Benutzeroberfläche (GUI) mit JavaFXML.
3. Speicherung und Abruf von Daten in einer Online-Datenbank (ursprünglich MongoDB, mittlerweile offline).
4. Speicherung von Daten in einem lokalen Ordner (Anpassung des Codes erforderlich, um diese Funktion zu aktivieren).

## Technologien
- **Programmiersprache**: Java 17
- **Build-Tool**: Maven 3.8.1
- **GUI**: JavaFXML
- **Datenbank**: MongoDB (nicht mehr online)

## Installationsanleitung

### Voraussetzungen
1. **Java JDK 17**
   - Stelle sicher, dass Java 17 auf deinem System installiert ist.
   - Überprüfe die Installation mit dem Befehl:
     ```bash
     java -version
     ```

2. **Maven**
   - Installiere Maven, falls noch nicht vorhanden.
   - Überprüfe die Installation mit dem Befehl:
     ```bash
     mvn -version
     ```

### Schritte zur Installation
1. **Repository klonen**
   Klone dieses Repository mit folgendem Befehl:
   ```bash
   git clone https://github.com/Simon-Wittker/Semesterarbeit
   cd Semesterarbeit
   ```

2. **Abhängigkeiten installieren**
   Führe Maven aus, um alle Abhängigkeiten zu laden:
   ```bash
   mvn clean install
   ```

### Hinweise zur Nutzung
- Um die Metadaten in dem lokalen Ordner zu speichern, muss der Speicherpfad im Code angepasst werden. Der Ordner mit dem Namen "Lokale Ordner" befindet sich bereits im Repository und enthält einige Beispiel-Metadatenfiles, die für Tests verwendet werden können.

**Pfad anpassen im Code**
- Öffne die Datei: src/main/java/project/simtv_mam_app/gui_model/Model.java.
- Navigiere zu Zeile 38.
- Ersetze den bestehenden Pfad durch den gewünschten Pfad zu deinem lokalen Ordner.
- Beispiel: ```private final String folderFilePath = "/Volumes/SIMTV MAM/Lokaler Ordner";```

**Datenbankersatz**
- Die ursprünglich verwendete Online-Datenbank (MongoDB) steht nicht mehr zur Verfügung.

### Starten der Applikation
   Führe die Datei `Start.java` aus, um die Applikation zu starten. Die Datei befindet sich im Verzeichnis:
   ```plaintext
   src/main/java/project/simtv_mam_app/Start.java
   ```
## Lizenz

Dieses Projekt ist urheberrechtlich geschützt (All Rights Reserved).

- Der Quellcode darf nur zur Ansicht oder zu Testzwecken verwendet werden.
- Eine Nutzung, Modifikation oder Weitergabe des Codes oder Teile davon ist ohne ausdrückliche schriftliche Genehmigung des Urhebers nicht erlaubt.

Falls du Interesse an einer Lizenzierung oder weiteren Informationen hast, kontaktiere mich bitte.


