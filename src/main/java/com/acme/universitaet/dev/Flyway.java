package com.acme.universitaet.dev;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

/**
 * Migrationsstrategie für Flyway im Profile "dev": Tabellen, Indexe etc. löschen und dann neu aufbauen.
 * <p>
 * Diese Schnittstelle definiert eine benutzerdefinierte Migrationsstrategie für Flyway im Profil "dev".
 * Sie verwendet die Spring Boot-Annotation @Bean, um eine Instanz von FlywayMigrationStrategy bereitzustellen.
 */
interface Flyway {

    /**
     * Bean-Definition, um eine Migrationsstrategie für Flyway im Profile "dev" bereitzustellen, so dass zuerst alle
     * Tabellen, Indexe etc. gelöscht und dann neu aufgebaut werden.
     *
     * @return FlywayMigrationStrategy - Eine Instanz von FlywayMigrationStrategy, die die spezifizierte Strategie implementiert.
     */
    @Bean
    default FlywayMigrationStrategy cleanMigrateStrategy() {
        // Implementierung der Migrationsstrategie für Flyway
        return flyway -> {
            // Löschen aller DB-Objekte im Schema: Tabellen, Indexe, Stored Procedures, Trigger, Views, ...
            // insbesondere die Tabelle flyway_schema_history
            flyway.clean();
            // Start der DB-Migration
            flyway.migrate();
        };
    }
}
