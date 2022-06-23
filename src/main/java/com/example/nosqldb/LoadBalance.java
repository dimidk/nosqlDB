package com.example.nosqldb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LoadBalance {
    private static Logger logger = LogManager.getLogger(LoadBalance.class);

    private  Queue<String> databaseInstance;

    private static LoadBalance loadBalance;

    private LoadBalance() {
        this.databaseInstance = new LinkedList<>();
    }


    public static LoadBalance getLoadBalance() {
        if (loadBalance == null)
            loadBalance = new LoadBalance();
        else {
            logger.info("There is already instance");
            throw new IllegalArgumentException();
        }
        return loadBalance;
    }

    public void roundRobin(String database) {


    }

}
