package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.PerformerApplication;
import com.semanticweb.processlogger.applications.execution.resources.Performer;
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
@RequestMapping("/performers")
public class PerformerController {
    private static final Logger logger = getLogger(PerformerController.class);

    @Autowired
    private PerformerApplication performerApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody Performer performer) throws URISyntaxException {
        logger.info("Saving performer");

        ResourceCreationResponse response = performerApplication.save(performer);

        return status(CREATED).body(response);
    }
}
