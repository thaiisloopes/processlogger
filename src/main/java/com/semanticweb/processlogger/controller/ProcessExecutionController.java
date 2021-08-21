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

@RestController
@RequestMapping("/process")
public class ProcessExecutionController {

    @Autowired
    private ProcessExecutionService processExecutionService;

    private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionController.class);

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

        processExecutionService.record(processes);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity deleteProcess(@RequestBody List<ProcessExecution> processes,
                                        @RequestParam String graph) {
        logger.info("Calling service to delete a list of process execution");

        processExecutionService.deleteProcessExecutionFrom(processes, graph);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/graph")
    public ResponseEntity deleteGraph(@RequestParam String graph) {
        logger.info("Calling service to delete a complete graph");

        processExecutionService.deleteGraph(graph);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
