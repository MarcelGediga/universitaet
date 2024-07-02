package com.acme.universitaet.repository;

import com.acme.universitaet.entity.Universitaet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import static com.acme.universitaet.entity.Universitaet.ADRESSE_FAKULTAETEN_GRAPH;
import static com.acme.universitaet.entity.Universitaet.ADRESSE_GRAPH;


/**
 * Repository für den DB-Zugriff bei Universitaet.
 */
@Repository
public interface UniversitaetRepository extends JpaRepository<Universitaet,
    UUID>, JpaSpecificationExecutor<Universitaet> {
    @EntityGraph(ADRESSE_GRAPH)
    @NonNull
    @Override
    List<Universitaet> findAll();

    @EntityGraph(ADRESSE_GRAPH)
    @NonNull
    @Override
    List<Universitaet> findAll(@NonNull Specification<Universitaet> spec);

    @EntityGraph(ADRESSE_GRAPH)
    @NonNull
    @Override
    Optional<Universitaet> findById(@NonNull UUID id);

    /**
     * Universitaet einschließlich Fakultaeten anhand der ID suchen.
     *
     * @param id Universitaet ID
     * @return Gefundener Universitaet
     */
    @Query("""
        SELECT DISTINCT k
        FROM     #{#entityName} k
        WHERE    k.id = :id
        """)
    @EntityGraph(ADRESSE_FAKULTAETEN_GRAPH)
    @NonNull
    Optional<Universitaet> findByIdFetchFakultaeten(UUID id);

    /**
     * Universitaet zu gegebener Emailadresse aus der DB ermitteln.
     *
     * @param email Emailadresse für die Suche
     * @return Optional mit dem gefundenen Universitaet oder leeres Optional
     */
    @Query("""
        SELECT k
        FROM   #{#entityName} k
        WHERE  lower(k.email) LIKE concat(lower(:email), '%')
        """)
    @EntityGraph(ADRESSE_GRAPH)
    Optional<Universitaet> findByEmail(String email);

    /**
     * Abfrage, ob es eine Universitaet mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return true, falls es eine solchen Universitaet gibt, sonst false
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean existsByEmail(String email);

    /**
     * Universitaet anhand des Namens suchen.
     *
     * @param name Der (Teil-) Name der gesuchten Universitaet
     * @return Die gefundenen Universitaeten oder eine leere Collection
     */
    @Query("""
        SELECT   k
        FROM     #{#entityName} k
        WHERE    lower(k.name) LIKE concat('%', lower(:name), '%')
        ORDER BY k.name
        """)
    @EntityGraph(ADRESSE_GRAPH)
    List<Universitaet> findByName(CharSequence name);

    /**
     * Abfrage, welche Namen es zu einem Präfix gibt.
     *
     * @param prefix Name-Präfix.
     * @return Die passenden Namen oder eine leere Collection.
     */
    @Query("""
        SELECT DISTINCT k.name
        FROM     #{#entityName} k
        WHERE    lower(k.name) LIKE concat(lower(:prefix), '%')
        ORDER BY k.name
        """)
    List<String> findNamenByPrefix(String prefix);

    /**
     * Dozent zu gegebener Universitaets-ID aus der DB ermitteln.
     *
     * @param dozentId Universitaets-ID für die Suche
     * @return Liste der gefundene Dozenten
     */
    @EntityGraph(ADRESSE_GRAPH)
    List<Dozent> findByDozentId(UUID dozentId);
}
