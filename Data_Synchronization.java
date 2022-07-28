
package com.mycompany.minigoldengate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author nadabounajma
 */
public class Data_Synchronization {
    private final FrontEnd frontend;
    private final BackOffice backoffice;
    private final DefaultListModel<String> report_list;
    private final Statement[] stmts = new Statement[4];
    private int a,b;
    private final ArrayList<Primary_key> Primary_key_list = new ArrayList<>();
    private final ArrayList<String> Non_PK_Column_names = new ArrayList<>();
    private ResultSet rst, rst2, rst3, set;
    private String pks = "", names = "", pk_conditions = "", non_pks_condition = "", query, str;
    private int row, flag;
    private boolean found, pk_changed, unique_found;
    
    public Data_Synchronization(FrontEnd frontend, BackOffice backoffice, DefaultListModel<String> report_list){
        this.frontend = frontend;
        this.backoffice = backoffice;
        this.report_list = report_list;
        stmts[0] = this.frontend.getStmt();
        stmts[1] = this.backoffice.getStmt();
        a = 0;
        b = 1;
    }
    
    
    public void DISABLING_CONSTRAINTS() throws SQLException{
        //DISABLE FOREIGN KEY CONSTRAINTS
        stmts[a].executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
        stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=0;");

        //DISABLE TRIGGERS
        stmts[a].executeUpdate("SET @TRIGGER_DISABLED=1;");
        stmts[b].executeUpdate("SET @TRIGGER_DISABLED=1;");
    }
    
    public void ENABLING_CONSTRAINTS() throws SQLException{
         //ENABLING BACK FOREIGN KEYS
        stmts[a].executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
        stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=1;");

        //ENABLING BACK TRIGGERS
        stmts[a].executeUpdate("SET @TRIGGER_DISABLED=0;");
        stmts[b].executeUpdate("SET @TRIGGER_DISABLED=0;");
    }
    
