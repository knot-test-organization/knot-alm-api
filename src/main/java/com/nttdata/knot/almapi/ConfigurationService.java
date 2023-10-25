package com.nttdata.knot.almapi;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.spring.cloud.autoconfigure.implementation.cosmos.properties.AzureCosmosProperties;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import com.nttdata.knot.almapi.Interfaces.ITektonPipelinesRepository;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.CosmosProperties;

import reactor.netty.http.client.HttpClient;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableReactiveCosmosRepositories
@EnableCosmosRepositories
@EnableConfigurationProperties(CosmosProperties.class)
@PropertySource("classpath:application.properties")
public class ConfigurationService extends AbstractCosmosConfiguration {

    // Github variables
    @Value("${github.token}")
    private String githubToken = "";

    // @Value("${azure.cosmos.uri}")
    // private String uri;

    // @Value("${azure.cosmos.key}")
    // private String key;

    // @Value("${azure.cosmos.database}")
    // private String dbName;

    // @Value("${azure.cosmos.queryMetricsEnabled}")
    // private boolean queryMetricsEnabled;

    private CosmosProperties properties;
    private HttpClient httpClient;
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
    private AzureKeyCredential azureKeyCredential;

    @Autowired
    public ConfigurationService(HttpClient httpClient, CosmosProperties properties) throws SSLException {
        this.properties = properties;
        this.httpClient = httpClient;
    }

    // Github WebClient
    @Bean
    public WebClient githubWebClient() {

        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .defaultHeader(HttpHeaders.USER_AGENT, "HttpRequestsSample")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + this.githubToken)
                .build();
    }

    @Bean
    public CosmosClientBuilder cosmosBuildClient() {
        DirectConnectionConfig directConnectionConfig = DirectConnectionConfig.getDefaultConfig();

        // use this for gateway connection
        GatewayConnectionConfig gatewayConnectionConfig = GatewayConnectionConfig.getDefaultConfig();

        return new CosmosClientBuilder()
                .endpoint(properties.getUri())
                .key(properties.getKey())
                .directMode(directConnectionConfig);
    }

    @Bean
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation(this.properties))
                .enableQueryMetrics(properties.isQueryMetricsEnabled())
                .build();
    }

    @Bean
    public AzureCosmosProperties azureCosmosProperties() {
        AzureCosmosProperties properties = new AzureCosmosProperties();
        // Configure your Cosmos DB properties here
        properties.setEndpoint("https://dapr-cosmosdb-westeurope.documents.azure.com:443/");
        properties.setKey("D28vMes7wPCVc0Vf3bEItwRHETU9ZpLYPcbdPKfsRL1ud52Wg07X90tjWmcMRFacCkHmXOoSzq5jACDb07agPA==");
        properties.setDatabase("daprdb");
        return properties;
    }

    // @Bean
    // public CosmosClientBuilder getCosmosClientBuilder() {
    // this.azureKeyCredential = new AzureKeyCredential(key);
    // DirectConnectionConfig directConnectionConfig = new DirectConnectionConfig();
    // GatewayConnectionConfig gatewayConnectionConfig = new
    // GatewayConnectionConfig();
    // return new CosmosClientBuilder()
    // .endpoint(uri)
    // .credential(azureKeyCredential)
    // .directMode(directConnectionConfig, gatewayConnectionConfig);
    // }

    // @Override
    // public CosmosConfig cosmosConfig() {
    // return CosmosConfig.builder()
    // .enableQueryMetrics(queryMetricsEnabled)
    // .build();
    // }
    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        private CosmosProperties properties;

        ResponseDiagnosticsProcessorImplementation(CosmosProperties properties) {
            this.properties = properties;
        }

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            if (this.properties.isResponseDiagnosticsEnabled()) {
                logger.info("Response Diagnostics {}", responseDiagnostics);
            }
        }
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }

}