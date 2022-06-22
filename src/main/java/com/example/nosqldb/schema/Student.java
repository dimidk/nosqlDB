package com.example.nosqldb.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class Student implements Comparable{

    private static Integer studObj = 0;

    private int uuid;
    private String surname;
    private String grade;

    public Student(String surname, String grade) {

        this.uuid = studObj++;
        this.surname = surname;
        this.grade = grade;
    }

    public static Integer getStudObj() {
        return studObj;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
