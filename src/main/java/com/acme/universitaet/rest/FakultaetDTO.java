package com.acme.universitaet.rest;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Universitaet.
 *
 * @param name Name der Universitaet.
 * @param ansprechpartner Ansprechpartner der Universitaet.
 * @param dekan Ddekan der Universitaet.
 */
record FakultaetDTO(
    String name,
    String ansprechpartner,
    String dekan
) {
}
