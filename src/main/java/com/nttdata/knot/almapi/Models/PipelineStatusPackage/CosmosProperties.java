package com.nttdata.knot.almapi.Models.PipelineStatusPackage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "azure.cosmos")
@Getter @Setter
public class CosmosProperties {
    private String uri;

    private String key;

    private String secondaryKey;

    private String database;

    private boolean queryMetricsEnabled;

    private boolean responseDiagnosticsEnabled;
}
