
package com.mycompany.minigoldengate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nadabounajma
 */
public class MySQL_Data_Sync implements SYNCHRONIZATION{
    private final FrontEnd frontend;
    private final BackOffice backoffice;
    private final DefaultListModel<String> report_list;
    private final Statement[] stmts = new Statement[4];
    private final ArrayList<String> PK_Column_names = new ArrayList<>(), Non_PK_Column_names = new ArrayList<>();
    private ArrayList<Unique_Constraint> unique_list = new ArrayList<>();
    private ResultSet rst, rst2, set;
    private String pks = "", non_pks = "", names = "", pk_conditions = "", non_pks_condition = "", query, db1, db2, mark = "";
    private int row, flag, a, b, newmax;
    private boolean found, pk_changed, unique_found;
    private int count=0;
    private final FileOutputStream fileOutdata;
    private final XSSFSheet sheetdata;
    private final XSSFWorkbook workbookdata = new XSSFWorkbook();
    private final ArrayList<String> ds_comment_list= new ArrayList();
    double timed;
    
    public MySQL_Data_Sync(FrontEnd frontend, BackOffice backoffice, DefaultListModel<String> report_list) throws FileNotFoundException{
        this.frontend = frontend;
        this.backoffice = backoffice;
        this.report_list = report_list;
        stmts[0] = this.frontend.getStmt();
        stmts[1] = this.backoffice.getStmt();
        a = 0;
        b = 1;
        fileOutdata = new FileOutputStream("data_sync_comments.xlsx");
        sheetdata = workbookdata.createSheet("Sheet 1");    
    }
    
   @Override
    public void DISABLING_CONSTRAINTS(String table_name){
        try{
            //DISABLE FOREIGN KEY CONSTRAINTS
            stmts[a].executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
            stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=0;");

            //DISABLE TRIGGERS
            stmts[a].executeUpdate("SET @TRIGGER_DISABLED=1;");
            stmts[b].executeUpdate("SET @TRIGGER_DISABLED=1;");
        }catch(SQLException ex){
            System.out.println("CONSTRINTS ERROR :"+ex.getMessage());
        }
    }
 
   @Override
   public void ENABLING_CONSTRAINTS(String table_name){
        try{
            //DISABLE FOREIGN KEY CONSTRAINTS
            stmts[a].executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
            stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=1;");

            //DISABLE TRIGGERS
            stmts[a].executeUpdate("SET @TRIGGER_DISABLED=0;");
            stmts[b].executeUpdate("SET @TRIGGER_DISABLED=0;");
        }catch(SQLException ex){
            System.out.println("CONSTRINTS ERROR :"+ex.getMessage());
        }
    }

    @Override
    public void Data_Synchronization(String table_name, int direction){
        int rows = 0,loop = 0;
        if(direction == 1){a = 0; b = 1;}//FE -> BO
        if(direction == 2){a = 1; b = 0;}//BO -> FE
        else if(direction == 3){a = 0; b = 1; loop = -1;}// FE -> BO, BO -> FE
        String q, data_type, unique_values_condition = "";
        String compare1 = "",compare2="";
        int k;
        boolean IsChar,not_created;
        ArrayList<String> columns = new ArrayList<>();
        Non_PK_Column_names.clear();
        PK_Column_names.clear();
        
        try{
            //STORING COLUMN NAMES + PKs +  NON-PKs
            rst = stmts[a].executeQuery("desc "+table_name+";");
            while(rst.next()){
                columns.add(rst.getString(1));
                if(rst.getString(4).equals("PRI"))PK_Column_names.add(rst.getString(1));
                else Non_PK_Column_names.add(rst.getString(1));
            }
        }catch(SQLException ex){
            System.out.println("Storing Column names error : "+ex.getMessage());
        }
        
        //STORING PRIMARY KEY COLUMNS NAMES SEPERATED WITH COMMA TO USE INSIDE QUERIES
        pks = "";
        for(int i=0 ; i<PK_Column_names.size() ; i++){
            pks += PK_Column_names.get(i);
            if(i <= PK_Column_names.size() - 2)pks += ", ";
        }

        //STORING NON-PRIMARY KEY COLUMNS NAMES SEPERATED WITH COMMA TO USE INSIDE QUERIES
        non_pks = "";
        for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
            non_pks += Non_PK_Column_names.get(i);
            if(i <= Non_PK_Column_names.size() - 2)non_pks += ", ";
            
        }
        //ADD ROW_NUM ATTRIBUTE
        try{
            stmts[a].executeUpdate("ALTER TABLE "+table_name+" ADD COLUMN row_num int FIRST;");
            stmts[b].executeUpdate("ALTER TABLE "+table_name+" ADD COLUMN row_num int FIRST;");
        }catch(SQLException ex){
            System.out.println("ROW NUM COLUMN ERROR :"+ex.getMessage());
        }
        
