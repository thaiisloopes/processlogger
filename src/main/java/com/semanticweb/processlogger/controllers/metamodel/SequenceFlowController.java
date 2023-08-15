package com.semanticweb.processlogger.controllers.metamodel;

import com.semanticweb.processlogger.applications.metamodel.RoleApplication;
import com.semanticweb.processlogger.applications.metamodel.SequenceFlowApplication;
import com.semanticweb.processlogger.applications.metamodel.resources.Role;
import com.semanticweb.processlogger.applications.metamodel.resources.SequenceFlow;
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
@RequestMapping("/sequenceflows")
public class SequenceFlowController {
    private static final Logger logger = getLogger(SequenceFlowController.class);

    @Autowired
    private SequenceFlowApplication sequenceFlowApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody SequenceFlow sequenceFlow) throws URISyntaxException {
        logger.info("Saving sequenceFlow");

        ResourceCreationResponse response = sequenceFlowApplication.save(sequenceFlow);

        return status(CREATED).body(response);
    }
}
