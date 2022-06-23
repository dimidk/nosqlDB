package com.example.nosqldb.services;

import com.example.nosqldb.InitialService;
import com.example.nosqldb.PrimitiveDatabase;
import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@Primary

public class ManageCRUDServices implements ManagerInterface {

    @Autowired
    public static InitialService service;

    private Logger logger = LogManager.getLogger(ManageCRUDServices.class);

    @Autowired
    public ManageCRUDServices(InitialService service) {
        this.service = service;
    }

    public static InitialService getService() {
        return service;
    }

//    protected Student fromJson(String field) {
    protected Student fromJson(int field) {

        Student student = null;
        Gson json = new Gson();
        try (Reader reader = new FileReader(InitialService.COLLECTION_DIR+String.valueOf(field)+".json")) {

            student = json.fromJson(reader,Student.class);
            logger.info(student.getUuid());

        }catch (IOException e ){
            e.printStackTrace();
        }
        return student;
    }

    @Override
    //@Async
//    public CompletableFuture<List<Student>> read() {
    public List<Student> read() {

        List<Student> students = new ArrayList<>();

        logger.info("read uuid index for all students");

        TreeSet<Integer> uuids = service.getDatabase().getUniqueIndex();
        uuids.stream().sorted().forEach(s -> {
            logger.info(Thread.currentThread().getName());
            logger.info("read each student");
            Student student = fromJson(s);
            students.add(student);
        });

        return students;
    //    return CompletableFuture.completedFuture(students);
    }

    @Override
  //  @Async
//    public CompletableFuture<Student> read(String uuid) {
    public Student read(String uuid) {

        Student student = null;
        logger.info("read student");
        student = fromJson(Integer.valueOf(uuid));
        return student;
    //    return CompletableFuture.completedFuture(student);
    }

    @Override
    public List<Student> findStud(String name) {

        List<Student> students = new ArrayList<>();
        TreeMap<String,List<String>> propIndex = service.getDatabase().getPropertyIndex();
        List<String> uuids = propIndex.get(name);

        for(String uuid:uuids){
            logger.info("this is student with "+uuid);
            Student student = fromJson(Integer.valueOf(uuid));
            students.add(student);
        }
        return students;
    }

    @Override
    public List<Student> displayByName() {

        logger.info("read property index");
        logger.info("index loaded from ");

        List<Student> allByName = new ArrayList<>();

        TreeMap<String,List<String>> propIndex = service.getDatabase().getPropertyIndex();
        for (Map.Entry s:propIndex.entrySet()) {
            String name = (String) s.getKey();
            List<String> uuids = (List<String>) s.getValue();

            for (String uuid:uuids){
                Student student = fromJson(Integer.valueOf(uuid));
                allByName.add(student);
                logger.info(student.getUuid() +" "+student.getSurname());
            }
        }

        return allByName;
    }

}
