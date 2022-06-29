/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minigoldengate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author nadabounajma
 */
public class BackOffice {
    private String url;
    private String uid;
    private String pw;
    private String database;
    private Connection conn = null;
    private Statement stmt = null, stmt2 = null;
    
    public BackOffice(){
    }
    
    public boolean StartConnection(String Host, String PortNum, String uid, String pw, String database){
        url = "jdbc:mysql://"+Host+":"+PortNum+"/"+database;
        this.database = database;
        this.uid = uid;
        this.pw = pw;
         try  
        {
            conn = DriverManager.getConnection(url, uid, pw); // Setup connection with DB
            stmt = conn.createStatement(); // Statements allow to issue SQL queries to the DB
            stmt2 = conn.createStatement();
            if(conn.isValid(0))return true;
        }
        catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
        return false;
    }
    
    public ResultSet TableContent(String table_name){
        //Result set get the result of the SQL query
       ResultSet rst = null;
        try {
            rst = stmt.executeQuery("show tables LIKE '"+table_name+"';");
            if(rst.next())rst = stmt.executeQuery("SELECT * FROM "+table_name+";");
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return rst;
             
    }
    
    public String getDatabase(){
        return database;
    }
    
    public Statement getStmt() {
        return stmt;
    }
    
    public Statement getStmt2() {
        return stmt2;
    }
    
}
