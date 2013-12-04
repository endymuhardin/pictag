package com.muhardin.endy.pictag.dao;

import java.util.List;
import training.pictag.domain.User;

public class UserDao {
    private DatabaseConfiguration configuration;
    
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
    
    public void connect(){
    
    }
    
    public void disconnect(){
    
    }
    
    public void insert(User u){
    
    }
    
    public List<User> findAllUsers(){
        return null;
    }
}
