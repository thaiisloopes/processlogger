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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.jena.rdf.model.ModelFactory.createDefaultModel;
import static org.slf4j.LoggerFactory.getLogger;

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

    public List<ResourceCreationResponse> save(List<Agent> agents) {
        logger.info("Calling repository to save a list of agents");

        List<List<Triple>> agentsToTriples = agents.stream()
                .map(this::buildAgentTriples)
                .collect(toList());

        agentRepository.save(agentsToTriples);

        return buildResourceCreationResponse(agentsToTriples);
    }

    private List<ResourceCreationResponse> buildResourceCreationResponse(List<List<Triple>> triples) {
        List<ResourceCreationResponse> resources = new ArrayList<>();

        triples.forEach(
                tripleList -> tripleList.stream()
                        .findFirst()
                        .ifPresent(
                                triple -> resources.add(
                                        new ResourceCreationResponse(triple.getResource(), "")
                                )
                        )
        );

        return resources;
    }

    private List<Triple> buildAgentTriples(Agent agent) {
        String resourceUri = "http://www.example.com/agent/" + UlidCreator.getUlid();

        return asList(
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
