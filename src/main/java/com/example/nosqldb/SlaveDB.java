package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

public class SlaveDB extends Slave {

    private static Logger logger = LogManager.getLogger(SlaveDB.class);

    public SlaveDB(String slaveDir) {
        super(slaveDir);
    }

    @Override
    public void createDbDir() {


        String slavedbDir = this.getSlaveDir();
        slavedbDir = slavedbDir +"/"+Slave.COLLECTION_DIR;

        //if (!dbDirExists()) {
        if (!Files.exists(Path.of(Slave.DATABASE_DIR))) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                Files.createDirectories(Path.of(slavedbDir), fileAttributes);
                logger.info("create slave's database main directory");

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadDatabase(String dir) throws IOException {

        dir = this.getSlaveDir() + "/"+Slave.COLLECTION_DIR;
        this.masterDB.loadDatabase(dir);

    }
}
