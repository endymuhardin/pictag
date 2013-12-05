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
import training.pictag.domain.User;

/**
 *
 * @author endy
 */
public class UserList extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("This is UserList servlet");
            
            DatabaseConfiguration config = createConfig();
            UserDao ud = new UserDao(config);
            
            ud.connect();
            List<User> result = ud.findAllUsers();
            ud.disconnect();
            
            // provide data for JSP template
            request.setAttribute("userList", result);
            
            // forward to JSP page for display
            request.getRequestDispatcher("/user/list.jsp")
                    .forward(request, response);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserList.class.getName()).log(Level.SEVERE, null, ex);
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
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
