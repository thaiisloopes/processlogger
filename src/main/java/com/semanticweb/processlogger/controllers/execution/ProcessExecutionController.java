package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.ProcessExecutionApplication;
import com.semanticweb.processlogger.applications.execution.SubProcessExecutionApplication;
import com.semanticweb.processlogger.applications.execution.resources.ProcessExecution;
import com.semanticweb.processlogger.applications.execution.resources.SubProcessExecution;
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
@RequestMapping("/processes/{processId}/executions")
public class ProcessExecutionController {
    private static final Logger logger = getLogger(ProcessExecutionController.class);

    @Autowired
    private ProcessExecutionApplication processExecutionApplication;

    @GetMapping("/{processExecutionId}")
    public String get(
            @RequestHeader("Accept") String accept,
            @PathVariable String processId,
            @PathVariable String processExecutionId
    ) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Getting process execution");

        OutputStream stream = new ByteArrayOutputStream() ;
        processExecutionApplication.getById(processId, processExecutionId).write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity save(
            @RequestBody ProcessExecution processExecution,
            @PathVariable String processId
    ) throws URISyntaxException {
        logger.info("Saving processExecution");

        ResourceCreationResponse response = processExecutionApplication.save(processExecution, processId);

        return status(CREATED).body(response);
    }
}
