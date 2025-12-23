package de.tobias_stenger.jgit.service;

import de.tobias_stenger.jgit.CredentialLoader;
import de.tobias_stenger.jgit.dto.GitCloneRepoDto;
import de.tobias_stenger.jgit.dto.GitCommitDto;
import de.tobias_stenger.jgit.exception.BranchMismatchException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitService {

    private static final String GIT_BASE = "~/repos/";

    public String cloneRepository(GitCloneRepoDto gitCloneRepoDto) {
        try (Git git = Git.cloneRepository()
                .setURI(gitCloneRepoDto.url())
                .setDirectory(new File(GIT_BASE + gitCloneRepoDto.repoName()))
                .call()) {

            return git.getRepository().getDirectory().toString();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkoutBranch(String repoName, String branchName, Boolean createBranch) {
        try (Git git = Git.init().setDirectory(new File(GIT_BASE + repoName)).call()) {
            git.checkout()
                    .setCreateBranch(createBranch)
                    .setName(branchName)
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRepositoryDirectory(String repositoryName) {
        try (Git git = Git.init().setDirectory(new File(GIT_BASE + repositoryName)).call()) {
            return git.getRepository().getWorkTree().getPath();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkForChanges(String repositoryName, String branchName) {
        if (isCorrectBranch(repositoryName, branchName)) {
            try (Git git = Git.init().setDirectory(new File(GIT_BASE + repositoryName)).call()) {
                Status status = git.status().call();
                return status.isClean();
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new BranchMismatchException(branchName);
        }
    }

    public void addAll(String repositoryName, String branchName) {
        if (isCorrectBranch(repositoryName, branchName)) {
            try (Git git = Git.init().setDirectory(new File(GIT_BASE + repositoryName)).call()) {
                git.add().setAll(true).call();
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new BranchMismatchException(branchName);
        }
    }

    public void commitAll(String repositoryName, String branchName, GitCommitDto gitCommitDto) {
        if (isCorrectBranch(repositoryName, branchName)) {
            try (Git git = Git.init().setDirectory(new File(GIT_BASE + repositoryName)).call()) {
                git.commit().setAll(true).setMessage(gitCommitDto.commitMessage()).call();
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new BranchMismatchException(branchName);
        }
    }

    public void push(String repositoryName, String branchName) {
        if (isCorrectBranch(repositoryName, branchName)) {
            try (Git git = Git.init().setDirectory(new File(GIT_BASE + repositoryName)).call()) {
                CredentialsProvider cp = CredentialLoader.loadGitHubCredentials();

                git.push()
                        .setCredentialsProvider(cp)
                        .setRemote("origin")
                        .call();
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new BranchMismatchException(branchName);
        }
    }

    private boolean isCorrectBranch(String repositoryName, String branchName) {
        try (Git git = Git.init().setDirectory(new File(GIT_BASE + repositoryName)).call()) {
            return branchName.equals(git.status().getRepository().getBranch());
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
