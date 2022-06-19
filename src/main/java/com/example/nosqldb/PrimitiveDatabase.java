package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
@Component
public class PrimitiveDatabase implements Runnable{

    //this is the main object to be called at once and through thread

    private static Logger logger = LogManager.getLogger(PrimitiveDatabase.class);
    public static final String DATABASE_DIR = "db/";
    public static final String COLLECTION_DIR = "db/student/";

 //   @Autowired
    private static InitialService server;

    private TreeSet<String> uniqueIndex = new TreeSet<>();
    private TreeMap<String, List<String>> propertyIndex = new TreeMap<>();

    @Autowired
    public PrimitiveDatabase(InitialService server){
        logger.info(Thread.currentThread().getName());

        this.server = server;
    };

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

    public void createThread() {

        Thread nodeDB1 = new Thread(new PrimitiveDatabase(server));
        Thread nodeDB2 = new Thread(new PrimitiveDatabase(server));

        nodeDB1.start();
        nodeDB2.start();
    }

    public void loadDatabase() throws IOException {

        if (propertyIndex.size() == 0 || uniqueIndex.size() == 0) {

            if (Files.exists(Path.of(InitialService.COLLECTION_DIR))) {
                logger.info(Thread.currentThread().getName()+" satisfies request");
                logger.info("directory exists");

            //    List<Path> files = InitialService.getInitialService().listFiles(Path.of(InitialService.COLLECTION_DIR));
                List<Path> files = server.listFiles(Path.of(InitialService.COLLECTION_DIR));
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

    @Override
    public void run() {

        logger.info("threads instances for primitive database");

        createThread();
    }
}