        while(++loop != 2){
            if(a == 0){
                stmts[2] = frontend.getStmt2();
                stmts[3] = backoffice.getStmt2();
                db1 = frontend.getSchema();
                db2 = backoffice.getSchema();
               
            }
            else{
                stmts[2] = backoffice.getStmt2();
                stmts[3] = frontend.getStmt2();
                db1 = backoffice.getSchema();
                db2 = frontend.getSchema();
            }
            
            //POPULATE ROW_NUM COLUMN
            try{
                String cond;
                rst = stmts[a].executeQuery("SELECT "+pks+" FROM "+table_name+" ORDER BY "+PK_Column_names.get(0)+";");
                if(rst.getMetaData().getColumnTypeName(1).contains("CHAR"))mark = "'";
                rows = 0;
                while(rst.next()){
                    rows++;
                    cond = "";
                    for(int i=0 ; i<PK_Column_names.size();i++){
                        cond += PK_Column_names.get(i)+" = "+mark+rst.getString(i+1)+mark;
                        if(i <= PK_Column_names.size() - 2)cond += " AND ";
                    }
                    stmts[2].executeUpdate("UPDATE "+table_name+" SET row_num = "+rows+" WHERE "+cond+";");
                }
            }catch(SQLException ex){
                System.out.println("Data Sync Error : "+ex.getMessage());
            }
            
            
            try{
                //GETTING UNIQUE COLUMNS IF THEY EXIST AS BACKUP PLAN CASE SEARCH USING PRIMARY KEYS FAILED
                Unique(table_name,db1); 
            }catch(SQLException ex){
                System.out.println("Unique Constraint Error : "+ex.getMessage());
            }
            
            //ITERATING OVER EACH ROW FROM THE GIVEN SOURCE TABLE
            for(row = 1 ; row <= rows ; row++){
                found = false;
                unique_found = false;
                pk_changed = false;
                flag = 0;
                not_created = true;
                //CALLING RANDOM FUNCTION TO PROVIDE US WITH COLUMNS TO SEARCH WITH
                for (k = PK_Column_names.size(); k >= 1; k--) {
                        if(k < PK_Column_names.size()){//SEARCHING FOR UNIQUE COLUMN(S) IF EXISTS BECAUSE MAIN PKs NOT FOUND IN TARGET
                            pk_changed = true;
                            flag = 1;
                            if(!unique_list.isEmpty()){
                                for(int i=0 ; i<unique_list.size() ; i++){
                                    try{
                                        unique_values_condition = Current_Unique_Values(table_name,unique_list.get(i).getColumns());
                                        if(rst2.isBeforeFirst()){
                                            unique_found = true;
                                            found = true;
                                            break;
                                        }
                                    }catch(SQLException ex){
                                        System.out.println("Unique values Exception : "+ex.getMessage());
                                    }
                                    
                                }
                                
                            }
                            if(unique_found == false && not_created){//UNIQUE COLUMNS NOT FOUND - MOVING TO SEARCHING FOR non-primary keys as a backup primary key
                                non_pks_condition = "";
                                q = "SELECT "+non_pks+" FROM "+table_name+" WHERE row_num = "+row+";";

                                try{
                                    rst = stmts[a].executeQuery(q);//selects values of non-pks
                                    while(rst.next()){//building non_pks_condition
                                    for(int i=0 ; i<rst.getMetaData().getColumnCount() ; i++){
                                        mark = "";
                                        if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))mark = "'";
                                        non_pks_condition += rst.getMetaData().getColumnName(i+1) +"="+ mark + rst.getString(i+1)+ mark;
                                        if(i <= Non_PK_Column_names.size() - 2)non_pks_condition += " AND ";   
                                    }  
                                }
                                }catch(SQLException ex){
                                    System.out.println("Error1 : "+ex.getMessage());
                                }
                                
                                not_created = false;
                            }
                            
                        }
                        if(unique_found == false)random(0, k, new String[k],table_name);
                        if(found == true)break;
                
                }
                
