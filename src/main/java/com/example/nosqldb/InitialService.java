package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.integration.IntegrationDataSourceScriptDatabaseInitializer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component

public class InitialService {

    private static Logger logger = LogManager.getLogger(InitialService.class);

    /*
    make static fields enumeration
     */
    public static final String DATABASE_DIR = "db/";
    public static final String COLLECTION_DIR = "db/student/";


    private TreeSet<String> uniqueIndex = new TreeSet<>();

    //private Vector<Student> propertyIndex = new Vector<Student>();
    private TreeMap<String, List<String>> propertyIndex = new TreeMap<>();
    private static InitialService initialThread;

    private InitialService() {
    }

    public static InitialService getInitialService() {

        if (initialThread == null)
            initialThread = new InitialService();
        else {
            logger.info("There is already an instance");
            throw new IllegalArgumentException();
        }

        return initialThread;
    }



    public TreeSet<String> getUniqueIndex() {
        return uniqueIndex;
    }

    public void setUniqueIndex(TreeSet<String> uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public  TreeMap<String,List<String>> getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(TreeMap<String,List<String>> propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public void addPropertyIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        if (propertyIndex.containsKey(stud.getSurname()))
            propertyIndex.get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        else {
            propertyIndex.put(stud.getSurname(),new ArrayList<>());
            propertyIndex.get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        }

    }

    public void deletePropertyIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        List<String> temp = propertyIndex.get(stud.getSurname());
        propertyIndex.remove(stud.getSurname(),temp);

        temp.remove(String.valueOf(stud.getUuid()));
        propertyIndex.put(stud.getSurname(),temp);
        //propertyIndex.remove(stud.getSurname());

    }

    public void addUniqueIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        uniqueIndex.add(String.valueOf(stud.getUuid()));
    }

    public void deleteUniqueIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        uniqueIndex.remove(String.valueOf(stud.getUuid()));
    }

    protected List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public void loadDatabase() throws IOException {

        if (propertyIndex.size() == 0 || uniqueIndex.size() == 0) {
            
            if (Files.exists(Path.of(InitialService.COLLECTION_DIR))) {
                logger.info("directory exists");

                List<Path> files = listFiles(Path.of(InitialService.COLLECTION_DIR));
                /*files.forEach(s -> {
                    logger.info("this is for loading each file" + s);
                });*/
                files.forEach(s -> {
                    Gson json = new Gson();
                    try (Reader reader = new FileReader(String.valueOf(s))) {
                        Student student = json.fromJson(reader, Student.class);
                        addPropertyIndex(student);
                        addUniqueIndex(student);

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    public boolean dbDirExists() {

        if (!Files.exists(Path.of(InitialService.DATABASE_DIR))) {

            return false;
        }

        return true;
    }

    public void createDbDir() {

        if (!dbDirExists()) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                Files.createDirectories(Path.of(InitialService.COLLECTION_DIR), fileAttributes);
                logger.info("create main database directory");

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
