package com.example.nosqldb;

import java.io.IOException;

public abstract class Slave extends PrimitiveDatabase {

    protected PrimitiveDatabase masterDB;
    private String slaveDir;

    public Slave(String slaveDir) {
        super(server);
        this.slaveDir = slaveDir;
    }

    public PrimitiveDatabase getMasterDB() {
        return masterDB;
    }

    public void setMasterDB(PrimitiveDatabase masterDB) {
        this.masterDB = masterDB;
    }

    public String getSlaveDir() {
        return slaveDir;
    }

    public void setSlaveDir(String slaveDir) {
        this.slaveDir = slaveDir;
    }

    @Override
    public abstract void createDbDir() ;

    @Override
    public void loadDatabase(String dir) throws IOException {

    }
    public abstract void createSlaveDB(String slaveDir);
}
