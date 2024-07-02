package com.acme.universitaet.controller;

import com.acme.universitaet.entity.Adresse;
import com.acme.universitaet.entity.Universitaet;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * Mapper zwischen Entity-Klassen.
 * Siehe build\generated\sources\annotationProcessor\java\main\...\UniversitaetMapperImpl.java.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Mapper(nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
@AnnotateWith(ExcludeFromJacocoGeneratedReport.class)
interface UniversitaetMapper {
    /**
     * Ein DTO-Objekt von UniversitaetDTO in ein Objekt für Universitaet konvertieren.
     *
     * @param dto DTO-Objekt für UniversitaetDTO ohne ID
     * @return Konvertiertes Universitaet-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dozentName", ignore = true)
    @Mapping(target = "dozentEmail", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    Universitaet toUniversitaet(UniversitaetDTO dto);

    /**
     * Ein DTO-Objekt von AdresseDTO in ein Objekt für Adresse konvertieren.
     *
     * @param dto DTO-Objekt für AdresseDTO ohne universitaet
     * @return Konvertiertes Adresse-Objekt
     */
    @Mapping(target = "id", ignore = true)
    Adresse toAdresse(AdresseDTO dto);
}
