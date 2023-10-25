package com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateGithubBranchRequest {
    
    @JsonProperty("ref")
    private String ref;

    @JsonProperty("sha")
    private String sha;

    public CreateGithubBranchRequest() {
    }

    public CreateGithubBranchRequest(String ref, String sha) {
        this.ref = ref;
        this.sha = sha;
    }

}
