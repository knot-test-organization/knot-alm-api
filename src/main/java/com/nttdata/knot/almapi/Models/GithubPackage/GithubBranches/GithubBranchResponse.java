package com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileResponse.Commit;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GithubBranchResponse {
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("commit")
    private Commit commit;

    public GithubBranchResponse() {
    }

    public GithubBranchResponse(String name, Commit commit) {
        this.name = name;
        this.commit = commit;
    }

}
