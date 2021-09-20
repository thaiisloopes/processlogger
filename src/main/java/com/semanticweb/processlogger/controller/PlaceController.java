package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Equipment;
import com.semanticweb.processlogger.domain.Place;
import com.semanticweb.processlogger.service.EquipmentService;
import com.semanticweb.processlogger.service.PlaceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/places")
public class PlaceController {
    private static final Logger logger = getLogger(PlaceController.class);

    @Autowired
    private PlaceService placeService;

    @GetMapping
    public String getAllPlaces(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded places");

        OutputStream stream = new ByteArrayOutputStream() ;
        placeService.getPlaces().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordPlace(@RequestBody Place place) {
        logger.info("Calling service to record a place");

        ResourceCreationResponse response = placeService.save(place);

        return status(CREATED).body(response);
    }
}
