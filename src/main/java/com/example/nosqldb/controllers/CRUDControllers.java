package com.example.nosqldb.controllers;

import com.example.nosqldb.schema.Student;
import com.example.nosqldb.services.ManageAdminServices;
import com.example.nosqldb.services.ManageCRUDServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Configuration
public class CRUDControllers {

    @Autowired
    private ManageCRUDServices services;
  //  @Autowired
  //  private ManageAdminServices adminServices;

    private Logger logger = LogManager.getLogger(CRUDControllers.class);

    @GetMapping("/read")
    public List<Student> read() {

        logger.info("read all students");
        List<Student> students = services.read();
        return students;

    }

    @GetMapping("/read/{uuid}")
    public Student read(@PathVariable String uuid) {

        logger.info("read for uuid"+uuid);
        Student student = services.read(uuid);

        return student;
    }

    @GetMapping("/read/stud-name")
    public List<Student> read_name(){

        logger.info("read students by name");
        List<Student> lStud = services.displayByName();
        return lStud;
    }

    @GetMapping("/read/stud-name/{surname}")
    public List<Student> read_name(@PathVariable String surname){

        logger.info("read students with name");
        List<Student> students = services.findStud(surname);
        return students;
    }

    /*@PostMapping("/write")
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

    }*/
}
