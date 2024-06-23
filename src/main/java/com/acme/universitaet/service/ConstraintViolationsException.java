package com.acme.universitaet.service;

import com.acme.universitaet.entity.Universitaet;
import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import lombok.Getter;

/**
 * Exception, falls es mindestens ein verletztes Constraint gibt.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Getter
public class ConstraintViolationsException extends RuntimeException {
    /**
     * Die verletzten Constraints.
     */
    private final transient Collection<ConstraintViolation<Universitaet>> violations;

    ConstraintViolationsException(
        @SuppressWarnings("ParameterHidesMemberVariable")
        final Collection<ConstraintViolation<Universitaet>> violations
    ) {
        super("Constraints sind verletzt");
        this.violations = violations;
    }
}
