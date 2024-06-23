package com.acme.universitaet.repository;

import com.acme.universitaet.entity.Adresse;
import com.acme.universitaet.entity.Fakultaet;
import com.acme.universitaet.entity.Universitaet;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Emulation der Datenbasis für persistente Universitaetn.
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "MagicNumber", "RedundantSuppression", "java:S1192"})
final class DB {
    /**
     * Liste der Universitaetn zur Emulation der DB.
     */
    @SuppressWarnings("StaticCollection")
    static final List<Universitaet> UNIVERSITAETEN;

    static {
        UNIVERSITAETEN = Stream.of(
            Universitaet.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .name("Universität A")
                .email("uni_a@example.com")
                .gruendungsdatum(LocalDate.parse("1950-01-01"))
                .homepage(buildURL("https://www.uni-a.example.com"))
                .adresse(Adresse.builder().ort("Stadt A").plz("12345").build())
                .fakultaeten(List.of(
                    Fakultaet.builder().name("Informatik").ansprechpartner("Prof. Schmidt")
                        .dekan("Prof. Müller").build(),
                    Fakultaet.builder().name("Wirtschaftswissenschaften").ansprechpartner("Prof. Maier")
                        .dekan("Prof. Schmitz").build()
                ))
                .build(),
            Universitaet.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("Universität B")
                .email("uni_b@example.com")
                .gruendungsdatum(LocalDate.parse("1960-01-01"))
                .homepage(buildURL("https://www.uni-b.example.com"))
                .adresse(Adresse.builder().ort("Stadt B").plz("23456").build())
                .fakultaeten(List.of(
                    Fakultaet.builder().name("Naturwissenschaften").ansprechpartner("Prof. Müller")
                        .dekan("Prof. Schmidt").build(),
                    Fakultaet.builder().name("Geisteswissenschaften").ansprechpartner("Prof. Schmitz")
                        .dekan("Prof. Maier").build(),
                    Fakultaet.builder().name("Medizin").ansprechpartner("Prof. Schmitz").dekan("Prof. Müller").build()
                ))
                .build(),
            Universitaet.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .name("Universität C")
                .email("uni_c@example.com")
                .gruendungsdatum(LocalDate.parse("1970-01-01"))
                .homepage(buildURL("https://www.uni-c.example.com"))
                .adresse(Adresse.builder().ort("Stadt C").plz("34567").build())
                .fakultaeten(List.of(
                    Fakultaet.builder().name("Rechtswissenschaften").ansprechpartner("Prof. Schmitz")
                        .dekan("Prof. Maier").build(),
                    Fakultaet.builder().name("Design").ansprechpartner("Prof. Müller").dekan("Prof. Schmitz").build()
                ))
                .build()
        ).collect(Collectors.toList());
    }

    private DB() {
    }

    private static URL buildURL(final String url) {
        try {
            return URI.create(url).toURL();
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