    public void Synchronization_Process(String table_name, int direction){
        int loop = 0;
        try{
        if(direction == 1){a = 0; b = 1;}
        if(direction == 2){a = 1; b = 0;}
        else if(direction == 3){a = 0; b = 1; loop = -1;}
        String q, data_type, column_values, db1, db2, unique_values_condition = "";
        String compare1 = "",compare2="";
        int k;
        boolean IsChar,not_created;
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Unique_Constraint> unique_list = new ArrayList<>();
        
        while(++loop != 2){
            Primary_key_list.clear();
            Non_PK_Column_names.clear();
            if(a == 0){
                stmts[2] = frontend.getStmt2();
                stmts[3] = backoffice.getStmt2();
                db1 = frontend.getDatabase();
                db2 = backoffice.getDatabase();
               
            }
            else{
                stmts[2] = backoffice.getStmt2();
                stmts[3] = frontend.getStmt2();
                db1 = backoffice.getDatabase();
                db2 = frontend.getDatabase();
            }
            
            try{
                //GETTING UNIQUE COLUMNS IF THEY EXIST AS BACKUP PLAN CASE SEARCH USING PRIMARY KEYS FAILED
                unique_list = Unique(table_name,db1);
                
            }catch(SQLException ex){
                System.out.println("Unique Constraint Error : "+ex.getMessage());
            }
            
            try{
                //STORING PRIMARY KEY VALUES
                rst = stmts[a].executeQuery("show columns from "+table_name+" where `key` = 'PRI';");
                rst.next();
                String order = rst.getString(1);
                rst.close();
                rst = stmts[a].executeQuery("show columns from "+table_name+" where `key` = 'PRI';");
                while(rst.next()){
                    rst3 = stmts[2].executeQuery("SELECT "+rst.getString(1)+" FROM "+table_name+" ORDER BY "+order+";");
                    Primary_key_list.add(new Primary_key(rst.getString(1),rst3));
                }
            }catch(SQLException ex){
                System.out.println("Show Columns Error : "+ex.getMessage());
            }
            
            //STORING COLUMN NAMES AND NON-PRIMARY COLUMN NAMES
            rst = stmts[a].executeQuery("show columns from "+table_name+";");
            while(rst.next()){
                columns.add(rst.getString(1));
                if(!rst.getString(4).equals("PRI"))Non_PK_Column_names.add(rst.getString(1));
            }
            
            //STORING PRIMARY KEY COLUMNS NAMES SEPERATED WITH COMMA TO USE INSIDE QUERIES
            pks = "";
            for(int i=0 ; i<Primary_key_list.size() ; i++){
                pks += Primary_key_list.get(i).getName();
                if(i <= Primary_key_list.size() - 2)pks += ", ";
            }
            
            //STORING NON-PRIMARY KEY COLUMNS NAMES SEPERATED WITH COMMA TO USE INSIDE QUERIES
            column_values = "";
            for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                column_values += Non_PK_Column_names.get(i);
                if(i <= Non_PK_Column_names.size() - 2)column_values += ", ";
            }
           
            //ITERATING OVER EACH ROW FROM THE GIVEN SOURCE TABLE
            for(row = 0 ; row < Primary_key_list.get(0).getValue().size() ; row++){
                found = false;
                unique_found = false;
                pk_changed = false;
                flag = 0;
                not_created = true;
                //CALLING RANDOM FUNCTION TO PROVIDE US WITH COLUMNS TO SEARCH WITH
                for (k = Primary_key_list.size(); k >= 1; k--) {
                        if(k < Primary_key_list.size()){//SEARCHING FOR UNIQUE COLUMN(S) IF EXISTS BECAUSE MAIN PKs NOT FOUND IN TARGET
                            pk_changed = true;
                            flag = 1;
                            if(!unique_list.isEmpty()){
                                for(int i=0 ; i<unique_list.size() ; i++){
                                    try{
                                        unique_values_condition = Current_Unique_Values(table_name,unique_list.get(i).getColumns());
                                    }catch(SQLException ex){
                                        System.out.println("Unique values Exception : "+ex.getMessage());
                                    }
                                    if(rst2.isBeforeFirst()){
                                        unique_found = true;
                                        found = true;
                                        break;
                                    }
                                }
                                
                            }
                            
                            else if(not_created){//UNIQUE COLUMNS NOT FOUND - MOVING TO SEARCHING FOR non-primary keys as a backup primary key
                                
                                    q = "SELECT "+column_values+" FROM "+table_name+" WHERE ";
                                    q += Current_PKs_Values() + ";";
                                    non_pks_condition = "";
                                    try{
                                         rst = stmts[a].executeQuery(q);//selects values of non-pks

                                    }catch(SQLException ex){
                                        System.out.println("Error1 : "+ex.getMessage());
                                    }
                                    while(rst.next()){//building non_pks_condition
                                        for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                                            if(i <= Non_PK_Column_names.size() - 2){
                                                if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))non_pks_condition += Non_PK_Column_names.get(i) +"='"+rst.getString(i+1)+"' AND ";
                                                else non_pks_condition += Non_PK_Column_names.get(i) +"="+rst.getString(i+1)+" AND ";
                                            }
                                            else {
                                                if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))non_pks_condition += Non_PK_Column_names.get(i) +"='"+rst.getString(i+1)+"'";
                                                else non_pks_condition += Non_PK_Column_names.get(i) +"="+rst.getString(i+1);
                                            }
                                        }  
                                    }
                                    not_created = false;
                            }
                            
                        }
                        if(unique_found == false)random(0, k, new String[k],table_name);
                        if(found == true)break;
                
                }
                
                
                if(found){
                    if(pk_changed && unique_found == false){//case sub-pks + non-prime values were found in target table
                        flag = 1;
                        if(!(direction == 3 && a == 0)){

                            set = stmts[a].executeQuery("SELECT * FROM "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition);
                            set.last();
                            int r1 = set.getRow();
                            rst2.last();
                            int r2 = rst2.getRow();
                            if(r1 >= r2){//case more than one row has been selected we delete all selected rows from target then insert all those selected from source table
                                                        //however this will fail if target has more selected rows than source, this means target has new inserted rows that should not be deleted as they will be inserted later on in source case of Bi Sync or simply belong to target

                                //delete all rst2 rows and insert all rst rows case # of rows of rst2 rows is smaller than rst's
                                System.out.println("DELETE FROM "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition + "AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+str+"."+table_name+");");
                                stmts[b].executeUpdate("DELETE FROM "+table_name+" WHERE "+pk_conditions+" AND "+ non_pks_condition + "AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+str+"."+table_name+");");
                                set.beforeFirst();
                                while(set.next())insertion(table_name);
                                set.close();
                            }

                            else found = false;

                        }   
  
                    }

                    else if(!(direction == 3 && a == 0)){//if it is Bidirectional Sync, only BO -> FE updation is allowed
                            System.out.println("Checking for UPDATE : ");
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
                                        
                                }catch(SQLException ex){
                                    System.out.println("Non Primary Columns Sync Error : "+ex.getMessage());
                                }
                                
                                data_type = rst.getMetaData().getColumnTypeName(1);
                                if(data_type.contains("CHAR"))IsChar = true;
                                

                                if((compare1 == null && compare2 == null) || compare1 != null && compare1.equals(compare2)){}
                                else {
                                    //updating
                                    if(IsChar && compare1 != null)compare1 = "'"+compare1+"'";
                                    if(a == 0)q = backoffice.getDatabase()+" ";   
                                    else q = frontend.getDatabase()+" ";
                                    report_list.addElement(q+" - Row#"+(row+1)+" Column '"+columns.get(i).toUpperCase()+"' : '"+compare2+"' changed to '"+compare1+"'");
                                    try{
                                            System.out.println("UPDATE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+pk_conditions+";");
                                            stmts[b].executeUpdate("UPDATE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+temp+";");
                                            
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
        
        }catch (SQLException ex) {
                ex.getMessage();
        }
    }
    
    
    public ArrayList<Unique_Constraint> Unique(String table_name, String db1) throws SQLException{
        
        ArrayList<Unique_Constraint> unique = new ArrayList<>();
        ArrayList<String> columns_list;
        String constraint;
        String column;
        rst = stmts[a].executeQuery("select CONSTRAINT_NAME, COLUMN_NAME from information_schema.key_column_usage"
              + " where table_name = '"+table_name+"' AND table_schema = '"+db1+"'"
              + " AND constraint_name IN (select constraint_name from information_schema.table_constraints where table_name = '"+table_name+"' and table_schema = '"+db1+"' AND constraint_type = 'UNIQUE');");
            
        while(rst.next()){
            columns_list  = new ArrayList<>();
            constraint = rst.getString(1);
            do{
                column = rst.getString(2);
                columns_list.add(column);

            }while(rst.next() && rst.getString(1).equals(constraint));
            unique.add(new Unique_Constraint(constraint,columns_list));
            rst.previous();
        }
        
        return unique;
    }
    
    
    public String Current_PKs_Values(){
        String pks_val = "";
        for(int i=0 ; i<Primary_key_list.size() ; i++){
            pks_val += Primary_key_list.get(i).getName() + "=" + Primary_key_list.get(i).getValue().get(row);
            if(i <= Primary_key_list.size() - 2)pks_val += " AND ";
        }
        return pks_val;
        
    }
    
    
    public String Current_Unique_Values(String table_name, ArrayList<String> columns_list) throws SQLException{
        String columns = "";
        for(int i=0 ; i<columns_list.size() ; i++){
            columns += columns_list.get(i);
            if(i <= columns_list.size() - 2)columns += ", ";
        }
        
        System.out.println("SELECT "+columns+" FROM "+table_name+" WHERE "+Current_PKs_Values()+";");
        rst = stmts[a].executeQuery("SELECT "+columns+" FROM "+table_name+" WHERE "+Current_PKs_Values()+";");
        rst.next();
        String unique_values = "";
        for(int i=1 ; i<=rst.getMetaData().getColumnCount(); i++){
            
            if(i <= rst.getMetaData().getColumnCount() - 1){
                if(rst.getMetaData().getColumnTypeName(i).contains("CHAR")){
                    unique_values += rst.getMetaData().getColumnName(i) + " = '" + rst.getString(i) + "' AND ";
                }
                else unique_values += rst.getMetaData().getColumnName(i) + " = "+ rst.getString(i) + " AND ";
            }
            
            else{
                if(rst.getMetaData().getColumnTypeName(i).contains("VAR")){
                    unique_values += rst.getMetaData().getColumnName(i) + " = '" + rst.getString(i) + "';";
                }
                else unique_values += rst.getMetaData().getColumnName(i) + " = "+ rst.getString(i) + ";";
                
            }
            
        }
        
        System.out.println("SELECT "+columns+" FROM "+table_name+" WHERE "+unique_values);
        rst2 = stmts[b].executeQuery("SELECT "+columns+" FROM "+table_name+" WHERE "+unique_values);
        return unique_values;
    }
    
    
   
    public void insertion(String table_name) throws SQLException{
        String cond = "";
        String Col_Names = "";
        String content = "";
                        
        for(int i = 0 ; i<Primary_key_list.size() ; i++){
            cond += Primary_key_list.get(i).getName()+"="+Primary_key_list.get(i).getValue().get(row);
            if(i <= Primary_key_list.size() - 2)cond += " AND ";
        }
        try{                
            if(set == null || set.isClosed()){
                rst = stmts[a].executeQuery("SELECT * FROM "+table_name+" WHERE "+cond+";");
                rst.next();
            }
            else rst = set;
            int col_count = rst.getMetaData().getColumnCount();

            for(int i=1 ; i<=col_count ; i++){
                Col_Names += rst.getMetaData().getColumnName(i);
                if(i <= col_count - 1){
                    Col_Names += ", ";
                    if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))content += "'"+rst.getString(i)+"', ";
                    else content += rst.getString(i)+", ";
                }
                else{
                    if(rst.getMetaData().getColumnTypeName(i).contains("CHAR"))content += "'"+rst.getString(i)+"'";
                    else content += rst.getString(i);
                }

            }
            
        String q = "INSERTING INTO ";
        if(b == 0)q += frontend.getDatabase()+" ";
        else q += backoffice.getDatabase()+" ";
        q += table_name+" VALUES ("+content+")";
        report_list.addElement(q);
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
                    for(int j=0 ; j<Primary_key_list.size() ; j++){    
                        if(PKs_To_Use[i].equals(Primary_key_list.get(j).getName())){
                            pk_conditions += PKs_To_Use[i]+"="+Primary_key_list.get(j).getValue().get(row);
                            break;
                        }
                    }
                    if(i <= PKs_To_Use.length - 2){
                        names += ", ";
                        pk_conditions += " AND ";
                    }
                }
                
                if(flag == 0)query="SELECT "+ names + " FROM "+ table_name +" WHERE "+ pk_conditions + ";";
                else{
                    if(a == 0)str = frontend.getDatabase();
                    else str = backoffice.getDatabase();
                    query = "SELECT * FROM "+table_name+" WHERE "+ pk_conditions + " AND " + non_pks_condition +
                            " AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+str+"."+table_name+");";
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
                for (int i = n; i <= Primary_key_list.size() - k; i++){
                    PKs_To_Use[PKs_To_Use.length - k] = Primary_key_list.get(i).getName();
                    random(i + 1, k - 1, PKs_To_Use, table_name);  
                }
            }
    }
}
