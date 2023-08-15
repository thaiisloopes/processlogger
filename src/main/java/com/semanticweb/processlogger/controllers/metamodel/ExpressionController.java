package com.semanticweb.processlogger.controllers.metamodel;

import com.semanticweb.processlogger.applications.metamodel.ExpressionApplication;
import com.semanticweb.processlogger.applications.metamodel.RoleApplication;
import com.semanticweb.processlogger.applications.metamodel.resources.Expression;
import com.semanticweb.processlogger.applications.metamodel.resources.Role;
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
@RequestMapping("/expressions")
public class ExpressionController {
    private static final Logger logger = getLogger(ExpressionController.class);

    @Autowired
    private ExpressionApplication expressionApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody Expression expression) throws URISyntaxException {
        logger.info("Saving expression");

        ResourceCreationResponse response = expressionApplication.save(expression);

        return status(CREATED).body(response);
    }
}
