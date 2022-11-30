package com.example.webstudentbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StudentControllerServlet", value = "/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

    private StudentDBUtil studentDBUtil;
    private DataSource dataSource;

    public StudentControllerServlet(){
        super();
    }

    private DataSource getDataSource() throws NamingException{
        String jndi = "java:comp/env/jdbc/studentdb";
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup(jndi);
        return dataSource;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try{
            dataSource = getDataSource();
            studentDBUtil = new StudentDBUtil(dataSource);
        }
        catch (NamingException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            listStudents(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        List<Student> students = studentDBUtil.getStudents();
        request.setAttribute("STUDENT_LIST", students);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
