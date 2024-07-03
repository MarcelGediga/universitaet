package com.acme.universitaet.controller;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

/**
 * ValueObject für das Neuanlegen und Ändern einer neuen Universitaet.
 * Beim Lesen wird die Klasse UniversitaetModel für die Ausgabe
 * verwendet.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 * @param name Gültiger Name einer Universitaet, d.h. mit einem geeigneten Muster.
 * @param email Email einer Universitaet.
 * @param gruendungsdatum Das Gruendungsdatum einer Universitaet.
 * @param homepage Die Homepage einer Universitaet.
 * @param adresse Die Adresse einer Universitaet.
 * @param fakultaeten Die Fakultaet einer Universitaet.
 * @param username Benutzername
 * @param password Passwort
 */
@Builder
@SuppressWarnings("RecordComponentNumber")
record UniversitaetDTO(

    @NotNull(message = "Der Namen fehlt")
    @Pattern(regexp = NAME_PATTERN)
    String name,

    @Email
    @NotNull(message = "Die Email fehlt")
    @Size(max = EMAIL_MAX_LENGTH)
    String email,

    @Past
    @NotNull
    LocalDate gruendungsdatum,

    @Column(length = HOMEPAGE_MAX_LAENGE)
    URL homepage,

    @Valid
    // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single#_requesting_groups
    @NotNull(groups = OnCreate.class)
    AdresseDTO adresse,

    List<FakultaetDTO> fakultaeten,

    String username,

    String password
) {
    /**
     * Marker-Interface f&uuml;r Jakarta Validation: zus&auml;tzliche Validierung beim Neuanlegen.
     */
    interface OnCreate { }

    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    private static final int EMAIL_MAX_LENGTH = 40;
    private static final int HOMEPAGE_MAX_LAENGE = 40;
}
