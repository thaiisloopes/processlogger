package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.SubProcessExecutionApplication;
import com.semanticweb.processlogger.applications.execution.TaskExecutionApplication;
import com.semanticweb.processlogger.applications.execution.resources.SubProcessExecution;
import com.semanticweb.processlogger.applications.execution.resources.TaskExecution;
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
@RequestMapping("/processes/{processId}/executions/{processExecutionId}/subProcesses/{subProcessId}/executions")
public class SubProcessExecutionController {
    private static final Logger logger = getLogger(SubProcessExecutionController.class);

    @Autowired
    private SubProcessExecutionApplication subProcessExecutionApplication;

    @PostMapping
    public ResponseEntity save(
            @RequestBody SubProcessExecution subProcessExecution,
            @RequestParam String processId,
            @RequestParam String processExecutionId,
            @RequestParam String subProcessId
    ) throws URISyntaxException {
        logger.info("Saving subProcessExecution");

        ResourceCreationResponse response = subProcessExecutionApplication.save(
                subProcessExecution, processId, processExecutionId, subProcessId
        );

        return status(CREATED).body(response);
    }
}
