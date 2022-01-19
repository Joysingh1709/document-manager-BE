package com.example.documentmanager.services;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import firebase.FirebaseInitialize;

@Service
public class UploadObjectImpService implements UploadObjectService {

    private static final String BUCKET_NAME = "document-manager-73275.appspot.com";

    private FirebaseInitialize dbService = new FirebaseInitialize();

    @Override
    public List<Blob> getDocuments(String userId) throws InterruptedException, ExecutionException, IOException {

        System.out.println("Getting documents from Cloud Storage");

        StorageOptions storage = dbService.getFirebaseStorage();

        return storage.getService().get();

    }

    @Override
    public Blob createDocuments(String path, MultipartFile file) throws ExecutionException, InterruptedException {

        System.out.println("path : " + path);
        System.out.println("file : " + file.getOriginalFilename());

        try {
            System.out.println(
                    dbService.getFirebaseStorage().getService().get(BlobId.of(BUCKET_NAME, path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getSignedUrlForDocument(String path, String fileName)
            throws ExecutionException, InterruptedException {

        try {
            Storage storage = dbService.getFirebaseStorage().getService();

            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(BUCKET_NAME + "/" + path, fileName)).build();

            URL url = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

            return url.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HashMap<String, Object> getDocumentTree(String treeId)
            throws ExecutionException, InterruptedException, IOException {
        DocumentReference docRef = dbService.getFirebaseFirestore().collection("documentRefs").document(treeId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {

            HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
            System.out.println("document : " + data);

            return data;
        } else {
            System.out.println("No such document!");
            return null;
        }
    }

}
