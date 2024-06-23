package com.acme.universitaet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.*;

/**
 * Repräsentiert eine Adresse.
 */
@Entity
@Table(name = "adresse")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@SuppressWarnings({"JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup", "MissingSummary"})
public class Adresse {
    @Id
    @GeneratedValue
    // Oracle: https://in.relation.to/2022/05/12/orm-uuid-mapping
    // @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    private UUID id;

    /**
     * Die Postleitzahl für die Adresse.
     * @param plz Die Postleitzahl als String
     * @return Die Postleitzahl als String
     */
    private String plz;

    /**
     * Der Ort für die Adresse.
     * @param ort Der Ort als String
     * @return Der Ort als String
     */
    private String ort;
}
