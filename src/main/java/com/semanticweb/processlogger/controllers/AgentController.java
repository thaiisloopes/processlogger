package com.semanticweb.processlogger.controllers;

import com.semanticweb.processlogger.controllers.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.applications.resources.Agent;
import com.semanticweb.processlogger.applications.AgentService;
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
@RequestMapping("/agents")
public class AgentController {

    private static final Logger logger = getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @GetMapping
    public String getAllAgent(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded agents");

        OutputStream stream = new ByteArrayOutputStream() ;
        agentService.getAgents().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordAgent(@RequestBody Agent agent) throws URISyntaxException {
        logger.info("Calling service to record an agent");

        ResourceCreationResponse response = agentService.save(agent);

        return status(CREATED).body(response);
    }
}
