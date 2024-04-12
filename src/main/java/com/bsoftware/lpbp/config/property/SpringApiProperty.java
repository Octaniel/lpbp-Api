package com.bsoftware.lpbp.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("lpbp")
public class SpringApiProperty {
    private Seguranca seguranca;
    private String originPermitida;

    @Setter
    @Getter
    public static class Seguranca {
        private boolean enableHttps;

    }
}
