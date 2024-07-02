package com.acme.universitaet.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;

/**
 * "HTTP Interface" für den REST-Client für Dozentdaten.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@HttpExchange("/rest")
public interface DozentRepository {
    /**
     * Einen Dozentendatensatz vom Microservice "dozent" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Dozenten
     * @param authorization String für den HTTP-Header "Authorization"
     * @return Gefundener Dozent
     */
    @GetExchange("/{id}")
    Dozent getById(@PathVariable String id, @RequestHeader(AUTHORIZATION) String authorization);

    /**
     * Einen Dozentendatensatz vom Microservice "dozent" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Dozenten
     * @param version Version des angeforderten Datensatzes
     * @param authorization String für den HTTP-Header "Authorization"
     * @return Gefundener Dozent
     */
    @GetExchange("/{id}")
    @SuppressWarnings("unused")
    ResponseEntity<Dozent> getById(
        @PathVariable String id,
        @RequestHeader(IF_NONE_MATCH) String version,
        @RequestHeader(AUTHORIZATION) String authorization
    );
}
