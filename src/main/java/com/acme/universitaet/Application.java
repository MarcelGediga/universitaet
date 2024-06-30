package com.acme.universitaet;

import com.acme.universitaet.dev.DevConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import static com.acme.universitaet.Banner.TEXT;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.hateoas.support.WebStack.WEBMVC;

/**
 * Klasse mit der main-Methode für die Anwendung auf Basis von Spring Boot.
 * <img src="../../../../asciidoc/Komponenten.svg" alt="Komponentendiagramm">
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@SpringBootApplication(proxyBeanMethods = false)
@Import({ApplicationConfig.class, DevConfig.class})
@EnableConfigurationProperties({KeycloakProps.class, MailProps.class})
@EnableHypermediaSupport(type = HAL, stacks = WEBMVC)
@EnableJpaRepositories
@EnableWebSecurity
@EnableMethodSecurity
@EnableAsync
@SuppressWarnings({"ImplicitSubclassInspection", "ClassUnconnectedToPackage"})
public final class Application {
    private Application() {
    }

    /**
     * Hauptprogramm, um den Microservice zu starten.
     *
     * @param args Evtl. zusätzliche Argumente für den Start des Microservice
     */
    @SuppressWarnings({"IllegalIdentifierName", "LambdaParameterName"})
    public static void main(final String... args) {
        final var app = new SpringApplication(Application.class);
        app.setBanner((_, _, out) -> out.println(TEXT));
        app.run(args);
    }
}
