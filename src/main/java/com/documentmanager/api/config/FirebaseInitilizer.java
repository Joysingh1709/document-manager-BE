package com.documentmanager.api.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import com.google.cloud.storage.StorageOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseInitilizer {

    @Value("${service.account.path}")
    private String serviceAccountPath;

    @PostConstruct
    @SuppressWarnings("derecation")
    private void initilize() throws IOException {

        System.out.println("*****************************Firebase Initialize**********************************");
        System.out.println("**********************************************************************************");

        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://document-manager-73275-default-rtdb.firebaseio.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public Firestore getFirebaseFirestore() {
        return FirestoreClient.getFirestore();
    }

    public StorageOptions getFirebaseStorage() throws IOException {

        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        return StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
    }

}
