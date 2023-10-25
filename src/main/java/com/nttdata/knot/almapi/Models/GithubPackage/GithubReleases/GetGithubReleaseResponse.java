package com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class GetGithubReleaseResponse {

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

    
}
