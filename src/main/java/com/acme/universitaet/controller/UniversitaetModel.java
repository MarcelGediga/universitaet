package com.acme.universitaet.controller;

import com.acme.universitaet.entity.Adresse;
import com.acme.universitaet.entity.Fakultaet;
import com.acme.universitaet.entity.Universitaet;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 * <img src="../../../../../asciidoc/UniversitaetModel.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
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
    private final UUID dozentId;
    private final String dozentName;
    private final String dozentEmail;
    private final List<Fakultaet> fakultaeten;

    UniversitaetModel(final Universitaet universitaet) {
        name = universitaet.getName();
        email = universitaet.getEmail();
        gruendungsdatum = universitaet.getGruendungsdatum();
        homepage = universitaet.getHomepage();
        adresse = universitaet.getAdresse();
        dozentId = universitaet.getDozentId();
        dozentName = universitaet.getDozentName();
        dozentEmail = universitaet.getDozentEmail();
        fakultaeten = universitaet.getFakultaeten();
    }
}
