package com.muhardin.endy.pictag.dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import training.pictag.domain.User;

public class UserDaoTest {
    
    @Test
    public void testFindAllUsers() throws ClassNotFoundException {
    
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDatabaseDriverName("com.mysql.jdbc.Driver");
        config.setDatabaseUrl("jdbc:mysql://localhost/pictagdb");
        config.setDatabaseUsername("root");
        config.setDatabasePassword("admin");
        
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
}
