package com.semanticweb.processlogger.domain.applications;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.inbound.resources.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.resources.Equipment;
import com.semanticweb.processlogger.domain.resources.Triple;
import com.semanticweb.processlogger.infrastructure.repository.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EquipmentService {

    private static final Logger logger = getLogger(EquipmentService.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String THING_CLASS_URI = "https://schema.org/Thing";
    private static final String SCHEMA_IDENTIFIER_PROPERTY_URI = "https://schema.org/identifier";
    private static final String SCHEMA_DESCRIPTION_PROPERTY_URI = "https://schema.org/description";

    @Autowired
    private TripleRepository repository;

    public Model getEquipments() {
        logger.info("Calling repository to get all recorded equipments");

        String queryString = "select ?s ?p ?o where { ?s ?p ?o. }";

        Model model = ModelFactory.createDefaultModel();
        List<Triple> triples = repository.get(queryString);
        triples.forEach(
                triple -> {
                    Resource resource = model.createResource(triple.getResource());
                    Property property = model.createProperty(triple.getProperty());
                    model.add(resource, property, triple.getValue());
                }
        );

        return model;
    }

    public ResourceCreationResponse save(Equipment equipment) throws URISyntaxException {
        logger.info("Calling repository to save an equipment");

        List<Triple> equipmentToTriples = buildEquipmentTriples(equipment);

        repository.save(equipmentToTriples);

        return buildResourceCreationResponse(equipmentToTriples);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private List<Triple> buildEquipmentTriples(Equipment equipment) {
        String resourceUri = "http://www.example.com/equipments/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, THING_CLASS_URI),
                buildTriple(resourceUri, SCHEMA_IDENTIFIER_PROPERTY_URI, equipment.getCode()),
                buildTriple(resourceUri, SCHEMA_DESCRIPTION_PROPERTY_URI, equipment.getDescription())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
