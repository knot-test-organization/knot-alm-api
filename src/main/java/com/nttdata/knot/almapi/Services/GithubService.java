package com.nttdata.knot.almapi.Services;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.nttdata.knot.almapi.Interfaces.IGithubService;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranch;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.CreateGithubBranchRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.CreateGithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.GithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.MergeGithubBranchRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.MergeGithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubCommits.GetGithubCommitsResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileResponse.CreateGithubFileResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileResponse.DeleteGithubFileResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileResponse.GetGithubFileResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.CreateGithubPullRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.CreateGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.MergeGithubPullRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.MergeGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.getGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases.CreateGithubRelease;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases.CreateGithubReleaseResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases.GetGithubReleaseResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubTag.CreateGithubTagRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubTag.CreateGithubTagResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubTag.GetGithubRefsTagResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubTag.GetGithubTagResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubTag.Person;
import com.nttdata.knot.almapi.Models.UserPackage.GetUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import reactor.core.publisher.Mono;

@Service
public class GithubService implements IGithubService {

    private final Logger logger = LoggerFactory.getLogger(GithubService.class);
    public WebClient webClient;

    // github variables
    @Value("${github.reposUrl}")
    private String githubUrl = "";

    // github variables
    @Value("${github.organization}")
    private String organization = "";

    // github variables
    @Value("${github.repositoryName}")
    private String repositoryName = "";

    public GetGithubFileResponse getGithubFileResponse;
    public CreateGithubFileResponse createGithubFileResponse;
    public CreateGithubTagRequest createGithubTagRequest;
    public DeleteGithubFileResponse deleteGithubFileResponse;
    public List<GetGithubCommitsResponse> getGithubCommitsResponse;
    public List<GetGithubTagResponse> getGithubTagResponse;
    public List<String> getDevcontainerListResponse;
    public List<GithubBranch> getGithubBranches;

    @Autowired
    public GithubService(@Qualifier("githubWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<GetGithubFileResponse> getGithubFileAsync(String repoName, String name) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/contents/" + name;
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GetGithubFileResponse.class)
                .doOnSuccess(response -> logger.info("Request sent"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<String> createGithubFileAsync(CreateGithubFileRequest createGithubFile, String repoName, String name) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/contents/" + name;
        return webClient.put()
                .uri(uri)
                .bodyValue(createGithubFile)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().toString())
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<String> deleteGithubFileAsync(DeleteGithubFileRequest deleteGithubFile, String repoName, String name) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/contents/" + name;
        return webClient.method(HttpMethod.DELETE)
                .uri(uri)
                .body(Mono.just(deleteGithubFile), DeleteGithubFileRequest.class)
                .exchangeToMono(response -> {
                    logger.info("Request sent");
                    return Mono.just(response.statusCode().toString());
                })
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<String> createGithubTagAsync(String repoName, String source, String target, String message) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/git/tags";
        String[] sourceContainer = { source };
        if (source.contains("-rc") || source.contains("-RC")) {
            String url2 = this.githubUrl + "/" + this.organization + "/" + repoName + "/git/refs/tags/" + source;
            webClient.get()
                    .uri(url2)
                    .retrieve()
                    .bodyToMono(GetGithubRefsTagResponse.class)
                    .doOnNext(response -> sourceContainer[0] = response.getObject().getSha())
                    .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()))
                    .subscribe();
            // reset the value of source
            source = sourceContainer[0];
        }

