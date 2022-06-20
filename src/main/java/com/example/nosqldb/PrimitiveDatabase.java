package com.example.nosqldb;


import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class PrimitiveDatabase implements PrimitiveDBInterface{

    public static final String DATABASE_DIR = "db/";
    public static final String COLLECTION_DIR = "db/student/";
    protected static InitialService server;

    private TreeSet<String> uniqueIndex = new TreeSet<>();
    private TreeMap<String, List<String>> propertyIndex = new TreeMap<>();

    public PrimitiveDatabase(InitialService server) {

        this.server = server;
    }

    public TreeSet<String> getUniqueIndex() {
        return uniqueIndex;
    }

    public void setUniqueIndex(TreeSet<String> uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public  TreeMap<String,List<String>> getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(TreeMap<String,List<String>> propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public abstract void createDbDir();

    public abstract void loadDatabase(String dir) throws IOException ;

}
