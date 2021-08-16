package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.domain.ProcessExecution;
import com.semanticweb.processlogger.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @GetMapping
    public String getAllProcess() {
        logger.info("Calling service to get all recorded process executions");

        OutputStream stream = new ByteArrayOutputStream() ;
        processService.getProcess().write(stream, "TURTLE");

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordAllProcess(@RequestBody List<ProcessExecution> processes) {
        logger.info("Calling service to record all process executions");

        processService.record(processes);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteProcess(@RequestBody List<ProcessExecution> processes,
                                        @RequestParam String graph) {
        logger.info("Calling service to delete a list of process execution");

        processService.deleteProcessExecutionFrom(processes, graph);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/graph")
    public ResponseEntity deleteGraph(@RequestParam String graph) {
        logger.info("Calling service to delete a complete graph");

        processService.deleteGraph(graph);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
