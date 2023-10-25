package com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sha"
})

public class MergeGithubBranchResponse {

    @JsonProperty("sha")
    private String sha;

    /**
     * No args constructor for use in serialization
     *
     */
    public MergeGithubBranchResponse() {
    }

    /**
     *
     * @param sha
     */
    public MergeGithubBranchResponse(String sha) {
        super();
        this.sha = sha;
    }

    @JsonProperty("sha")
    public String getSha() {
        return sha;
    }

    @JsonProperty("sha")
    public void setSha(String sha) {
        this.sha = sha;
    }

}