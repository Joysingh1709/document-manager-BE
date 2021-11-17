package com.example.documentmanager.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.storage.Blob;

import org.springframework.web.multipart.MultipartFile;

public interface UploadObjectService {
    public List<Blob> getDocuments(String userId) throws ExecutionException, InterruptedException;

    public Blob createDocuments(String path, MultipartFile file) throws ExecutionException, InterruptedException;

    // public ObjectUpload getDoctor(String docId) throws InterruptedException,
    // ExecutionException;
    //
    // public ObjectUpload addDoctor(Doctor doctor, String docId) throws
    // InterruptedException, ExecutionException;
    //
    // public Map<String, Object> updateDoctor(String doctorId, HashMap<String,
    // Object> userUpdateData)
    // throws InterruptedException, ExecutionException;
}