                if(found && !(direction == 3 && a == 0)){
                    if(pk_changed && unique_found == false){//case sub-pks + non-prime values were found in target table
                        flag = 1;
                        try{
                            set = stmts[a].executeQuery("SELECT "+pks+", "+non_pks+" FROM "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition);
                            set.last();
                            int r1 = set.getRow();
                            rst2.last();
                            int r2 = rst2.getRow();
                            if(r1 >= r2){//case more than one row has been selected we delete all selected rows from target then insert all those selected from source table
                                                        //however this will fail if target has more selected rows than source, this means target has new inserted rows that should not be deleted as they will be inserted later on in source case of Bi Sync or simply belong to target

                                //delete all rst2 rows and insert all rst rows case # of rows of rst2 rows is smaller than rst's
                                stmts[b].executeUpdate("DELETE FROM "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition + " AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+db1+"."+table_name+");");
                                ds_comment_list.add("DELETING FROM "+db2+" TABLE "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition + " AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+db1+"."+table_name+");\n");
                                set.beforeFirst();
                                while(set.next())insertion(table_name);
                                set.close();
                            }
                            else found = false;
                            
                        }catch(SQLException ex){
                            System.out.println("Inside Sub-Pks found statement "+ex.getMessage());
                        }
                    }

                    else{
                            String temp;
                            if(unique_found == false)temp = pk_conditions;
                            else temp = unique_values_condition;
                            
                            for(int i=0 ; i<columns.size() ; i++){
                                IsChar = false;
                                try{
                                       rst = stmts[a].executeQuery("SELECT "+columns.get(i)+" FROM "+table_name+" where "+pk_conditions+";");
                                       rst.next(); compare1 = rst.getString(1);  
                                       
                                       rst2 = stmts[b].executeQuery("SELECT "+columns.get(i)+" FROM "+table_name+" where "+temp+";");
                                       rst2.next(); compare2 = rst2.getString(1);
                                       data_type = rst.getMetaData().getColumnTypeName(1);
                                       if(data_type.contains("CHAR") || data_type.contains("DATE"))IsChar = true;

                                }catch(SQLException ex){
                                    System.out.println("Non Primary Columns Sync Error : "+ex.getMessage());
                                }

                                if((compare1 == null && compare2 == null) || compare1 != null && compare1.equals(compare2)){}
                                else {
                                    //updating
                                    if(IsChar && compare1 != null)compare1 = "'"+compare1+"'";
                                    if(a == 0)q = backoffice.getSchema()+" ";   
                                    else q = frontend.getSchema()+" ";
                                    report_list.addElement(q+" - Row#"+row+" Column '"+columns.get(i).toUpperCase()+"' : '"+compare2+"' changed to '"+compare1+"'");
                                    try{
                                            stmts[b].executeUpdate("UPDATE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+temp+";");
                                            ds_comment_list.add("UPDATING "+db2+" TABLE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+pk_conditions+";\n");
                                    }catch(SQLException ex){
                                        System.out.println("Updation Error : "+ex.getMessage());
                                    }  
                                }
                            }
                        } 
                    }

                if(found == false) insertion(table_name);
                  
            }

            
            a = 1;
            b = 0;
        }
        
