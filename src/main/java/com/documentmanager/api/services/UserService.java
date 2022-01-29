package com.documentmanager.api.services;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.documentmanager.api.models.User;

public interface UserService {

    public User getUser(String userId) throws NumberFormatException, InterruptedException, ExecutionException;

    public User setUser(String userId, Map<String, Object> body)
            throws NumberFormatException, InterruptedException, ExecutionException;

}
