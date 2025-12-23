package de.tobias_stenger.jgit;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CredentialLoader {

    public static UsernamePasswordCredentialsProvider loadGitHubCredentials() {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("credentials.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = props.getProperty("github.username");
        String token = props.getProperty("github.token");

        return new UsernamePasswordCredentialsProvider(username, token);
    }
}
