package com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGithubRelease {
    
    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("target_commitish")
    private String targetCommitish;

    @JsonProperty("name")
    private String name;

    public CreateGithubRelease() {
    }

    public CreateGithubRelease(String tagName, String targetCommitish, String name) {
        this.tagName = tagName;
        this.targetCommitish = targetCommitish;
        this.name = name;
    }

}
