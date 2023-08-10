package com.semanticweb.processlogger.controllers.metamodel;

import com.semanticweb.processlogger.applications.metamodel.InputOutputSpecificationApplication;
import com.semanticweb.processlogger.applications.metamodel.resources.InputOutputSpecification;
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
@RequestMapping("/inputOutputSpecifications")
public class InputOutputSpecificationController {
    private static final Logger logger = getLogger(InputOutputSpecificationController.class);

    @Autowired
    private InputOutputSpecificationApplication inputOutputSpecificationApplication;

    @PostMapping
    public ResponseEntity save(
            @RequestBody InputOutputSpecification inputOutputSpecification
    ) throws URISyntaxException {
        logger.info("Saving inputOutputSpecification");

        ResourceCreationResponse response = inputOutputSpecificationApplication.save(inputOutputSpecification);

        return status(CREATED).body(response);
    }
}
