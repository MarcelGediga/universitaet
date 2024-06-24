package com.acme.universitaet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Repräsentiert eine Fakultaet.
 */
@Entity
@Table(name = "fakultaet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@SuppressWarnings({"JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup", "MissingSummary"})
public class Fakultaet {

    @Id
    @GeneratedValue
    // Oracle: https://in.relation.to/2022/05/12/orm-uuid-mapping
    // @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    private UUID id;

    /**
     * Der Name der Fakultät.
     * @param name Der Name.
     * @return Der Name.
     */
    private String name;

    /**
     * Der Ansprechpartener der Fakultät.
     * @param ansprechpartener Der Ansprechpartener.
     * @return Der Ansprechpartener.
     */
    private String ansprechpartner;

    /**
     * Der Dekan der Fakultät.
     * @param dekan Der Dekan.
     * @return Der Dekan.
     */
    private String dekan;
}
