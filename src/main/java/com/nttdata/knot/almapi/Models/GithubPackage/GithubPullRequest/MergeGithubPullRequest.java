package com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "commit_title",
        "commit_message"
})
public class MergeGithubPullRequest {

    @JsonProperty("commit_title")
    private String commitTitle;
    @JsonProperty("commit_message")
    private String commitMessage;

    /**
     * No args constructor for use in serialization
     *
     */
    public MergeGithubPullRequest() {
    }

    /**
     *
     * @param commitMessage
     * @param commitTitle
     */
    public MergeGithubPullRequest(String commitTitle, String commitMessage) {
        super();
        this.commitTitle = commitTitle;
        this.commitMessage = commitMessage;
    }

    @JsonProperty("commit_title")
    public String getCommitTitle() {
        return commitTitle;
    }

    @JsonProperty("commit_title")
    public void setCommitTitle(String commitTitle) {
        this.commitTitle = commitTitle;
    }

    @JsonProperty("commit_message")
    public String getCommitMessage() {
        return commitMessage;
    }

    @JsonProperty("commit_message")
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

}