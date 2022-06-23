package com.example.nosqldb;

import com.example.nosqldb.schema.Student;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationDataSourceScriptDatabaseInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

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

    private static InitialService initialThread;
    @Autowired
    private PrimitiveDatabase database;

    private InitialService() {
    }

    public static InitialService getInitialService() {

        if (initialThread == null) {
            initialThread = new InitialService();

        }
        else {
            logger.info("There is already an instance");
            throw new IllegalArgumentException();
        }

        return initialThread;
    }

    public PrimitiveDatabase getDatabase() {
        return database;
    }

    public void setDatabase(PrimitiveDatabase database) {
        this.database = database;
    }

    public List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
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

        /*Thread mainNode = new Thread(new PrimitiveDatabase(initialThread));
        mainNode.start();
        logger.info(mainNode.getName());*/
    }

}
