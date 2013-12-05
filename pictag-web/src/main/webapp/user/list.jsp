<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User List</title>
    </head>
    <body>
        <h1>User List</h1>
        
        <b>This is /user/list.jsp</b>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>&nbsp</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${userList}">
                <tr>
                    <td>${u.username}</td>
                    <td>${u.email}</td>
                    <td>
                        <a href="edit?id=${u.id}">edit</a> | 
                        <a href="delete?id=${u.id}">delete</a>
                    </td>
                </tr>
                </c:forEach>
            </tbody>
        </table>

            
        
        
        
        
    </body>
</html>
