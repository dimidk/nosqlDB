package com.example.nosqldb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync(proxyTargetClass=true )
public class NosqlDbApplication {

    //private static  InitialService server ;

    @Autowired
    static
    InitialService server = InitialService.getInitialService();
    @Bean
    MasterDB getDatabase() {
        return new MasterDB(server);
    }
    //PrimitiveDatabase database = new MasterDB(server);



    private static Logger logger = LogManager.getLogger(NosqlDbApplication.class);

  /*  @Bean
    public Executor taskExecutor() {
        logger.info("creating threads");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("DatabaseInstance-");
        executor.initialize();
        return executor;

    }*/


    public static void main(String[] args) {

        SpringApplication.run(NosqlDbApplication.class, args);

        /*(server.createDbDir();
        System.out.println("Thread finished");*/




    }

}
