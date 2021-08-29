package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Agent;
import com.semanticweb.processlogger.domain.Equipment;
import com.semanticweb.processlogger.service.AgentService;
import com.semanticweb.processlogger.service.EquipmentService;
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
@RequestMapping("/agent")
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
    public ResponseEntity recordAllAgents(@RequestBody List<Agent> agents) {
        logger.info("Calling service to record all agents");

        List<ResourceCreationResponse> response = agentService.save(agents);

        return status(CREATED).body(response);
    }
}
