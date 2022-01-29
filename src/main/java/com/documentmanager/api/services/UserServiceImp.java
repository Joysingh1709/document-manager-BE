package com.documentmanager.api.services;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.documentmanager.api.config.FirebaseInitilizer;
import com.documentmanager.api.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    private FirebaseInitilizer dbService = new FirebaseInitilizer();

    @Override
    public User getUser(String userId) throws NumberFormatException, InterruptedException, ExecutionException {

        DocumentReference docRef = dbService.getFirebaseFirestore().collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        User user = new User();

        if (document.exists()) {
            // convert document to POJO
            user = document.toObject(User.class);
            System.out.println(user);
            return user;
        } else {
            System.out.println("No such document!");
            return null;
        }
    }

    @Override
    public User setUser(String userId, Map<String, Object> body)
            throws NumberFormatException, InterruptedException, ExecutionException {

        System.out.println("Body In Service Imp : " + body);

        User user = new User();
        user.setUserId(userId);
        user.setName(body.get("name").toString());
        user.setEmail(body.get("email").toString());
        user.setDocTreeRefId(body.get("docTreeRefId").toString());
        user.setDocsCount(Integer.parseInt(body.get("docsCount").toString()));
        user.setPictureUrl(body.get("pictureUrl").toString());
        user.setCreatedAt(Timestamp.now());
        user.setLastUpdatedAt(Timestamp.now());
        user.setLastLoginAt(Timestamp.now());

        ApiFuture<WriteResult> future = dbService.getFirebaseFirestore().collection("users").document(userId).set(user);
        WriteResult result = future.get();
        if (result.getUpdateTime() != null) {
            return user;
        } else {
            System.out.println("User not created **ERROR**");
            return null;
        }
    }

}
