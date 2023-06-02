package com.semanticweb.processlogger.controllers;

import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.applications.ProcessApplication;
import com.semanticweb.processlogger.applications.resources.Process;
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
@RequestMapping("/processes")
public class ProcessController {

    private static final Logger logger = getLogger(ProcessController.class);

    @Autowired
    private ProcessApplication processApplication;

    @GetMapping
    public String getAllProcess(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded process");

        OutputStream stream = new ByteArrayOutputStream() ;
        processApplication.getProcess().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordProcess(@RequestBody Process process) throws URISyntaxException {
        logger.info("Calling service to record all processes");

        ResourceCreationResponse response = processApplication.save(process);

        return status(CREATED).body(response);
    }
}
