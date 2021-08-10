package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.domain.Process;
import com.semanticweb.processlogger.repository.ProcessRepository;
import com.semanticweb.processlogger.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @GetMapping
    @ResponseBody
    public List<Process> getAllProcess() {
        logger.info("Calling service to get all recorded process executions");
        return processService.getProcess();
    }

    @PostMapping
    public ResponseEntity recordAllProcess(@RequestBody List<Process> processes) {
        logger.info("Calling service to record all process executions");
        processService.record(processes);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
