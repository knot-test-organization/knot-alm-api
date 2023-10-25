package com.nttdata.knot.almapi.Models.ALM;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ALM {
    // private boolean enabled;
    private String projectType;
    private String name;
    private String repoName;
    private String repoDescription;
    private String repoType;
    private String organizationName;
    private Boolean sonarqubeScan;
    private String projectLanguage;
    private String automationTool;
    private String pipelineTemplate;
    private String gitBranching;
    private String containerRegistry;
    private String artifactRegistry;
    private String idProduct;
    private String organizationProduct;
    private String areaProduct;

    public ALM(String name, String organizationName, String projectType, String repoDescription, String repoName, String repoType, Boolean sonarqubeScan, String projectLanguage, String automationTool, String pipelineTemplate, String gitBranching, String containerRegistry, String artifactRegistry, String idProduct, String organizationProduct, String areaProduct) {
        this.name = name;
        this.organizationName = organizationName;
        this.projectType = projectType;
        this.repoDescription = repoDescription;
        this.repoName = repoName;
        this.repoType = repoType;
        this.sonarqubeScan = sonarqubeScan;
        this.projectLanguage = projectLanguage;
        this.automationTool = automationTool;
        this.pipelineTemplate = pipelineTemplate;
        this.gitBranching = gitBranching;
        this.containerRegistry = containerRegistry;
        this.artifactRegistry = artifactRegistry;
        this.idProduct = idProduct;
        this.organizationProduct = organizationProduct;
        this.areaProduct = areaProduct;
    }
}

    
