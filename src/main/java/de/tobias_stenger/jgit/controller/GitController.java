package de.tobias_stenger.jgit.controller;

import de.tobias_stenger.jgit.dto.GitCloneRepoDto;
import de.tobias_stenger.jgit.dto.GitCommitDto;
import de.tobias_stenger.jgit.service.GitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("git")
public class GitController {

    GitService gitService;

    public GitController(GitService gitService) {
        this.gitService = gitService;
    }

    @GetMapping("/cloneRepo")
    public ResponseEntity<String> cloneRepository(@RequestBody GitCloneRepoDto gitCloneRepoRequest) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(gitService.cloneRepository(gitCloneRepoRequest));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/chekoutBranch")
    public ResponseEntity<String> checkoutBranch(
            @RequestParam String repoName,
            @RequestParam String branchName,
            @RequestParam Boolean createBranch) {
        try {
            gitService.checkoutBranch(repoName, branchName, createBranch);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(branchName);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("checkStatus")
    public ResponseEntity<Boolean> checkForChanges(@RequestParam String repoName, @RequestParam String branchName) {
        return (gitService.checkForChanges(repoName, branchName)) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @GetMapping("/addAll")
    public ResponseEntity<String> addAll(
            @RequestParam String repoName,
            @RequestParam String branchName) {
        try {
            gitService.addAll(repoName, branchName);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("All Changes added");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/commitAll")
    public ResponseEntity<String> commitAll(
            @RequestParam String repoName,
            @RequestParam String branchName,
            @RequestBody GitCommitDto gitCommitDto) {
        try {
            gitService.commitAll(repoName, branchName, gitCommitDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("All changes committed");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/push")
    public ResponseEntity<String> push(
            @RequestParam String repoName,
            @RequestParam String branchName) {
        try {
            gitService.push(repoName, branchName);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("All changes pushed");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

}
