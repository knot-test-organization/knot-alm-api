package com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateGithubReleaseResponse {
    
    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("name")
    private String nameTitle;

    @JsonProperty("published_at")
    private String publishedAt;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("target_commitish")
    private String targetCommitish;

    public CreateGithubReleaseResponse() {
    }

    public CreateGithubReleaseResponse(String tagName, String nameTitle, String publishedAt, String htmlUrl,
            String targetCommitish) {
        this.tagName = tagName;
        this.nameTitle = nameTitle;
        this.publishedAt = publishedAt;
        this.htmlUrl = htmlUrl;
        this.targetCommitish = targetCommitish;
    }

}
