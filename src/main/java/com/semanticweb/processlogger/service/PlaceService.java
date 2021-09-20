package com.semanticweb.processlogger.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Place;
import com.semanticweb.processlogger.domain.Triple;
import com.semanticweb.processlogger.repository.TripleRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PlaceService {

    private static final Logger logger = getLogger(PlaceService.class);
    private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    private static final String SCHEMA_DESCRIPTION_PROPERTY_URI = "https://schema.org/description";
    public static final String SCHEMA_PLACE_CLASS_URI = "https://schema.org/Place";

    @Autowired
    private TripleRepository repository;

    public Model getPlaces() {
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

    public ResourceCreationResponse save(Place place) {
        logger.info("Calling repository to save a place");

        List<Triple> placeToTriples = buildPlaceTriples(place);

        repository.save(placeToTriples);

        return buildResourceCreationResponse(placeToTriples);
    }

    private ResourceCreationResponse buildResourceCreationResponse(List<Triple> triples) {
        return new ResourceCreationResponse(triples.get(0).getResource());
    }

    private List<Triple> buildPlaceTriples(Place place) {
        String resourceUri = "http://www.example.com/places/" + UlidCreator.getUlid();

        return asList(
                buildTriple(resourceUri, RDF_TYPE_URI, SCHEMA_PLACE_CLASS_URI),
                buildTriple(resourceUri, SCHEMA_DESCRIPTION_PROPERTY_URI, place.getDescription())
        );
    }

    private Triple buildTriple(String resource, String property, String value) {
        return new Triple(resource, property, value);
    }
}
