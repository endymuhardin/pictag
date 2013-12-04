package com.muhardin.endy.pictag.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import training.pictag.domain.User;

public class UserDao {
    private DatabaseConfiguration configuration;
    private Connection databaseConnection;

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
    
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
        try {
            String sql = "insert into tbl_user (id, username, password, email) "
                    + "values (?, ?, ?, ?)";
            
            databaseConnection.setAutoCommit(false);
            
            PreparedStatement ps = databaseConnection.prepareStatement(sql);
            String id = UUID.randomUUID().toString();
            ps.setString(1, id);
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getEmail());
            ps.executeUpdate();
            databaseConnection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            try {
                databaseConnection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                databaseConnection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<User> findAllUsers(){
        try {
            String sql = "select * from tbl_user order by id";
            PreparedStatement ps = databaseConnection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            List<User> result = new ArrayList<User>();
            while(rs.next()){
                User u = new User();
                u.setId(rs.getString("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                result.add(u);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
