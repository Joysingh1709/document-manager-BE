package com.documentmanager.api;

import java.util.concurrent.ExecutionException;

import com.documentmanager.api.models.User;
import com.documentmanager.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "http://localhost:8100", "http://192.168.29.38:8100", "https://documentmanager.vercel.app" })
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable String userId)
            throws NumberFormatException, InterruptedException, ExecutionException {

        User user = this.userService.getUser(userId);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public ResponseEntity<?> setUser(@PathVariable String userId, @RequestBody User body)
            throws NumberFormatException, InterruptedException, ExecutionException {

        User user = this.userService.setUser(userId, body);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    // public ResponseEntity<?> setUser(@PathVariable String userId, @RequestBody
    // User body)
    // throws NumberFormatException, InterruptedException, ExecutionException {
    //
    // User user = this.userService.setUser(userId, body);
    //
    // if (user != null) {
    // return new ResponseEntity<>(user, HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }
}
