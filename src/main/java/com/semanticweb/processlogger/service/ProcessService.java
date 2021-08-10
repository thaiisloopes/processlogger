package com.semanticweb.processlogger.service;

import com.semanticweb.processlogger.domain.Process;
import com.semanticweb.processlogger.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    private ProcessRepository processRepository;

    public List<Process> getProcess() {
        String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select ?s ?p ?o where { ?s ?p ?o. }";

        return processRepository.get(queryString);
    }

    public void record(List<Process> processes) {
        processRepository.save(processes);
    }
}
