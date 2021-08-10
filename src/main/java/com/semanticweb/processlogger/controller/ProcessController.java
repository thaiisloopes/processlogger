package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.domain.Process;
import com.semanticweb.processlogger.repository.ProcessRepository;
import com.semanticweb.processlogger.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProcessController {

    @Autowired
    private ProcessService processService;

    private static final Logger logger = LoggerFactory.getLogger(ProcessRepository.class);

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public List<Process> getAllProcess() {
        logger.info("Calling service to get all recorded process executions");
        return processService.getProcess();
    }
}
