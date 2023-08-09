package com.semanticweb.processlogger.controllers.metamodel;

import com.semanticweb.processlogger.applications.metamodel.ResourceApplication;
import com.semanticweb.processlogger.applications.metamodel.resources.Resource;
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
@RequestMapping("/resources")
public class ResourceController {
    private static final Logger logger = getLogger(ResourceController.class);

    @Autowired
    private ResourceApplication resourceApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody Resource resource) throws URISyntaxException {
        logger.info("Saving resource");

        ResourceCreationResponse response = resourceApplication.save(resource);

        return status(CREATED).body(response);
    }
}
