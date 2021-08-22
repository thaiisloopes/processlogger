package com.semanticweb.processlogger.repository;

import com.semanticweb.processlogger.domain.Triple;
import com.stardog.ext.spring.SnarlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcessRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProcessRepository.class);

    @Autowired
    SnarlTemplate snarlTemplate;

    public void save(List<List<Triple>> processes) {
        logger.info("Calling StarDog from SnarlTemplate to save a process");
        processes.forEach(
                process -> process.forEach(triple1 ->
                        snarlTemplate.add(
                                triple1.getResource(),
                                triple1.getProperty(),
                                triple1.getValue()
                        )
                )
        );
    }
}
