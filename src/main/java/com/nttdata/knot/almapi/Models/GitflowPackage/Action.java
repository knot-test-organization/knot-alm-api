package com.nttdata.knot.almapi.Models.GitflowPackage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Action {

    public String id;
    public String name;
    public boolean enabled;

    public Action() {
    }
    
    public Action(String id, String name, boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

}
