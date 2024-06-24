package com.acme.universitaet.rest;

import com.acme.universitaet.entity.Adresse;
import com.acme.universitaet.entity.Universitaet;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URL;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;



/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 */
@JsonPropertyOrder({
    "name", "email", "gruendungsdatum", "homepage",
    "adresse", "faktultaeten"
})
@Relation(collectionRelation = "universitaeten", itemRelation = "universitaet")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@ToString(callSuper = true)
class UniversitaetModel extends RepresentationModel<UniversitaetModel> {
    private final String name;

    @EqualsAndHashCode.Include
    private final String email;

    private final LocalDate gruendungsdatum;
    private final URL homepage;
    private final Adresse adresse;

    UniversitaetModel(final Universitaet universitaet) {
        name = universitaet.getName();
        email = universitaet.getEmail();
        gruendungsdatum = universitaet.getGruendungsdatum();
        homepage = universitaet.getHomepage();
        adresse = universitaet.getAdresse();
    }
}
