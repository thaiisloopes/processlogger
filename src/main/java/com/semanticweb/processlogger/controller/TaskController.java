package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Task;
import com.semanticweb.processlogger.service.TaskService;
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
@RequestMapping("/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

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
    public ResponseEntity recordAllTasks(@RequestBody List<Task> tasks) {
        logger.info("Calling service to record all tasks");

        List<ResourceCreationResponse> response = taskService.save(tasks);

        return ResponseEntity.status(CREATED).body(response);
    }
}
