package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.domain.ProcessExecution;
import com.semanticweb.processlogger.service.ProcessExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/process-execution")
public class ProcessExecutionController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionController.class);

    @Autowired
    private ProcessExecutionService processExecutionService;

    @GetMapping
    public String getAllProcess(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded process executions");

        OutputStream stream = new ByteArrayOutputStream() ;
        processExecutionService.getProcess().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordAllProcess(@RequestBody List<ProcessExecution> processes) {
        logger.info("Calling service to record all process executions");

        List<ResourceCreationResponse> response = processExecutionService.record(processes);

        return ResponseEntity.status(CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity deleteProcess(@RequestBody List<ProcessExecution> processes,
                                        @RequestParam String graph) {
        logger.info("Calling service to delete a list of process execution");

        processExecutionService.deleteProcessExecutionFrom(processes, graph);

        return ResponseEntity.ok(OK);
    }

    @DeleteMapping("/graph")
    public ResponseEntity deleteGraph(@RequestParam String graph) {
        logger.info("Calling service to delete a complete graph");

        processExecutionService.deleteGraph(graph);

        return ResponseEntity.ok(OK);
    }
}
