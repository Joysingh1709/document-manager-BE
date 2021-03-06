package com.documentmanager.api.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.google.cloud.storage.Blob;
import com.documentmanager.api.DocumentManagerApplication;
import com.documentmanager.api.config.FirebaseInitilizer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.val;

@Service
public class UploadObjectImpService implements UploadObjectService {

    private static final String BUCKET_NAME = "document-manager-73275.appspot.com";

    @Value("${service.account.path}")
    private String serviceAccountPath;

    private FirebaseInitilizer dbService = new FirebaseInitilizer();

    @Override
    public List<Blob> getDocuments(String userId) throws InterruptedException, ExecutionException, IOException {

        System.out.println("Getting documents from Cloud Storage");

        StorageOptions storage = dbService.getFirebaseStorage();

        return storage.getService().get();

    }

    @Override
    public String createDocuments(String path, MultipartFile file, String docRefId, Map<String, Object> fileMapping)
            throws ExecutionException, InterruptedException {

        try {

            String pathWithoutContents = (!path.isEmpty()) ? path.replace("/contents", "") : path;

            System.out.println("path : " + pathWithoutContents);
            System.out.println("file : " + file.getOriginalFilename());

            BlobId blobId = (!path.isEmpty())
                    ? BlobId.of(BUCKET_NAME, docRefId + "/" + pathWithoutContents + "/" + file.getOriginalFilename())
                    : BlobId.of(BUCKET_NAME, docRefId + "/" + file.getOriginalFilename());

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            this.getBucketRef().create(blobInfo, file.getBytes());

            String res = (!path.isEmpty())
                    ? this.updateDocumentRefTree(path, docRefId, file.getOriginalFilename(), fileMapping)
                    : this.updateDocumentRefTree(null, docRefId, file.getOriginalFilename(), fileMapping);

            System.out.println(
                    "File at : " + pathWithoutContents + " uploaded to bucket " + BUCKET_NAME + " as "
                            + file.getOriginalFilename()
                            + " at : " + res);

            return (!path.isEmpty()) ? docRefId + "/" + pathWithoutContents + "/" + file.getOriginalFilename()
                    : docRefId + "/" + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String updateDocumentRefTree(String path, String docRefId, String fileName, Map<String, Object> fileMapping)
            throws InterruptedException, ExecutionException {

        DocumentReference docRef = dbService.getFirebaseFirestore().collection("documentRefs").document(docRefId);
        ApiFuture<WriteResult> writeResult = docRef.set(fileMapping, SetOptions.merge());
        // ...
        System.out.println("Update time : " + writeResult.get().getUpdateTime());

        return writeResult.get().getUpdateTime().toDate().toString();
    }

    public String updateFolderInDocRefTree(String docRefId, Map<String, Object> folderMapping)
            throws InterruptedException, ExecutionException {

        DocumentReference docRef = dbService.getFirebaseFirestore().collection("documentRefs").document(docRefId);

        // Async update document
        ApiFuture<WriteResult> writeResult = docRef.set(folderMapping, SetOptions.merge());
        // ...
        System.out.println("Update time : " + writeResult.get().getUpdateTime());

        return writeResult.get().getUpdateTime().toDate().toString();
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

    public Storage getBucketRef() throws JsonParseException, JsonMappingException, IOException {

        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        Storage storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("document-manager-73275")
                .build()
                .getService();

        return storage;
    }

    @Override
    public String initFolder(String docRefId) {

        try {
            File emptyFile = new File("ghostFile.txt");
            emptyFile.createNewFile();

            byte[] byteArray = new byte[(int) emptyFile.length()];

            BlobId blobId = BlobId.of(BUCKET_NAME, docRefId + "/" + "ghostFile.txt");

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            this.getBucketRef().create(blobInfo, byteArray);

            System.out.println(
                    "ghost File at : " + " uploaded to bucket " + BUCKET_NAME + " as "
                            + "ghostFile.txt");

            emptyFile.delete();

            DocumentReference docRef = dbService.getFirebaseFirestore().collection("documentRefs").document(docRefId);

            val documentTree = new HashMap<>();
            documentTree.put("documentTree", new HashMap<>());

            docRef.set(documentTree);

            return "Directory initiated at : /" + docRefId + "/";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String createFolder(String docRefId, String path, String folderName, Map<String, Object> folderMapping)
            throws InterruptedException, ExecutionException {
        try {
            File emptyFile = new File("ghostFile.txt");
            emptyFile.createNewFile();

            byte[] byteArray = new byte[(int) emptyFile.length()];

            String pathWithoutContents = (!path.isEmpty()) ? path.replace("/contents", "") : path;

            System.out.println("path : " + pathWithoutContents);

            BlobId blobId = (!path.isEmpty())
                    ? BlobId.of(BUCKET_NAME,
                            docRefId + "/" + pathWithoutContents + "/" + folderName + "/" + "ghostFile.txt")
                    : BlobId.of(BUCKET_NAME, docRefId + "/" + folderName + "/" + "ghostFile.txt");

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            this.getBucketRef().create(blobInfo, byteArray);

            String res = this.updateFolderInDocRefTree(docRefId, folderMapping);

            System.out.println(
                    "Folder at : " + pathWithoutContents + "/" + folderName + " Created to bucket " + BUCKET_NAME
                            + " as "
                            + folderName
                            + " at : " + res);

            emptyFile.delete();

            return (!path.isEmpty()) ? docRefId + "/" + pathWithoutContents + "/" + folderName + "/"
                    : docRefId + "/" + folderName + "/";

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
