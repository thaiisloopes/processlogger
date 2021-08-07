package com.semanticweb.processlogger;

import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.api.*;
import com.stardog.stark.Resource;
import com.stardog.stark.Values;
import com.stardog.stark.IRI;
import com.stardog.stark.Literal;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultWriters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
public class ProcessLoggerApplication {

	public static void main(String[] args) throws Exception {
		ConnectionConfiguration aConn = ConnectionConfiguration
				.to("teste")
				.server("http://localhost:5820")
				.credentials("admin", "admin");
		Connection connection = aConn.connect();
		connection.begin();

		Resource aContext = Values.iri("urn:test:context");
		IRI iri = Values.iri("http://xmlns.com/foaf/0.1/name");
		Literal value = Values.literal("Thais2");

		connection.add()
				.statement(aContext, iri, value);
		connection.commit();

		String queryString = "prefix foaf: <http://xmlns.com/foaf/0.1/> select * where { ?person a foaf:Person. }";

        SelectQuery select = connection.select(queryString);
        try(SelectQueryResult aResult = select.execute()) {
            System.out.println("The first ten results...");

            QueryResultWriters.write(aResult, System.out, TextTableQueryResultWriter.FORMAT);
        }

		SpringApplication.run(ProcessLoggerApplication.class, args);
	}
}
