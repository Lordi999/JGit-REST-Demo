package de.tobias_stenger.jgit.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class FileService {

    private GitService gitService;

    public FileService(GitService gitService) {
        this.gitService = gitService;
    }

    public Resource getFile(String repoName, String filePath) {
        String repositoryDirectory = gitService.getRepositoryDirectory(repoName);
        String fullFilePath = repositoryDirectory + filePath;

        return new FileSystemResource(fullFilePath);
    }

    public void writeFile(String repoName, String filePath, MultipartFile file) throws IOException {
        String repositoryDirectory = gitService.getRepositoryDirectory(repoName);
        Path path = Path.of(repositoryDirectory + filePath);

        Files.write(path, file.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}

