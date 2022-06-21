package com.example.nosqldb.services;

import com.example.nosqldb.schema.Student;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ManagerInterface {

    //public CompletableFuture<List<Student>> read();
    public List<Student> read();
    //public CompletableFuture<Student> read(String uuid);
    public Student read(String uuid);

    public List<Student> findStud(String name);
    public List<Student> displayByName();
}
