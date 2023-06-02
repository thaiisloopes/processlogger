package com.semanticweb.processlogger.controllers;

import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.applications.resources.Execution;
import com.semanticweb.processlogger.applications.ExecutionApplication;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/processes/{processId}/executions")
public class ExecutionController {
    private static final Logger logger = getLogger(ExecutionController.class);

    @Autowired
    private ExecutionApplication executionApplication;

    @PostMapping
    public ResponseEntity recordProcessExecution(
            @RequestBody Execution execution,
            @PathVariable String processId
    ) throws URISyntaxException {
        logger.info("Calling service to record a process execution");

        ResourceCreationResponse response = executionApplication.recordProcessExecution(execution, processId);

        return status(CREATED).body(response);
    }

    @PostMapping("/{processExecutionId}/tasks/{taskId}/executions")
    public ResponseEntity recordTaskExecution(
            @RequestBody Execution execution,
            @PathVariable String processId,
            @PathVariable String processExecutionId,
            @PathVariable String taskId
    ) throws URISyntaxException {
        logger.info("Calling service to record a task execution related to a process Execution");

        ResourceCreationResponse response = executionApplication.recordTaskExecution(
                execution, processId, processExecutionId, taskId);

        return status(CREATED).body(response);
    }

    @PostMapping("/{processExecutionId}/processes/{subProcessId}/executions")
    public ResponseEntity recordSubProcessExecution(
            @RequestBody Execution execution,
            @PathVariable String processId,
            @PathVariable String processExecutionId,
            @PathVariable String subProcessId
    ) throws URISyntaxException {
        logger.info("Calling service to record a subProcess execution related to a process Execution");

        ResourceCreationResponse response = executionApplication.recordSubProcessExecution(
                execution, processId, processExecutionId, subProcessId);

        return status(CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity deleteProcess(@RequestBody List<Execution> processes,
                                        @RequestParam String graph) {
        logger.info("Calling service to delete a list of process execution");

        executionApplication.deleteProcessExecutionFrom(processes, graph);

        return status(OK).build();
    }

    @DeleteMapping("/graph")
    public ResponseEntity deleteGraph(@RequestParam String graph) {
        logger.info("Calling service to delete a complete graph");

        executionApplication.deleteGraph(graph);

        return status(OK).build();
    }
}
