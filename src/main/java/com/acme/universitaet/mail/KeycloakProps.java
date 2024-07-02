package com.acme.universitaet.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Spring-Konfiguration für Properties "app.keycloak.*".
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 * @param schema http oder https (für den Keycloak-Server)
 * @param host Rechnername des Keycloak-Servers
 * @param port Port des Keycloak-Servers
 * @param clientId Client-ID im Keycloak-Server
 * @param clientSecret Client-Secret gemäß der Client-Konfiguration in Keycloak
 */
@ConfigurationProperties(prefix = "app.keycloak")
public record KeycloakProps(
    @DefaultValue("http")
    String schema,

    @DefaultValue("localhost")
    String host,

    @DefaultValue("8880")
    int port,

    @DefaultValue("spring-client")
    String clientId,

    String clientSecret
) {
}
