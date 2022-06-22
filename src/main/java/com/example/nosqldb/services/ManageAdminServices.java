package com.example.nosqldb.services;

import com.example.nosqldb.InitialService;
import com.example.nosqldb.PrimitiveDatabase;
import com.example.nosqldb.Slave;
import com.example.nosqldb.SlaveDB;
import com.example.nosqldb.schema.Student;
import com.example.nosqldb.shared.SharedClass;
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
        super(service);
    }

    protected Student fromJson(String field) {

        Student student = null;
        Gson json = new Gson();
        try (Reader reader = new FileReader(InitialService.COLLECTION_DIR+field+".json")) {
            student = json.fromJson(reader,Student.class);
            logger.info(student.getUuid());

        }catch (IOException e ){
            e.printStackTrace();
        }
        return student;
    }

    public  void write(Student student) {

        if (service == null)
            System.exit(-1);

        logger.info("get a student object");
        if (!service.dbDirExists())
            service.createDbDir();
        Gson json = new Gson();
        int objNum = SharedClass.checkForDocuments(service.getDatabase().getUniqueIndex());

        String filename = String.valueOf(objNum);
    //    synchronized (this) {
            try (Writer writer = new FileWriter(PrimitiveDatabase.COLLECTION_DIR + filename + ".json")) {
                student.setUuid(objNum);
                json.toJson(student, writer);
                logger.info("write new student to db");
                service.getDatabase().addUniqueIndex(student);
                service.getDatabase().addPropertyIndex(student);
            } catch (IOException e) {
                e.printStackTrace();

            }
    //        this.notifyAll();
    //    }

    }


    public void update(String field) {

        //update wants two parameters one uuid and one the update field

        Student student = null;
        student = fromJson(field);
        student.setGrade(field);

    }


    public  void delete(String uuid) throws IOException {

        Student student = null;
        student = fromJson(uuid);


        service.getDatabase().deletePropertyIndex(student);
        service.getDatabase().deleteUniqueIndex(student);
        Files.delete(Path.of(InitialService.COLLECTION_DIR + uuid + ".json"));
    }

    public  void export(String dbName) {

        logger.info("export database");

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
        try {
            String filename = InitialService.DATABASE_DIR + "datafile.txt";
            if (!Files.exists(Path.of(filename)))
                Files.createFile(Path.of(filename),fileAttributes);

            TreeSet<String> uniqIndex = service.getDatabase().getUniqueIndex();
            TreeMap<String,List<String>> propIndex = service.getDatabase().getPropertyIndex();

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
            service.getDatabase().createDbDir();
            String dir = PrimitiveDatabase.COLLECTION_DIR;
            service.getDatabase().loadDatabase(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void replicate(String slaveDb){

        logger.info("try to replicate database");
        Slave slaveDatabase = new SlaveDB(slaveDb);
        slaveDatabase.createDbDir();
        slaveDatabase.createSlaveDB(slaveDb);

    }

}
