package com.example.nosqldb.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UsersDB {

    String uuid;
    String username;
    String password;
    String database;

}
