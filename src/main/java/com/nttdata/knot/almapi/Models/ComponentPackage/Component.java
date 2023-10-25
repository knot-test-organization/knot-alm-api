package com.nttdata.knot.almapi.Models.ComponentPackage;

import java.util.List;

import com.nttdata.knot.almapi.Models.Codespace;
import com.nttdata.knot.almapi.Models.MicrosoftTeams;
import com.nttdata.knot.almapi.Models.UserPackage.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Component {
    private String id;
    private String name;
    private boolean AlmEnabled;
    private boolean CodeEnabled;
    private boolean CollabortionEnabled;
    private boolean DeployEnabled;
    private boolean IacEnabled;
    private boolean ha;
    private int minReplicas;
    private int maxReplicas;
    private List<Env> environments;
    private int targetAverageUtilization;
    private String targetMemoryAverageUtilization;
    private String description;
    private String type;
    private String technology;
    private String repository;
    private String automationTool;
    private String pipelineTemplate;
    private String gitBranching;
    private String containerRegistry;
    private String artifactRegistry;
    private BBDD bbdd;
    private Serverless serverless;
    private boolean edge;
    private boolean dockerImage;
    private String organizationName;
    private List<User> users;
    private List<String> devcontainers;
    private String date;
    private Boolean sonarqubeScan;
    private List<Codespace> codespaces;
    private MicrosoftTeams microsoftTeams;
    private ProductDetails productDetails;

}
