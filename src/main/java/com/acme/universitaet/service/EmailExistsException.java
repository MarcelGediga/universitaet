package com.acme.universitaet.service;

import lombok.Getter;

/**
 * Exception, falls die Emailadresse bereits existiert.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Getter
public class EmailExistsException extends RuntimeException {

    /**
     * Bereits vorhandene Emailadresse.
     */
    private final String email;

    EmailExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String email) {
        super(STR."Die Emailadresse \{email} existiert bereits");
        this.email = email;
    }
}
