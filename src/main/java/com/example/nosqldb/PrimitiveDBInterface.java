package com.example.nosqldb;

import com.example.nosqldb.schema.Student;

public interface PrimitiveDBInterface {

    public void addPropertyIndex(Student stud);
    public void deletePropertyIndex(Student stud);
    public void addUniqueIndex(Student stud);
    public void deleteUniqueIndex(Student stud);
}
