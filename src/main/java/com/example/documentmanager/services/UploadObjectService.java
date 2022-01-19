package com.example.documentmanager.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.storage.Blob;

import org.springframework.web.multipart.MultipartFile;

public interface UploadObjectService {
    public HashMap<String, Object> getDocumentTree(String treeId)
            throws ExecutionException, InterruptedException, IOException;

    public List<Blob> getDocuments(String userId) throws ExecutionException, InterruptedException, IOException;

    public Blob createDocuments(String path, MultipartFile file) throws ExecutionException, InterruptedException;

    public String getSignedUrlForDocument(String path, String fileName)
            throws ExecutionException, InterruptedException;
}
