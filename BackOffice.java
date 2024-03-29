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
    private String Schema;
    private String SID;
    private Connection conn = null;
    private Statement statement = null, stmt = null, stmt2 = null, stmt3 = null;
    
    
    public BackOffice(){
    }
    
    public boolean StartConnection(String Host, String PortNum, String uid, String pw, String Schema, String SID){
        if(SID.isEmpty()) url = "jdbc:mysql://"+Host+":"+PortNum+"/"+Schema;
        else url = "jdbc:oracle:thin:@"+Host+":"+PortNum+":"+SID;
        this.Schema = Schema;
        this.uid = uid;
        this.pw = pw;
        this.SID = SID;
         try  
        {
            conn = DriverManager.getConnection(url, this.uid, this.pw); // Setup connection with DB
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // Statements allow to issue SQL queries to the DB
            stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt3 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if(conn.isValid(0))return true;
        }
        catch (SQLException ex) {
            System.err.println("StartConnection Error: " + ex);
        }
        return false;
    }
    
    public void CloseConnection(){
        try{
            
            conn.close();
            if(conn.isClosed())System.out.println("Connection is Closed");
            else System.out.println("Connection Failed to close");
        
        }catch(SQLException ex){
            System.out.println("CloseConneciton error : "+ex.getMessage());
        }   
    }
    
   
    public ResultSet TableContent(String table_name){
       ResultSet rst = null;
        String query, semicolon = "";
        try {
            if(SID.isEmpty()){
                query = "show tables LIKE '"+table_name+"';";
                semicolon = ";";
            }
            else query = "select table_name from user_tables where table_name=UPPER('"+table_name+"')";
            rst = stmt.executeQuery(query);
            if(rst.next())rst = stmt.executeQuery("SELECT * FROM "+table_name+semicolon);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return rst;
             
    }
    

    public String getSchema(){
        return Schema;
    }

    public String getSID() {
        return SID;
    }

    public Statement getStatement() {
        return statement;
    }

    public Statement getStmt() {
        return stmt;
    }
    
    public Statement getStmt2() {
        return stmt2;
    }

    public Statement getStmt3() {
        return stmt3;
    }
    
}
