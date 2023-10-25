package com.nttdata.knot.almapi.Models.GitflowPackage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {

    public String id;
    public String name;

    public Status() {
    }

    public Status(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
