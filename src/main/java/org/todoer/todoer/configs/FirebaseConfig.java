package org.todoer.todoer.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {
    @Value("${firebase.credentials-path}")
    private String credentialsPath;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials;

        try {
            // Try as file path first
            File credentialsFile = new File(credentialsPath);
            if (credentialsFile.exists()) {
                log.info("Loading Firebase credentials from file: {}", credentialsPath);
                googleCredentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFile));
            }
            // Try as classpath resource
            else {
                log.info("Loading Firebase credentials from classpath: {}", credentialsPath);
                Resource resource = new ClassPathResource(credentialsPath);
                googleCredentials = GoogleCredentials.fromStream(resource.getInputStream());
            }

            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            // Check if Firebase app is already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
            }

            return FirebaseMessaging.getInstance();
        } catch (IOException e) {
            log.error("Error loading Firebase credentials from {}", credentialsPath, e);
            throw new IllegalStateException("Cannot load Firebase credentials", e);
        }
    }

}
