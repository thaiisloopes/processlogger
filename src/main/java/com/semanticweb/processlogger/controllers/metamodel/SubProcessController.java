package com.semanticweb.processlogger.controllers.metamodel;

import com.semanticweb.processlogger.applications.metamodel.ProcessApplication;
import com.semanticweb.processlogger.applications.metamodel.SubProcessApplication;
import com.semanticweb.processlogger.applications.metamodel.resources.Process;
import com.semanticweb.processlogger.applications.metamodel.resources.SubProcess;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/processes/{processId}/subprocesses")
public class SubProcessController {
    private static final Logger logger = getLogger(SubProcessController.class);

    @Autowired
    private SubProcessApplication subProcessApplication;

    @PostMapping
    public ResponseEntity save(
            @RequestBody SubProcess subProcess,
            @PathVariable String processId
    ) throws URISyntaxException {
        logger.info("Saving subprocess");

        ResourceCreationResponse response = subProcessApplication.save(subProcess, processId);

        return status(CREATED).body(response);
    }
}
