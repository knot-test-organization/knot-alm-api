package com.nttdata.knot.almapi.Interfaces;

import java.util.List;

import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranch;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.CreateGithubBranchRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.CreateGithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.GithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.MergeGithubBranchRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubBranches.MergeGithubBranchResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubCommits.GetGithubCommitsResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubFileResponse.GetGithubFileResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.CreateGithubPullRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.CreateGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.MergeGithubPullRequest;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.MergeGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubPullRequest.getGithubPullRequestResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases.CreateGithubRelease;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubReleases.CreateGithubReleaseResponse;
import com.nttdata.knot.almapi.Models.GithubPackage.GithubTag.GetGithubTagResponse;
import com.nttdata.knot.almapi.Models.UserPackage.GetUser;

import reactor.core.publisher.Mono;

public interface IGithubService {

    Mono<GetGithubFileResponse> getGithubFileAsync(String repoName, String name);

    Mono<String> createGithubFileAsync(CreateGithubFileRequest createGithubFile, String repoName, String name);

    Mono<String> deleteGithubFileAsync(DeleteGithubFileRequest deleteGithubFile, String repoName, String name);

    Mono<String> createGithubTagAsync(String repoName, String source, String target, String message);

    Mono<List<GetGithubCommitsResponse>> getGithubCommitsAsync(String repoName);

    Mono<List<GetGithubTagResponse>> getGithubTagAsync(String repoName);

    Mono<List<GetUser>> getGithubUserList();

    Mono<List<String>> listDevContainers();

    Mono<List<GithubBranch>> getGithubBranches(String repoName, String userToken);

    Mono<List<String>> getGithubReleasesAsync(String repoName);

    Mono<CreateGithubReleaseResponse> createRelease(CreateGithubRelease createGithubRelease, String repoName);

    Mono<CreateGithubPullRequestResponse> createPullRequest(CreateGithubPullRequest createGithubPullRequest, String repoName);

    Mono<MergeGithubPullRequestResponse> approvePullRequest(MergeGithubPullRequest mergeGithubPullRequest, String repoName, String numberPullRequest);

    Mono<List<String>> getPullRequestsHeadRefAsync( String repoName);
    
    Mono<List<getGithubPullRequestResponse>> getListPullRequestsAsync(String repoName);

    Mono<List<GithubBranchResponse>> getListBranches(String repoName);

    Mono<CreateGithubBranchResponse> createBranch(CreateGithubBranchRequest createGithubBranchRequest, String repoName);

    Mono<MergeGithubBranchResponse> mergeBranch(MergeGithubBranchRequest mergeGithubBranchRequest, String repoName);
}
