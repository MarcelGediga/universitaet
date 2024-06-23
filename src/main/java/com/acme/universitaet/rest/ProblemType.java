package com.acme.universitaet.rest;

/**
 * Enum für ProblemDetail.type.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
enum ProblemType {
    /**
     * Constraints als Fehlerursache.
     */
    CONSTRAINTS("constraints"),

    /**
     * Fehler, wenn z.B. Emailadresse bereits existiert.
     */
    UNPROCESSABLE("unprocessable"),

    /**
     * Fehler beim Header `If-Match`.
     */
    PRECONDITION("precondition"),

    /**
     * Fehler bei z.B. einer Patch-Operation.
     */
    BAD_REQUEST("badRequest");

    private final String value;

    ProblemType(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
