package com.acme.universitaet.dev;

import org.springframework.context.annotation.Profile;
import static com.acme.universitaet.dev.DevConfig.DEV;

/**
 * Konfigurationsklasse für die Anwendung bzw. den Microservice, falls das Profile _dev_ aktiviert ist.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
@Profile(DEV)
@SuppressWarnings({"ClassNamePrefixedWithPackageName", "HideUtilityClassConstructor"})
public class DevConfig implements Flyway, LogRequestHeaders, LogSignatureAlgorithms, K8s {
    /**
     * Konstante für das Spring-Profile "dev".
     */
    public static final String DEV = "dev";

    DevConfig() {
    }
}
