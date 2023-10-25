package com.nttdata.knot.almapi.Interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.almapi.Models.ALM.ALM;
import com.nttdata.knot.almapi.Models.ComponentPackage.Component;
import com.nttdata.knot.almapi.Models.GitflowPackage.ComponentRelease;
import com.nttdata.knot.almapi.Models.GitflowPackage.Release;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineResponseStatus;

import reactor.core.publisher.Mono;

public interface IAlmService {

    Mono<ALM> createAlmAsync(String org, String area, String product, Component component)
            throws JsonProcessingException;

    Mono<DeleteGithubFileRequest> deleteAlmAsync(String org, String area, String product, String deletedComponentName);

    Mono<ALM> updateAlmAsync(String org, String area, String product, Component component)
            throws JsonProcessingException;

    Mono<List<PipelineResponseStatus>> getPipelineStatusAsync(PipelineRequestStatus pipelineRequestStatus);

    Mono<List<String>> listPipelineExecutionsAsync(String pipelineName);

    Mono<List<String>> listPipelineStepsAsync(String executionName);

    Mono<List<PipelineResponseStatus>> getInitialTableAsync(String componentName);

    Mono<List<Release>> listReleases(Component component);

    Mono<Release> createRelease(ComponentRelease componentRelease);

    Mono<Release> promoteRelease(ComponentRelease componentRelease);

    Mono<Release> approvePreRelease(ComponentRelease componentRelease);

    Mono<Release> approveProRelease(ComponentRelease componentRelease);

}
