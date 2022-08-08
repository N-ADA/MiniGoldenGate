/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minigoldengate;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;


/**
 *
 * @author Yassine Chtaini
 */
public class Structure_synchronization {
   
    DefaultListModel<String> tables_list;
     DefaultListModel<String> report_list;
    FrontEnd frontend;
    BackOffice backoffice;
    
    
   
    int row, flag, row_diff, jar=0, row_count,jj,jjdata;
   
    DefaultListModel file_list=new DefaultListModel<>();
    int nb_bo = 0, nb_fe = 0, row_fe=0, row_bo=0, count=0,count_data=0, a,b;
    ExcelReader reading;
    FileOutputStream fileOut;
    XSSFSheet sheet;
    XSSFWorkbook workbook = new XSSFWorkbook();
    FileOutputStream fileOutdata;
    XSSFSheet sheetdata;
    XSSFWorkbook workbookdata = new XSSFWorkbook();
    String direction;
    ArrayList<String> comment_list= new ArrayList();
    ArrayList<String> ds_comment_list= new ArrayList();
    double time=0;
    List<String> list_filtering= new ArrayList<>();
     List<String> list_update= new ArrayList<>();
    List<String> list_filtering_data= new ArrayList<>();
    File f_ex = new File("path");
    int ln,newmax;
    String path_ex;

  
public Structure_synchronization(FrontEnd frontend, BackOffice backoffice, ExcelReader reading, DefaultListModel report_list) throws FileNotFoundException, IOException, InvalidFormatException{
        report_list = new DefaultListModel<>();
        this.frontend = frontend;
        this.backoffice = backoffice;
        this.reading=reading;
        this.report_list=report_list;
     
        fileOut = new FileOutputStream("Details.xlsx");
        fileOutdata = new FileOutputStream("data_sync_comments.xlsx");
        sheet = workbook.createSheet("Sheet 1");
        sheetdata=workbookdata.createSheet("sheet 1");
       
        
    }

