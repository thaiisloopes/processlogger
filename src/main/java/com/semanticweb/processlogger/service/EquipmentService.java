package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Equipment;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.repository.EquipmentRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EquipmentService {

    private static final Logger logger = getLogger(EquipmentService.class);
    private static final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String THING_CLASS_URI = "https://schema.org/Thing";
    private static final String SCHEMA_IDENTIFIER_PROPERTY_URI = "https://schema.org/identifier";
    private static final String SCHEMA_DESCRIPTION_PROPERTY_URI = "https://schema.org/description";

    @Autowired
    private EquipmentRepository equipmentRepository;

    public Model getEquipments() {
        logger.info("Calling repository to get all recorded equipments");

        String queryString = "select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = equipmentRepository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public List<ResourceCreationResponse> save(List<Equipment> equipments) {
        logger.info("Calling repository to save a list of equipments");

        List<List<Triple>> equipmentsToTriples = equipments.stream()
                .map(this::buildEquipmentTriples)
                .collect(toList());

        equipmentRepository.save(equipmentsToTriples);

        return buildResourceCreationResponse(equipmentsToTriples);
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

    private List<Triple> buildEquipmentTriples(Equipment equipment) {
        String resourceUri = "http://www.example.com/equipment/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_URI, THING_CLASS_URI),
                buildTriple(resourceUri, SCHEMA_IDENTIFIER_PROPERTY_URI, equipment.getCode()),
                buildTriple(resourceUri, SCHEMA_DESCRIPTION_PROPERTY_URI, equipment.getDescription())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
