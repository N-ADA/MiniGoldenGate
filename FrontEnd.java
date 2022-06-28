
//This class established a connection with the FrontEnd
package com.mycompany.minigoldengate;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private Statement stmt = null;
    public XSSFWorkbook workbook = new XSSFWorkbook();
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
            stmt = conn.createStatement(); // Statements allow to issue SQL queries to the DB
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
    
  
        public FileOutputStream func_file() throws FileNotFoundException{
             FileOutputStream fileOut = new FileOutputStream("Details.xlsx");
             System.out.println("file was created");
        return fileOut;
        }
        
        public XSSFSheet funct_wb(){
              XSSFSheet sheet = workbook.createSheet("Sheet 1");
              
        return sheet;
        }
        public XSSFWorkbook getWorkbook(){
         return workbook;
        }
    
}
