package com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateGithubPullRequestResponse {
    
    @JsonProperty("title")
    private String title;

    public CreateGithubPullRequestResponse() {
    }

    public CreateGithubPullRequestResponse(String title) {
        this.title = title;
    }

}
