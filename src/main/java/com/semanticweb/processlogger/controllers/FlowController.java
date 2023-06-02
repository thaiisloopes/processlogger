package com.semanticweb.processlogger.controllers;

import com.semanticweb.processlogger.applications.FlowApplication;
import com.semanticweb.processlogger.applications.resources.Flow;
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
@RequestMapping("/flows")
public class FlowController {
    private static final Logger logger = getLogger(FlowController.class);

    @Autowired
    private FlowApplication flowApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody Flow flow) throws URISyntaxException {
        logger.info("Saving flow");

        ResourceCreationResponse response = flowApplication.save(flow);

        return status(CREATED).body(response);
    }
}
