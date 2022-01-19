package com.example.documentmanager.services;

import java.util.concurrent.ExecutionException;

import com.example.documentmanager.models.User;

public interface UserService {

    public User getUser(String userId) throws NumberFormatException, InterruptedException, ExecutionException;

    public User setUser(String userId, User body)
            throws NumberFormatException, InterruptedException, ExecutionException;

}
