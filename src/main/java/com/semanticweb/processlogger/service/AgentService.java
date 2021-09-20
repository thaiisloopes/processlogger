package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Agent;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.repository.AgentRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.jena.rdf.model.ModelFactory.createDefaultModel;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AgentService {

    private static final Logger logger = getLogger(AgentService.class);

    @Autowired
    private AgentRepository agentRepository;

    public Model getAgents() {
        logger.info("Calling repository to get all recorded agents");

        String queryString = "select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = createDefaultModel();
        List<Triple> triples = agentRepository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public ResourceCreationResponse save(Agent agent) {
        logger.info("Calling repository to save an agent");

        List<Triple> agentToTriples = buildAgentTriples(agent);

        agentRepository.save(agentToTriples);

        return buildResourceCreationResponse(agentToTriples);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private List<Triple> buildAgentTriples(Agent agent) {
        String resourceUri = "http://www.example.com/agents/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, "http://www.w3.org/2000/01/rdf-schema#Class", "http://purl.org/NET/c4dm/event.owl#agent"),
                buildTriple(resourceUri, "http://www.w3.org/2000/01/rdf-schema#Class", "http://xmlns.com/foaf/0.1/Person"),
                buildTriple(resourceUri, "http://xmlns.com/foaf/0.1/name", agent.getName())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
