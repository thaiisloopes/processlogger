package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.SubProcessExecutionApplication;
import com.semanticweb.processlogger.applications.execution.resources.SubProcessExecution;
import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/processes/{processId}/executions/{processExecutionId}/subprocesses/{subprocessId}/executions")
public class SubProcessExecutionController {
    private static final Logger logger = getLogger(SubProcessExecutionController.class);

    @Autowired
    private SubProcessExecutionApplication subProcessExecutionApplication;

    @PostMapping
    public ResponseEntity save(
            @RequestBody SubProcessExecution subProcessExecution,
            @PathVariable String processId,
            @PathVariable String processExecutionId,
            @PathVariable String subprocessId
    ) throws URISyntaxException {
        logger.info("Saving subProcessExecution");

        ResourceCreationResponse response = subProcessExecutionApplication.save(
                subProcessExecution, processId, processExecutionId, subprocessId
        );

        return status(CREATED).body(response);
    }
}
