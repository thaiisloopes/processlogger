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
public class ProcessRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProcessRepository.class);

    @Autowired
    SnarlTemplate snarlTemplate;

    public List<Process> get(String queryString) {
        return snarlTemplate.query(queryString, bindingSet -> new Process(
                requireNonNull(bindingSet.get("s")).toString(),
                requireNonNull(bindingSet.get("p")).toString(),
                requireNonNull(bindingSet.get("o")).toString()
        ));
    }

    public void save(List<Process> processes) {
        processes.forEach(
                process -> snarlTemplate.add(
                        process.getResource(),
                        process.getProperty(),
                        process.getValue()
                )
        );
    }
}
