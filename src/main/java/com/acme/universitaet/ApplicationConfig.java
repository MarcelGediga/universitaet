package com.acme.universitaet;

import com.acme.universitaet.security.KeycloakClientConfig;
import com.acme.universitaet.security.SecurityConfig;

/**
 * Konfigurationsklasse für die Anwendung bzw. den Microservice.
 */
final class ApplicationConfig implements SecurityConfig, KeycloakClientConfig {
    ApplicationConfig() {
    }
}
