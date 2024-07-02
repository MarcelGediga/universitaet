package com.acme.universitaet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import static com.acme.universitaet.entity.Universitaet.ADRESSE_FAKULTAETEN_GRAPH;
import static com.acme.universitaet.entity.Universitaet.ADRESSE_GRAPH;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Repräsentiert eine Universität.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Entity
@Table(name = "universitaet")
@NamedEntityGraph(name = ADRESSE_GRAPH, attributeNodes = @NamedAttributeNode("adresse"))
@NamedEntityGraph(name = ADRESSE_FAKULTAETEN_GRAPH, attributeNodes = {
    @NamedAttributeNode("adresse"), @NamedAttributeNode("fakultaeten")
})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@Builder
public class Universitaet {
    /**
     * NamedEntityGraph für das Attribut "adresse".
     */
    public static final String ADRESSE_GRAPH = "Universitaet.adresse";

    /**
     * NamedEntityGraph für die Attribute "adresse" und "fakulltaeten".
     */
    public static final String ADRESSE_FAKULTAETEN_GRAPH = "Universitaet.adresseFakultaeten";

    /**
     * Die ID der Universität.
     *
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Versionsnummer für optimistische Synchronisation.
     */
    @Version
    private int version;

    /**
     * Der Name der Universität.
     *
     */
    private String name;

    /**
     * Die Emailadresse der Universität.
     *
     */
    private String email;

    /**
     * Das Gründungsdatum der Universität.
     *
     */
    private LocalDate gruendungsdatum;

    /**
     * Die URL zur Homepage der Universität.
     *
     */
    private URL homepage;

    /**
     * Die Adresse der Universität.
     *
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @ToString.Exclude
    private Adresse adresse;

    /**
     * Die Fakultäten der Universität.
     *
     */
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "universitaet_id")
    @OrderColumn(name = "idx", nullable = false)
    @ToString.Exclude
    private List<Fakultaet> fakultaeten;

    private String username;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    @Column(name = "dozent_id")
    // @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.CHAR)
    private UUID dozentId;

    @Transient
    private String dozentName;

    @Transient
    private String dozentEmail;

    /**
     * Universitaetdaten überschreiben.
     *
     * @param universitaet Neue Universitaetdaten.
     */
    public void set(final Universitaet universitaet) {
        name = universitaet.name;
        email = universitaet.email;
        gruendungsdatum = universitaet.gruendungsdatum;
        homepage = universitaet.homepage;
    }
}
