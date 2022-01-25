package com.documentmanager.api.services;

import java.util.concurrent.ExecutionException;

import com.documentmanager.api.config.FirebaseInitilizer;
import com.documentmanager.api.models.User;
import com.google.api.core.ApiFuture;
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
    public User setUser(String userId, User body)
            throws NumberFormatException, InterruptedException, ExecutionException {
        ApiFuture<WriteResult> future = dbService.getFirebaseFirestore().collection("users").document(userId).set(body);
        WriteResult result = future.get();
        if (result.getUpdateTime() != null) {
            return body;
        } else {
            System.out.println("User not created **ERROR**");
            return null;
        }
    }

}
