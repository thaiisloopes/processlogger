package com.semanticweb.processlogger.controller;

import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.api.SelectQuery;
import com.semanticweb.processlogger.service.ProcessService;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultWriters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping
    public void getAllProcess() {
        System.out.println("Calling service to get all recorded process executions");
        processService.getProcess();
        System.out.println("Recorded process executions retrieved with success");
    }
}
