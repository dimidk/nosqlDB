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
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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

        //dir = PrimitiveDatabase.COLLECTION_DIR;
        logger.info("directory to read from "+dir);

        TreeSet<String> tempTree = this.getUniqueIndex();
        TreeMap<String,List<String>> tempMap = this.getPropertyIndex();

        logger.info(tempTree.size());
        logger.info(tempMap.entrySet().size());

        if (this.getPropertyIndex().size() == 0 || this.getUniqueIndex().size() == 0) {

            if (Files.exists(Path.of(dir))) {
                logger.info("directory exists");
                List<Path> files = PrimitiveDatabase.server.listFiles(Path.of(dir));
                files.forEach(s -> {
                    Gson json = new Gson();
                    try (Reader reader = new FileReader(String.valueOf(s))) {
                        Student student = json.fromJson(reader, Student.class);

                        addPropertyIndex(student);
                        addUniqueIndex(student);
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
    }
    @Override
    public void createSlaveDB(String slaveDir) {

        //dir = slaveDb-1, slave-Db2 ....

        createDbDir();

        String copyFromDir = PrimitiveDatabase.DATABASE_DIR;
        String copyToDir = slaveDir + "/"+Slave.DATABASE_DIR;

        try (Stream<Path> stream = Files.walk(Path.of(copyFromDir))) {

            stream.forEach(source -> {
                try {
                    Files.copy(source, Path.of(copyToDir), REPLACE_EXISTING);
                    logger.info("file copied");
                }
                catch (IOException e ){
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
