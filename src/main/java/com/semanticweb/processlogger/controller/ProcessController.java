package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.ProcessExecution;
import com.semanticweb.processlogger.service.ProcessExecutionService;
import com.semanticweb.processlogger.service.ProcessService;
import com.semanticweb.processlogger.domain.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessService processService;

    @GetMapping
    public String getAllProcess(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded process");

        OutputStream stream = new ByteArrayOutputStream() ;
        processService.getProcess().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordAllProcess(@RequestBody List<Process> processes) {
        logger.info("Calling service to record all processes");

        List<ResourceCreationResponse> response = processService.save(processes);

        return ResponseEntity.status(CREATED).body(response);
    }
}
