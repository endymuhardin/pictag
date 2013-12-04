package com.muhardin.endy.pictag.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import training.pictag.domain.User;

public class UserDaoTest {
    
    @Test
    public void testFindAllUsers() throws ClassNotFoundException {
        DatabaseConfiguration config = createConfig();
        
        UserDao ud = new UserDao(config);
        
        ud.connect();
        List<User> result = ud.findAllUsers();
        ud.disconnect();
        
        /*
        for (User user : result) {
            System.out.println("ID : "+user.getId());
            System.out.println("Username : "+user.getUsername());
            System.out.println("Password : "+user.getPassword());
            System.out.println("Email : "+user.getEmail());
        }
        */
        
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.size() == 3);
        
        User u = result.get(0);
        Assert.assertEquals("abc123", u.getId());
        
        User u1 = result.get(1);
        Assert.assertEquals("hamzah", u1.getUsername());
    }

    private DatabaseConfiguration createConfig() {
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDatabaseDriverName("com.mysql.jdbc.Driver");
        config.setDatabaseUrl("jdbc:mysql://localhost/pictagdb");
        config.setDatabaseUsername("root");
        config.setDatabasePassword("admin");
        return config;
    }
    
    @After
    public void cleanInsertedData() throws ClassNotFoundException, SQLException{
        DatabaseConfiguration config = createConfig();
        UserDao ud = new UserDao(config);
        ud.connect();
        
        Connection conn = ud.getDatabaseConnection();
        conn.createStatement()
                .executeUpdate("delete from tbl_user where username = 'tester'");
        
        ud.disconnect();
    }
    
    @Test
    public void testInsert() throws ClassNotFoundException{
        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setUsername("tester");
        u.setPassword("test123");
        u.setEmail("tester@gmail.com");
        
        DatabaseConfiguration c = createConfig();
        UserDao ud = new UserDao(c);
        ud.connect();
        
        List<User> prevCondition = ud.findAllUsers();
        ud.insert(u);
        List<User> afterCondition = ud.findAllUsers();
        
        ud.disconnect();
        
        Assert.assertTrue(prevCondition.size() + 1 == afterCondition.size());
    }
}
