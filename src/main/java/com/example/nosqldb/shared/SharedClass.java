package com.example.nosqldb.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeSet;

public class SharedClass {

    private static Logger logger = LogManager.getLogger(SharedClass.class);

    public SharedClass(){};

    public static int checkForDocuments(TreeSet<String> index) {

        int numOfDocuments = index.size();
        return numOfDocuments++;
    }



}
