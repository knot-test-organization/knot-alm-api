package com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sha",
        "merged",
        "message"
})

public class MergeGithubPullRequestResponse {

    @JsonProperty("sha")
    private String sha;
    @JsonProperty("merged")
    private Boolean merged;
    @JsonProperty("message")
    private String message;

    /**
     * No args constructor for use in serialization
     *
     */
    public MergeGithubPullRequestResponse() {
    }

    /**
     *
     * @param merged
     * @param message
     * @param sha
     */
    public MergeGithubPullRequestResponse(String sha, Boolean merged, String message) {
        super();
        this.sha = sha;
        this.merged = merged;
        this.message = message;
    }

    @JsonProperty("sha")
    public String getSha() {
        return sha;
    }

    @JsonProperty("sha")
    public void setSha(String sha) {
        this.sha = sha;
    }

    @JsonProperty("merged")
    public Boolean getMerged() {
        return merged;
    }

    @JsonProperty("merged")
    public void setMerged(Boolean merged) {
        this.merged = merged;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}