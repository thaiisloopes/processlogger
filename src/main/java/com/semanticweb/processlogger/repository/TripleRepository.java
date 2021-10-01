package com.semanticweb.processlogger.repository;

import com.semanticweb.processlogger.domain.Triple;
import com.stardog.ext.spring.SnarlTemplate;
import com.stardog.stark.Values;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import static java.util.Objects.*;
import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class TripleRepository {

    private static final Logger logger = getLogger(TripleRepository.class);

    @Autowired
    SnarlTemplate snarlTemplate;

    public List<Triple> get(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get all triples");
        return snarlTemplate.query(queryString, bindingSet -> new Triple(
                requireNonNull(bindingSet.get("s")).toString(),
                requireNonNull(bindingSet.get("p")).toString(),
                requireNonNull(bindingSet.get("o")).toString()
        ));
    }

    public void save(List<Triple> triples) throws URISyntaxException {
        logger.info("Calling StarDog from SnarlTemplate to save triples");
        triples.forEach(
                triple -> {
                    String value = triple.getValue();

                    try {
                        if(value.contains("http")) {
                            snarlTemplate.add(
                                    new URI(triple.getResource()),
                                    new URI(triple.getProperty()),
                                    new URI(value)
                            );
                        } else {
                            snarlTemplate.add(
                                    triple.getResource(),
                                    triple.getProperty(),
                                    value
                            );
                        }

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void deleteManyTriples(List<List<Triple>> triples, String graph) {
        logger.info("Calling StarDog from SnarlTemplate to delete many triples");
        triples.forEach(
                triple -> triple.forEach(triple1 ->
                        snarlTemplate.remove(
                                triple1.getResource(),
                                triple1.getProperty(),
                                triple1.getValue(),
                                graph
                        )
                )
        );
    }

    public void deleteGraph(String graph) {
        logger.info("Calling StarDog from SnarlTemplate to delete all triples from a graph");

        snarlTemplate.remove(graph);
    }
}
