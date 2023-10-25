package com.nttdata.knot.almapi.Controllers;

import reactor.core.publisher.Mono;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.almapi.Interfaces.IAlmService;
import com.nttdata.knot.almapi.Interfaces.IGithubService;
import com.nttdata.knot.almapi.Models.ALM.ALM;
import com.nttdata.knot.almapi.Models.ComponentPackage.Component;
import com.nttdata.knot.almapi.Models.GitflowPackage.Release;
import com.nttdata.knot.almapi.Models.GitflowPackage.ComponentRelease;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.getGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineResponseStatus;

@RestController
@RequestMapping("/alm")
public class AlmController {

    private IAlmService AlmService;
    private IGithubService githubService;
    private static final Logger logger = LoggerFactory.getLogger(ALM.class);

    @Autowired
    public AlmController(IAlmService AlmService, IGithubService githubService) {
        this.AlmService = AlmService;
        this.githubService = githubService;

    }

    @PostMapping("/{org}/{area}/{product}")
    public ResponseEntity<Mono<ALM>> create(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody Component component) throws JsonProcessingException {
        var alm = AlmService.createAlmAsync(org, area, product, component);
        logger.info("The component {} is being created", component.getName());
        return ResponseEntity.ok(alm);
    }

    @DeleteMapping("/{org}/{area}/{product}/{name}")
    public ResponseEntity<Mono<DeleteGithubFileRequest>> delete(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @PathVariable String name) throws JsonProcessingException {

        var alm = AlmService.deleteAlmAsync(org, area, product, name);
        logger.info("The component {} is being deleted", name);

        return ResponseEntity.ok(alm);
    }

    @PutMapping("/{org}/{area}/{product}")
    public ResponseEntity<Mono<ALM>> update(@PathVariable String org, @PathVariable String area,
            @PathVariable String product, @RequestBody Component component) throws JsonProcessingException {

        var alm = AlmService.updateAlmAsync(org, area, product, component);
        logger.info("The component {} is being Updated", component.getName());

        return ResponseEntity.ok(alm);
    }
   
    @PostMapping("/getPipelineStatus")
    public ResponseEntity<Mono<List<PipelineResponseStatus>>> getPipelineStatus(@RequestBody PipelineRequestStatus pipelineRequestStatus) throws JsonProcessingException {
        var pipelineStatusResponse = AlmService.getPipelineStatusAsync(pipelineRequestStatus);
        return ResponseEntity.ok(pipelineStatusResponse);
    }

    @PostMapping("/createRelease")
    public ResponseEntity<Mono<Release>> createRelease(@RequestBody ComponentRelease componentRelease) {

        var createRelease = AlmService.createRelease(componentRelease);

        return ResponseEntity.ok(createRelease);
    }

    @PutMapping("/promoteRelease")
    public ResponseEntity<Mono<Release>> promoteRelease(@RequestBody ComponentRelease componentRelease) {

        var promoteRelease = AlmService.promoteRelease(componentRelease);

        return ResponseEntity.ok(promoteRelease);
    }

    @PostMapping("/listReleases")
    public ResponseEntity<Mono<List<Release>>> listReleases(@RequestBody Component component) {

        var listReleases = this.AlmService.listReleases(component);

        return ResponseEntity.ok(listReleases);
    }

    @PutMapping("/approvePreRelease")
    public ResponseEntity<Mono<Release>> approvePreRelease(@RequestBody ComponentRelease componentRelease) {

        var approvePreRelease = AlmService.approvePreRelease(componentRelease);

        return ResponseEntity.ok(approvePreRelease);
    }

    @PutMapping("/approveProRelease")
    public ResponseEntity<Mono<Release>> approveProRelease(@RequestBody ComponentRelease componentRelease) {

        var approveProRelease = AlmService.approveProRelease(componentRelease);

        return ResponseEntity.ok(approveProRelease);
    }

    // @GetMapping("/test")
    // public ResponseEntity<Mono<List<String>>> test(String repo) {
    //     var test = this.githubService.getGithubReleasesAsync(repo);
    //     return ResponseEntity.ok(test);
    // }

    // @GetMapping("/test2")
    // public ResponseEntity<Mono<List<String>>> test2(String repo) {
    //     var test = this.AlmService.test(repo);
    //     return ResponseEntity.ok(test);
    // }
    @GetMapping("/listPipelineExecutions/{pipelineName}")
    public ResponseEntity<Mono<List<String>>> listPipelineExecutions(@PathVariable String pipelineName) throws JsonProcessingException {
        var pipelineStatusResponse = AlmService.listPipelineExecutionsAsync(pipelineName);
        return ResponseEntity.ok(pipelineStatusResponse);
    }

    @GetMapping("/listPipelineSteps/{executionName}")
    public ResponseEntity<Mono<List<String>>> listPipelineSteps(@PathVariable String executionName) throws JsonProcessingException {
        var pipelineStatusResponse = AlmService.listPipelineStepsAsync(executionName);
        return ResponseEntity.ok(pipelineStatusResponse);
    }

    @GetMapping("/getInitialTable/{componentName}")
    public ResponseEntity<Mono<List<PipelineResponseStatus>>> getInitialTableAsync(@PathVariable String componentName) throws JsonProcessingException {
        var pipelineStatusResponse = AlmService.getInitialTableAsync(componentName);
        return ResponseEntity.ok(pipelineStatusResponse);
    }

}
