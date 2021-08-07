package com.semanticweb.processlogger.service;

import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.api.SelectQuery;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultWriters;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    public void getProcess() {
//        String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select * where { ?person a foaf:Person. }";
//
//        SelectQuery select = connection.select(queryString);
//        try(SelectQueryResult aResult = select.execute()) {
//            System.out.println("The first ten results...");
//
//            QueryResultWriters.write(aResult, System.out, TextTableQueryResultWriter.FORMAT);
//        }
    }
}
