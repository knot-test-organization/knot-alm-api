package com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateGithubPullRequest {
    
    @JsonProperty("title")
    private String title;

    @JsonProperty("head")
    private String head;

    @JsonProperty("base")
    private String base;

    public CreateGithubPullRequest() {
    }

    public CreateGithubPullRequest(String title, String head, String base) {
        this.title = title;
        this.head = head;
        this.base = base;
    }

}
