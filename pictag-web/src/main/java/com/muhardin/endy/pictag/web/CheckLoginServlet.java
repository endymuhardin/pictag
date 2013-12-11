/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muhardin.endy.pictag.web;

import com.muhardin.endy.pictag.dao.DatabaseConfiguration;
import com.muhardin.endy.pictag.dao.UserDao;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import training.pictag.domain.User;
import training.pictag.dto.PictagServerResponse;

/**
 *
 * @author endy
 */
public class CheckLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            
            DatabaseConfiguration config = createConfig();
            UserDao ud = new UserDao(config);
            
            ud.connect();
            Boolean valid = ud.isUsernamePasswordValid(username, password);
            ud.disconnect();
            
            PictagServerResponse response = new PictagServerResponse();
            response.setSuccess(valid);
            if(valid){
                response.setMessage(username);
            } else {
                response.setMessage("Invalid username or password");
            }
            
            ObjectMapper jacksonMapper = new ObjectMapper();
            String json = jacksonMapper.writeValueAsString(response);
            
            resp.setContentType("application/json");
            resp.getWriter().write(json);
            resp.getWriter().flush();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CheckLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DatabaseConfiguration createConfig() {
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDatabaseDriverName("com.mysql.jdbc.Driver");
        config.setDatabaseUrl("jdbc:mysql://localhost/pictagdb");
        config.setDatabaseUsername("root");
        config.setDatabasePassword("admin");
        return config;
    }

}
