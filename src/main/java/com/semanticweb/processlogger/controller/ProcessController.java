package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.service.ProcessService;
import com.semanticweb.processlogger.domain.Process;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/processes")
public class ProcessController {

    private static final Logger logger = getLogger(ProcessController.class);

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

        return status(CREATED).body(response);
    }
}
