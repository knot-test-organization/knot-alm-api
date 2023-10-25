package com.nttdata.knot.almapi.Models.PipelineStatusPackage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineRequestStatus {
    
    private String componentName;
    private String pipelineName;
    private String pipelineExecutionName;
    private String stepName;
    private String startTime;
    private String endTime;

    public PipelineRequestStatus() {

    }

    public PipelineRequestStatus(String componentName, String pipelineName, String pipelineExecutionName,
            String stepName, String startTime, String endTime) {
        this.componentName = componentName;
        this.pipelineName = pipelineName;
        this.pipelineExecutionName = pipelineExecutionName;
        this.stepName = stepName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}