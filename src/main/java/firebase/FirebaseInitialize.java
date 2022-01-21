package firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.example.documentmanager.DocumentManagerApplication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseInitialize {

    @Value("${service.account.path}")
    private String serviceAccountPath;

    public static StorageOptions storageOptions;
    // private DatabaseReference mDatabase;

    @PostConstruct
    @SuppressWarnings("derecation")
    public void initilize() throws IOException {

        // ClassLoader classLoader = DocumentManagerApplication.class.getClassLoader();
        //
        // File serviceAccFile = new File(
        // Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        //
        // FileInputStream serviceAccount = new
        // FileInputStream(serviceAccFile.getAbsolutePath());

        System.out.println("***************************************************************");
        System.out.println(serviceAccountPath);
        System.out.println("***************************************************************");

        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

        // this.storageOptions =
        // StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
        // .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public Firestore getFirebaseFirestore() {
        return FirestoreClient.getFirestore();
    }

    // public DatabaseReference getFirebaseRealtimeDatabase() {
    // return this.mDatabase;
    // }

    public StorageOptions getFirebaseStorage() throws IOException {
        ClassLoader classLoader = DocumentManagerApplication.class.getClassLoader();

        File serviceAccFile = new File(
                Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());

        FileInputStream serviceAccount = new FileInputStream(serviceAccFile.getAbsolutePath());

        return StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
    }

}
