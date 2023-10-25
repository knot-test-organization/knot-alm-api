package com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateGithubBranchResponse {
    
    @JsonProperty("ref")
    private String ref;

    @JsonProperty("url")
    private String url;

    public CreateGithubBranchResponse() {
    }

    public CreateGithubBranchResponse(String ref, String url) {
        this.ref = ref;
        this.url = url;
    }

}
