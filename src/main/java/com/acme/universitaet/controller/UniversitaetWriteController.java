package com.acme.universitaet.controller;

import com.acme.universitaet.service.UniversitaetWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.acme.universitaet.controller.UniversitaetGetController.REST_PATH;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Funktionen der Klasse abgebildet werden.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Controller
@RequestMapping(REST_PATH)
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Universitaeten API")
@SuppressWarnings("java:S1075")
class UniversitaetWriteController {
    private static final String PROBLEM_PATH = "/problem/";

    private final UniversitaetWriteService service;
    private final UniversitaetMapper mapper;
    private final UriHelper uriHelper;

    /**
     * Einen neuen Universitaet-Datensatz anlegen.
     *
     * @param universitaetDTO Das Universitaetsobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um Location im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls Constraints verletzt
     *      sind oder der JSON-Datensatz syntaktisch nicht korrekt ist.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Eine neue Universitaet anlegen")
    @ApiResponse(responseCode = "201", description = "Universitaet neu angelegt")
    @ApiResponse(responseCode = "400", description = "Ungültige Werte vorhanden")
    ResponseEntity<Void> post(
        @RequestBody
        @Valid
        final UniversitaetDTO universitaetDTO,
        final HttpServletRequest request
    ) {
        log.debug("post(): {}", universitaetDTO);
        final var universitaetInput = mapper.toUniversitaet(universitaetDTO);
        final var universitaet = service.create(universitaetInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var id = universitaet.getId() == null ? "" : universitaet.getId().toString();
        final var location = URI.create(baseUri + '/' + id);
        log.debug("post: {}", location);
        return created(location).build();
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var detailMessages = ex.getDetailMessageArguments();
        final var detail = detailMessages == null
            ? "Constraint Violations"
            : ((String) detailMessages[1]).replace(", and ", ", ");
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }
}
