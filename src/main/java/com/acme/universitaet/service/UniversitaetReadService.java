package com.acme.universitaet.service;

import com.acme.universitaet.entity.Universitaet;
import com.acme.universitaet.repository.SpecificationBuilder;
import com.acme.universitaet.repository.UniversitaetRepository;
import com.acme.universitaet.security.Rolle;
import io.micrometer.observation.annotation.Observed;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.acme.universitaet.security.Rolle.ADMIN;

/**
 * Anwendungslogik für Universitaet.
 * <img src="../../../../../asciidoc/UniversitaetReadService.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UniversitaetReadService {
    private final UniversitaetRepository repo;
    private final SpecificationBuilder specificationBuilder;

    /**
     * Eine Universitaet anhand ihrer ID suchen.
     *
     * @param id Die Id der gesuchten Universitaet
     * @param username Benutzername aus einem JWT
     * @param rollen Rollen als Liste von Enums
     * @param fetchFakultaeten true, falls die Umsätze mitgeladen werden sollen
     * @return Die gefundene Universitaet
     * @throws NotFoundException Falls keine Universitaet gefunden wurde
     * @throws AccessForbiddenException Falls die erforderlichen Rollen nicht gegeben sind
     */
    @Observed(name = "find-by-id")
    public @NonNull Universitaet findById(
        final UUID id,
        final String username,
        final List<Rolle> rollen,
        final boolean fetchFakultaeten
    ) {
        log.debug("findById: id={}, username={}, rollen={}", id, username, rollen);

        final var universitaetOptional = fetchFakultaeten ? repo.findByIdFetchFakultaeten(id) : repo.findById(id);
        final var universitaet = universitaetOptional.orElse(null);
        log.trace("findById: universitaet={}", universitaet);

        // beide find()-Methoden liefern ein Optional
        if (universitaet != null && universitaet.getUsername().contentEquals(username)) {
            // eigene Universitaetsdaten
            return universitaet;
        }

        if (!rollen.contains(ADMIN)) {
            // nicht admin, aber keine eigenen (oder keine) Universitaetsdaten
            throw new AccessForbiddenException(rollen);
        }

        // admin: Universitaetsdaten evtl. nicht gefunden
        if (universitaet == null) {
            throw new NotFoundException(id);
        }
        log.debug("findById: universitaet={}, fakultaet={}",
            universitaet, fetchFakultaeten ? universitaet.getFakultaeten() : "N/A");
        return universitaet;
    }

    /**
     * Universitaeten anhand von Suchkriterien als Collection suchen.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Universitaeten oder eine leere Liste
     * @throws NotFoundException Falls keine Universitaeten gefunden wurden
     */
    @SuppressWarnings("ReturnCount")
    public @NonNull Collection<Universitaet> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var namen = suchkriterien.get("name");
            if (namen != null && namen.size() == 1) {
                return findByName(namen.getFirst(), suchkriterien);
            }

            final var emails = suchkriterien.get("email");
            if (emails != null && emails.size() == 1) {
                return findByEmail(emails.getFirst(), suchkriterien);
            }
        }

        final var specification = specificationBuilder
            .build(suchkriterien)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var universitaeten = repo.findAll(specification);
        if (universitaeten.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", universitaeten);
        return universitaeten;
    }

    private List<Universitaet> findByName(final String name, final Map<String, List<String>> suchkriterien) {
        log.trace("findByName: {}", name);
        final var universitaeten = repo.findByName(name);
        if (universitaeten.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("findByName: {}", universitaeten);
        return universitaeten;
    }

    private Collection<Universitaet> findByEmail(final String email, final Map<String, List<String>> suchkriterien) {
        log.trace("findByEmail: {}", email);
        final var universitaet = repo
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var universitaeten = List.of(universitaet);
        log.debug("findByEmail: {}", universitaeten);
        return universitaeten;
    }

    /**
     * Abfrage, welche Namen es zu einem Präfix gibt.
     *
     * @param prefix Name-Präfix.
     * @return Die passenden Namen in alphabetischer Reihenfolge.
     * @throws NotFoundException Falls keine Namen gefunden wurden.
     */
    public @NonNull List<String> findNamenByPrefix(final String prefix) {
        log.debug("findNamenByPrefix: {}", prefix);
        final var namen = repo.findNamenByPrefix(prefix);
        if (namen.isEmpty()) {
            //noinspection NewExceptionWithoutArguments
            throw new NotFoundException();
        }
        log.debug("findNamenByPrefix: {}", namen);
        return namen;
    }
}
