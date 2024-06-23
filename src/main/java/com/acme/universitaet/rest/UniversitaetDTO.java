package com.acme.universitaet.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.net.URL;
import java.util.List;

/**
 * ValueObject für das Neuanlegen und Ändern einer neuen Universitaet.
 * Beim Lesen wird die Klasse UniversitaetModel für die Ausgabe
 * verwendet.
 *
 * @param name Gültiger Name einer Universitaet, d.h. mit einem geeigneten Muster.
 * @param email Email einer Universitaet.
 * @param gruendungsdatum Das Gruendungsdatum einer Universitaet.
 * @param homepage Die Homepage einer Universitaet.
 * @param adresse Die Adresse einer Universitaet.
 * @param fakultaeten Die Fakultaet einer Universitaet.
 */
@SuppressWarnings("RecordComponentNumber")
record UniversitaetDTO(

    @NotNull
    @Pattern(regexp = NAME_PATTERN)
    String name,

    @Email
    @NotNull
    String email,

    @Past
    String gruendungsdatum,

    URL homepage,

    AdresseDTO adresse,

    @Valid
    @NotNull(groups = OnCreate.class)
    List<FakultaetDTO> fakultaeten
) {
    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Marker-Interface f&uuml;r Jakarta Validation: zus&auml;tzliche Validierung beim Neuanlegen.
     */
    interface OnCreate { }
}
