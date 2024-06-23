package com.acme.universitaet.rest;

import com.acme.universitaet.rest.UniversitaetDTO.OnCreate;
import com.acme.universitaet.service.EmailExistsException;
import com.acme.universitaet.service.UniversitaetWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.acme.universitaet.rest.UniversitaetGetController.ID_PATTERN;
import static com.acme.universitaet.rest.UniversitaetGetController.REST_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/UniversitaetWriteController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075", "preview", "java:S6856"})
class UniversitaetWriteController {
    private static final String PROBLEM_PATH = "/problem/";

    private final UniversitaetWriteService service;
    private final UniversitaetMapper mapper;
    private final UriHelper uriHelper;

    /**
     * Einen neuen Universitaet-Datensatz anlegen.
     *
     * @param universitaetDTO Das Universitaetobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls Constraints verletzt
     *      sind oder die Emailadresse bereits existiert oder Statuscode 400 falls syntaktische Fehler im Request-Body
     *      vorliegen.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Universitaeten anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Universitaet neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    ResponseEntity<Void> post(
        @RequestBody @Validated({Default.class, OnCreate.class}) final UniversitaetDTO universitaetDTO,
        final HttpServletRequest request
    ) {
        log.debug("post: {}", universitaetDTO);

        final var universitaetInput = mapper.toUniversitaet(universitaetDTO);
        final var universitaet = service.create(universitaetInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(baseUri + '/' + universitaet.getId());
        return created(location).build();
    }

    /**
     * Einen vorhandenen Universitaet-Datensatz überschreiben.
     *
     * @param id ID des zu aktualisierenden Universitaeten.
     * @param universitaetDTO Das Universitaetenobjekt aus dem eingegangenen Request-Body.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eine Universitaet mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Universitaet nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    void put(@PathVariable final UUID id,
             @RequestParam(name = "version") int version,
             @RequestBody @Valid final UniversitaetDTO universitaetDTO) {
        log.debug("put: id={}, {}, version={}", id, universitaetDTO, version);
        final var universitaetInput = mapper.toUniversitaet(universitaetDTO);
        service.update(universitaetInput, id, version);
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final ConstraintViolationException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            // Methodenname und Argumentname entfernen: siehe @Valid in der Service-Klasse
            ex.getMessage().replace("create.universitaet.", "")
                .replace("update.universitaet.", "")
        );
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}/\{ProblemType.CONSTRAINTS.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onEmailExists(final EmailExistsException ex, final HttpServletRequest request) {
        log.debug("onEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}/\{ProblemType.CONSTRAINTS.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        log.debug("onMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}/\{ProblemType.BAD_REQUEST.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
