package com.acme.universitaet.service;

import com.acme.universitaet.entity.Universitaet;
import com.acme.universitaet.repository.UniversitaetRepository;
import com.acme.universitaet.repository.Dozent;
import com.acme.universitaet.repository.DozentRepository;
import com.acme.universitaet.mail.KeycloakProps;
import com.acme.universitaet.security.KeycloakRepository;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.graphql.client.GraphQlTransportException;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * Anwendungslogik für Universitaeten.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UniversitaetReadService {
    private static final String ADMIN = "admin";
    private static final String PASSWORD = "p";

    private final UniversitaetRepository repo;
    private final DozentRepository dozentRepo;
    private final HttpSyncGraphQlClient graphQlClient;
    private final KeycloakRepository keycloakRepository;
    private final KeycloakProps keycloakProps;

    private String clientAndSecretEncoded;

    @PostConstruct
    private void encodeClientAndSecret() {
        final var clientAndSecret = keycloakProps.clientId() + ':' + keycloakProps.clientSecret();
        clientAndSecretEncoded = Base64
            .getEncoder()
            .encodeToString(clientAndSecret.getBytes(Charset.defaultCharset()));
    }

    /**
     * Alle Universitaeten ermitteln.
     *
     * @return Alle Universitaeten.
     */
    public Collection<Universitaet> findAll() {
        final var universitaeten = repo.findAll();
        final var authorization = getAuthorization();
        universitaeten.forEach(universitaet -> {
            final var dozentId = universitaet.getDozentId();

            final var name = findDozentById(dozentId, authorization).name();
            universitaet.setDozentName(name);

            final var email = findEmailById(dozentId, authorization);
            universitaet.setDozentEmail(email);
        });
        return universitaeten;
    }

    private String getAuthorization() {
        final var tokenDTO = keycloakRepository.login(
            "grant_type=password&username=" + ADMIN + "&password=" + PASSWORD,
            "Basic " + clientAndSecretEncoded,
            APPLICATION_FORM_URLENCODED_VALUE
        );
        return "Bearer " + tokenDTO.accessToken();
    }

    /**
     * Eine Universitaet anhand der ID suchen.
     *
     * @param id Die Id der gesuchten Universitaet.
     * @return Die gefundene Universitaet.
     * @throws NotFoundException Falls keine Universitaet gefunden wurde.
     */
    @Observed(name = "find-by-id")
    public Universitaet findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var universitaet = repo.findById(id).orElseThrow(NotFoundException::new);
        log.trace("findById: {}", universitaet);

        final var authorization = getAuthorization();
        final var name = findDozentById(universitaet.getDozentId(), authorization).name();
        universitaet.setDozentName(name);

        final var email = findEmailById(universitaet.getDozentId(), authorization);
        universitaet.setDozentEmail(email);

        return universitaet;
    }

    /**
     * Universitaeten zur Dozent-ID suchen.
     *
     * @param dozentId Die Id des gegebenen Dozentn.
     * @return Die gefundenen Universitaeten.
     * @throws NotFoundException Falls keine Universitaeten gefunden wurden.
     */
    public Collection<Universitaet> findByDozentId(final UUID dozentId) {
        log.debug("findByDozentId: dozentId={}", dozentId);

        final var universitaeten = repo.findByDozentId(dozentId);
        if (universitaeten.isEmpty()) {
            throw new NotFoundException();
        }

        final var authorization = getAuthorization();
        final var name = findDozentById(dozentId, authorization).name();
        final var email = findEmailById(dozentId, authorization);
        log.trace("findByDozentId: name={}, email={}", name, email);

        universitaeten.forEach(universitaet -> {
            universitaet.setDozentName(name);
            universitaet.setDozentEmail(email);
        });

        log.debug("findByDozentId: universitaeten={}", universitaeten);
        return universitaeten;
    }

    @SuppressWarnings("ReturnCount")
    private Dozent findDozentById(final UUID dozentId, final String authorization) {
        log.debug("findDozentById: dozentId={}", dozentId);

        final Dozent dozent;
        try {
            dozent = dozentRepo.getById(dozentId.toString(), authorization);
        } catch (final HttpClientErrorException.NotFound ex) {
            // Statuscode 404
            log.debug("findDozentById: HttpClientErrorException.NotFound");
            return new Dozent("N/A", "not.found@acme.com");
        } catch (final HttpStatusCodeException ex) {
            // sonstiger Statuscode 4xx oder 5xx
            // HttpStatusCodeException oder RestClientResponseException (z.B. ServiceUnavailable)
            log.debug("findDozentById", ex);
            return new Dozent("Exception", "exception@acme.com");
        }

        log.debug("findDozentById: {}", dozent);
        return dozent;
    }

    private String findEmailById(final UUID dozentId, final String authorization) {
        log.debug("findEmailById: dozentId={}", dozentId);

        final var query = """
            query {
                dozent(id: "$id") {
                    email
                }
            }
            """.replace("$id", dozentId.toString());

        final String email;
        try {
            final var dozentEmail = graphQlClient
                .mutate()
                .header(AUTHORIZATION, authorization)
                .build()
                .document(query)
                .retrieveSync("dozent")
                .toEntity(DozentEmail.class);
            if (dozentEmail == null) {
                return "N/A";
            }
            email = dozentEmail.email();
        } catch (final FieldAccessException | GraphQlTransportException ex) {
            log.debug("findEmailById", ex);
            return "N/A";
        }

        log.debug("findEmailById: email={}", email);
        return email == null ? "N/A" : email;
    }
}
