package com.example.nosqldb.controllers;

import com.example.nosqldb.schema.Student;
import com.example.nosqldb.services.ManageAdminServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Configuration
public class AdminControllers {

    @Autowired
   // private ManagerInterface adminServices;
    private ManageAdminServices adminServices;

    private Logger logger = LogManager.getLogger(AdminControllers.class);

    @GetMapping("/export/{dbName}")
    public void export(@PathVariable String dbName) {

        logger.info("export database");
        adminServices.export(dbName);

    }

    @GetMapping("/import/{dbName}")
    public void import_db(@PathVariable String dbName) {

        logger.info("import database");
        adminServices.import_db(dbName);

    }

    @PostMapping("/write")
    public void write(@RequestBody Student student) {

        logger.info("write for student:"+student);
        logger.info(student.getUuid()+" "+student.getSurname());
        if (adminServices == null) {
            logger.info("no normal exit");
            System.exit(-1);
        }
        adminServices.write(student);

    }


    @PutMapping("/update/{field}")
    public void update(@PathVariable String field) {

        logger.info("update certain student");
        adminServices.update(field);

    }


    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable String uuid) throws IOException {

        logger.info("delete certain student");
        adminServices.delete(uuid);

    }
    @GetMapping("/replica/{slaveDb}")
    public void replica(@PathVariable String slaveDb) {

        logger.info("create replication database");
        adminServices.replicate(slaveDb);

    }

}
