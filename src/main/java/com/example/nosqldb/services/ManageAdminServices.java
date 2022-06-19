package com.example.nosqldb.services;

import com.example.nosqldb.InitialService;
import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

@Component
//public class ManageAdminServices implements ManagerInterface {
public class ManageAdminServices extends ManageCRUDServices {

    @Autowired
    private  InitialService service ;

    private Logger logger = LogManager.getLogger(ManageAdminServices.class);
    @Autowired
    public ManageAdminServices(InitialService service) {
 //   public ManageAdminServices() {

 //      this.service = InitialService.getInitialService();
        super(service);

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

    public  void write(Student student) {


    //    if (this.getService() == null)
        if (service == null)
            System.exit(-1);

        logger.info("get a student object");
    //    if (!this.getService().dbDirExists()) {
        if (!service.dbDirExists()) {
            service.createDbDir();
    //        this.getService().createDbDir();
        }
        Gson json = new Gson();
        String filename = String.valueOf(student.getUuid());
        synchronized (this) {
            try (Writer writer = new FileWriter(InitialService.COLLECTION_DIR + filename + ".json")) {
                json.toJson(student, writer);
                logger.info("write new student to db");
                service.addUniqueIndex(student);
                service.addPropertyIndex(student);

                //    this.getService().addUniqueIndex(student);
                //    this.getService().addPropertyIndex(student);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }


    public void update(String field) {

        //update wants two parameters one uuid and one the update field

        Student student = null;
        student = fromJson(field);
        student.setGrade(field);

    }


    public synchronized void delete(String uuid) throws IOException {

        Student student = null;
        student = fromJson(uuid);

        service.deleteUniqueIndex(student);
        service.deletePropertyIndex(student);
    //    this.getService().deleteUniqueIndex(student);
    //    this.getService().deletePropertyIndex(student);
        Files.delete(Path.of(InitialService.COLLECTION_DIR + uuid + ".json"));
    }

    public synchronized void export(String dbName) {

        logger.info("export database");

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
        try {
            String filename = InitialService.DATABASE_DIR + "datafile.txt";
            if (!Files.exists(Path.of(filename)))
                Files.createFile(Path.of(filename),fileAttributes);

            TreeSet<String> uniqIndex = service.getUniqueIndex();
            TreeMap<String,List<String>> propIndex = service.getPropertyIndex();

        //    TreeSet<String> uniqIndex = this.getService().getUniqueIndex();
        //    TreeMap<String,List<String>> propIndex = this.getService().getPropertyIndex();

            logger.info("write each record to datafile");
            Files.write(Paths.get(filename), "create database file\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(filename), "Documents\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(filename), InitialService.COLLECTION_DIR.getBytes(), StandardOpenOption.APPEND);
            uniqIndex.stream().sorted().forEach(s -> {

                Student student = fromJson(s);
                Gson json = new Gson();
                String jsonString = json.toJson(student);
                try {
                    Files.write(Paths.get(filename), jsonString.getBytes(), StandardOpenOption.APPEND);
                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            logger.info("write unique index in datafile");
            Files.write(Paths.get(filename), "Unique Index of Documents\n".getBytes(), StandardOpenOption.APPEND);
            uniqIndex.stream().sorted().forEach(s -> {

                try {
                    Files.write(Paths.get(filename), s.getBytes(), StandardOpenOption.APPEND);
                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            logger.info("write property index in datafile");
            Files.write(Paths.get(filename), "Property Index of Documents\n".getBytes(), StandardOpenOption.APPEND);
            for (Map.Entry s:propIndex.entrySet()) {
                String name = (String) s.getKey();
                List<String> uuids = (List<String>) s.getValue();

                for (String uuid: uuids) {
                    try {
                        Files.write(Paths.get(filename), name.getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(filename), "\t".getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(filename), uuid.getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void import_db(String dbName){

        logger.info("try to load/import database");

        try {
            service.loadDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
