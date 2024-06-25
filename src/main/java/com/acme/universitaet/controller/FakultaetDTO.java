package com.acme.universitaet.controller;

import jakarta.validation.constraints.NotNull;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Universitaet.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 * @param name Name der Universitaet.
 * @param ansprechpartner Ansprechpartner der Universitaet.
 * @param dekan Ddekan der Universitaet.
 */
record FakultaetDTO(

    @NotNull(message = "Der Name fehlt")
    String name,

    @NotNull(message = "Die Fakultaet muss einen Ansprechpartner haben")
    String ansprechpartner,

    @NotNull(message = "Die Fakultaet muss einen Dekan haben")
    String dekan
) {
}
