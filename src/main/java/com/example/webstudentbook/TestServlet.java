package com.example.webstudentbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "TestServlet", value = "/TestServlet")
public class TestServlet extends HttpServlet {

    private DataSource dataSource;

    private DataSource getDataSource() throws NamingException {
        String jndi = "java:comp/env/jdbc/studentdb";
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup(jndi);
        return dataSource;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Set up the print writer
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        //Get a connection to the database
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try{
            dataSource = getDataSource();
            myConn = dataSource.getConnection();

            //Create SQL statements
            String sql = "SELECT * FROM student";
            myStmt = myConn.createStatement();

            //Execute SQL statement
            myRs = myStmt.executeQuery(sql);

            //Process the ResultSet
            while (myRs.next()){
                String email = myRs.getString("email");
                out.println(email);
            }
        }
        catch (Exception exc){
            System.out.println(exc.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


}
