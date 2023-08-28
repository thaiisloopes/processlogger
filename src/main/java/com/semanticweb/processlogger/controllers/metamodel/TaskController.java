package com.semanticweb.processlogger.controllers.metamodel;

import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.applications.metamodel.resources.Task;
import com.semanticweb.processlogger.applications.metamodel.TaskApplication;
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
@RequestMapping("/processes/{processId}/tasks")
public class TaskController {
    private static final Logger logger = getLogger(TaskController.class);

    @Autowired
    private TaskApplication taskApplication;

    @GetMapping
    public String getAll(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all tasks");

        OutputStream stream = new ByteArrayOutputStream() ;
        taskApplication.getTasks().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity save(
            @RequestBody Task task ,
            @PathVariable String processId
    ) throws URISyntaxException {
        logger.info("Saving task");

        ResourceCreationResponse response = taskApplication.save(task, processId);

        return status(CREATED).body(response);
    }
}
