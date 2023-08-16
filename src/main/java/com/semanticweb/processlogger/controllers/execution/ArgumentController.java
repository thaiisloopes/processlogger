package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.ArgumentApplication;
import com.semanticweb.processlogger.applications.execution.resources.Argument;
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
@RequestMapping("/arguments")
public class ArgumentController {
    private static final Logger logger = getLogger(ArgumentController.class);

    @Autowired
    private ArgumentApplication argumentApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody Argument argument) throws URISyntaxException {
        logger.info("Saving argument");

        ResourceCreationResponse response = argumentApplication.save(argument);

        return status(CREATED).body(response);
    }
}
