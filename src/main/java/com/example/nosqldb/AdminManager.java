package com.example.nosqldb;

import com.example.nosqldb.schema.UsersDB;
import com.example.nosqldb.shared.SharedClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminManager {

    private static Logger logger = LogManager.getLogger(AdminManager.class);

    private static AdminManager adminManager;

    private AdminManager(){}

    public static AdminManager getAdminManager() {
        if (adminManager == null)
            adminManager = new AdminManager();
        else {
            logger.info("There is already an instance");
            throw new IllegalArgumentException();
        }
        return adminManager;
    }

    public String connect(UsersDB userdb) {

        String result = null;
        int uuid = userdb.getUuid();
        UsersDB user = SharedClass.fromJsonUser(uuid);
        if (user.getUuid() == userdb.getUuid()) {
            result = "true";
            String database = userdb.getDatabase();

        }
        else
            result = "false";





    }

}
