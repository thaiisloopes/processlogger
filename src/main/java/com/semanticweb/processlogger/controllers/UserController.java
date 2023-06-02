package com.semanticweb.processlogger.controllers;

import com.semanticweb.processlogger.applications.UserApplication;
import com.semanticweb.processlogger.applications.resources.User;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = getLogger(UserController.class);

    @Autowired
    private UserApplication userApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody User user) throws URISyntaxException {
        logger.info("Saving user");

        ResourceCreationResponse response = userApplication.save(user);

        return status(CREATED).body(response);
    }
}
