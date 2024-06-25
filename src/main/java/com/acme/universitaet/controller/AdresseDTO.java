package com.acme.universitaet.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Universitaet.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 * @param plz Postleitzahl
 * @param ort Ort
 */
@Builder
record AdresseDTO(
    @NotNull
    @Pattern(regexp = PLZ_PATTERN)
    String plz,

    @NotBlank
    String ort
) {
    /**
     * Konstante für den regulären Ausdruck einer Postleitzahl als 5-stellige Zahl mit führender Null.
     */
    public static final String PLZ_PATTERN = "^\\d{5}$";
}
