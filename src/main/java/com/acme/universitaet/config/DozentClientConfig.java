package com.acme.universitaet.config;

import com.acme.universitaet.repository.DozentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Beans für die REST-Schnittstelle zu "dozent" (DozentRepository) und für die GraphQL-Schnittstelle zu "dozent"
 * (HttpGraphQlClient).
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@SuppressWarnings("java:S1075")
sealed interface DozentClientConfig permits ApplicationConfig {
    /**
     * Logger-Objekt für DozentClientConfig.
     */
    Logger LOGGER = LoggerFactory.getLogger(DozentClientConfig.class);

    /**
     * Bean-Methode, um ein Objekt von UriComponentsBuilder für die URI für Keycloak zu erstellen.
     *
     * @return Objekt von UriComponentsBuilder für die URI für Keycloak
     */
    @Bean
    @SuppressWarnings("CallToSystemGetenv")
    default UriComponentsBuilder uriComponentsBuilder() {
        final var dozentDefaultPort = 8080;

        // Umgebungsvariable in Kubernetes, sonst: null
        final var dozentSchemaEnv = System.getenv("DOZENT_SERVICE_SCHEMA");
        final var dozentHostEnv = System.getenv("DOZENT_SERVICE_HOST");
        final var dozentPortEnv = System.getenv("DOZENT_SERVICE_PORT");

        // TODO URI bei Docker Compose
        final var schema = dozentSchemaEnv == null ? "https" : dozentSchemaEnv;
        final var host = dozentHostEnv == null ? "localhost" : dozentHostEnv;
        final int port = dozentPortEnv == null ? dozentDefaultPort : Integer.parseInt(dozentPortEnv);

        LOGGER.debug("dozent: host={}, port={}", host, port);
        return UriComponentsBuilder.newInstance()
            .scheme(schema)
            .host(host)
            .port(port);
    }

    /**
     * Bean-Methode, um ein Objekt von DozentRepository für die REST-Schnittstelle von dozent zu erstellen.
     *
     * @param uriComponentsBuilder Injiziertes Objekt von UriComponentsBuilder
     * @param restClientBuilder Injiziertes Objekt von RestClient.Builder
     * @param restClientSsl Injiziertes Objekt von RestClientSsl
     * @return Objekt von DozentRepository für die REST-Schnittstelle von dozent
     */

    @Bean
    default DozentRepository dozentRepository(
        final UriComponentsBuilder uriComponentsBuilder,
        final RestClient.Builder restClientBuilder,
        final RestClientSsl restClientSsl
    ) {
        final var baseUrl = uriComponentsBuilder.build().toUriString();
        LOGGER.info("REST-Client: baseUrl={}", baseUrl);

        final var restClient = restClientBuilder
            .baseUrl(baseUrl)
            // siehe Property "spring.ssl.bundle.jks.microservice" in src\main\resources\application.yml
            // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.ssl
            // org.springframework.boot.autoconfigure.web.client.AutoConfiguredRestClientSsl
            .apply(restClientSsl.fromBundle("microservice"))
            .build();
        final var clientAdapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        return proxyFactory.createClient(DozentRepository.class);
    }

    /**
     * Bean-Methode, um ein Objekt von HttpSyncGraphQlClient für die GraphQL-Schnittstelle von dozent zu erstellen.
     *
     * @param uriComponentsBuilder Injiziertes Objekt von UriComponentsBuilder
     * @param restClientBuilder Injiziertes Objekt von RestClient.Builder
     * @param restClientSsl Injiziertes Objekt von RestClientSsl
     * @return Objekt von HttpSyncGraphQlClient für die GraphQL-Schnittstelle von dozent
     */
    // siehe org.springframework.graphql.client.DefaultHttpGraphQlClientBuilder.DefaultHttpGraphQlClient
    @Bean
    default HttpSyncGraphQlClient graphQlClient(
        final UriComponentsBuilder uriComponentsBuilder,
        final RestClient.Builder restClientBuilder,
        final RestClientSsl restClientSsl
    ) {
        final var graphqlPath = "/graphql";
        final var baseUrl = uriComponentsBuilder
            .path(graphqlPath)
            .build()
            .toUriString();
        LOGGER.info("GraphQL-Client: baseUrl={}", baseUrl);

        final var restClient = restClientBuilder
            .baseUrl(baseUrl)
            // siehe Property "spring.ssl.bundle.jks.microservice" in src\main\resources\application.yml
            // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.ssl
            // https://spring.io/blog/2023/06/07/securing-spring-boot-applications-with-ssl
            // org.springframework.boot.autoconfigure.web.reactive.function.client.AutoConfiguredWebClientSsl
            .apply(restClientSsl.fromBundle("microservice"))
            .build();
        return HttpSyncGraphQlClient.builder(restClient).build();
    }
}
