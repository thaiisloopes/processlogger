package com.semanticweb.processlogger.controllers.execution;

import com.semanticweb.processlogger.applications.execution.ArgumentApplication;
import com.semanticweb.processlogger.applications.execution.resources.Argument;
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
@RequestMapping("/arguments")
public class ArgumentController {
    private static final Logger logger = getLogger(ArgumentController.class);

    @GetMapping("/{argumentId}")
    public String get(
            @RequestHeader("Accept") String accept,
            @PathVariable String argumentId
    ) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Getting argument");

        OutputStream stream = new ByteArrayOutputStream() ;
        argumentApplication.getById(argumentId).write(stream, format);

        return stream.toString();
    }

    @Autowired
    private ArgumentApplication argumentApplication;

    @PostMapping
    public ResponseEntity save(@RequestBody Argument argument) throws URISyntaxException {
        logger.info("Saving argument");

        ResourceCreationResponse response = argumentApplication.save(argument);

        return status(CREATED).body(response);
    }
}
