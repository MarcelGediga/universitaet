package com.acme.universitaet.config;

/**
 * Konfigurationsklasse für die Anwendung bzw. den Microservice.
 */
public final class ApplicationConfig implements SecurityConfig, KeycloakClientConfig, DozentClientConfig {
    ApplicationConfig() {
    }
}
