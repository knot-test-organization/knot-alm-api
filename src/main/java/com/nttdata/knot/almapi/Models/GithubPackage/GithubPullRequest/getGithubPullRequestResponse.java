package com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class getGithubPullRequestResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private String number;

    @JsonProperty("title")
    private String title;

    @JsonProperty("state")
    private String state;

    @JsonProperty("head")
    private Head head;
    
    @JsonProperty("created_at")
    private String createdAt;

}