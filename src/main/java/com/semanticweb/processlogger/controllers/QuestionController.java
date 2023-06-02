package com.semanticweb.processlogger.controllers;

import com.semanticweb.processlogger.applications.QuestionApplication;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/processes/{processId}")
public class QuestionController {

    private static final Logger logger = getLogger(QuestionController.class);

    @Autowired
    private QuestionApplication questionApplication;

    @GetMapping("/executions/{processExecutionId}/activities")
    public String getAllActivities(
            @RequestHeader("Accept") String accept,
            @PathVariable String processId,
            @PathVariable String processExecutionId
    ) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all activities");

        OutputStream stream = new ByteArrayOutputStream() ;
        questionApplication.getActivities(processId, processExecutionId).write(stream, format);

        return stream.toString();
    }

    @GetMapping("/executions")
    public String getExecutionWithTask(
            @RequestHeader("Accept") String accept,
            @RequestParam String taskId
    ) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all processes executions with given taskId");

        OutputStream stream = new ByteArrayOutputStream() ;
        questionApplication.getProcessesExecutionsWithTask(taskId).write(stream, format);

        return stream.toString();
    }

    @GetMapping("/tasks/{taskId}/objects")
    public String getObjectsFromTaskExecution(
            @RequestHeader("Accept") String accept,
            @PathVariable String taskId
    ) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all objects related to given taskId");

        OutputStream stream = new ByteArrayOutputStream() ;
        questionApplication.getObjectsFromTask(taskId).write(stream, format);

        return stream.toString();
    }
}