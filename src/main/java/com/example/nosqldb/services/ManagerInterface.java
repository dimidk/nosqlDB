package com.example.nosqldb.services;

import com.example.nosqldb.schema.Student;

import java.io.IOException;
import java.util.List;

public interface ManagerInterface {

    public List<Student> read();
    public Student read(String uuid);

    public List<Student> findStud(String name);
    public List<Student> displayByName();
}