        CreateGithubTagRequest createGithubTagRequest = new CreateGithubTagRequest();
        createGithubTagRequest.setTag(target);
        createGithubTagRequest.setMessage(message);
        createGithubTagRequest.setObject(source);
        createGithubTagRequest.setType("commit");
        Person person = new Person();
        person.setName("github-actions[bot]");
        person.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
        person.setDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(ZonedDateTime.now()));
        createGithubTagRequest.setTagger(person);

        return webClient.post()
                .uri(uri)
                .bodyValue(createGithubTagRequest)
                .retrieve()
                .bodyToMono(CreateGithubTagResponse.class)
                .flatMap(response -> {
                    String url = this.githubUrl + "/" + this.organization + "/" + repoName + "/git/refs";
                    return webClient.post()
                            .uri(url)
                            .bodyValue("{\"ref\":\"refs/tags/" + target + "\", \"sha\":\"" + response.getSha() + "\"}")
                            .retrieve()
                            .toBodilessEntity()
                            .map(res -> {
                                logger.info("Request sent");
                                return res.getStatusCode().toString();
                            });
                })
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<GetGithubCommitsResponse>> getGithubCommitsAsync(String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/commits";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GetGithubCommitsResponse.class)
                .collectList()
                .doOnNext(response -> logger.info("Request sent"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<GetGithubTagResponse>> getGithubTagAsync(String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/tags";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GetGithubTagResponse.class)
                .collectList()
                .doOnNext(response -> logger.info("Request sent"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<String>> getGithubReleasesAsync(String repoName) {
        return this.webClient.get()
                .uri("https://api.github.com/repos/{owner}/{repo}/releases", this.organization, repoName)
                .retrieve()
                .bodyToFlux(GetGithubReleaseResponse.class)
                .map(GetGithubReleaseResponse::getTagName)
                .collectList();
    }
    @Override
    public Mono<List<getGithubPullRequestResponse>> getListPullRequestsAsync(String repoName) {
        return this.webClient.get()
                .uri("https://api.github.com/repos/{owner}/{repo}/pulls",  this.organization, repoName)
                .retrieve()
                .bodyToFlux(getGithubPullRequestResponse.class)
                .map(response -> response)
                .collectList();
    }

    @Override
    public Mono<List<String>> getPullRequestsHeadRefAsync( String repoName) {
        return this.webClient.get()
                .uri("https://api.github.com/repos/{owner}/{repo}/pulls", this.organization, repoName)
                .retrieve()
                .bodyToFlux(getGithubPullRequestResponse.class)
                .map(response -> response.getHead().getRef())
                .collectList();
    }

    @Override
    public Mono<List<GetUser>> getGithubUserList() {
        String uri = "https://api.github.com/orgs/" + this.organization + "/members";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GetUser.class)
                .collectList()
                .doOnNext(response -> logger.info("Request sent"))
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<String>> listDevContainers() {
        String repoName = "knot-devcontainers-versions";
        String fileName = "metadata.yaml";

        GetGithubFileResponse getGithubFileResponse = getGithubFileAsync(repoName, fileName).block();

        logger.info("The response from GitHub is " + getGithubFileResponse.getName());
        String base64String = getGithubFileResponse.getContent().replaceAll("\\s", "");
        try {
            byte[] content = Base64.getDecoder().decode(base64String);
            String contentAsString = new String(content, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            List<String> devcontainerListResponse = objectMapper.readValue(contentAsString,
                    List.class);

            return Mono.just(devcontainerListResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<List<GithubBranch>> getGithubBranches(String repoName, String userToken) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/branches";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GithubBranch.class)
                .collectList()
                .doOnNext(response -> {
                    logger.info("Request sent");
                    response.forEach(item -> item.setLabel(item.getBranchName()));
                })
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override

    public Mono<MergeGithubPullRequestResponse> approvePullRequest(MergeGithubPullRequest mergeGithubPullRequest,
            String repoName, String idPullRequest) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/pulls/" + idPullRequest + "/merge";
        return webClient.put()
                .uri(uri)
                .bodyValue(mergeGithubPullRequest)
                .retrieve()
                .bodyToMono(MergeGithubPullRequestResponse.class)
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<CreateGithubPullRequestResponse> createPullRequest(CreateGithubPullRequest createGithubPullRequest, String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/pulls";
        return webClient.post()
                .uri(uri)
                .bodyValue(createGithubPullRequest)
                .retrieve()
                .bodyToMono(CreateGithubPullRequestResponse.class)
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<CreateGithubReleaseResponse> createRelease(CreateGithubRelease createGithubRelease, String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/releases";
        return webClient.post()
                .uri(uri)
                .bodyValue(createGithubRelease)
                .retrieve()
                .bodyToMono(CreateGithubReleaseResponse.class)
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<List<GithubBranchResponse>> getListBranches(String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/branches";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GithubBranchResponse.class)
                .collectList()
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<CreateGithubBranchResponse> createBranch(CreateGithubBranchRequest createGithubBranchRequest,
            String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/git/refs";
        return webClient.post()
                .uri(uri)
                .bodyValue(createGithubBranchRequest)
                .retrieve()
                .bodyToMono(CreateGithubBranchResponse.class)
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }

    @Override
    public Mono<MergeGithubBranchResponse> mergeBranch(MergeGithubBranchRequest mergeGithubBranchRequest,
            String repoName) {
        String uri = this.githubUrl + "/" + this.organization + "/" + repoName + "/merges";
        return webClient.post()
                .uri(uri)
                .bodyValue(mergeGithubBranchRequest)
                .retrieve()
                .bodyToMono(MergeGithubBranchResponse.class)
                .doOnError(e -> logger.error("Unable to process request, exception is " + e.getMessage()));
    }
}
