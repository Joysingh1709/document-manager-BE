package com.example.documentmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
// import java.io.InputStream;
import java.util.Objects;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.core.io.Resource;

@SpringBootApplication
public class DocumentManagerApplication {

	public static void main(String[] args) throws IOException {

		ClassLoader classLoader = DocumentManagerApplication.class.getClassLoader();

		File serviceAccFile = new File(
				Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());

		FileInputStream serviceAccount = new FileInputStream(serviceAccFile.getAbsolutePath());

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}

		SpringApplication.run(DocumentManagerApplication.class, args);
	}
}
