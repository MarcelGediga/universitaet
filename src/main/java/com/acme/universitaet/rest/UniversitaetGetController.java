package com.acme.universitaet.rest;

import com.acme.universitaet.service.UniversitaetReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static com.acme.universitaet.rest.UniversitaetGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller für die Abfrage von Universitätsdaten.
 * <img src="../../../../../asciidoc/UniversitaetGetController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@RestController
@RequestMapping(REST_PATH)
@OpenAPIDefinition(info = @Info(title = "Universitaet API", version = "v1"))
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"java:S1075", "java:S6856"})
public class UniversitaetGetController {
    /**
     * REST-Pfad für die Universitäts-Ressourcen.
     */
    public static final String REST_PATH = "/rest";

    /**
     * Pfad, um Namen abzufragen.
     */
    public static final String NAME_PATH = "/name";

    /**
     * Muster für die Universitäts-ID.
     */
    public static final String ID_PATTERN = "[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}";

    private final UniversitaetReadService service;
    private final UriHelper uriHelper;

    /**
     * Sucht eine Universität anhand ihrer ID.
     *
     * @param id Die ID der gesuchten Universität.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Die gesuchte Universität.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche nach ID", tags = "Pfad-Suche")
    @ApiResponse(responseCode = "200", description = "Universitaet gefunden")
    @ApiResponse(responseCode = "404", description = "Universitaet nicht gefunden")
    UniversitaetModel getById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("getById: id={}, Thread={}", id, Thread.currentThread().getName());

        // Geschaeftslogik bzw. Anwendungskern
        final var universitaet = service.findById(id);

        // HATEOAS
        final var model = new UniversitaetModel(universitaet);
        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + universitaet.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        log.debug("getById: {}", model);
        return model;
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     *
     * @param suchkriterien Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundenen Universitaet als Collection.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Universitaeten")
    @ApiResponse(responseCode = "404", description = "Keine Universitaeten gefunden")
    CollectionModel<UniversitaetModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("get: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();

        // Geschaeftslogik bzw. Anwendungskern
        final var models = service.find(suchkriterien)
            .stream()
            .map(universitaet -> {
                final var model = new UniversitaetModel(universitaet);
                model.add(Link.of(baseUri + '/' + universitaet.getId()));
                return model;
            })
            .toList();

        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }

    /**
     * Abfrage, welche Namen es zu einem Präfix gibt.
     *
     * @param prefix Name-Präfix als Pfadvariable.
     * @return Die passenden Namen oder Statuscode 404, falls es keine gibt.
     */
    @GetMapping(path = NAME_PATH + "/{prefix}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche Namen mit Praefix", tags = "Suchen")
    String getNamenByPrefix(@PathVariable final String prefix) {
        log.debug("getNamenByPrefix: {}", prefix);
        final var namen = service.findNamenByPrefix(prefix);
        log.debug("getNamenByPrefix: {}", namen);
        return namen.stream()
            .map(name -> STR."\"\{name}\"")
            .toList()
            .toString();
    }
}
