package com.nttdata.knot.almapi.Models.PipelineStatusPackage;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.azure.spring.data.cosmos.core.mapping.Container;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Container(containerName = "tektonpipelines")
public class PipelineResponseStatus {
    @Id
    @PartitionKey
    private String key;
    private Value value;
    public PipelineResponseStatus() {

    }

    public PipelineResponseStatus(String key, Value value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "PipelineStatus [key=" + key + ", end_time=" + value.getEnd_time() + ", pipeline_id="
                + value.getPipeline_id() + ", start_time=" + value.getStart_time() + ", status=" + value.getStatus() + ", executionName=" + value.getExecutioName() + ", executionName=" + value.getStepName()
                + "]";
    }
}
