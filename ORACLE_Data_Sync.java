/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.util.StringTokenizer;
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
 * @author nbounajma
 */
public class ORACLE_Data_Sync implements SYNCHRONIZATION{
    private final FrontEnd frontend;
    private final BackOffice backoffice;
    private final DefaultListModel<String> report_list;
    private final Statement[] stmts = new Statement[6];
    private final ArrayList<String> PK_Column_names = new ArrayList<>(), Non_PK_Column_names = new ArrayList<>();
    private ArrayList<Unique_Constraint> unique_list = new ArrayList<>();
    private ResultSet rst, rst2, set, rowid;
    private String pks = "", non_pks = "", names = "", pk_conditions = "", non_pks_condition = "", query, schema1, schema2, mark = "";
    private int flag, a, b, newmax;
    private boolean found, pk_changed, unique_found;
    private int count=0;
    private final FileOutputStream fileOutdata;
    private final XSSFSheet sheetdata;
    private final XSSFWorkbook workbookdata = new XSSFWorkbook();
    private final ArrayList<String> ds_comment_list= new ArrayList();
    double timed;

    public ORACLE_Data_Sync(FrontEnd frontend, BackOffice backoffice, DefaultListModel<String> report_list) throws FileNotFoundException {
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
            rst = frontend.getStmt2().executeQuery("select constraint_name from user_constraints where table_name = UPPER('"+table_name+"') AND constraint_type = 'R' AND OWNER = UPPER('"+frontend.getSchema()+"')");
            rst2 = backoffice.getStmt2().executeQuery("select constraint_name from user_constraints where table_name = UPPER('"+table_name+"') AND constraint_type = 'R' AND OWNER = UPPER('"+backoffice.getSchema()+"')");
            while(rst.next() && rst2.next()){
                System.out.println("ALTER TABLE "+frontend.getSchema()+"."+table_name+" DISABLE CONSTRAINT "+rst.getString(1));
                System.out.println("ALTER TABLE "+backoffice.getSchema()+"."+table_name+" DISABLE CONSTRAINT "+rst.getString(1));
                frontend.getStmt().executeUpdate("ALTER TABLE "+frontend.getSchema()+"."+table_name+" DISABLE CONSTRAINT "+rst.getString(1));
                backoffice.getStmt().executeUpdate("ALTER TABLE "+backoffice.getSchema()+"."+table_name+" DISABLE CONSTRAINT "+rst2.getString(1));
            }
            //DISABLING TRIGGERS
            frontend.getStmt().executeUpdate("ALTER TABLE "+frontend.getSchema()+"."+table_name+" DISABLE ALL TRIGGERS");
            backoffice.getStmt().executeUpdate("ALTER TABLE "+backoffice.getSchema()+"."+table_name+" DISABLE ALL TRIGGERS");

            frontend.getStmt().executeUpdate("commit");
            backoffice.getStmt().executeUpdate("commit");

        }catch(SQLException ex){
            System.out.println("CONSTRAINTS ERROR :"+ex.getMessage());
        }
    }
   
    @Override
    public void ENABLING_CONSTRAINTS(String table_name){
        try{
          //ENABLING FOREIGN KEY CONSTRAINTS
            rst = frontend.getStmt2().executeQuery("select constraint_name from user_constraints where table_name = UPPER('"+table_name+"') AND constraint_type = 'R' AND OWNER = UPPER('"+frontend.getSchema()+"')");
            rst2 = backoffice.getStmt2().executeQuery("select constraint_name from user_constraints where table_name = UPPER('"+table_name+"') AND constraint_type = 'R' AND OWNER = UPPER('"+backoffice.getSchema()+"')");        
            while(rst.next() && rst2.next()){
                frontend.getStmt().executeUpdate("ALTER TABLE "+frontend.getSchema()+"."+table_name+" ENABLE CONSTRAINT "+rst.getString(1));
                backoffice.getStmt().executeUpdate("ALTER TABLE "+backoffice.getSchema()+"."+table_name+" ENABLE CONSTRAINT "+rst2.getString(1));
            }
            //ENABLING TRIGGERS
            frontend.getStmt().executeUpdate("ALTER TABLE "+frontend.getSchema()+"."+table_name+" ENABLE ALL TRIGGERS");
            backoffice.getStmt().executeUpdate("ALTER TABLE "+backoffice.getSchema()+"."+table_name+" ENABLE ALL TRIGGERS");
            
            frontend.getStmt().executeUpdate("commit");
            backoffice.getStmt().executeUpdate("commit");

        }catch(SQLException ex){
                System.out.println("CONSTRINTS ERROR :"+ex.getMessage());
        }
    }

   @Override
    public void Data_Synchronization(String table_name, int direction){
        int loop = 0;
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
            //STORING COLUMN NAMES
            rst = stmts[a].executeQuery("select column_name from all_tab_columns where table_name = UPPER('"+table_name+"') AND OWNER = UPPER('"+frontend.getSchema()+"')");
            while(rst.next())columns.add(rst.getString(1));
            //STORING PK_Column_names
            rst = stmts[a].executeQuery("SELECT column_name FROM all_cons_columns WHERE OWNER = UPPER('"+frontend.getSchema()+"') AND constraint_name IN" +
                "(SELECT constraint_name FROM user_constraints WHERE table_name = UPPER('"+table_name+"') AND CONSTRAINT_TYPE = 'P') AND OWNER = UPPER('"+frontend.getSchema()+"')");
            while(rst.next())PK_Column_names.add(rst.getString(1));
            //STORING Non_Pk_column_names
            for(int i=0 ; i<columns.size() ; i++){
                if(!PK_Column_names.contains(columns.get(i)))Non_PK_Column_names.add(columns.get(i));
            }
            //STORING PKS SEPERATED WITH COMMA
            pks = "";
            for(int i=0 ; i<PK_Column_names.size() ; i++){
                pks += PK_Column_names.get(i);
                if(i <= PK_Column_names.size() - 2)pks+=", ";
            }
            //STORING NON PKS WITH COMMA
            non_pks = "";
            for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                non_pks += Non_PK_Column_names.get(i);
                if(i <= Non_PK_Column_names.size() - 2)non_pks+=", ";
            }
        }catch(SQLException ex){
            System.out.println("Storage error : "+ex.getMessage());
        }
        //GETTING UNIQUE COLUMNS IF THEY EXIST CASE SEARCH USING PRIMARY KEYS FAILED
        try{
            Unique(table_name);
        }catch(SQLException ex){
            System.out.println("unique error : "+ex.getMessage());
        }

        while(++loop != 2){
           if(a == 0){
                stmts[2] = frontend.getStmt2();
                stmts[3] = backoffice.getStmt2();
                stmts[4] = frontend.getStatement();
                stmts[5] = frontend.getStmt3();
                schema1 = frontend.getSchema();
                schema2 = backoffice.getSchema();
            }
            else{
                stmts[2] = backoffice.getStmt2();
                stmts[3] = frontend.getStmt2();
                stmts[4] = backoffice.getStatement();
                stmts[5] = backoffice.getStmt3();
                schema1 = backoffice.getSchema();
                schema2 = frontend.getSchema();
            }
               
            try{
                rowid = stmts[4].executeQuery("SELECT rowid FROM "+schema1+"."+table_name);
                while(rowid.next()){
                    found = false;
                    unique_found = false;
                    pk_changed = false;
                    flag = 0;
                    not_created = true;
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
                                q = "SELECT "+non_pks+" FROM "+schema1+"."+table_name+" WHERE rowid = '"+rowid.getString(1)+"'";
                                System.out.println(q);
                                try{
                                    rst = stmts[a].executeQuery(q);//selects values of non-pks
                                    while(rst.next()){//building non_pks_condition
                                    for(int i=1 ; i<=rst.getMetaData().getColumnCount() ; i++){
                                        mark = "";
                                        if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))mark = "'";
                                        if(rst.getMetaData().getColumnTypeName(i).equals("DATE")){
                                            StringTokenizer st = new StringTokenizer(rst.getString(i));
                                            non_pks_condition += rst.getMetaData().getColumnName(i) +"=" + "TO_DATE('"+st.nextToken()+"', 'YYYY-MM-DD')";
                                        }
                                        else non_pks_condition += rst.getMetaData().getColumnName(i) +"="+ mark + rst.getString(i)+ mark;
                                        if(i <= Non_PK_Column_names.size() - 1)non_pks_condition += " AND ";   
                                    } 
                                }
                                }catch(SQLException ex){
                                    System.out.println("nonpks condition error : "+ex.getMessage());
                                }
                                
                                not_created = false;
                            }
                            
                        }
                        if(unique_found == false)random(0, k, new String[k],table_name);
                        if(found == true)break;
                
                    }
                   
                    if(found  && !(direction == 3 && a == 0)){
                        if(pk_changed && unique_found == false){//case sub-pks + non-prime values were found in target table
                            flag = 1;
                            try{
                                set = stmts[a].executeQuery("SELECT * FROM "+schema1+"."+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition);
                                set.last();
                                int r1 = set.getRow();
                                rst2.last();
                                int r2 = rst2.getRow();
                                if(r1 >= r2){//case more than one row has been selected we delete all selected rows from target then insert all those selected from source table
                                                            //however this will fail if target has more selected rows than source, this means target has new inserted rows that should not be deleted as they will be inserted later on in source case of Bi Sync or simply belong to target

                                    //delete all rst2 rows and insert all rst rows case # of rows of rst2 rows is smaller than rst's
                                    stmts[b].executeUpdate("DELETE FROM "+schema2+"."+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition);
                                    stmts[b].executeUpdate("commit");
                                    ds_comment_list.add("DELETING FROM "+schema2+" TABLE "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition);
                                    set.beforeFirst();
                                    while(set.next())insertion(table_name);
                                    set.close();
                                }

                                else found = false;

                            }catch(SQLException ex){
                                System.out.println("Inside Sub-Pks found statement "+ex.getMessage());
                            }
                        }

                        else{//if it is Bidirectional Sync, only BO -> FE updation is allowed
                            String temp;
                            if(unique_found == false)temp = pk_conditions;
                            else temp = unique_values_condition;
                            for(int i=0 ; i<columns.size() ; i++){
                                IsChar = false;
                                try{   
                                       rst = stmts[a].executeQuery("SELECT "+columns.get(i)+" FROM "+schema1+"."+table_name+" WHERE rowid = '"+rowid.getString(1)+"'");
                                       rst.next(); compare1 = rst.getString(1);
                                       rst2 = stmts[b].executeQuery("SELECT "+columns.get(i)+" FROM "+schema2+"."+table_name+" WHERE "+temp);
                                       rst2.next(); compare2 = rst2.getString(1);
                                       data_type = rst.getMetaData().getColumnTypeName(1);
                                       if(data_type.contains("CHAR"))IsChar = true;

                                }catch(SQLException ex){
                                    System.out.println("Non Primary Columns Sync Error : "+ex.getMessage());
                                }

                                if((compare1 == null && compare2 == null) || compare1 != null && compare1.equals(compare2)){}
                                else {
                                    //updating
                                    if(IsChar && compare1 != null)compare1 = "'"+compare1+"'";
                                    report_list.addElement(schema2+" - Row#"+Get_Row_Num(table_name, temp)+" Column '"+columns.get(i).toUpperCase()+"' : '"+compare2+"' changed to '"+compare1+"'");
                                    try{
                                            if(rst.getMetaData().getColumnTypeName(1).equals("DATE")){
                                                compare1 = "TO_DATE('"+compare1+"', 'YYYY-MM-DD hh24:mi:ss')";
                                            }
                                            stmts[b].executeUpdate("UPDATE "+schema2+"."+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+temp);
                                            stmts[b].executeUpdate("commit");
                                            ds_comment_list.add("UPDATING "+schema2+" TABLE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+temp+"\n");
                                    }catch(SQLException ex){
                                        System.out.println("Updation Error : "+ex.getMessage());
                                    }  
                                }
                            }
                        } 
                }
                if(found == false)insertion(table_name);
                
                }
                a = 1;
                b = 0;
            }catch(SQLException ex){
                System.out.println("rowid error : "+ex.getMessage());
            }

        }
        
    }

    public int Get_Row_Num(String table_name, String temp) throws SQLException{
      int row=0;
      ResultSet r = stmts[b].executeQuery("SELECT rowid FROM "+schema2+"."+table_name+" WHERE "+temp);
      r.next();
      ResultSet result = stmts[5].executeQuery("SELECT rowid FROM "+schema2+"."+table_name);
      while(result.next()){
        row++;
        System.out.println(r.getString(1) +" "+result.getString(1));
        if(r.getString(1).equals(result.getString(1)))break;
      }
      r.close();
      result.close();
      return row;
    }

    public void Unique(String table_name) throws SQLException{
        unique_list.clear();
        ArrayList<String> columns_list;
        rst = stmts[a].executeQuery("SELECT column_name, position FROM all_cons_columns WHERE OWNER = UPPER('"+frontend.getSchema()+"') AND constraint_name IN" +
            "(SELECT constraint_name FROM user_constraints WHERE table_name = UPPER('"+table_name+"') AND CONSTRAINT_TYPE = 'U')");
        while(rst.next()){
          columns_list = new ArrayList<>();
          do{
                columns_list.add(rst.getString(1));
          }while(rst.next() && !rst.getString(2).equals("1"));
          unique_list.add(new Unique_Constraint(columns_list));
          rst.previous();
        }

        for(int i=0 ; i<unique_list.size() ; i++)System.out.println("unique columns : "+unique_list.get(i).getColumns().toString());
    }

    public String Current_Unique_Values(String table_name, ArrayList<String> unique_columns_list) throws SQLException{
        String columns = "";
        for(int i=0 ; i<unique_columns_list.size() ; i++){
            columns += unique_columns_list.get(i);
            if(i <= unique_columns_list.size() - 2)columns += ", ";
        }
       
        rst = stmts[a].executeQuery("SELECT "+columns+" FROM "+schema1+"."+table_name+" WHERE rowid = '"+rowid.getString(1)+"'");
        rst.next();
        String unique_values = "";
        for(int i=1 ; i<=rst.getMetaData().getColumnCount(); i++){
            mark = "";
            if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))mark = "'";
            unique_values += rst.getMetaData().getColumnName(i) + "="+ mark + rst.getString(i) + mark;
            if(i <= rst.getMetaData().getColumnCount() - 1)unique_values += " AND "; 
            
        }
        System.out.println("SELECT "+columns+" FROM "+table_name+" WHERE "+unique_values);
        rst2 = stmts[b].executeQuery("SELECT "+columns+" FROM "+schema2+"."+table_name+" WHERE "+unique_values);
        return unique_values;
    }

    public void insertion(String table_name){
        String Col_Names = "";
        String content = "";
        try{               
                if(set == null || set.isClosed()){
                    System.out.println("SELECT * FROM "+schema1+"."+table_name+" WHERE rowid = '"+rowid.getString(1)+"'");
                    rst = stmts[a].executeQuery("SELECT * FROM "+schema1+"."+table_name+" WHERE rowid = '"+rowid.getString(1)+"'");
                    rst.next();
                }
                else rst = set;
                int col_count = rst.getMetaData().getColumnCount();

                for(int i=1 ; i<=col_count ; i++){
                    mark = "";
                    if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))mark = "'";
                    Col_Names += rst.getMetaData().getColumnName(i);
                    if(rst.getMetaData().getColumnTypeName(i).equals("DATE")){
                       content += "TO_DATE('"+rst.getString(i)+"', 'YYYY-MM-DD hh24:mi:ss')";
                    }
                    else content += mark +rst.getString(i)+ mark;
                    if(i <= col_count - 1){
                        Col_Names += ", ";
                        content += ", ";
                    }
                }

            String q = "INSERTING INTO "+schema2+" "+table_name+" VALUES ("+content+")";
            report_list.addElement(q);
            ds_comment_list.add(q+"\n");
            System.out.println("INSERT INTO "+table_name+"("+Col_Names+") VALUES ("+content+")");
            stmts[b].executeUpdate("INSERT INTO "+schema2+"."+table_name+"("+Col_Names+") VALUES ("+content+")");
            stmts[b].executeUpdate("commit");
            
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
                    rst = stmts[a].executeQuery("SELECT "+PKs_To_Use[i]+" FROM "+schema1+"."+table_name+" WHERE rowid = '"+rowid.getString(1)+"'");
                    if(rst.getMetaData().getColumnTypeName(1).contains("CHAR"))mark = "'";
                    rst.next();
                    pk_conditions += PKs_To_Use[i]+"="+mark+rst.getString(1)+mark;
                    if(i <= PKs_To_Use.length - 2){
                        names += ", ";
                        pk_conditions += " AND ";
                    }
                }
                
                if(flag == 0)query = "SELECT "+ names + " FROM "+ schema2 + "." +table_name +" WHERE "+ pk_conditions;
                else{
                    rst = stmts[a].executeQuery("SELECT "+pks+" FROM "+table_name+" WHERE "+non_pks_condition);
                    String condition = "";
                    while(rst.next()){
                        condition += "(";
                           for(int i=1 ; i<=rst.getMetaData().getColumnCount() ; i++){
                                condition += rst.getString(i);
                                if(i <= rst.getMetaData().getColumnCount()-1)condition += ", ";
                            }
                        condition += ")";
                        if(!rst.isLast())condition += ", ";
                    }
                    query = "SELECT * FROM "+schema2+"."+table_name+" WHERE "+ pk_conditions + " AND " + non_pks_condition +
                            " AND ("+pks+") NOT IN ("+condition+")";
                }

                System.out.println("Random Function selects : "+query);
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
