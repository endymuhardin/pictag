package com.muhardin.endy.pictag.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import training.pictag.domain.User;

public class UserDao {
    private DatabaseConfiguration configuration;
    private Connection databaseConnection;
    
    public UserDao(String databaseDriver, String databaseUrl, 
            String databaseUsername, String databasePassword){
        
        DatabaseConfiguration dc = new DatabaseConfiguration();
        dc.setDatabaseDriverName(databaseDriver);
        dc.setDatabaseUrl(databaseUrl);
        dc.setDatabaseUsername(databaseUsername);
        dc.setDatabasePassword(databasePassword);
        
        configuration = dc;
    }
    
    public UserDao(DatabaseConfiguration config){
        configuration = config;
    }
    
    public void connect() throws ClassNotFoundException {
        try {
            // 1. load database driver
            Class.forName(configuration.getDatabaseDriverName());

            // 2. connect to database
            databaseConnection = DriverManager.getConnection(
                    configuration.getDatabaseUrl(), 
                    configuration.getDatabaseUsername(), 
                    configuration.getDatabasePassword());
        } catch (SQLException err){
            System.out.println("Error connecting to database");
            System.out.println("Error description : "+err.getMessage());
        }
    }
    
    public void disconnect(){
        try {
            databaseConnection.close();
        } catch (SQLException err) {
            System.out.println("Error disconnecting to database");
            System.out.println("Error description : "+err.getMessage());
        }
    }
    
    public void insert(User u){
    
    }
    
    public List<User> findAllUsers(){
        return null;
    }
}
