package com.semanticweb.processlogger.repository;

import com.stardog.ext.spring.SnarlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.semanticweb.processlogger.domain.Process;

import java.util.List;
import static java.util.Objects.*;

@Repository
public class ProcessExecutionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionRepository.class);

    @Autowired
    SnarlTemplate snarlTemplate;

    public List<Process> get(String queryString) {
        logger.info("Calling StarDog from SnarlTemplate to get all triples");
        return snarlTemplate.query(queryString, bindingSet -> new Process(
                requireNonNull(bindingSet.get("s")).toString(),
                requireNonNull(bindingSet.get("p")).toString(),
                requireNonNull(bindingSet.get("o")).toString()
        ));
    }

    public void save(List<List<Process>> processes) {
        logger.info("Calling StarDog from SnarlTemplate to save a list of process triples");
        processes.forEach(
                process -> process.forEach( process1 ->
                        snarlTemplate.add(
                                process1.getResource(),
                                process1.getProperty(),
                                process1.getValue()
                        )
                )
        );
    }

    public void deleteManyTriples(List<List<Process>> processes, String graph) {
        logger.info("Calling StarDog from SnarlTemplate to delete many process triples");
        processes.forEach(
                process -> process.forEach(process1 ->
                        snarlTemplate.remove(
                                process1.getResource(),
                                process1.getProperty(),
                                process1.getValue(),
                                graph
                        )
                )
        );
    }

    public void deleteGraph(String graph) {
        logger.info("Calling StarDog grom SnarlTemplate to delete all process triples from a graph");
        snarlTemplate.remove(graph);
    }
}
