package com.example.documentmanager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.documentmanager.services.UploadObjectService;
import com.google.cloud.storage.Blob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.val;

@RestController
@CrossOrigin(origins = { "http://localhost:8100", "http://192.168.29.38:8100" })
@RequestMapping(value = "/api/v1/docs")
public class MainDocumentController {

    @Autowired
    private UploadObjectService docService;

    @RequestMapping(value = "getDocumentTree/{treeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getDocumentTree(@PathVariable String treeId)
            throws NumberFormatException, InterruptedException, ExecutionException, IOException {
        val res = new HashMap<String, Object>();
        Map<?, ?> data = docService.getDocumentTree(treeId);
        if (data != null) {
            res.put("status", true);
            res.put("data", data);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.put("status", false);
            res.put("message", "No data found");
            res.put("data", data);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getAllDocs/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDocs(@PathVariable String userId)
            throws NumberFormatException, InterruptedException, ExecutionException, IOException {
        val res = new HashMap<>();
        List<Blob> data = docService.getDocuments(userId);
        if (data != null) {
            res.put("status", true);
            res.put("data", data);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.put("status", false);
            res.put("data", data);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/createDocument/{docRefId}", method = RequestMethod.POST)
    public ResponseEntity<?> createDocument(@PathVariable(value = "docRefId", required = true) String docRefId,
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "path", required = true) String path)
            throws ExecutionException, InterruptedException {

        System.out.println(docService.createDocuments(path, file));

        val response = new HashMap<>();
        response.put("status", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getSignedUrl/{docName}", method = RequestMethod.GET)
    public ResponseEntity<?> getSignedUrl(@PathVariable String docName,
            @RequestParam(value = "path", required = true) String path)
            throws ExecutionException, InterruptedException, IOException {

        String url = docService.getSignedUrlForDocument(path, docName);

        val response = new HashMap<>();
        response.put("status", true);
        response.put("signedUrl", url);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
