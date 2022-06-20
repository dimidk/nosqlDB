package com.example.nosqldb.services;

import com.example.nosqldb.InitialService;
import com.example.nosqldb.PrimitiveDatabase;
import com.example.nosqldb.controllers.CRUDControllers;
import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Component
@Primary

public class ManageCRUDServices implements ManagerInterface {

    @Autowired
    public static InitialService service;
    public static PrimitiveDatabase database;

    private Logger logger = LogManager.getLogger(ManageCRUDServices.class);

    @Autowired
    public ManageCRUDServices(InitialService service,PrimitiveDatabase database) {
//    public ManageCRUDServices() {
        logger.info("ManageCRUDServices for primitive database "+Thread.currentThread().getName() +
                " "+Thread.currentThread().getId());
        this.service = service;
        this.database = database;
    }

    public static InitialService getService() {
        return service;
    }

    protected Student fromJson(String field) {

        Student student = null;
        Gson json = new Gson();
        try (Reader reader = new FileReader(InitialService.COLLECTION_DIR+field+".json")) {
            student = json.fromJson(reader,Student.class);
            logger.info(student.getUuid());
            //students.add(student);

        }catch (IOException e ){
            e.printStackTrace();
        }
        return student;
    }

    @Override
    public List<Student> read() {

        List<Student> students = new ArrayList<>();

        logger.info("read uuid index for all students");
        TreeSet<String> uuids = database.getUniqueIndex();
        uuids.stream().sorted().forEach(s -> {
            logger.info("read each student");
            Student student = fromJson(s);
            students.add(student);
        });

        return students;
    }

    @Override
    public Student read(String uuid) {

        Student student = null;
        logger.info("read student");
        student = fromJson(uuid);
        return student;
    }

    @Override
    public List<Student> findStud(String name) {

        List<Student> students = new ArrayList<>();
        TreeMap<String,List<String>> propIndex = database.getPropertyIndex();
        List<String> uuids = propIndex.get(name);
        /*uuids.forEach(uuid->{
            logger.info("this is student with "+uuid);
            Student student = fromJson(uuid);
            students.add(student);
        });*/
        for(String uuid:uuids){
            logger.info("this is student with "+uuid);
            Student student = fromJson(uuid);
            students.add(student);
        }


        return students;
    }

    @Override
    public List<Student> displayByName() {

        List<Student> allByName = new ArrayList<>();
        TreeMap<String,List<String>> propIndex = database.getPropertyIndex();
        for (Map.Entry s:propIndex.entrySet()) {
            String name = (String) s.getKey();
            List<String> uuids = (List<String>) s.getValue();

            for (String uuid:uuids){
                Student student = fromJson(uuid);
                allByName.add(student);
                logger.info(student.getUuid() +" "+student.getSurname());
            }
        }

        return allByName;
    }

}
