package com.acme.universitaet.repository;

import com.acme.universitaet.entity.Universitaet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import static com.acme.universitaet.entity.Universitaet.ADRESSE_GRAPH;

/**
 * Repository für den DB-Zugriff bei Dozenten.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Repository
public interface UniversitaetRepository extends JpaRepository<Universitaet, UUID> {
    @EntityGraph(ADRESSE_GRAPH)
    @NonNull
    @Override
    List<Universitaet> findAll();

    @EntityGraph(ADRESSE_GRAPH)
    @NonNull
    @Override
    Optional<Universitaet> findById(@NonNull UUID id);

    /**
     * Universitaeten zu gegebener Dozent-ID aus der DB ermitteln.
     *
     * @param dozentId Dozent-ID für die Suche
     * @return Liste der gefundenen Universitaeten
     */
    @EntityGraph(ADRESSE_GRAPH)
    List<Universitaet> findByDozentId(UUID dozentId);
}
