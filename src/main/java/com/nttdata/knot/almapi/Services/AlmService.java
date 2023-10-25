package com.nttdata.knot.almapi.Services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.nttdata.knot.almapi.Interfaces.IAlmService;
import com.nttdata.knot.almapi.Interfaces.IGithubService;
import com.nttdata.knot.almapi.Interfaces.ITektonPipelinesRepository;
import com.nttdata.knot.almapi.Models.ALM.ALM;
import com.nttdata.knot.almapi.Models.ComponentPackage.Component;

import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.CreateGithubBranchRequest;

import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.GithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.MergeGithubBranchRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.Committer;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.getGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.CreateGithubPullRequest;

import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.MergeGithubPullRequest;

import com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases.CreateGithubRelease;

import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineRequestStatus;
import com.nttdata.knot.almapi.Models.PipelineStatusPackage.PipelineResponseStatus;
import com.nttdata.knot.almapi.Models.GitflowPackage.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlmService implements IAlmService {

        private String repoName = "knot-onboarding-resources";
        private static final Logger logger = LoggerFactory.getLogger(AlmService.class);

        private IGithubService githubService;

        @Autowired
        ITektonPipelinesRepository productRepository;

        @Autowired
        private ApplicationContext applicationContext;

        public AlmService(IGithubService githubService) {
                this.githubService = githubService;
        }

        @Override
        public Mono<ALM> createAlmAsync(String org, String area, String product, Component component)
                        throws JsonProcessingException {

                String technology;
                if (component.getServerless().getCreateFunctionApp()) {
                        technology = "azfunc";
                } else {
                        technology = component.getTechnology();
                }

                // Populate the alm object
                ALM alm = new ALM(component.getId(),
                                component.getOrganizationName(),
                                technology,
                                component.getDescription(),
                                component.getId(),
                                component.getTechnology(),
                                component.getSonarqubeScan(),
                                component.getTechnology(),
                                component.getAutomationTool(),
                                component.getPipelineTemplate(),
                                component.getGitBranching(),
                                component.getContainerRegistry(),
                                component.getArtifactRegistry(),
                                component.getProductDetails().getId(),
                                component.getProductDetails().getOrganization(),
                                component.getProductDetails().getArea());

                // prepare the verticals values commit
                var values_alm = prepareValueForCommit(component, alm);

                // push the values of each vertical in knot-onboarding-resources
                this.githubService.createGithubFileAsync(values_alm, repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + component.getId()
                                                + "/alm/values.yaml")
                                .block();

                return Mono.just(alm);
        }

        @Override
        public Mono<DeleteGithubFileRequest> deleteAlmAsync(String org, String area, String product,
                        String deletedComponentName) {

                // get the file to delete
                var valuesFile = this.githubService.getGithubFileAsync(repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + deletedComponentName
                                                + "/alm/values.yaml")
                                .block();

                // set the commit
                Committer committer = new Committer();
                committer.setEmail("41898282+github-actions[bot]@PipelineRequestStatuss.noreply.github.com");
                committer.setName("github-actions[bot]");

                DeleteGithubFileRequest deleteGithubFileRequest = new DeleteGithubFileRequest();
                deleteGithubFileRequest.setMessage(
                                "Removing ALM vertical into a Component, with name " + deletedComponentName);
                deleteGithubFileRequest.setCommitter(committer);
                deleteGithubFileRequest.setSha(valuesFile.getSha());

                // delete the file
                this.githubService.deleteGithubFileAsync(deleteGithubFileRequest,
                                repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + deletedComponentName
                                                + "/alm/values.yaml")
                                .block();

                return Mono.just(deleteGithubFileRequest);
        }

        @Override
        public Mono<ALM> updateAlmAsync(String org, String area, String product, Component component)
                        throws JsonProcessingException {

                String technology;
                if (component.getServerless().getCreateFunctionApp()) {
                        technology = "azfunc";
                } else {
                        technology = component.getTechnology();
                }

                // Populate the alm object
                ALM alm = new ALM(component.getId(),
                                component.getOrganizationName(),
                                technology,
                                component.getDescription(),
                                component.getId(),
                                component.getTechnology(),
                                component.getSonarqubeScan(),
                                component.getTechnology(),
                                component.getAutomationTool(),
                                component.getPipelineTemplate(),
                                component.getGitBranching(),
                                component.getContainerRegistry(),
                                component.getArtifactRegistry(),
                                component.getProductDetails().getId(),
                                component.getProductDetails().getOrganization(),
                                component.getProductDetails().getArea());

                // get the values file to update
                var valuesFile = this.githubService.getGithubFileAsync(repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + component.getId()
                                                + "/alm/values.yaml")
                                .block();

                // prepare the verticals values commit and set its SHA
                var values_alm = prepareValueForCommit(component, alm);
                values_alm.setSha(valuesFile.getSha());

                // push the values to the repository
                this.githubService.createGithubFileAsync(values_alm, repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + component.getId()
                                                + "/alm/values.yaml")
                                .block();

                return Mono.just(alm);
        }

        // serialize content of values and prepare a commit
        private CreateGithubFileRequest prepareValueForCommit(Component component, Object vertical)
                        throws JsonProcessingException {
                YAMLFactory yamlFactory = new YAMLFactory();
                yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
                ObjectMapper objectMapper = new ObjectMapper(yamlFactory);

                String verticalInBase64String = Base64.getEncoder()
                                .encodeToString(objectMapper
                                                .writeValueAsString(vertical).getBytes(StandardCharsets.UTF_8));

                Committer committer = new Committer();
                committer.setEmail("41898282+github-actions[bot]@PipelineRequestStatuss.noreply.github.com");
                committer.setName("github-actions[bot]");

                CreateGithubFileRequest createGithubFileRequest = new CreateGithubFileRequest();
                createGithubFileRequest
                                .setMessage("Add new ALM vertical into a Component, with name " + component.getName());
                createGithubFileRequest.setCommitter(committer);
                createGithubFileRequest.setContent(verticalInBase64String);

                return createGithubFileRequest;

        }

        @Override
        public Mono<List<PipelineResponseStatus>> getPipelineStatusAsync(PipelineRequestStatus pipelineRequestStatus) {

                // productRepository.deleteAll();
                // Value value = new Value();
                // value.setEnd_time("2023-09-28T12:30:00Z");
                // value.setStart_time("2023-10-20 10:59:52");
                // value.setPipeline_id("testFromAPI");
                // value.setStatus("terminated");
                // value.setPipelineName("build-with-dedalowangular");
                // value.setExecutioName("build-with-dedalowangular-3qcv");
                // value.setStepName("core-info");
                // PipelineResponseStatus saveTest1 = new PipelineResponseStatus(
                // "build-with-dedalowangular-build-push-pod||dedalowangulartest|build-with-dedalowangular|build-with-dedalowangular-3qcv|core-info",
                // value);
                // productRepository.save(saveTest1);

                List<PipelineResponseStatus> findall = new ArrayList<>();
                productRepository.findAll().iterator().forEachRemaining(findall::add);
                logger.info("Esto es lo que hay dentro -> " + findall.toString());
                List<PipelineResponseStatus> results = new ArrayList<>();

                // results = productRepository.filterbyDate("2023-09-28T12:00:00Z");
                if (pipelineRequestStatus.getPipelineName() != null &&
                                pipelineRequestStatus.getPipelineExecutionName() != null &&
                                pipelineRequestStatus.getStepName() != null) {
                        PipelineResponseStatus target = new PipelineResponseStatus();
                        PipelineResponseStatus coreInfo = new PipelineResponseStatus();
                        target = productRepository
                                        .findKeyByKeyContains(pipelineRequestStatus.getComponentName() + "||"
                                                        + pipelineRequestStatus.getComponentName() + "|"
                                                        + pipelineRequestStatus.getPipelineName() + "|"
                                                        + pipelineRequestStatus.getPipelineExecutionName() + "|" +
                                                        pipelineRequestStatus.getStepName());

                        coreInfo = productRepository
                                        .findKeyByKeyContains(pipelineRequestStatus.getComponentName() + "||"
                                                        + pipelineRequestStatus.getComponentName() + "|"
                                                        + pipelineRequestStatus.getPipelineName() + "|"
                                                        + pipelineRequestStatus.getPipelineExecutionName()
                                                        + "|core-info");

                        results.add(coreInfo);
                        results.add(target);
                } else if (pipelineRequestStatus.getPipelineName() != null &&
                                pipelineRequestStatus.getPipelineExecutionName() != null &&
                                pipelineRequestStatus.getStepName() == null) {
                        results = productRepository
                                        .findByKeyStartingWith(pipelineRequestStatus.getComponentName() + "||"
                                                        + pipelineRequestStatus.getComponentName() + "|"
                                                        + pipelineRequestStatus.getPipelineName() + "|"
                                                        + pipelineRequestStatus.getPipelineExecutionName());
                } else if (pipelineRequestStatus.getPipelineName() != null &&
                                pipelineRequestStatus.getPipelineExecutionName() == null &&
                                pipelineRequestStatus.getStepName() == null) {
                        results = productRepository
                                        .findByKeyStartingWith(pipelineRequestStatus.getComponentName() + "||" +
                                                        pipelineRequestStatus.getComponentName() + "|"
                                                        + pipelineRequestStatus.getPipelineName());
                } else if (pipelineRequestStatus.getPipelineName() == null &&
                                pipelineRequestStatus.getPipelineExecutionName() == null &&
                                pipelineRequestStatus.getStepName() == null) {
                        results = productRepository
                                        .findByKeyStartingWith(pipelineRequestStatus.getComponentName() + "||" +
                                                        pipelineRequestStatus.getComponentName());
                }

                List<PipelineResponseStatus> finalResults = new ArrayList<>();
                if (pipelineRequestStatus.getStartTime() != null && pipelineRequestStatus.getEndTime() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime requestStartTime = LocalDateTime.parse(pipelineRequestStatus.getStartTime(),
                                        formatter);
                        LocalDateTime requestEndTime = LocalDateTime.parse(pipelineRequestStatus.getEndTime(),
                                        formatter);
                        // String executionName = pipelineRequestStatus.getPipelineExecutionName() !=
                        // null ? pipelineRequestStatus.getPipelineExecutionName(): "";
                        for (PipelineResponseStatus item : results) {
                                if (item.getValue().getEnd_time() != null) {
                                        String startTimeString = item.getValue().getStart_time();
                                        String endTimeString = item.getValue().getEnd_time();
                                        LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);
                                        LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);
                                        if (!startTime.isBefore(requestStartTime) && !endTime.isAfter(requestEndTime)) {
                                                finalResults.add(item);
                                        }
                                }

                                // if (pipelineRequestStatus.getPipelineExecutionName() == null
                                // && startTime.equals(requestStartTime)) {
                                // executionName = item.getValue().getExecutioName();
                                // }
                        }
                        // for (PipelineResponseStatus i : results) {
                        // if (i.getValue().getExecutioName().equals(executionName)) {
                        // finalResults.add(i);
                        // }
                        // }
                } else if (pipelineRequestStatus.getStartTime() != null && pipelineRequestStatus.getEndTime() == null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime requestStartTime = LocalDateTime.parse(pipelineRequestStatus.getStartTime(),
                                        formatter);
                        for (PipelineResponseStatus item : results) {
                                String startTimeString = item.getValue().getStart_time();
                                LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);

                                if (!startTime.isBefore(requestStartTime)) {
                                        finalResults.add(item);
                                }
                        }
                } else if (pipelineRequestStatus.getStartTime() == null && pipelineRequestStatus.getEndTime() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime requestEndTime = LocalDateTime.parse(pipelineRequestStatus.getEndTime(),
                                        formatter);
                        for (PipelineResponseStatus item : results) {
                                if (item.getValue().getEnd_time() != null) {
                                        String endTimeString = item.getValue().getEnd_time();
                                        LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);

                                        if (!endTime.isAfter(requestEndTime)) {
                                                finalResults.add(item);
                                        }
                                }

                        }
                } else if (pipelineRequestStatus.getStartTime() == null && pipelineRequestStatus.getEndTime() == null) {
                        finalResults = results;
                }

                logger.info("Esto es lo que hay dentro -> " + finalResults.toString());

                return Mono.just(finalResults);
        }

        @Override
        public Mono<List<Release>> listReleases(Component component) {
                // releases list
                List<Release> releasesList = new ArrayList<>();

                // releases tags from github list
                List<String> listOfAllReleases = this.githubService.getGithubReleasesAsync(component.getId()).block();
                // modified list of release with unique tags
                List<String> listUniqueReleases = removePrefix(listOfAllReleases);
                // list of pull request in the repository used by argoCD to deploy
                List<getGithubPullRequestResponse> listOnboardingPullRequest = this.githubService
                                .getListPullRequestsAsync("knot-onboarding-resources").block();
                // list of pull request in the code repository created by knot
                List<getGithubPullRequestResponse> listCodePullRequest = this.githubService
                                .getListPullRequestsAsync(component.getId()).block();

                String pullRequestBranch = null;

                // set release variables
                for (String uniqueReleaseTag : listUniqueReleases) {
                        // Release object
                        Release release = new Release();
                        // set release tagName
                        release.setTagName(uniqueReleaseTag);
                        // set release nameTitle
                        release.setNameTitle("release/" + uniqueReleaseTag);
                        // set release List of action
                        List<Action> actionsList = new ArrayList<>();

                        if (listOfAllReleases.contains(uniqueReleaseTag + "-RC")
                                        || listOfAllReleases.contains(uniqueReleaseTag + "-rc")) {
                                for (String releaseTag : listOfAllReleases) {
                                        if (releaseTag.contains(uniqueReleaseTag)) {
                                                release.setVersionPre(releaseTag);
                                                break;
                                        }
                                }
                        }
                        if (listOfAllReleases.contains(uniqueReleaseTag)) {
                                release.setVersionPro(uniqueReleaseTag);
                                // set release pre status
                                Status status = new Status();
                                status.setId("promotion-successful");
                                status.setName("The promotion is successfully deployed");
                                release.setStatus(status);
                        }

                        // set release approve pre status & action
                        if (release.getVersionPre() != null) {

                                final String pullRequestOnboardingTitle = "release/" + component.getId() + "/"
                                                + release.getVersionPre();
                                boolean containsRequestOnboarding = listOnboardingPullRequest.stream()
                                                .map(getGithubPullRequestResponse::getTitle)
                                                .anyMatch(title -> title.equals(pullRequestOnboardingTitle));

                                if (containsRequestOnboarding) {
                                        // set release pre status
                                        Status status = new Status();
                                        status.setId("pending-approve-pre");
                                        status.setName("Pending approval to promote to PRE");
                                        release.setStatus(status);
                                        // set release action approve_pre to enabled
                                        Action action = new Action();
                                        action.setEnabled(true);
                                        action.setId("approve_pre");
                                        action.setName("Approve PRE");
                                        // add action to release's list of actions
                                        actionsList.add(action);

                                } else {
                                        // set release action approve_pre to disabled
                                        Action action = new Action();
                                        action.setEnabled(false);
                                        action.setId("approve_pre");
                                        action.setName("Approve PRE");
                                        // add action to release's list of actions
                                        actionsList.add(action);
                                }

                                // set actions promote-pro
                                int index = release.getVersionPre().indexOf('-');

                                final String pullRequestCodeTitle = "release/" + component.getId() + "/"
                                                + release.getVersionPre().substring(0, index);

                                boolean containsRequestCode = listCodePullRequest.stream()
                                                .map(getGithubPullRequestResponse::getTitle)
                                                .anyMatch(title -> title.equals(pullRequestCodeTitle));

                                boolean matchFoundPre = component.getEnvironments().stream()
                                                .anyMatch(env -> env.isEnabled()
                                                                && env.getNameSpace().equals("pre")
                                                                && release.getVersionPre()
                                                                                .equals(env.getVersion()));

                                boolean matchFoundPro = component.getEnvironments().stream()
                                                .anyMatch(env -> env.isEnabled()
                                                                && env.getNameSpace().equals("pro")
                                                                && env.getVersion()
                                                                                .equals(release.getVersionPro()));

                                if (!containsRequestCode && matchFoundPre && !matchFoundPro) {

                                        // set release action promote_pro to enabled
                                        Action action = new Action();
                                        action.setEnabled(true);
                                        action.setId("promote_pro");
                                        action.setName("Promote PRO");
                                        // add action to release's list of actions
                                        actionsList.add(action);

                                        // set release promote_pro status
                                        Status status = new Status();
                                        status.setId("pending-promote-pro");
                                        status.setName("Pending Promotion to PRO");
                                        release.setStatus(status);
                                } else {
                                        // set release action promote_pro to disabled
                                        Action actionPromotePro = new Action();
                                        actionPromotePro.setEnabled(false);
                                        actionPromotePro.setId("promote_pro");
                                        actionPromotePro.setName("Promote PRO");
                                        // add action to release's list of actions
                                        actionsList.add(actionPromotePro);
                                }

                        } else {
                                // set release action approve_pre to disabled
                                Action action = new Action();
                                action.setEnabled(false);
                                action.setId("approve_pre");
                                action.setName("Approve PRE");
                                // add action to release's list of actions
                                actionsList.add(action);

                                // set release action promote_pro to disabled
                                Action actionPromotePro = new Action();
                                actionPromotePro.setEnabled(false);
                                actionPromotePro.setId("promote_pro");
                                actionPromotePro.setName("Promote PRO");
                                // add action to release's list of actions
                                actionsList.add(actionPromotePro);
                        }

                        // set release approve pro & promote pro status/action
                        // if (release.getVersionPro() != null) {
                        // set actions aprove-pro
                        final String pullRequestCodeTitle = "release/" + component.getId() + "/"
                                        + uniqueReleaseTag;
                        boolean containsRequestCode = listCodePullRequest.stream()
                                        .map(getGithubPullRequestResponse::getTitle)
                                        .anyMatch(title -> title.equals(pullRequestCodeTitle));

                        // set actions
                        if (containsRequestCode) {
                                // set release action approve_pro to enabled
                                Action action = new Action();
                                action.setEnabled(true);
                                action.setId("approve_pro");
                                action.setName("Approve PRO");
                                // add action to release's list of actions
                                actionsList.add(action);

                                // set release aprove_pro status
                                Status status = new Status();
                                status.setId("pending-approve-pro");
                                status.setName("Pending approval to promote to PRO");
                                release.setStatus(status);
                        } else {
                                // set release action approve_pro to disabled
                                Action actionAprovePro = new Action();
                                actionAprovePro.setEnabled(false);
                                actionAprovePro.setId("approve_pro");
                                actionAprovePro.setName("Approve PRO");
                                // add action to release's list of actions
                                actionsList.add(actionAprovePro);
                        }

                        // } else {
                        // // set release action approve_pro to disabled
                        // Action actionAprovePro = new Action();
                        // actionAprovePro.setEnabled(false);
                        // actionAprovePro.setId("approve_pro");
                        // actionAprovePro.setName("Approve PRO");
                        // // add action to release's list of actions
                        // actionsList.add(actionAprovePro);
                        // }

                        // populate the release list of actions
                        release.setActions(actionsList);

                        // populate the list of releases
                        releasesList.add(release);
                }

                return Mono.just(releasesList);
        }

        public static List<String> removePrefix(List<String> tags) {
                List<String> uniqueTags = new ArrayList<>();

                for (String tag : tags) {
                        String withPrefix = "";
                        if (tag.endsWith("-rc") || tag.endsWith("-RC")) {
                                withPrefix = tag.substring(0, tag.length() - 3);
                                if (!tags.contains(withPrefix)) {
                                        uniqueTags.add(withPrefix);
                                }
                        } else {

                                if (!tag.endsWith(tag + "-rc") && !tag.endsWith(tag + "-RC")
                                                && !uniqueTags.contains(tag)) {
                                        uniqueTags.add(tag);
                                }
                        }
                }

                return uniqueTags;
        }

        @Override
        public Mono<Release> createRelease(ComponentRelease componentRelease) {
                String repoName = componentRelease.getComponent().getId();

                // List branch
                List<GithubBranchResponse> listBranchResponses = this.githubService.getListBranches(repoName).block();

                // Create Branch if dont exists
                Boolean branchExist = false;
                String shaDevelop = "";
                for (GithubBranchResponse branch : listBranchResponses) {
                        if (branch.getName().equals("release")) {
                                branchExist = true;
                        }

                        if (branch.getName().equals("develop")) {
                                shaDevelop = branch.getCommit().getSha();
                        }
                }

                if (branchExist) {
                        MergeGithubBranchRequest mergeGithubBranchRequest = new MergeGithubBranchRequest(
                                        "release",
                                        "develop",
                                        "Merge branch develop to release");
                        this.githubService.mergeBranch(mergeGithubBranchRequest, repoName).block();
                } else {
                        CreateGithubBranchRequest createGithubBranchRequest = new CreateGithubBranchRequest(
                                        "refs/heads/release",
                                        shaDevelop);
                        this.githubService.createBranch(createGithubBranchRequest, repoName).block();
                }
                ;

                // Create a release
                CreateGithubRelease createGithubRelease = new CreateGithubRelease(
                                componentRelease.getRelease().getTagName(),
                                "release",
                                componentRelease.getRelease().getTagName());

                this.githubService.createRelease(createGithubRelease, repoName).block();

                return Mono.just(componentRelease.getRelease());
        }

        @Override
        public Mono<Release> promoteRelease(ComponentRelease componentRelease) {
                String repoName = componentRelease.getComponent().getId();

                CreateGithubPullRequest createGithubPullRequest = new CreateGithubPullRequest(
                                "release/" + componentRelease.getComponent().getId() + "/"
                                                + componentRelease.getRelease().getVersionPro(),
                                "release",
                                "master");

                this.githubService.createPullRequest(createGithubPullRequest, repoName).block();

                return Mono.just(componentRelease.getRelease());
        }

        @Override
        public Mono<Release> approvePreRelease(ComponentRelease componentRelease) {
                String repoName = "knot-onboarding-resources";
                List<getGithubPullRequestResponse> listOnboardingPullRequest = this.githubService
                                .getListPullRequestsAsync(repoName).block();

                String releaseRef = "release/" + componentRelease.getRelease().getVersionPre();

                String numberPullRequest = null;

                for (getGithubPullRequestResponse pullRequest : listOnboardingPullRequest) {
                        if (pullRequest.getHead().getRef().equals(releaseRef)) {
                                numberPullRequest = pullRequest.getNumber();
                        }
                }

                MergeGithubPullRequest mergeGithubPullRequest = new MergeGithubPullRequest(
                                "Approve pull request",
                                "Approve pull request");

                this.githubService.approvePullRequest(
                                mergeGithubPullRequest,
                                repoName,
                                numberPullRequest).block();

                return Mono.just(componentRelease.getRelease());
        }

        @Override
        public Mono<Release> approveProRelease(ComponentRelease componentRelease) {
                String repoName = componentRelease.getComponent().getId();
                List<getGithubPullRequestResponse> listOnboardingPullRequest = this.githubService
                                .getListPullRequestsAsync(repoName).block();

                String releaseRef = "release/" + componentRelease.getComponent().getId() + "/"
                                + componentRelease.getRelease().getVersionPro();

                String numberPullRequest = null;

                for (getGithubPullRequestResponse pullRequest : listOnboardingPullRequest) {
                        if (pullRequest.getTitle().equals(releaseRef)) {
                                numberPullRequest = pullRequest.getNumber();
                        }
                }

                MergeGithubPullRequest mergeGithubPullRequest = new MergeGithubPullRequest(
                                "Approve pull request " + componentRelease.getRelease().getVersionPro() + " to the "
                                                + componentRelease.getComponent().getName() + " component",
                                "Approve pull request " + componentRelease.getRelease().getVersionPro() + " to the "
                                                + componentRelease.getComponent().getName() + " component");

                this.githubService.approvePullRequest(
                                mergeGithubPullRequest,
                                repoName,
                                numberPullRequest).block();

                CreateGithubRelease createGithubRelease = new CreateGithubRelease(
                                componentRelease.getRelease().getVersionPro(),
                                "master",
                                componentRelease.getRelease().getVersionPro());

                this.githubService.createRelease(createGithubRelease, repoName).block();

                return Mono.just(componentRelease.getRelease());
        }

        @Override
        public Mono<List<String>> listPipelineExecutionsAsync(String pipelineName) {
                List<PipelineResponseStatus> findall = new ArrayList<>();
                productRepository.findAll().iterator().forEachRemaining(findall::add);
                List<PipelineResponseStatus> results = new ArrayList<>();
                results = productRepository.findKeysByKeyContains(pipelineName);
                List<String> listExecutionNames = new ArrayList<>();
                for (PipelineResponseStatus item : results) {
                        String key = item.getKey();
                        String[] keyParts = key.split("\\|");
                        String executioName = keyParts[4];
                        if (!listExecutionNames.contains(executioName)) {
                                listExecutionNames.add(executioName);
                        }

                }

                return Mono.just(listExecutionNames);
        }

        @Override
        public Mono<List<String>> listPipelineStepsAsync(String executionName) {
                List<PipelineResponseStatus> findall = new ArrayList<>();
                productRepository.findAll().iterator().forEachRemaining(findall::add);
                List<PipelineResponseStatus> results = new ArrayList<>();
                results = productRepository.findKeysByKeyContains(executionName);
                List<String> listStepNames = new ArrayList<>();
                for (PipelineResponseStatus item : results) {
                        String key = item.getKey();
                        String[] keyParts = key.split("\\|");
                        String executioName = keyParts[5];
                        if (!listStepNames.contains(executioName) && !executioName.equals("core-info")) {
                                listStepNames.add(executioName);
                        }

                }
                return Mono.just(listStepNames);
        }

        @Override
        public Mono<List<PipelineResponseStatus>> getInitialTableAsync(String componentName) {
                List<PipelineResponseStatus> results = new ArrayList<>();
                List<PipelineResponseStatus> finalResults = new ArrayList<>();
                results = productRepository.findByKeyStartingWith(componentName);
                int count = 0;
                for (PipelineResponseStatus item : results) {
                        if (count <= 5) {
                                if (item.getValue().getStepName().equals("core-info")) {
                                        count++;
                                }
                                finalResults.add(item);
                        }
                }
                // logger.info(" Esto es lo mio -> " + finalResults.toString());
                return Mono.just(finalResults);
        }

}