
package com.mycompany.minigoldengate;

import java.sql.*;
/**
 *
 * @author nadabounajma
 */
public class FrontEnd {
    private String url;
    private String uid;
    private String pw;
    private String database;
    private Connection conn = null;
    private Statement stmt = null, stmt2 = null;
   
    public FrontEnd(){
    }
   
    public boolean StartConnection(String Host, String PortNum, String uid, String pw, String database){
        url = "jdbc:mysql://"+Host+":"+PortNum+"/"+database;
        this.database = database;
        this.uid = uid;
        this.pw = pw;
         try  
        {   
            conn = DriverManager.getConnection(url, this.uid, this.pw); // Setup connection with DB
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // Statements allow to issue SQL queries to the DB
            stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if(conn.isValid(0))return true;
        }
        catch (SQLException ex) {
            System.err.println("Error: " + ex);
        }
        return false;
    }
    
    
    public ResultSet TableContent(String table_name){
        ResultSet rst = null;
        try {
            rst = stmt.executeQuery("show tables LIKE '"+table_name+"';");
            if(rst.next())rst = stmt.executeQuery("SELECT * FROM "+table_name+";");
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return rst;
    }
  
    public String getDatabase() {
        return database;
    }

    public Statement getStmt() {
        return stmt;
    }

    public Statement getStmt2() {
        return stmt2;
    }
    
}
