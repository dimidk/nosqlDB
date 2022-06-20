package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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

public class MasterDB extends PrimitiveDatabase {

    private static Logger logger = LogManager.getLogger(PrimitiveDatabase.class);
    
    public MasterDB(InitialService server) {
        super(server);
    }
    
    
    public void addPropertyIndex(Student stud) {
        
        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        if (this.getPropertyIndex().containsKey(stud.getSurname()))
            this.getPropertyIndex().get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        else {
            this.getPropertyIndex().put(stud.getSurname(),new ArrayList<>());
            this.getPropertyIndex().get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        }

    }

    public void deletePropertyIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        List<String> temp = this.getPropertyIndex().get(stud.getSurname());
        this.getPropertyIndex().remove(stud.getSurname(),temp);

        temp.remove(String.valueOf(stud.getUuid()));
        this.getPropertyIndex().put(stud.getSurname(),temp);
        //this.getPropertyIndex().remove(stud.getSurname());

    }

    public void addUniqueIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        this.getUniqueIndex().add(String.valueOf(stud.getUuid()));
    }

    public void deleteUniqueIndex(Student stud) {

        if (stud == null) {
            logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        this.getUniqueIndex().remove(String.valueOf(stud.getUuid()));
    }

    public boolean dbDirExists() {
        
        if (!Files.exists(Path.of(PrimitiveDatabase.DATABASE_DIR))) {
            return false;
        }
        return true;
    }

    public void createDbDir() {

        if (!dbDirExists()) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                Files.createDirectories(Path.of(PrimitiveDatabase.COLLECTION_DIR), fileAttributes);
                logger.info("create main database directory");

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void loadDatabase(String dir) throws IOException {

        dir = PrimitiveDatabase.COLLECTION_DIR;

        if (this.getPropertyIndex().size() == 0 || this.getUniqueIndex().size() == 0) {

        //    if (Files.exists(Path.of(PrimitiveDatabase.COLLECTION_DIR))) {
            if (Files.exists(Path.of(dir)))
                logger.info(Thread.currentThread().getName()+" satisfies request");
                logger.info("directory exists");
                
                //    List<Path> files = InitialService.getInitialService().listFiles(Path.of(InitialService.COLLECTION_DIR));
                List<Path> files = PrimitiveDatabase.server.listFiles(Path.of(dir));
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

}
