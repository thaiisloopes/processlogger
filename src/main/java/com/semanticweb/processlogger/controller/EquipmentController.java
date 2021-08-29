package com.semanticweb.processlogger.controller;

import com.semanticweb.processlogger.controller.response.ResourceCreationResponse;
import com.semanticweb.processlogger.domain.Equipment;
import com.semanticweb.processlogger.service.EquipmentService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private static final Logger logger = getLogger(EquipmentController.class);

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    public String getAllEquipment(@RequestHeader("Accept") String accept) {
        String format = (accept != null && accept.equals("text/turtle")) ? "TURTLE" : "RDF/XML-ABBREV";

        logger.info("Calling service to get all recorded equipments");

        OutputStream stream = new ByteArrayOutputStream() ;
        equipmentService.getEquipments().write(stream, format);

        return stream.toString();
    }

    @PostMapping
    public ResponseEntity recordAllEquipments(@RequestBody List<Equipment> equipments) {
        logger.info("Calling service to record all equipments");

        List<ResourceCreationResponse> response = equipmentService.save(equipments);

        return status(CREATED).body(response);
    }
}
