package com.nttdata.knot.almapi.Models.GitflowPackage;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Release {

    public String tagName;
    public String nameTitle;
    public String versionPre;
    public String versionPro;
    public Status status;
    public List<Action> actions;

    public Release() {
    }

    public Release(String tagName, String nameTitle, String versionPre, String versionpro, Status status, List<Action> actions) {
        this.tagName = tagName;
        this.nameTitle = nameTitle;
        this.versionPre = versionPre;
        this.versionPro = versionpro;
        this.status = status;
        this.actions = actions;
    }
    
}