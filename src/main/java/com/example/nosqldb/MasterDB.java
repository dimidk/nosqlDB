package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

public class MasterDB extends PrimitiveDatabase {

    private static Logger logger = LogManager.getLogger(PrimitiveDatabase.class);
    
    public MasterDB(InitialService server) {
        super(server);

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
        else
            logger.info("directory exists!!");
    }


    public void loadDatabase(String dir) throws IOException {

        dir = PrimitiveDatabase.COLLECTION_DIR;
        logger.info("directory to read from "+dir);

        TreeSet<String> tempTree = this.getUniqueIndex();
        TreeMap<String,List<String>> tempMap = this.getPropertyIndex();

        logger.info(tempTree.size());
        logger.info(tempMap.entrySet().size());

        if (this.getPropertyIndex().size() == 0 || this.getUniqueIndex().size() == 0) {
    //    if (tempTree.size() == 0 || tempMap.size() == 0) {

        //    if (Files.exists(Path.of(PrimitiveDatabase.COLLECTION_DIR))) {
            if (Files.exists(Path.of(dir))) {
                logger.info(Thread.currentThread().getName()+" satisfies request");
                logger.info("directory exists");
                
                //    List<Path> files = InitialService.getInitialService().listFiles(Path.of(InitialService.COLLECTION_DIR));
                List<Path> files = PrimitiveDatabase.server.listFiles(Path.of(dir));
                files.forEach(s -> {
                    Gson json = new Gson();
                    try (Reader reader = new FileReader(String.valueOf(s))) {
                        Student student = json.fromJson(reader, Student.class);

                    //    this.addPropertyIndex(student);
                    //    this.addUniqueIndex(student);

                        tempTree.add(String.valueOf(student.getUuid()));

                        addPropertyIndex(student);
                        addUniqueIndex(student);
                        logger.info(tempTree.first());
                        logger.info(tempMap.firstKey());
                        logger.info(getPropertyIndex().size());
                        logger.info(getUniqueIndex().size());

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        //this.setUniqueIndex(tempTree);
        setUniqueIndex(tempTree);
        for(String uuid:tempTree) {
            logger.info("this is from loaddatabase method to import unique index"+uuid);
        }
    }

}
