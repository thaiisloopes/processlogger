package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.TaskExecutionApplication;
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
@RequestMapping("/processes/{processId}/executions/{processExecutionId}/tasks/{taskId}/executions")
public class TaskExecutionController {
    private static final Logger logger = getLogger(PerformerController.class);

    @Autowired
    private TaskExecutionApplication taskExecutionApplication;

    @PostMapping
    public ResponseEntity save(
            @RequestBody TaskExecution taskExecution,
            @RequestParam String processId,
            @RequestParam String processExecutionId,
            @RequestParam String taskId
    ) throws URISyntaxException {
        logger.info("Saving taskExecution");

        ResourceCreationResponse response = taskExecutionApplication.save(
                taskExecution, processId, processExecutionId, taskId
        );

        return status(CREATED).body(response);
    }
}
