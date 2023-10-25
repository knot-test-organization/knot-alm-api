package com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "base",
        "head",
        "commit_message"
})

public class MergeGithubBranchRequest {
    
    @JsonProperty("base")
    private String base;

    @JsonProperty("head")
    private String head;

    @JsonProperty("commit_message")
    private String commitMessage;

    /**
     * No args constructor for use in serialization
     *
     */
    public MergeGithubBranchRequest() {
    }

    /**
     *
     * @param base
     * @param head
     * @param commit_message
     */
    public MergeGithubBranchRequest(String base, String head, String commitMessage) {
        super();
        this.base = base;
        this.head = head;
        this.commitMessage = commitMessage;
    }

    @JsonProperty("base")
    public String getBase() {
        return base;
    }

    @JsonProperty("base")
    public void setBase(String base) {
        this.base = base;
    }

    @JsonProperty("head")
    public String getHead() {
        return head;
    }

    @JsonProperty("head")
    public void setHead(String head) {
        this.head = head;
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