    public int call_function(String table_name, int max){
        
        report_list.addElement("---Checking "+table_name+"---");
         newmax=max;
                long startTime = System.nanoTime();
                try {
                    
                   
                     if(table_count_checker(table_name)==0){
                         
                        primary_key(table_name);
                        column_count_checker(table_name);
                        datatype_checker(table_name);
                        column_difference(table_name);
                        null_func(table_name);
                        constraint_check(table_name);
                        constraint_checker(table_name);
                        constraint_type(table_name);
                        data_length_checker(table_name);
                        default_func(table_name);
                        filter(table_name);
                        call( table_name);
                        
                        list_filtering.clear();
                        long endTime = System.nanoTime();
                        time=endTime*0.000000001-startTime*0.000000001;
                        
                     }
                } catch (Exception ex) {
                    ex.getMessage();
                }
        
        if(list_update.contains("not")==true){
                            return 1;
                             }
        
    return 0;
    }
     public void call(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{
        if (jj==2){
            comment_list.clear();
            comment_list.add("Struture is Sycnhronized\n");
             
            report_list.addElement("Struture is Sycnhronized");
           
            create_excel(table_name,nb_fe,nb_bo,time,"Y",comment_list,direction);
            
            comment_list.clear();
        }else if (jj==1){
            create_excel(table_name,nb_fe,nb_bo,time,"N",comment_list,direction);
          
            comment_list.clear();
            
        }
    
    }
       public int  filter(String table_name){
            jj=0;
          
        if(list_filtering.contains("N")==true){
             jj=1;
             list_update.add("not");
            
        }else if(list_filtering.contains("N")!=true){ 
             jj=2;
             list_update.add("sys");
           
        }
  
            return 0;
  
    }
    
    


    public int table_count_checker(String table_name) throws FileNotFoundException, SQLException, IOException, IOException, IOException, IOException, InvalidFormatException{
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
            

        String q4="show tables from "+frontend.getSchema()+" like '"+table_name+"';";
        ResultSet rst= frontend.getStmt().executeQuery(q4);
        while (rst.next()){
            list4.add(rst.getString(1));
        }
         String q5="show tables from "+backoffice.getSchema()+" like '"+table_name+"' ;";
        ResultSet rst1= backoffice.getStmt().executeQuery(q4);
        while (rst1.next()){
            list5.add(rst1.getString(1));
        }
       
      
         if(list5.isEmpty()==true && list4.isEmpty()==true){
            report_list.addElement("the table you have selected:'"+table_name+"' does not exist in database '"+frontend.getSchema()+"' and '"+backoffice.getSchema()+"'");
            comment_list.add("Table does not exist in both databases\n");
            row_fe=0;
            row_bo=0;
            nb_bo=0;
            nb_fe=0;
            create_excel(table_name,nb_fe,nb_bo,time,"N",comment_list,direction);
            comment_list.clear();
            list_filtering.add("N");
            return 1;
        }                     
        if(list4.isEmpty()==true){
            report_list.addElement("the table you have selected:'"+table_name+"' does not exist in database '"+frontend.getSchema()+"'");
            comment_list.add("Table does not exist in database '"+frontend.getSchema()+"\n");
            row_bo=0;
            row_fe=0;
            nb_bo=0;
            nb_fe=0;
            create_excel(table_name,nb_fe,nb_bo,time,"N",comment_list,direction);
             comment_list.clear();
             list_filtering.add("N");
            return 1;
        }

        if(list5.isEmpty()==true){
            report_list.addElement("the table you have selected:'"+table_name+"' does not exist in database '"+backoffice.getSchema()+"'"); 
            comment_list.add("Table does not exist in database '"+backoffice.getSchema()+"\n");
            row_fe=0;
            row_bo=0;
            nb_bo=0;
            nb_fe=0;
            create_excel(table_name,nb_fe,nb_bo,time,"N",comment_list,direction);
            comment_list.clear();
            list_filtering.add("N");
            return 1;
        }


        return 0;
    }


   
    public void column_count_checker(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{

        String query="show columns from "+frontend.getSchema()+"."+table_name+";";
       
        ResultSet rst = frontend.getStmt().executeQuery(query);
        int count=0;
        while (rst.next())count++;
        nb_fe=count;
        
        count=0;
        query = "show columns from "+backoffice.getSchema()+"."+table_name+";"  ;
        rst = backoffice.getStmt().executeQuery(query);
        while (rst.next())count++; 
        nb_bo=count;
        
       
        
        if(nb_bo==nb_fe){
            
           
        }else{         
          report_list.addElement("Nb of columns  in the '"+frontend.getSchema()+"' and '"+backoffice.getSchema()+"' is not the same\n");
          comment_list.add("Nb of columns  in the '"+frontend.getSchema()+"' and '"+backoffice.getSchema()+"' is not the same\n");
          list_filtering.add("N");
        }

    }
   
public void primary_key(String table_name) throws SQLException{
       ArrayList<String> list_type = new ArrayList();
        ArrayList<String> list_type2 = new ArrayList();
        ArrayList<String> list_names = new ArrayList();
        ArrayList<String> list_names2 = new ArrayList();
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;

        if(reading.FE_list.contains(table_name)==true && reading.getBO_list().contains(table_name)==false){
             direction="FE->BO";
           
        String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
             while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getString(4));
        }
        String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getString(4));
            
        }
        if(list_names.isEmpty()!=true && list_names2.isEmpty()!=true){
            for(count=0;count<list_names.size();count++){
            
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement( " the table in "+backoffice.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add( " the table in "+backoffice.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
            
             for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement( " the table in "+backoffice.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add( " the table in "+backoffice.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            
            }
            if(cc==2){
                cc=0;
                break;
                }
            }
         }else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement(" the table in "+backoffice.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add(" the table in "+backoffice.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement(" the table in "+backoffice.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add(" the table in "+backoffice.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    
             
         
         }

        }else if(reading.FE_list.contains(table_name)==false && reading.BO_list.contains(table_name)==true){
            direction="BO->FE";
            
            String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
             while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getString(4));
        }
        String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getString(4));
            
        } if ( list_names.isEmpty()!=true && list_names.isEmpty()!=true){
            for(count=0;count<list_names.size();count++){
                
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement( " the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add( " the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
            for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement( " the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add( " the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            
            }
            if(cc==2){
                cc=0;
                break;
                }
         
          }
             }else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement(" the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add(" the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement(" the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add(" the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    
             
         
         }



        }else if(reading.getBID_FE_and_BO().contains(table_name)==true ){
            direction="Bidirectional";
          
              String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
             while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getString(4));
        }
        String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getString(4));
            
        }
        if(list_names.isEmpty()!=true && list_names.isEmpty()!=true){
            for(count=0;count<list_names.size();count++){
                
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement( " the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add( " the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
            for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement( " the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add( " the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }
            if(cc==2){
                cc=0;
                break;
                }
         
        }

       }else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement(" the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add(" the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement(" the table in "+frontend.getSchema()+" contains a wrong primary key= ");
                                    comment_list.add(" the table in "+frontend.getSchema()+" contains a wrong primary key\n");
                                    list_filtering.add("N");
                                    
             
         
         }

    }
}

  public void datatype_checker(String table_name) throws SQLException{
      ArrayList<String> list_type = new ArrayList();
        ArrayList<String> list_type2 = new ArrayList();
        ArrayList<String> list_names = new ArrayList();
        ArrayList<String> list_names2 = new ArrayList();
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;
               

        String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        
        while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getString(2));
        }
        
        String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getString(2));
            
        }
        if(list_names.isEmpty()!=true && list_names.isEmpty()!=true){
        for(count=0;count<list_names.size();count++){
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement("different datatypes  in both databases\n");
                                    comment_list.add("different datatypes  in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
                  for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement("different datatypes  in both databases\n");
                                    comment_list.add("different datatypes  in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            
            
            }
            if(cc==2){
                cc=0;
                break;
                }
         
        } }else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement("different datatypes  in both databases\n");
                                    comment_list.add("different datatypes  in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement("different datatypes  in both databases\n");
                                    comment_list.add("different datatypes  in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }
    }
 
    public void constraint_checker(String table_name) throws SQLException{
        ArrayList<String> list_type = new ArrayList();
        ArrayList<String> list_type2 = new ArrayList();
        
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;
        
         String q4="select constraint_name from information_schema.table_constraints where constraint_schema='"+frontend.getSchema()+"' AND TABLE_NAME= '"+table_name+"'";
         ResultSet rst49 = frontend.getStmt().executeQuery(q4);
              while (rst49.next()){
                 
                  list_type.add(rst49.getString(1));
                 
              }
         String q5="select constraint_name from information_schema.table_constraints where constraint_schema='"+backoffice.getSchema()+"' AND TABLE_NAME= '"+table_name+"'";
         ResultSet rst48 = backoffice.getStmt().executeQuery(q5);
              while (rst48.next()){
                 
                  list_type2.add(rst48.getString(1));
              }
              
              if(list_type.size()== list_type2.size()){
                     if( list_type.equals( list_type2)!=true ){
                                    report_list.addElement("different constraint_name  in both databases\n");
                                    comment_list.add("different constraint_name  in both databases\n");
                                    list_filtering.add("N");
                                    
                            }
              }else {
                                   report_list.addElement("different constraint_name  in both databases\n");
                                    comment_list.add("different constraint_name  in both databases\n");
                                    list_filtering.add("N");
              
              }
        
         
    }
     public void constraint_type(String table_name) throws SQLException{
        ArrayList<String> list_type = new ArrayList();
        ArrayList<String> list_type2 = new ArrayList();
      
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;
        
         String q4="select * from information_schema.table_constraints where constraint_schema='"+frontend.getSchema()+"'AND TABLE_NAME= '"+table_name+"'";
         ResultSet rst49 = frontend.getStmt().executeQuery(q4);
              while (rst49.next()){
                  
                  list_type.add(rst49.getString(6));
              }
         String q5="select * from information_schema.table_constraints where constraint_schema='"+backoffice.getSchema()+"'AND TABLE_NAME= '"+table_name+"'" ;
         ResultSet rst48 = backoffice.getStmt().executeQuery(q5);
              while (rst48.next()){
                 
                  list_type2.add(rst48.getString(6));
              }
        if(list_type.isEmpty()!=true && list_type2.isEmpty()!=true){
         if( list_type.equals( list_type2)!=true ){
                                    report_list.addElement("different constraint_type  in both databases\n");
                                    comment_list.add("different constraint_type  in both databases\n");
                                    list_filtering.add("N");
                                    
                            }
        }else if(list_type.isEmpty()!=true && list_type2.isEmpty()==true){
                
                                    report_list.addElement("different constraint_type  in both databases\n");
                                    comment_list.add("different constraint_type  in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_type.isEmpty()==true && list_type2.isEmpty()!=true){
                
                                    report_list.addElement("different constraint_type  in both databases\n");
                                    comment_list.add("different constraint_type  in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }
        
    }
      public void constraint_check(String table_name) throws SQLException{
        ArrayList<String> list_type = new ArrayList();
        ArrayList<String> list_type2 = new ArrayList();
        ArrayList<String> list_names = new ArrayList();
        ArrayList<String> list_names2 = new ArrayList();
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;
        
         String q4="SELECT CONSTRAINT_NAME, CHECK_CLAUSE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS natural join INFORMATION_SCHEMA.CHECK_CONSTRAINTS WHERE TABLE_NAME = '"+table_name+"' AND TABLE_SCHEMA = '"+frontend.getSchema()+"';";
            
         ResultSet rst49 = frontend.getStmt().executeQuery(q4);
              while (rst49.next()){
                  list_names.add(rst49.getString(1));
                  list_type.add(rst49.getString(2));
              }
              
              
         String q5="SELECT CONSTRAINT_NAME, CHECK_CLAUSE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS natural join INFORMATION_SCHEMA.CHECK_CONSTRAINTS WHERE TABLE_NAME = '"+table_name+"' AND TABLE_SCHEMA = '"+backoffice.getSchema()+"';";
        
         ResultSet rst48 = backoffice.getStmt().executeQuery(q5);
              while (rst48.next()){
                  list_names2.add(rst48.getString(1));
                  list_type2.add(rst48.getString(2));
              }
             
         if(list_names.isEmpty()!=true && list_names2.isEmpty()!=true){
              for(count=0;count<list_names.size();count++){
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement("different check_clause in both databases\n");
                                    comment_list.add("different check_clause  in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement("different check_clause in both databases\n");
                                    comment_list.add("different check_clause in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }
            if(cc==2){
                cc=0;
                break;
                }
         
        }
         }else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement("different check_clause in both databases\n");
                                    comment_list.add("different check_clause in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement("different check_clause in both databases\n");
                                    comment_list.add("different check_clause in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }
    }
  public void column_difference(String table_name) throws SQLException{
        List<String> list_fe = new ArrayList<>();
        List<String> list_bo = new ArrayList<>();
        String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        while (rst44.next()){
            list_fe.add(rst44.getString(1));
        }
         String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_bo.add(rst45.getString(1));
        }
        
        List<String> list = new ArrayList<>(CollectionUtils.disjunction(list_fe, list_bo));
        if(list.isEmpty()!=true){
                report_list.addElement("the different columns between the two tables are "+list);
                comment_list.add(" the different columns between the two tables are "+list+"\n");
                list_filtering.add("N");
        }
    }
 
   public void default_func(String table_name) throws SQLException{
        ArrayList<Object> list_type = new ArrayList();
        ArrayList<Object> list_type2 = new ArrayList();
        ArrayList<String> list_names = new ArrayList();
        ArrayList<String> list_names2 = new ArrayList();
         ArrayList<String> verify = new ArrayList();
        int i=0,count2=0;
        int j=0;
        int count;
        int cc = 0;
               

        String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        
        while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getObject(5));
            
        }
        
        String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getObject(5));
         
        }if(list_names.isEmpty()!=true && list_names2.isEmpty()!=true){
         for(count=0;count<list_names.size();count++){
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                                      if(list_type.get(count)==null && list_type2.get(count)!=null){
                                          verify.add("d");
                                         
                                         break;
                                 }else if (list_type.get(count)==null && list_type2.get(count)==null){
            
                                        break;
            
                                 }else if(list_type.get(count)!=null && list_type2.get(count)==null){
                                    verify.add("d");
                                   
                                    break;
                                  }else if(list_type.get(count)!=null && list_type2.get(count)!=null){
                                        
                                        if(list_type.get(count).equals(list_type2.get(count))==true){
                                        
                                            break;
                                        }else{
                                             verify.add("d");
                                               break;
                                        }
                                     }
                        }
                }
            
            }
            if(list_type.get(count)==null && list_type2.get(count)!=null){
                verify.add("d");
                      
                }else if (list_type.get(count)==null && list_type2.get(count)==null){
            
                }else if(list_type.get(count)!=null && list_type2.get(count)==null){
                verify.add("d");
                  
            }else if(list_type.get(count)!=null && list_type2.get(count)!=null){
                
                                        if(list_type.get(count).equals(list_type2.get(count))==true){
                                        
                                            break;
                                        }else{
                                             verify.add("d");
                                    
                                               break;
            }
        }
         }}else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement("different default in both databases\n");
                                    comment_list.add("different default in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement("different default in both databases\n");
                                    comment_list.add("different default in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }
         if(verify.contains("d")==true){
              report_list.addElement("different default  in both databases\n");
                   comment_list.add("different default  in both databases\n");
                   list_filtering.add("N");
                   verify.clear();
         }
    
    
   }
       public void null_func(String table_name) throws SQLException{
        ArrayList<String> list_type = new ArrayList();
        ArrayList<String> list_type2 = new ArrayList();
        ArrayList<String> list_names = new ArrayList();
        ArrayList<String> list_names2 = new ArrayList();
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;
               

        String q31="desc "+frontend.getSchema()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        
        while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getString(3));
        }
        
        String q32="desc "+backoffice.getSchema()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getString(3));
            
        }
        if(list_names.isEmpty()==true && list_names2.isEmpty()==true){
        for(count=0;count<list_names.size();count++){
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                              
                                    report_list.addElement("different null values in both databases\n");
                                    comment_list.add("different null values in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                             
                                    report_list.addElement("different null values in both databases\n");
                                    comment_list.add("different null values in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        
                        
                        }
                       
                }
            
            
            }
            if(cc==2){
                cc=0;
                break;
                }
         
        }}else if(list_names.isEmpty()!=true && list_names2.isEmpty()==true){
                
                                    report_list.addElement("different null values in both databases\n");
                                    comment_list.add("different null values in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }else if (list_names.isEmpty()==true && list_names2.isEmpty()!=true){
                
                                    report_list.addElement("different null values in both databases\n");
                                    comment_list.add("different null values in both databases\n");
                                    list_filtering.add("N");
                                    
             
         
         }
       
    
    }
    public void data_length_checker(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{
        ArrayList<Integer> list_type = new ArrayList();
        ArrayList<Integer> list_type2 = new ArrayList();
        ArrayList<String> list_names = new ArrayList();
        ArrayList<String> list_names2 = new ArrayList();
        int i=0,count2=0;
        int j=0;
       int count;
       int cc = 0;
               

        String q31="Select column_name, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+frontend.getSchema()+"' AND TABLE_NAME= '"+table_name+"'";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        
        while (rst44.next()){
            list_names.add(rst44.getString(1));
            list_type.add(rst44.getInt(2));
        }
        
        String q32="Select  column_name,CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+backoffice.getSchema()+"' AND TABLE_NAME= '"+table_name+"'";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_names2.add(rst45.getString(1));
            list_type2.add(rst45.getInt(2));
            
        }
        
        for(count=0;count<list_names.size();count++){
            if(list_names.get(count).equals(list_names2.get(count))!=true ){
                
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement("different datatypes length  in both databases\n");
                                    comment_list.add("different datatypes length  in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        }
                }
            
            }else if(list_type.get(count).equals(list_type2.get(count))!=true){
                for(count2=0;count2<list_names2.size();count2++){
                        if(list_names.get(count).equals(list_names2.get(count2))==true){
                            if(list_type.get(count).equals(list_type2.get(count2))!=true){
                               
                                    report_list.addElement("different datatypes length in both databases\n");
                                    comment_list.add("different datatypes length in both databases\n");
                                    list_filtering.add("N");
                                    cc=2;
                                    break;
                            }
                        }
                }
            
            }
            if(cc==2){
                cc=0;
                break;
                }
        }
    }

   
    public void create_excel(String table_name,int Col_fe,int COL_bo,double time,String Status,ArrayList<String> comment_list,String direction) throws FileNotFoundException, IOException, InvalidFormatException{
            
            File xlsxFile = new File("Details.xlsx");
            String listString = String.join("", comment_list);
            Object[][] newinfo = {{table_name,Col_fe,COL_bo,time,Status,listString,direction}};
            FileInputStream inputStream = new FileInputStream(xlsxFile);
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();
            Object[][] newinfo1 = {{"table_name","Col_fe","COL_bo","Elapsed time(s)","Status","Comments","Direction"}};
            for (Object[] details : newinfo1) {

                Row row1 = sheet.createRow(0);
                int columnCount1 = 0;

                for (Object info : details) {
                    Cell cell = row1.createCell(columnCount1++);
                    if (info instanceof String) {
                        cell.setCellValue((String) info);
                       
                        if(columnCount1==5){
                        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(5);}
                        else if(columnCount1==6){
                          CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(6);
                        } if(columnCount1==7){
                        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(7);}
                        else if(columnCount1==1){
                          CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(1);
                        }
                    }else if (info instanceof Integer) {
                        cell.setCellValue((Integer) info);
                         if(columnCount1==2){
                        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(2);}
                        else if(columnCount1==3){
                          CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(3);
                        } 
                    }else if (info instanceof Double) {
                        cell.setCellValue((Double) info);
                        if(columnCount1==4){
                          CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                        sheet.autoSizeColumn(4);
                        }
                    } 
                }
            }      
            for (Object[] details : newinfo) {

                Row row = sheet.createRow(++rowCount);

                int columnCount = 0;


                for (Object info : details) {


                        Cell cell = row.createCell(columnCount++);
                        if (info instanceof String) {
                        cell.setCellValue((String) info);
                         if(columnCount==1){
                        cell.setCellValue(table_name+"");
                       CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
                       sheet.autoSizeColumn(1);
                       
                        }else if(columnCount==5){
                            cell.setCellValue(Status+"");
                            CellUtil.setVerticalAlignment(cell,  VerticalAlignment.CENTER);
                            sheet.autoSizeColumn(5);
                        }else if(columnCount==6){
                            cell.setCellValue(listString);
                            CellUtil.setVerticalAlignment(cell,  VerticalAlignment.JUSTIFY);
                             sheet.autoSizeColumn(6);
                        }else if(columnCount==7){
                            cell.setCellValue(direction+"");
                            CellUtil.setVerticalAlignment(cell,  VerticalAlignment.CENTER);
                            sheet.autoSizeColumn(7);
                        }
                        } else if (info instanceof Integer) {
                        cell.setCellValue((Integer) info);
                        if(columnCount==2){
                        cell.setCellValue(Col_fe);
                       CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
                       sheet.autoSizeColumn(2);
                        }else if(columnCount==3){
                        cell.setCellValue(COL_bo);
                       CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
                       sheet.autoSizeColumn(3);
                        }
                        }  else if (info instanceof Double) {
                           cell.setCellValue((Double) info);
                           if(columnCount==4){
                        cell.setCellValue(time);
                       CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
                       sheet.autoSizeColumn(4);
                        }
                                 } 
                }
            }
                if(Status.contains("N")){
                    jar++;
                    }else{ jar=0;}
            inputStream.close();
            FileOutputStream os = new FileOutputStream(xlsxFile);
            workbook.write(os);

            //Close the workbook and output stream
            count++;
            if(count==newmax){  
            workbook.close();


            os.close();

        }
    }

    public DefaultListModel<String> getReport_list() {
        return report_list;
    }

  
   
}