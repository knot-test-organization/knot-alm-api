package com.nttdata.knot.almapi.Models.PipelineStatusPackage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Value {
    private String end_time;
    private String pipeline_id;
    private String start_time;
    private String status;
    private String pipelineName;
    private String executioName;
    private String stepName;

    public Value() {
    }

    public Value(String end_time, String pipeline_id, String start_time, String status, String pipelineName,
            String executioName, String stepName) {
        this.end_time = end_time;
        this.pipeline_id = pipeline_id;
        this.start_time = start_time;
        this.status = status;
        this.pipelineName = pipelineName;
        this.executioName = executioName;
        this.stepName = stepName;
    }

    @Override
    public String toString() {
        return "end_time=" + end_time + ", pipeline_id=" + pipeline_id + ", start_time=" + start_time + ", status="
                + status + ", pipelineName=" + pipelineName + ", executioName=" + executioName + ", stepName=" + stepName;
    }
}
