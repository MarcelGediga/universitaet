package com.acme.universitaet.service;

import lombok.Getter;

/**
 * Exception, falls die Versionsnummer nicht aktuell ist.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Getter
public class VersionOutdatedException extends RuntimeException {
    private final int version;

    VersionOutdatedException(final int version) {
        super("Die Versionsnummer " + version + " ist veraltet.");
        this.version = version;
    }
}
