package com.acme.universitaet.rest;

import com.acme.universitaet.entity.Adresse;
import com.acme.universitaet.entity.Fakultaet;
import com.acme.universitaet.entity.Universitaet;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * Mapper zwischen Entity-Klassen.
 * Siehe build\generated\sources\annotationProcessor\java\main\...\UniversitaetMapperImpl.java.
 */
@Mapper(nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
@AnnotateWith(com.acme.universitaet.rest.ExcludeFromJacocoGeneratedReport.class)
interface UniversitaetMapper {
    /**
     * Ein DTO-Objekt von UniversitaetDTO in ein Objekt für Universitaet konvertieren.
     *
     * @param dto DTO-Objekt für UniversitaetDTO ohne ID
     * @return Konvertiertes Universitaet-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    Universitaet toUniversitaet(UniversitaetDTO dto);

    /**
     * Ein DTO-Objekt von AdresseDTO in ein Objekt für Adresse konvertieren.
     *
     * @param dto DTO-Objekt für AdresseDTO ohne universitaet
     * @return Konvertiertes Adresse-Objekt
     */
    Adresse toAdresse(AdresseDTO dto);

    /**
     * Ein DTO-Objekt von FakultaetDTO in ein Objekt für Fakultaet konvertieren.
     *
     * @param dto DTO-Objekt für FakultaetDTO ohne universitaet
     * @return Konvertiertes Fakultaet-Objekt
     */
    Fakultaet toFakultaet(Fakultaet dto);
}
