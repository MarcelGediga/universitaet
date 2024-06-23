package com.acme.universitaet.repository;


import com.acme.universitaet.entity.Adresse_;
import com.acme.universitaet.entity.Universitaet;
import com.acme.universitaet.entity.Universitaet_;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Singleton-Klasse, um Specifications für Queries in Spring Data JPA zu bauen.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
// TODO https://github.com/checkstyle/checkstyle/issues/14444
@Component
@Slf4j
@SuppressWarnings({"LambdaParameterName", "IllegalIdentifierName"})
public class SpecificationBuilder {
    /**
     * Specification für eine Query mit Spring Data bauen.
     *
     * @param queryParams als MultiValueMap
     * @return Specification für eine Query mit Spring Data
     */
    public Optional<Specification<Universitaet>> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);

        if (queryParams.isEmpty()) {
            // keine Suchkriterien
            return Optional.empty();
        }

        final var specs = queryParams
            .entrySet()
            .stream()
            .map(this::toSpecification)
            .toList();

        if (specs.isEmpty() || specs.contains(null)) {
            return Optional.empty();
        }

        return Optional.of(Specification.allOf(specs));
    }

    @SuppressWarnings("CyclomaticComplexity")
    private Specification<Universitaet> toSpecification(final Map.Entry<String, ? extends List<String>> entry) {
        log.trace("toSpec: entry={}", entry);
        final var key = entry.getKey();
        final var values = entry.getValue();

        if (values == null || values.size() != 1) {
            return null;
        }

        final var value = values.getFirst();
        return switch (key) {
            case "name" -> name(value);
            case "email" ->  email(value);
            case "plz" -> plz(value);
            case "ort" -> ort(value);
            default -> null;
        };
    }

    private Specification<Universitaet> name(final String teil) {
        // root ist jakarta.persistence.criteria.Root<Universitaet>
        // query ist jakarta.persistence.criteria.CriteriaQuery<Universitaet>
        // builder ist jakarta.persistence.criteria.CriteriaBuilder
        // https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/meta-model.html
        return (root, _, builder) -> builder.like(
            builder.lower(root.get(Universitaet_.name)),
            builder.lower(builder.literal("%" + teil + '%'))
        );
    }

    private Specification<Universitaet> email(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get(Universitaet_.email)),
            builder.lower(builder.literal("%" + teil + '%'))
        );
    }

    private Specification<Universitaet> plz(final String prefix) {
        return (root, _, builder) -> builder.like(root.get(Universitaet_.adresse).get(Adresse_.plz), prefix + '%');
    }

    private Specification<Universitaet> ort(final String prefix) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get(Universitaet_.adresse).get(Adresse_.ort)),
            builder.lower(builder.literal(prefix + '%'))
        );
    }
}
