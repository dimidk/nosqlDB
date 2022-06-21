package com.example.nosqldb;


import com.example.nosqldb.schema.Student;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

//public abstract class PrimitiveDatabase implements PrimitiveDBInterface{
public abstract class PrimitiveDatabase {

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

    public void addPropertyIndex(Student stud) {

        if (stud == null) {
        //    logger.info("ERROR");
            throw new IllegalArgumentException();
        }
    //    if (this.getPropertyIndex().containsKey(stud.getSurname()))
        if (propertyIndex.containsKey(stud.getSurname()))
        //    this.getPropertyIndex().get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
            propertyIndex.get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        else {
        //    this.getPropertyIndex().put(stud.getSurname(),new ArrayList<>());
        //    this.getPropertyIndex().get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
            propertyIndex.put(stud.getSurname(),new ArrayList<>());
            propertyIndex.get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        }
    }

    public void deletePropertyIndex(Student stud) {

        if (stud == null)
            throw new IllegalArgumentException();

        List<String> temp = this.getPropertyIndex().get(stud.getSurname());
        propertyIndex.remove(stud.getSurname(),temp);
        //this.getPropertyIndex().remove(stud.getSurname(),temp);

        temp.remove(String.valueOf(stud.getUuid()));
        propertyIndex.put(stud.getSurname(),temp);
    //    this.getPropertyIndex().put(stud.getSurname(),temp);
        //this.getPropertyIndex().remove(stud.getSurname());
    }

    public void addUniqueIndex(Student stud) {

        if (stud == null) {
            throw new IllegalArgumentException();
        }
        //this.getUniqueIndex().add(String.valueOf(stud.getUuid()));

        uniqueIndex.add(String.valueOf(stud.getUuid()));

    }

    public void deleteUniqueIndex(Student stud) {


        if (stud == null) {
            throw new IllegalArgumentException();
        }
        //this.getUniqueIndex().remove(String.valueOf(stud.getUuid()));
        uniqueIndex.remove(String.valueOf(stud.getUuid()));

    }
    public abstract void createDbDir();

    public abstract void loadDatabase(String dir) throws IOException ;

}
