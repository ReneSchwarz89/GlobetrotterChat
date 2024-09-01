# GlobetrotterChat

## Projektbeschreibung
GlobetrotterChat ist eine Android-Anwendung, die es Benutzern ermöglicht, in verschiedenen Sprachen zu kommunizieren. Die App bietet Funktionen wie Echtzeit-Übersetzungen, Profilverwaltung und Bild-Uploads.

## Installation
### Voraussetzungen
- Android Studio installiert ([Tutorial](https://www.youtube.com/watch?v=gYEOxDPHsp4))
- Google Cloud Platform Konto ([Tutorial](https://www.youtube.com/watch?v=Y4gb4Ce-I88))
- Firebase Projekt eingerichtet ([Tutorial](https://www.youtube.com/watch?v=fgdpvwEWJ9M))

### Schritte
1. **Repository klonen**:
    ```bash
    git clone https://github.com/dein-repo/globetrotterchat.git
    ```
2. **Projekt in Android Studio öffnen**:
    - Öffne Android Studio.
    - Wähle `File > Open` und navigiere zum geklonten Repository.

3. **Abhängigkeiten synchronisieren**:
    - Stelle sicher, dass alle Abhängigkeiten in der `build.gradle` Datei synchronisiert sind.

4. **Google Cloud Translation API einrichten**:
    - Erstelle ein Google Cloud Platform Konto (Tutorial).
    - Aktiviere die Google Cloud Translation API und erstelle einen API-Schlüssel.
    - Füge den API-Schlüssel in deiner `build.gradle` Datei oder in einer separaten Konfigurationsdatei hinzu.

5. **Firebase einrichten**:
    - Erstelle ein Firebase-Projekt (Tutorial).
    - Füge deine Android-App zu Firebase hinzu und binde die `google-services.json` Datei in dein Projekt ein.
    - Aktiviere Firebase Auth, Firestore und Storage in der Firebase-Konsole.

## Verwendung
1. **Registrierung und Anmeldung**:
    - Benutzer können sich mit ihrer E-Mail-Adresse und einem Passwort registrieren und anmelden.

2. **Profilverwaltung**:
    - Benutzer können ihr Profil erstellen und bearbeiten, einschließlich des Hochladens eines Profilbildes.

3. **Nachrichten senden und empfangen**:
    - Benutzer können Nachrichten in Echtzeit senden und empfangen.
    - Nachrichten werden automatisch in die Muttersprache des Empfängers übersetzt.

## Hauptfunktionen
- **Echtzeit-Übersetzungen**: Nachrichten werden automatisch in die Muttersprache des Empfängers übersetzt.
- **Profilverwaltung**: Benutzer können ihre Profile erstellen und bearbeiten.
- **Bild-Uploads**: Benutzer können Profilbilder hochladen und anzeigen.

## Technologien
- **Programmiersprache**: Kotlin
- **Architektur**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase Firestore, Firebase Auth, Firebase Storage
- **Übersetzungs-API**: Google Translation API
- **Bibliotheken**: Retrofit, Moshi, Glide

## Mitwirkende
- **Dein Name** - GitHub-Profil

## Lizenz
Dieses Projekt steht unter der MIT-Lizenz. Weitere Informationen findest du in der LICENSE Datei.
