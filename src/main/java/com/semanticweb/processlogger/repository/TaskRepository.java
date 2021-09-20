package com.semanticweb.processlogger.repository;

import com.semanticweb.processlogger.domain.Triple;
import com.stardog.ext.spring.SnarlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
public class TaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);

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

    public void save(List<Triple> triples) {
        logger.info("Calling StarDog from SnarlTemplate to save a task");

        triples.forEach(
                triple ->
                        snarlTemplate.add(
                                triple.getResource(),
                                triple.getProperty(),
                                triple.getValue()
                        )
        );
    }
}
