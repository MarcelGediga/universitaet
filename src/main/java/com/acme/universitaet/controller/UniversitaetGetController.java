package com.acme.universitaet.controller;

import com.acme.universitaet.entity.Universitaet;
import com.acme.universitaet.service.UniversitaetReadService;
import com.acme.universitaet.service.NotFoundException;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static com.acme.universitaet.controller.UniversitaetGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Funktionen der Klasse abgebildet werden.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@RestController
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Universitaeten API")
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class UniversitaetGetController {
    /**
     * Basispfad für die REST-Schnittstelle.
     */
    public static final String REST_PATH = "/rest";
    /**
     * Muster bzw. regulärer Ausdruck für eine UUID.
     */
    static final String ID_PATTERN = "[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}";


    private final UniversitaetReadService service;
    private final UriHelper uriHelper;

    /**
     * Suche anhand der Universitaet-ID.
     *
     * @param id ID der zu suchenden Universitaet
     * @param version Versionsnummer beim Request-Header If-None-Match
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Ein Response mit dem Statuscode 200 und der gefundenen Universitaet einschließlich Atom-Links,
     *      oder aber Statuscode 204.
     */
    @GetMapping(path = "/{id:" + ID_PATTERN + '}', produces = HAL_JSON_VALUE)
    @Observed(name = "get-by-id")
    @Operation(summary = "Suche mit der Universitaet-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Universitaet gefunden")
    @ApiResponse(responseCode = "404", description = "Universitaet nicht gefunden")
    ResponseEntity<UniversitaetModel> getById(
        @PathVariable final UUID id,
        @RequestHeader("If-None-Match") final Optional<String> version,
        final HttpServletRequest request
    ) {
        log.debug("getById: id={}, version={}", id, version);

        final var universitaet = service.findById(id);
        final var currentVersion = "\"" + universitaet.getVersion() + '"';
        if (Objects.equals(version.orElse(null), currentVersion)) {
            return status(NOT_MODIFIED).build();
        }

        // HATEOAS
        final var model = new UniversitaetModel(universitaet);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + universitaet.getId();

        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        return ok(model);

    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter. Es wird eine Collection zurückgeliefert, damit auch der
     * Statuscode 204 möglich ist.
     *
     * @param queryParams Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Ein Response mit dem Statuscode 200 und einer Collection mit den gefundenen Universitaeten einschließlich
     *      Atom-Links, oder aber Statuscode 204.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Universitaeten")
    @ApiResponse(responseCode = "404", description = "Keine Universitaeten gefunden")
    @SuppressWarnings("ReturnCount")
    ResponseEntity<CollectionModel<UniversitaetModel>> get(
        @RequestParam final Map<String, String> queryParams,
        final HttpServletRequest request
    ) {
        log.debug("get: queryParams={}", queryParams);
        if (queryParams.size() > 1) {
            return notFound().build();
        }

        final Collection<Universitaet> universitaeten;
        if (queryParams.isEmpty()) {
            universitaeten = service.findAll();
        } else {
            final var dozentIdStr = queryParams.get("dozentId");
            if (dozentIdStr == null) {
                return notFound().build();
            }
            final var dozentId = UUID.fromString(dozentIdStr);
            universitaeten = service.findByDozentId(dozentId);
        }

        final var baseUri = uriHelper.getBaseUri(request).toString();
        @SuppressWarnings("LambdaBodyLength")
        final var models = universitaeten
            .stream()
            .map(universitaet -> {
                final var model = new UniversitaetModel(universitaet);
                model.add(Link.of(baseUri + '/' + universitaet.getId()));
                return model;
            })
            .toList();
        log.trace("get: {}", models);

        return ok(CollectionModel.of(models));
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        log.debug("onNotFound: {}", ex.getMessage());
    }
}
