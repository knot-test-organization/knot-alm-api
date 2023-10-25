package com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Head {

    @JsonProperty("ref")
    private String ref;

    @JsonProperty("sha")
    private String SHA;

}
