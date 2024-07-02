package com.acme.universitaet.dev;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;
import static org.springframework.context.annotation.Bean.Bootstrap.BACKGROUND;

/**
 * Protokoll-Ausgabe, wenn Kubernetes erkannt wird.
 *
 * @author <a href="mailto:Marcel.Gediga@h-ka.de">Marcel Gediga</a>
 */
interface K8s {
    /**
     * Protokoll-Ausgabe, wenn Kubernetes erkannt wird.
     *
     * @return Listener zur Ausgabe, ob Kubernetes erkannt wird.
     */
    @Bean(bootstrap = BACKGROUND)
    @ConditionalOnCloudPlatform(KUBERNETES)
    default ApplicationListener<ApplicationReadyEvent> detectK8s() {
        return _ -> LoggerFactory.getLogger(K8s.class).debug("Plattform \"Kubernetes\"");
    }
}
