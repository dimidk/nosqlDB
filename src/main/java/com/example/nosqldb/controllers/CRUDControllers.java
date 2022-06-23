package com.example.nosqldb.controllers;

import com.example.nosqldb.schema.Student;
import com.example.nosqldb.services.ManageAdminServices;
import com.example.nosqldb.services.ManageCRUDServices;
import com.example.nosqldb.shared.SharedClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@Configuration
public class CRUDControllers {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ManageCRUDServices services;
    @Autowired
    private SharedClass sharedClass;

    private Logger logger = LogManager.getLogger(CRUDControllers.class);

    @RequestMapping(value="/read",method= RequestMethod.GET,
            consumes = {"*/*"})
    public HttpStatus read() {

        logger.info("return all students");

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read");
        status = SharedClass.returnStatus(students);


        /*ResponseEntity<List<Student>> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseEntity = restTemplate.exchange("http://localhost:9080/read",
    //    responseEntity = restTemplate.exchange("http://slavedb:9080/read",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = responseEntity.getBody();
   //     List<Student> students = (List<Student>) restTemplate.getForObject("http://localhost:9080/read",Student.class);
    //    responseEntity = restTemplate.getForEntity("http://dbsrv-app:8060/course/"+courseName,StudGrades[].class);*/

        /*if ( status != null) {
            status = HttpStatus.OK;
        }
        else
            status = HttpStatus.BAD_REQUEST;*/

        return status;
    }

    /*@GetMapping("/read")
    public List<Student> read() {

        logger.info("read all students");


        List<Student> students = services.read();
        return students;

    }*/

    @RequestMapping(value="/read/{uuid}",method= RequestMethod.GET,
            consumes = {"*/*"})
    public Student read(@PathVariable String uuid) {

        logger.info("return student with uuid:"+uuid);
        /*Student student = services.read(uuid);
        return student;*/

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read/"+uuid);
        status = SharedClass.returnStatus(students);

        /*ResponseEntity<Student> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseEntity = restTemplate.exchange("http://localhost:9080/read/"+uuid,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Student>() {}
        );
        Student student = responseEntity.getBody();

        HttpStatus status = null;
        if ( student != null) {
            status = HttpStatus.OK;
        }
        else {
            logger.info("there is an error in request");
            status = HttpStatus.BAD_REQUEST;
        }*/


        return students.get(0);
    }

    @RequestMapping(value="/read/stud-name",method= RequestMethod.GET,
            consumes = {"*/*"})
    public List<Student> read_name(){

        logger.info("read students by name");
        /*List<Student> lStud = services.displayByName();
        return lStud;*/

        /*ResponseEntity<List<Student>> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseEntity = restTemplate.exchange("http://localhost:9080/read/stud-name",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = responseEntity.getBody();

        HttpStatus status = null;
        if ( students != null) {
            status = HttpStatus.OK;
        }
        else {
            logger.info("There is an error in GET request for getting all students in alphabetical order");
            status = HttpStatus.BAD_REQUEST;
        }*/

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read/stud-name");
        status = SharedClass.returnStatus(students);


        return students;
    }

    @GetMapping("/read/stud-name/{surname}")
    public List<Student> read_name(@PathVariable String surname){

        logger.info("read students with name:"+surname);
        /*List<Student> students = services.findStud(surname);
        return students;*/

        /*ResponseEntity<List<Student>> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseEntity = restTemplate.exchange("http://localhost:9080/read/stud-name/"+surname,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = responseEntity.getBody();

        HttpStatus status = null;
        if ( students != null) {
            status = HttpStatus.OK;
        }
        else {
            logger.info("There is an error in GET request for getting all students in alphabetical order");
            status = HttpStatus.BAD_REQUEST;
        }*/

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read/stud-name/"+surname);
        status = SharedClass.returnStatus(students);

        return students;

    }
}
