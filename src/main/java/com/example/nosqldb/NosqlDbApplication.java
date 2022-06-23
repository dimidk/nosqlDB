package com.example.nosqldb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.Executor;

@SpringBootApplication
//@EnableAsync(proxyTargetClass=true )
public class NosqlDbApplication {

    //private static  InitialService server ;

    @Autowired
    static
    InitialService server = InitialService.getInitialService();

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    @Bean
    MasterDB getDatabase()  {
        MasterDB masterDB = new MasterDB(server);
        if (masterDB.dbDirExists()) {
            try  {
                masterDB.loadDatabase(MasterDB.COLLECTION_DIR);
                System.out.println("everything ok");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            masterDB.createDbDir();

        return masterDB;
    //    return new MasterDB(server);
    }
    //PrimitiveDatabase database = new MasterDB(server);



    private static Logger logger = LogManager.getLogger(NosqlDbApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(NosqlDbApplication.class, args);

        /*(server.createDbDir();
        System.out.println("Thread finished");*/




    }

}
