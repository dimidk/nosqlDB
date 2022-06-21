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
import java.util.concurrent.CompletableFuture;

@RestController
@Configuration
public class CRUDControllers {

    @Autowired
    private ManageCRUDServices services;
  //  @Autowired
  //  private ManageAdminServices adminServices;

    private Logger logger = LogManager.getLogger(CRUDControllers.class);

    @GetMapping("/read")
//    public CompletableFuture<List<Student>> read() {
    public List<Student> read() {

        logger.info("read all students");
    //    CompletableFuture<List<Student>> students = services.read();
        List<Student> students = services.read();
       // return CompletableFuture.completedFuture(students);
        return students;

    }

    @GetMapping("/read/{uuid}")
    //public CompletableFuture<Student> read(@PathVariable String uuid) {
    public Student read(@PathVariable String uuid) {

        logger.info("read for uuid"+uuid);
    //    CompletableFuture<Student> student = services.read(uuid);
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


}
