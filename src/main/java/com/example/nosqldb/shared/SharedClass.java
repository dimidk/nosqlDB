package com.example.nosqldb.shared;

import com.example.nosqldb.schema.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.TreeSet;

@Component
public class SharedClass {

    private static Logger logger = LogManager.getLogger(SharedClass.class);
    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    public SharedClass(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    };

    public static int checkForDocuments(TreeSet<String> index) {

        int numOfDocuments = index.size();
        return numOfDocuments++;
    }

    public  List<Student> makeRestTemplateRequest(String restUrl) {

        logger.info("make Rest Template Request");
        ResponseEntity<List<Student>> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseEntity = restTemplate.exchange("http://localhost:9080/"+restUrl,
                //    responseEntity = restTemplate.exchange("http://slavedb:9080/read",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = responseEntity.getBody();
        //     List<Student> students = (List<Student>) restTemplate.getForObject("http://localhost:9080/read",Student.class);
        //    responseEntity = restTemplate.getForEntity("http://dbsrv-app:8060/course/"+courseName,StudGrades[].class);

        /*HttpStatus status = null;
        if ( students != null) {
            status = HttpStatus.OK;
        }
        else
            status = HttpStatus.BAD_REQUEST;*/

        return students;
    }
    public static HttpStatus returnStatus(List<Student> students) {

        logger.info("return status request");
        if (students != null)
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;

    }


}
