package com.example.webstudentbook;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDBUtil {

    private DataSource dataSource;

    public StudentDBUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Student> getStudents() throws Exception{
        List<Student> students = new ArrayList<Student>();

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try{
            myConn = dataSource.getConnection();
            myStmt = myConn.createStatement();
            String sql = "SELECT * FROM STUDENT ORDER BY  last_name";
            myRs = myStmt.executeQuery(sql);

            while (myRs.next()){
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                Student tempStudent = new Student(id,firstName,lastName,email);
                students.add(tempStudent);
            }
            return students;
        }
        finally {
            close(myConn,myStmt,myRs);
        }
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs){
        try{
            if (myStmt!=null){
                myStmt.close();
            }
            if (myRs!=null){
                myRs.close();
            }
            if (myConn!=null){
                myConn.close();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
