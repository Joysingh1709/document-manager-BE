package com.example.documentmanager.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.io.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import firebase.FirebaseInitialize;

@Service
public class UploadObjectImpService implements UploadObjectService {

    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/document-manager-73275/o/%s?alt=media";
    // @Autowired
    private FirebaseInitialize dbService;

    @Override
    public List<Blob> getDocuments(String userId) throws InterruptedException, ExecutionException {

        System.out.println("Getting documents from Cloud Storage");

        Storage storage = dbService.getFirebaseStorage().getService();

        return storage.get();

    }

    @Override
    public Blob createDocuments(String path, MultipartFile file) throws ExecutionException, InterruptedException {
        Storage storage = dbService.getFirebaseStorage().getService();

        String fileName = file.getOriginalFilename();

        BlobId blobId = BlobId.of("bucket name", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        return storage.create(blobInfo);
        // return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName,
        // StandardCharsets.UTF_8));
    }

    // private File convertToFile(MultipartFile multipartFile, String fileName)
    // throws IOException {
    // File tempFile = new File(fileName);
    // try (FileOutputStream fos = new FileOutputStream(tempFile)) {
    // fos.write(multipartFile.getBytes());
    // fos.close();
    // }
    // return tempFile;
    // }
    //
    // private String getExtension(String fileName) {
    // return fileName.substring(fileName.lastIndexOf("."));
    // }

}
