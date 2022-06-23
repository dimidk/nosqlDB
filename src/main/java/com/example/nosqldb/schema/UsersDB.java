package com.example.nosqldb.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


public class UsersDB implements Comparable{

    private static Integer userObj=0;

    @JsonProperty("uuid")
    int uuid;
    @JsonProperty("username")
    String username;
    @JsonProperty("password")
    String password;
    @JsonProperty("database")
    String database;

    public UsersDB(){}

    public UsersDB(String username, String password, String database) {
        this.uuid = userObj++;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public int compareTo(Object o) {

        return 0;
    }
}
