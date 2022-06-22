package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SlaveDB extends Slave {

    private static Logger logger = LogManager.getLogger(SlaveDB.class);

    public SlaveDB(String slaveDir) {
        super(slaveDir);
    }

    @Override
    public void createDbDir() {

        String slavedbDir = this.getSlaveDir();
        String dbdir = slavedbDir + "/" +Slave.DATABASE_DIR;
        String collectionDir = slavedbDir + "/"+Slave.COLLECTION_DIR;
        logger.info("create directories to Slave Database");

        if (!Files.exists(Path.of(dbdir))) {

            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                Files.createDirectories(Path.of(dbdir),fileAttributes);
                Files.createDirectories(Path.of(collectionDir), fileAttributes);
                logger.info("create slave's database main directory");

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            logger.info("directory exists");
    }

    @Override
    public void loadDatabase(String dir) throws IOException {

        dir = this.getSlaveDir() + "/"+Slave.COLLECTION_DIR;
        this.masterDB.loadDatabase(dir);

    }


    public void createSlaveDB(String slaveDir) {

        //dir = slaveDb-1, slave-Db2 ....

    //    if (!Files.exists(Path.of(slaveDir+"/"+Slave.COLLECTION_DIR))) {
            logger.info(slaveDir+"/"+Slave.COLLECTION_DIR);

            createDbDir();
            String copyFromDir = PrimitiveDatabase.DATABASE_DIR;
            try (Stream<Path> stream = Files.walk(Path.of(copyFromDir))) {

                stream.forEach(source -> {
                    try {
                        logger.info("copy "+source);
                        String copyToDir = new String(this.getSlaveDir() + "/" + source);
                        logger.info("to directory "+copyToDir);
                        Files.copy(source, Path.of(copyToDir), REPLACE_EXISTING);
                        logger.info("file copied");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    //    }
    }
}
