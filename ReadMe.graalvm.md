# GraalVM

Um ein native Image mit GraalVM zu erstellen, sind folgende Vorbereitungen notwendig:

- GraalVM von https://www.graalvm.org/downloads muss als JDK in JAVA_HOME installiert sein.
  Dazu muss man ggf. IntelliJ IDEA beenden und den Gradle-Daemon mit `gradle --stop` beenden.
- Preview-Features dürfen _nicht_ verwendet werden, z.B. Virtual Threads aus Java 20
- in `build.gradle.kts`:
  - Plugin `org.graalvm.buildtools.native` aktivieren, d.h. Kommentar entfernen
  - `toolchain` auskommentieren und stattdessen `sourceCompatibility` und
    `targetCompatibility` verwenden

Jetzt kann man in einer _Eingabeaufforderung_ (NICHT: PowerShell) das Native Image
erstellen, was ca. 15 Min. dauern kann:

```cmd
    "C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
    PATH
    REM Kein Flyway, da die SQL-Skripte jetzt in C:\db\migration\h2 erwartet werden
    .\gradlew nativeCompile -Dflyway=false
```

Das Native Image wird dann folgendermaßen in einer PowerShell oder in der Eingabeaufforderung aufgerufen:

```PowerShell
    .\build\native\nativeCompile\universitaet.exe `
        --spring.profiles.active=dev --logging.file.name=.\build\log\application.log `
        --spring.datasource.url=jdbc:h2:mem:testdb `
        --spring.datasource.username=sa --spring.datasource.password=""
```
