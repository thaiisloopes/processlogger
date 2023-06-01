package com.semanticweb.processlogger.inbound.controller;

import com.semanticweb.processlogger.domain.applications.UserApplication;
import com.semanticweb.processlogger.domain.resources.User;
import com.semanticweb.processlogger.inbound.resources.ResourceCreationResponse;
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
    public ResponseEntity recordAgent(@RequestBody User user) throws URISyntaxException {
        logger.info("Calling service to record an user");

        ResourceCreationResponse response = userApplication.save(user);

        return status(CREATED).body(response);
    }
}
