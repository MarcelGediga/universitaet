package com.acme.universitaet.repository;

/**
 * Entity-Klasse für den REST-Client.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 * @param name Name
 * @param email Emailadresse
 */
public record Dozent(String name, String email) {
}
