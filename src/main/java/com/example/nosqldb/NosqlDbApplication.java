package com.example.nosqldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NosqlDbApplication {

    //private static  InitialService server ;

    @Autowired
    static
    InitialService server = InitialService.getInitialService();

    public static void main(String[] args) {



        SpringApplication.run(NosqlDbApplication.class, args);


        server.createDbDir();


    }

}
