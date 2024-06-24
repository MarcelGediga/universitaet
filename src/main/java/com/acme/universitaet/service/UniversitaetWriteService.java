package com.acme.universitaet.service;

import com.acme.universitaet.entity.Universitaet;
import com.acme.universitaet.mail.Mailer;
import com.acme.universitaet.repository.UniversitaetRepository;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Anwendungslogik für Universitaeten auch mit Bean Validation.
 * <img src="../../../../../asciidoc/UniversitaetWriteService.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UniversitaetWriteService {
    private final UniversitaetRepository repo;
    // private final CustomUserDetailsService userService; // NOSONAR
    private final Mailer mailer;

    /**
     * Einen neuen Universitaeten anlegen.
     *
     * @param universitaet Das Objekt des neu anzulegenden Universitaeten.
     * @return Der neu angelegte Universitaeten mit generierter ID
     * @throws EmailExistsException Es gibt bereits einen Universitaeten mit der Emailadresse.
     */
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions
    @Transactional
    @SuppressWarnings("TrailingComment")
    public Universitaet create(final Universitaet universitaet) {
        log.debug("create: universitaet={}", universitaet);
        log.debug("create: adresse={}", universitaet.getAdresse());
        log.debug("create: fakultaeten={}", universitaet.getFakultaeten());

        if (repo.existsByEmail(universitaet.getEmail())) {
            throw new EmailExistsException(universitaet.getEmail());
        }

        // final var login = userService.save(user); // NOSONAR
        universitaet.setUsername("user");

        final var universitaetDB = repo.save(universitaet);

        log.trace("create: Thread-ID={}", Thread.currentThread().threadId());
        mailer.send(universitaetDB);

        log.debug("create: universitaetDB={}", universitaetDB);
        return universitaetDB;
    }

    /**
     * Einen vorhandenen Universitaeten aktualisieren.
     *
     * @param universitaet Das Objekt mit den neuen Daten (ohne ID)
     * @param id           ID des zu aktualisierenden Universitaeten
     * @param version Die erforderliche Version
     * @return Aktualisierter Universitaet mit erhöhter Versionsnummer
     * @throws NotFoundException        Kein Universitaet zur ID vorhanden.
     * @throws VersionOutdatedException Die Versionsnummer ist veraltet und nicht aktuell.
     * @throws EmailExistsException     Es gibt bereits einen Universitaeten mit der Emailadresse.
     */
    @Transactional
    public Universitaet update(final Universitaet universitaet, final UUID id,  final int version) {
        log.debug("update: universitaet={}", universitaet);
        log.debug("update: id={}, version={}", id, version);

        var universitaetDb = repo
            .findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.trace("update: version={}, universitaetDb={}", version, universitaetDb);
        if (version != universitaetDb.getVersion()) {
            throw new VersionOutdatedException(version);
        }

        final var email = universitaet.getEmail();
        // Ist die neue E-Mail bei einem *ANDEREN* Universitaeten vorhanden?
        if (!Objects.equals(email, universitaetDb.getEmail()) && repo.existsByEmail(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }
        log.trace("update: Kein Konflikt mit der Emailadresse");

        // Zu ueberschreibende Werte uebernehmen
        universitaetDb.set(universitaet);
        universitaetDb = repo.save(universitaetDb);

        log.debug("update: {}", universitaetDb);
        return universitaetDb;
    }
}
