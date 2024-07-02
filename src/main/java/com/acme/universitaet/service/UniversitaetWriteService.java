package com.acme.universitaet.service;

import com.acme.universitaet.entity.Universitaet;
import com.acme.universitaet.repository.UniversitaetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Anwendungslogik für Universitaeten.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UniversitaetWriteService {
    private final UniversitaetRepository repo;

    /**
     * Eine neue Universitaet anlegen, falls keine Constraint-Verletzungen vorliegen.
     *
     * @param universitaet Das Objekt der neu anzulegenden Universitaet.
     * @return Die neu angelegte Universitaet mit generierter ID.
     */
    @Transactional
    public Universitaet create(final Universitaet universitaet) {
        log.debug("create: {}", universitaet);
        final var universitaetDb = repo.save(universitaet);
        log.debug("create: {}", universitaet);
        return universitaetDb;
    }
}
