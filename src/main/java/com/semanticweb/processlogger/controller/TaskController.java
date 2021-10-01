package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Task;
import com.semanticweb.processlogger.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String getAllTasks(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded tasks");

        OutputStream stream = new ByteArrayOutputStream() ;
        taskService.getTasks().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordTask(@RequestBody Task task) throws URISyntaxException {
        logger.info("Calling service to record a task");

        ResourceCreationResponse response = taskService.save(task);

        return status(CREATED).body(response);
    }
}