        try{
            stmts[a].executeUpdate("ALTER TABLE "+table_name+" DROP COLUMN row_num;");
            stmts[b].executeUpdate("ALTER TABLE "+table_name+" DROP COLUMN row_num;");
        }catch(SQLException ex){
            System.out.println("ROW NUM ERROR:"+ex.getMessage());
        }
    }
    
    
    public void Unique(String table_name, String db1) throws SQLException{
        
        unique_list.clear();
        ArrayList<String> columns_list;
        String position;
        rst = stmts[a].executeQuery("select ORDINAL_POSITION, COLUMN_NAME from information_schema.key_column_usage"
              + " where table_name = '"+table_name+"' AND table_schema = '"+db1+"'"
              + " AND constraint_name IN (select constraint_name from information_schema.table_constraints where table_name = '"+table_name+"' and table_schema = '"+db1+"' AND constraint_type = 'UNIQUE');");
            
        while(rst.next()){
            columns_list  = new ArrayList<>();
            do{
                columns_list.add(rst.getString(2));
            }while(rst.next() && !rst.getString(1).equals("1"));
            unique_list.add(new Unique_Constraint(columns_list));
            rst.previous();
        }
        
    }
    
    
    public String Current_Unique_Values(String table_name, ArrayList<String> unique_columns_list) throws SQLException{
        String columns = "";
        for(int i=0 ; i<unique_columns_list.size() ; i++){
            columns += unique_columns_list.get(i);
            if(i <= unique_columns_list.size() - 2)columns += ", ";
        }
        
        rst = stmts[a].executeQuery("SELECT "+columns+" FROM "+table_name+" WHERE row_num = "+row+";");
        rst.next();
        String unique_values = "";
        for(int i=1 ; i<=rst.getMetaData().getColumnCount(); i++){
            mark = "";
            if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))mark = "'";
            unique_values += rst.getMetaData().getColumnName(i) + "="+ mark + rst.getString(i) + mark;
            if(i <= rst.getMetaData().getColumnCount() - 1)unique_values += " AND ";   
            
        }
        
        rst2 = stmts[b].executeQuery("SELECT "+columns+" FROM "+table_name+" WHERE "+unique_values + ";");
        return unique_values;
    }
    
   
    public void insertion(String table_name){
        String Col_Names = "";
        String content = "";
        try{               
                if(set == null || set.isClosed()){
                    rst = stmts[a].executeQuery("SELECT "+pks+", "+non_pks+" FROM "+table_name+" WHERE row_num = "+row+";");
                    rst.next();
                }
                else rst = set;
                int col_count = PK_Column_names.size() + Non_PK_Column_names.size();

                for(int i=1 ; i<=col_count ; i++){
                    mark = "";
                    if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))mark = "'";
                    Col_Names += rst.getMetaData().getColumnName(i);
                    content += mark +rst.getString(i)+ mark;
                    if(i <= col_count - 1){
                        Col_Names += ", ";
                        content += ", ";
                    }
                }

            String q = "INSERTING INTO "+db2+" "+table_name+" VALUES ("+content+")";
            report_list.addElement(q);
            ds_comment_list.add(q+"\n");
            stmts[b].executeUpdate("INSERT INTO "+table_name+"("+Col_Names+") VALUES ("+content+");");
            System.out.println(q);
            
        }catch(SQLException ex){
            System.out.println("Insertion error : "+ex.getMessage());
        }

    }
   
    public void random(int n, int k, String[] PKs_To_Use, String table_name){
        if (k == 0 && found == false){
            names = "";
            pk_conditions = "";
            try{
                for(int i=0 ; i<PKs_To_Use.length ; i++){
                    names += PKs_To_Use[i];
                    mark = "";
                    rst = stmts[a].executeQuery("SELECT "+PKs_To_Use[i]+" FROM "+table_name+" WHERE row_num = "+row+";");
                    if(rst.getMetaData().getColumnTypeName(1).contains("CHAR"))mark = "'";
                    rst.next();
                    pk_conditions += PKs_To_Use[i]+"="+mark+rst.getString(1)+mark;
                    if(i <= PKs_To_Use.length - 2){
                        names += ", ";
                        pk_conditions += " AND ";
                    }
                }
                
                if(flag == 0)query = "SELECT "+ names + " FROM "+ table_name +" WHERE "+ pk_conditions + ";";
                else{
                    query = "SELECT "+pks+", "+non_pks+" FROM "+table_name+" WHERE "+ pk_conditions + " AND " + non_pks_condition +
                            " AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+db1+"."+table_name+" WHERE "+ pk_conditions + " AND " + non_pks_condition+";";
                }
                
                System.out.println("Function selects : "+query);
                rst2 = stmts[b].executeQuery(query);
                if(rst2.isBeforeFirst())found = true;
                
            }catch(SQLException ex){
                    System.out.println("random Error : " + ex.getMessage());
            }    
        }
        
        else{
            if(found)return;
            for (int i = n; i <= PK_Column_names.size() - k; i++){
                PKs_To_Use[PKs_To_Use.length - k] = PK_Column_names.get(i);
                random(i + 1, k - 1, PKs_To_Use, table_name);  
            }
        }
    }
    
    @Override
    //teammate work
    public void call_data(String table_name,int nmax12,double time){
        try{
            newmax=nmax12;
            timed=time;
            if (ds_comment_list.isEmpty()==true){
                ds_comment_list.clear();
                ds_comment_list.add("Data is Sycnhronized");

                create_excel_data(table_name,ds_comment_list,timed);

                ds_comment_list.clear();
            }else if (ds_comment_list.isEmpty()!=true){
                create_excel_data(table_name,ds_comment_list,timed);
                ds_comment_list.clear();

            }
        }catch(Exception ex){
            System.out.println("call data error :"+ex.getMessage());
        }
    }
   
   public void create_excel_data(String table_name,ArrayList<String> ds_comment_list,double time) throws FileNotFoundException, IOException, InvalidFormatException{

        File xlsxFile = new File("data_sync_comments.xlsx");
        String listString = String.join("",ds_comment_list);
        Object[][] newinfo = {{table_name,listString,time}};
        FileInputStream inputStream = new FileInputStream(xlsxFile);
        Sheet sheetd = workbookdata.getSheetAt(0);

        CellStyle wrapStyle = workbookdata.createCellStyle();
        wrapStyle.setWrapText(true);
        int rowCount = sheetd.getLastRowNum();
        Object[][] newinfo1 = {{"table name","Comments","time elapsed(s)"}};
        for (Object[] details : newinfo1) {

            Row row1 = sheetd.createRow(0);
            int columnCount1 = 0;

            for (Object info : details) {
                Cell cell = row1.createCell(columnCount1++);
                if (info instanceof String) {
                    cell.setCellValue((String) info);
                    if(columnCount1==2){
                    CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                    sheetdata.autoSizeColumn(2);}
                    else if(columnCount1==1){
                      CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                    sheetdata.autoSizeColumn(1);
                    }
                }else if (info instanceof Integer) {
                    cell.setCellValue((Integer) info);
                }else if (info instanceof Double) {
                    cell.setCellValue((Double) info);
                    if(columnCount1==3){
                    CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                    sheetdata.autoSizeColumn(3);
                    }
                } 
            }
        }      
        for (Object[] details : newinfo) {

            Row rowd = sheetd.createRow(++rowCount);
            int columnCount = 0;
            for (Object info : details) {


                    Cell cell = rowd.createCell(columnCount++);
                    cell.setCellStyle(wrapStyle);
                    if (info instanceof String) {
                    cell.setCellValue((String) info);

                    if(columnCount==2){
                    cell.setCellValue(listString);
                   CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
                   sheetdata.autoSizeColumn(2);

                    }else if(columnCount==1){
                        cell.setCellValue(table_name);
                        CellUtil.setVerticalAlignment(cell,  VerticalAlignment.CENTER);
                        sheetdata.autoSizeColumn(1);
                    }
                    } else if (info instanceof Integer) {
                    cell.setCellValue((Integer) info);
                    }  else if (info instanceof Double) {
                       cell.setCellValue((Double) info);
                       if(columnCount==3){
                    cell.setCellValue(time);
                   CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
                   sheetdata.autoSizeColumn(3);

                    }
                             } 
            }
        }

        inputStream.close();
        FileOutputStream os = new FileOutputStream(xlsxFile);
        workbookdata.write(os);

        //Close the workbook and output stream
        count++;
        if(count==newmax){  
            workbookdata.close();
            os.close();
        }
    }

}
