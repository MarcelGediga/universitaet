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
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Entity
@Table(name = "fakultaet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Fakultaet {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Der Name der Fakultät.
     *
     */
    private String name;

    /**
     * Der Ansprechpartener der Fakultät.
     *
     */
    private String ansprechpartner;

    /**
     * Der Dekan der Fakultät.
     *
     */
    private String dekan;
}
