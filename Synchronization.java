package com.mycompany.minigoldengate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author nadabounajma
 */
public class Synchronization extends javax.swing.JFrame {

    Statement[] stmts = new Statement[4];
    DefaultListModel<String> tables_list, report_list;
    FrontEnd frontend;
    BackOffice backoffice;
    ResultSet rst, rst2, rst3, set;
    ArrayList<Primary_key> Primary_key_list = new ArrayList<>();
    ArrayList<String> Non_PK_Column_names = new ArrayList<>();
    String pks = "", names = "", conditions = "", condition = "", query, str;
    int row, flag, row_diff, jar=0, row_count,jj;
    boolean found, pk_changed, unique_found;
    DefaultListModel file_list=new DefaultListModel<>();
    int nb_bo = 0, nb_fe = 0, row_fe=0, row_bo=0, count=0, a,b;
    ExcelReader reading;
    FileOutputStream fileOut;
    XSSFSheet sheet;
    XSSFWorkbook workbook = new XSSFWorkbook();
    String direction;
    ArrayList<String> comment_list= new ArrayList();
    double time=0;
    List<String> list_filtering= new ArrayList<>();
    
    
    public Synchronization(){
        initComponents();
    }
    
    public Synchronization(FrontEnd frontend, BackOffice backoffice) throws FileNotFoundException, IOException, InvalidFormatException{
        initComponents();
        Data_Sync_Button.setEnabled(false);
        Golden_Gate_Button.setEnabled(false);
        this.frontend = frontend;
        this.backoffice = backoffice;
        stmts[0] = this.frontend.getStmt();
        stmts[1] = this.backoffice.getStmt();
        fileOut = new FileOutputStream("Details.xlsx");
        sheet = workbook.createSheet("Sheet 1");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        Read_Excel_Button = new javax.swing.JButton();
        excel_name = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        Structure_Sync_Button = new javax.swing.JButton();
        Data_Sync_Button = new javax.swing.JButton();
        Golden_Gate_Button = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tables");

        jScrollPane1.setViewportView(jList);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setLabelFor(jLabel2);

        Read_Excel_Button.setText("Read Excel Sheet");
        Read_Excel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Read_Excel_ButtonActionPerformed(evt);
            }
        });

        excel_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excel_nameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(excel_name, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(Read_Excel_Button)
                        .addGap(40, 40, 40))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(excel_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Read_Excel_Button)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        jLabel3.setText("FrontEnd");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        jLabel4.setText("BackEnd");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setAutoscrolls(false);
        jScrollPane2.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        Structure_Sync_Button.setText("Structure Synchronization");
        Structure_Sync_Button.setSize(new java.awt.Dimension(160, 30));
        Structure_Sync_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Structure_Sync_ButtonActionPerformed(evt);
            }
        });

        Data_Sync_Button.setText("Data Synchronization");
        Data_Sync_Button.setPreferredSize(new java.awt.Dimension(160, 30));
        Data_Sync_Button.setSize(new java.awt.Dimension(160, 30));
        Data_Sync_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Data_Sync_ButtonActionPerformed(evt);
            }
        });

        Golden_Gate_Button.setText("Enable GoldenGate");
        Golden_Gate_Button.setPreferredSize(new java.awt.Dimension(160, 30));
        Golden_Gate_Button.setSize(new java.awt.Dimension(160, 30));
        Golden_Gate_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Golden_Gate_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(199, 199, 199))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(Structure_Sync_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(Data_Sync_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(Golden_Gate_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Structure_Sync_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Data_Sync_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Golden_Gate_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Structure_Sync_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Structure_Sync_ButtonActionPerformed
        
        report_list = new DefaultListModel<>();
        
        for (int i = 0; i < jList.getModel().getSize(); i++){    
            String table_name = jList.getModel().getElementAt(i);
            report_list.addElement("---Checking "+table_name+"---");
            
                long startTime = System.nanoTime();
                try {
                     if(table_count_checker(table_name)==0){
                        primary_key(table_name);
                        row_count(table_name);
                        column_count_checker(table_name);
                        datatype_checker(table_name);
                        column_difference(table_name);
                        data_length_checker(table_name);
                        long endTime = System.nanoTime();
                        time=endTime*0.000000001-startTime*0.000000001;
                        default_func(table_name);
                        null_func(table_name);
                        filter(table_name);
                        call( table_name);
                        list_filtering.clear();
                     }
                } catch (Exception ex) {
                    ex.getMessage();
                }
        
        }
        
        new Structure_Report(report_list).setVisible(true);
        if(jj == 2){
            System.out.println("synchro");
            Data_Sync_Button.setEnabled(true);
            Golden_Gate_Button.setEnabled(true);
        }
        
        
    }//GEN-LAST:event_Structure_Sync_ButtonActionPerformed

   
    public void call(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{
        if (jj==2){
            comment_list.clear();
            comment_list.add("Struture is Sycnhronized");
            report_list.addElement("Struture is Sycnhronized");
            create_excel(table_name,row_fe,row_bo,nb_fe,nb_bo,time,"Y",comment_list,direction);
            comment_list.clear();
        }else if (jj==1){
            create_excel(table_name,row_fe,row_bo,nb_fe,nb_bo,time,"N",comment_list,direction);
            comment_list.clear(); 
        }
    
    }
       public int  filter(String table_name){
            jj=0;
          
        if(list_filtering.contains("N")==true){
             jj=1;
            
        }else if(list_filtering.contains("N")!=true){ 
             jj=2;
           
        }
  
            return 0;
  
    }
    public void primary_key(String table_name) throws SQLException{

        String a = null;
        String b = null;
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
      

        if(reading.FE_list.contains(table_name)==true && reading.getBO_list().contains(table_name)==false){
             direction="FE->BO";
             String q4="show columns FROM "+frontend.getDatabase()+"."+table_name+" where `Key` = 'PRI';"  ;


            ResultSet rst49 = frontend.getStmt().executeQuery(q4);
              while (rst49.next()){
                  list4.add(rst49.getString(1));
              }
           String q5="show columns FROM "+backoffice.getDatabase()+"."+table_name+" where `Key` = 'PRI';"  ;
           ResultSet rst50= backoffice.getStmt().executeQuery(q5);

              while (rst50.next()){
                  list5.add(rst50.getString(1));
              }
              if(list5.equals(list4)==true){
                   
                   
          }else{
                  report_list.addElement("the table in "+backoffice.getDatabase()+" contains a wrong primary key");
                  comment_list.add(" the table in "+backoffice.getDatabase()+" contains a wrong primary key= "+"\n");
                  list_filtering.add("N");

          }
        }else if(reading.FE_list.contains(table_name)==false && reading.BO_list.contains(table_name)==true){
            direction="BO->FE";
            String q4="show columns FROM "+frontend.getDatabase()+"."+table_name+" where `Key` = 'PRI';"  ;
            ResultSet rst49 = frontend.getStmt().executeQuery(q4);

              while (rst49.next()){
                  list4.add(rst49.getString(1));
              }
              String q5="show columns FROM "+backoffice.getDatabase()+"."+table_name+" where `Key` = 'PRI';"  ;
              ResultSet rst50= backoffice.getStmt().executeQuery(q5);
              while (rst50.next()){
                  list5.add(rst50.getString(1));
              }
              if(list5.equals(list4)==true){
                
                 
          }else{
                  report_list.addElement("the table in "+frontend.getDatabase()+" contains a wrong primary key");
                  comment_list.add(" the table in "+frontend.getDatabase()+" contains a wrong primary key");
                  list_filtering.add("N");
          }



        }else if(reading.getBID_FE_and_BO().contains(table_name)==true ){
            direction="Bidirectional";
             String q4="show columns FROM "+frontend.getDatabase()+"."+table_name+" where `Key` = 'PRI';"  ;
             ResultSet rst49 = frontend.getStmt().executeQuery(q4);

              while (rst49.next()){
                  list4.add(rst49.getString(1));
              }
             String q5="show columns FROM "+backoffice.getDatabase()+"."+table_name+" where `Key` = 'PRI';"  ;
               ResultSet rst50= backoffice.getStmt().executeQuery(q5);

              while (rst50.next()){
                  list5.add(rst50.getString(1));
              }
               if(list5.equals(list4)==true){
                  
                  
          }else{
                 report_list.addElement("the table in "+frontend.getDatabase()+" contains a wrong primary key");
                  comment_list.add(" the table in "+frontend.getDatabase()+" contains a wrong primary key");
                  list_filtering.add("N");
          }
        }



    }


    public int table_count_checker(String table_name) throws FileNotFoundException, SQLException, IOException, IOException, IOException, IOException, InvalidFormatException{
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
            

        String q4="show tables from "+frontend.getDatabase()+" like '"+table_name+"';";
        ResultSet rst= frontend.getStmt().executeQuery(q4);
        while (rst.next()){
            list4.add(rst.getString(1));
        }
         String q5="show tables from "+backoffice.getDatabase()+" like '"+table_name+"' ;";
        ResultSet rst1= backoffice.getStmt().executeQuery(q4);
        while (rst1.next()){
            list5.add(rst1.getString(1));
        }
       

         if(list5.isEmpty()==true && list4.isEmpty()==true){
            report_list.addElement("the table you have selected:'"+table_name+"' does not exist in database '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"'");
            comment_list.add("Table does not exist in both databases");
            row_fe=0;
            row_bo=0;
            create_excel(table_name,row_fe,row_bo,nb_fe,nb_bo,time,"N",comment_list,direction);
            comment_list.clear();
            return 1;
        }                     
        if(list4.isEmpty()==true){
            report_list.addElement("the table you have selected:'"+table_name+"' does not exist in database '"+frontend.getDatabase()+"'");
            comment_list.add("Table does not exist in database '"+frontend.getDatabase());
            row_bo=0;
            row_fe=0;
            create_excel(table_name,row_fe,row_bo,nb_fe,nb_bo,time,"N",comment_list,direction);
             comment_list.clear();
            return 1;
        }

        if(list5.isEmpty()==true){
            report_list.addElement("the table you have selected:'"+table_name+"' does not exist in database '"+backoffice.getDatabase()+"'"); 
            comment_list.add("Table does not exist in database '"+backoffice.getDatabase());
            row_fe=0;
            row_bo=0;
            create_excel(table_name,row_fe,row_bo,nb_fe,nb_bo,time,"N",comment_list,direction);
            comment_list.clear();
            return 1;
        }


        return 0;
    }

    public void row_count(String table_name) throws SQLException{

        String q4="SELECT COUNT(*) FROM "+frontend.getDatabase()+"."+table_name+";"  ;

        ResultSet rst49 = frontend.getStmt().executeQuery(q4);

        while (rst49.next()){
            row_fe=rst49.getInt(1);
        }

         String q5="SELECT COUNT(*) FROM "+backoffice.getDatabase()+"."+table_name+";"  ;
        ResultSet rst400= backoffice.getStmt().executeQuery(q5);

        while (rst400.next()){
            row_bo=rst400.getInt(1);
        }
         
    }
   
   
    public void column_count_checker(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{

        String q="show columns from "+frontend.getDatabase()+"."+table_name+";";
       
        ResultSet rst1 = frontend.getStmt().executeQuery(q);
        int cnt=0;
        while (rst1.next())cnt++;
        nb_fe=cnt;
        
        cnt=0;
        q = "show columns from "+backoffice.getDatabase()+"."+table_name+";"  ;
        rst1 = backoffice.getStmt().executeQuery(q);
        while (rst1.next())cnt++; 
        nb_bo=cnt;
        
       
        
        if(nb_bo==nb_fe){
            
           
        }else{         
          report_list.addElement("Nb of columns  in the '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"' is not the same");
          comment_list.add("Nb of columns  in the '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"' is not the same");
          list_filtering.add("N");
        }

    }



  public void datatype_checker(String table_name) throws SQLException{
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();

        String q31="desc "+frontend.getDatabase()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        while (rst44.next()){
            list4.add(rst44.getString(2));
        }
        String q32="desc "+backoffice.getDatabase()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list5.add(rst45.getString(2));
        }
        
        if(list4.containsAll(list5)==true && list5.containsAll(list4)==true){
           
            
        }
        else{ 
           report_list.addElement("different datatypes  in both databases");
           comment_list.add("different datatypes  in both databases");
           list_filtering.add("N");
        }

    }
 
  public void column_difference(String table_name) throws SQLException{
        List<String> list_fe = new ArrayList<>();
        List<String> list_bo = new ArrayList<>();
        String q31="desc "+frontend.getDatabase()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        while (rst44.next()){
            list_fe.add(rst44.getString(1));
        }
         String q32="desc "+backoffice.getDatabase()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list_bo.add(rst45.getString(1));
        }
        
        List<String> list = new ArrayList<>(CollectionUtils.disjunction(list_fe, list_bo));
        if(list.isEmpty()!=true){
                report_list.addElement("the different columns between the two tables are "+list);
                comment_list.add(" the different columns between the two tables are "+list);
                list_filtering.add("N");
        }
    }
 
    public void default_func(String table_name) throws SQLException{
         List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();

        String q31="desc "+frontend.getDatabase()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        while (rst44.next()){
            list4.add(rst44.getString(5));
        }
        String q32="desc "+backoffice.getDatabase()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list5.add(rst45.getString(5));
        }
        
        if(list4.size()==list5.size() && list4.equals(list5)==true && list5.equals(list4)==true){
           
            
        }
        else{ 
           report_list.addElement("different DEFAULT field  in both databases");
           comment_list.add("different DEFAULT field  in both databases");
           list_filtering.add("N");
        }
    
    
    }
       public void null_func(String table_name) throws SQLException{
         List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();

        String q31="desc "+frontend.getDatabase()+"."+table_name+";";
        ResultSet rst44 = frontend.getStmt().executeQuery(q31);
        while (rst44.next()){
            list4.add(rst44.getString(3));
        }
        String q32="desc "+backoffice.getDatabase()+"."+table_name+";";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list5.add(rst45.getString(3));
        }
        
        ;
        if( list4.size()==list5.size()&& list4.equals(list5)==true && list5.equals(list4)==true){
           
        }
        else{ 
           report_list.addElement("different NULL field  in both databases");
           comment_list.add("different NULL field  in both databases");
           list_filtering.add("N");
        }
    
    
    }
    public int data_length_checker(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{

        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String q="SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+frontend.getDatabase()+"' AND TABLE_NAME= '"+table_name+"'";
        ResultSet rst = frontend.getStmt().executeQuery(q);
        while (rst.next()){
            list.add(rst.getString(1));
        }

        String q2="SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+backoffice.getDatabase()+"' AND TABLE_NAME= '"+table_name+"'";
        ResultSet rst2 = backoffice.getStmt().executeQuery(q2);
        while (rst2.next()){
            list2.add(rst2.getString(1));
        }
        if(list.equals(list2)==true){
           
            return 1;
        }
        else{
             report_list.addElement(" different datatypes length in both databases");
             comment_list.add(" different datatypes length in both databases");
             list_filtering.add("N");
            return 3;
        }
        
   }

    
    public void create_excel(String table_name,int Rows_FE,int Rows_BO,int Col_fe,int COL_bo,double time,String Status,ArrayList<String> comment_list,String direction) throws FileNotFoundException, IOException, InvalidFormatException{
            int max=jList.getModel().getSize();
            File xlsxFile = new File("Details.xlsx");
            String listString = String.join(", ", comment_list);
            Object[][] newinfo = {{table_name,Rows_FE,Rows_BO,Col_fe,COL_bo,time,Status,listString,direction}};
            FileInputStream inputStream = new FileInputStream(xlsxFile);
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();
            Object[][] newinfo1 = {{"table_name","Rows_FE","Rows_BO","Col_fe","Col_bo","Elapsed time(s)","Status","Comments","Direction"}};
            for (Object[] details : newinfo1) {

                Row row1 = sheet.createRow(0);
                int columnCount1 = 0;

                for (Object info : details) {
                    Cell cell = row1.createCell(columnCount1++);
                    if (info instanceof String) {
                        cell.setCellValue((String) info);
                    }else if (info instanceof Integer) {
                        cell.setCellValue((Integer) info);
                    }else if (info instanceof Double) {
                        cell.setCellValue((Double) info);
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
                        } else if (info instanceof Integer) {
                        cell.setCellValue((Integer) info);
                        }  else if (info instanceof Double) {
                           cell.setCellValue((Double) info);
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
            if(count==max){  
            workbook.close();


            os.close();

            System.out.println("Excel file has been updated successfully.");

        }
    }

    

    private void Golden_Gate_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Golden_Gate_ButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Golden_Gate_ButtonActionPerformed

    private void excel_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excel_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_excel_nameActionPerformed

    private void Read_Excel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Read_Excel_ButtonActionPerformed

        String excel = excel_name.getText();
        tables_list = new DefaultListModel<>();
        reading = new ExcelReader();
        
        try {
            tables_list.addAll(reading.check_status(excel));
        } catch (IOException ex) {
           ex.getMessage();
        }
        
        jList.setModel(tables_list);
        DisplayTables_OnClick();
        
    }//GEN-LAST:event_Read_Excel_ButtonActionPerformed
   
    public void DisplayTables_OnClick(){
        jList.addMouseListener(new MouseListener(){
           
            @Override
            public void mousePressed(MouseEvent e) {
                DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
                model1.setRowCount(0);
                DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
                model2.setRowCount(0);
                Object[] row;
                int index = jList.getSelectedIndex();
                String table_name = tables_list.getElementAt(index);
                rst = frontend.TableContent(table_name);
                rst2 = backoffice.TableContent(table_name);
                
                try {
                    if(rst.isBeforeFirst()){
                        ResultSetMetaData rsmd = rst.getMetaData();
                        int c = rsmd.getColumnCount();
                        DefaultTableModel dtm = new DefaultTableModel();
                        for (int i = 1; i <= c; i++)dtm.addColumn(rsmd.getColumnName(i)); 
                        while (rst.next()){
                            row = new Object[c];
                            for (int i = 0; i < c; i++)row[i] = rst.getString(i + 1);
                            dtm.addRow(row);
                        }
                        jTable1.setModel(dtm);
                        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        for (int i = 0; i < c; i++)jTable1.getColumnModel().getColumn(i).setPreferredWidth(100);
                    }
                    
                    if(rst2.isBeforeFirst()){
                        ResultSetMetaData rsmd2 = rst2.getMetaData();
                        int c2 = rsmd2.getColumnCount();
                        DefaultTableModel dtm2 = new DefaultTableModel();
                        for (int i = 1; i <= c2; i++)dtm2.addColumn(rsmd2.getColumnName(i));
                        while (rst2.next()) {
                        row = new Object[c2];
                        for (int i = 0; i < c2; i++)row[i] = rst2.getString(i + 1);
                        dtm2.addRow(row);
                        }
                        jTable2.setModel(dtm2);
                        jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        for (int i = 0; i < c2; i++)jTable2.getColumnModel().getColumn(i).setPreferredWidth(100);
                    }
                
                    jScrollPane2.getHorizontalScrollBar();
                    jScrollPane3.getHorizontalScrollBar();
                    
                }catch (SQLException ex) {
                    System.err.println("SQLException: " + ex);
                }
                
            }
             @Override
            public void mouseClicked(MouseEvent e) {  
            }
            @Override
            public void mouseReleased(MouseEvent e) { 
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) { 
            }
        }
        );
    }
    
    private void Data_Sync_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Data_Sync_ButtonActionPerformed

        report_list = new DefaultListModel<>();
        //iterate through the list1 of the tables
        long startTime = System.nanoTime();
        if(!reading.getFE_list().isEmpty())report_list.addElement("FE -> BO");
        for (int i = 0; i < reading.getFE_list().size(); i++) {
            String table_name = reading.getFE_list().get(i);
            report_list.addElement("---Checking "+table_name+"---");
            System.out.println("---Checking "+table_name+"---");
            Data_Synchronization(table_name,1);

        }
        report_list.addElement("");
        report_list.addElement("");
        if(!reading.getBO_list().isEmpty())report_list.addElement("BO -> FE");
        for (int i = 0; i < reading.getBO_list().size(); i++) {
            String table_name = reading.getBO_list().get(i);
            report_list.addElement("---Checking "+table_name+"---");
            System.out.println("---Checking "+table_name+"---");
            Data_Synchronization(table_name,2);

        }
        
        report_list.addElement("");
        report_list.addElement("");
        if(!reading.getBID_FE_and_BO().isEmpty())report_list.addElement("FE -> BO && BO -> FE");
        for (int i = 0; i < reading.getBID_FE_and_BO().size(); i++) {
            String table_name = reading.getBID_FE_and_BO().get(i);
            report_list.addElement("---Checking "+table_name+"---");
            System.out.println("---Checking "+table_name+"---");
            Data_Synchronization(table_name,3);

        }
        long endTime = System.nanoTime();
        time=endTime*0.000000001-startTime*0.000000001;
        new Data_Report(report_list).setVisible(true);
        
    }//GEN-LAST:event_Data_Sync_ButtonActionPerformed
   
  
    public void Data_Synchronization(String table_name, int sync){
        int loop = 0;
        try{
        if(sync == 1){a = 0; b = 1;}
        if(sync == 2){a = 1; b = 0;}
        else if(sync == 3){a = 0; b = 1; loop = -1;}
        String q, data_type, column_values, db1, db2,unique_values_condition = "";
        String compare1 = "",compare2="";
        int k;
        boolean IsChar;
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
                
                //DISABLING FOREIGN KEY CONSTRAINTS   
                stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
                
                
                if(!(sync == 3 && a == 0)){
                    //SYNCING NOT NULL CONSTRAINTS - we may get a NOT NULL CONSTRAINT ERROR when trying to delete from target
                    rst = stmts[a].executeQuery("desc "+table_name+";");
                    while(rst.next()){
                        if(rst.getString(3).equals("YES"))
                        stmts[b].executeUpdate("ALTER TABLE "+table_name+" MODIFY "+rst.getString(1)+" "+rst.getString(2)+" "+"NULL;");    
                    }
                    
                    
                    //SYNCING CHECK CONSTRAINTS - we may get a CHECK CONSTRAINT ERROR when trying to insert values in the target table
                    rst2 = stmts[b].executeQuery("SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = '"+db2+"' AND TABLE_NAME = '"+table_name+"' AND CONSTRAINT_TYPE = 'CHECK';");
                    while(rst2.next()){
                        rst = stmts[a].executeQuery("SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = '"+db1+"' AND TABLE_NAME = '"+table_name+"' AND CONSTRAINT_NAME = '"+rst2.getString(1)+"' AND CONSTRAINT_TYPE = 'CHECK';");
                        if(!rst.next()){
                            try{
                                System.out.println("ALTER TABLE "+table_name+" DROP CHECK "+rst2.getString(1)+";");
                                stmts[3].executeUpdate("ALTER TABLE "+table_name+" DROP CHECK "+rst2.getString(1)+";");
                            }catch(SQLException ex){
                                System.out.println("Error0 : "+ex.getMessage());
                            }
                        }

                    }//the target now has no CHECK CONSTRAINTS that the Source does not have

                    //SYNCING CHECK CONSTRAINTS
                    System.out.println("moving to synchronization");
                    rst = stmts[a].executeQuery("SELECT CONSTRAINT_NAME, CHECK_CLAUSE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS natural join INFORMATION_SCHEMA.CHECK_CONSTRAINTS WHERE TABLE_NAME = '"+table_name+"' AND TABLE_SCHEMA = '"+db1+"';");
                    while(rst.next()){
                        rst2 = stmts[b].executeQuery("SELECT CONSTRAINT_NAME, CHECK_CLAUSE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS natural join INFORMATION_SCHEMA.CHECK_CONSTRAINTS WHERE CONSTRAINT_NAME = '"+rst.getString(1)+"' AND TABLE_NAME = '"+table_name+"' AND TABLE_SCHEMA = '"+db2+"';");
                        if(rst2.next()){
                            if(!rst2.getString(2).equals(rst.getString(2))){
                                stmts[3].executeUpdate("ALTER TABLE "+table_name+" DROP CHECK "+rst.getString(1)+";");
                                stmts[3].executeUpdate("ALTER TABLE "+table_name+" ADD CONSTRAINT "+rst.getString(1)+" CHECK "+rst.getString(2)+";");
                            }
                        }
                        else{
                            stmts[3].executeUpdate("ALTER TABLE "+table_name+" ADD CONSTRAINT "+rst.getString(1)+" CHECK "+rst.getString(2)+";");
                        }
                    }
                }
            
                //SYNCING UNIQUE & DEFAULT & FOREIGN KEY & PRIMARY KEY CONSTRAINTS
                //UNIQUE is the only constraint that has column name as the default constraint name
                //GETTING UNIQUE COLUMNS IF THEY EXIST AS BACKUP PLAN CASE SEARCH USING PRIMARY KEY FAILED
                unique_list = Unique(table_name,db1);
                
            }catch(SQLException ex){
                System.out.println("Constraints Error : "+ex.getMessage());
            }
            
            try{
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
                System.out.println("Error1 : "+ex.getMessage());
            }
            
            rst = stmts[a].executeQuery("show columns from "+table_name+";");
            while(rst.next()){
                columns.add(rst.getString(1));
                if(!rst.getString(4).equals("PRI"))Non_PK_Column_names.add(rst.getString(1));
            }
            
            pks = "";
            for(int i=0 ; i<Primary_key_list.size() ; i++){
                pks += Primary_key_list.get(i).getName();
                if(i <= Primary_key_list.size() - 2)pks += ", ";
            }
            
            column_values = "";
            for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                column_values += Non_PK_Column_names.get(i);
                if(i <= Non_PK_Column_names.size() - 2)column_values += ", ";
            }
           
            for(row = 0 ; row < Primary_key_list.get(0).getValue().size() ; row++){
                found = false;
                unique_found = false;
                pk_changed = false;
                flag = 0;
                for (k = Primary_key_list.size(); k >= 1; k--) {
                        if(k < Primary_key_list.size()){
                            pk_changed = true;
                            flag = 1;
                            if(!unique_list.isEmpty()){//using unique columns for searching
                                for(int i=0 ; i<unique_list.size() ; i++){
                                    unique_values_condition = Current_Unique_Values(table_name,unique_list.get(i).getColumns());
                                    if(rst2.isBeforeFirst()){
                                        unique_found = true;
                                        found = true;
                                        break;
                                    }
                                }
                                
                            }
                            
                            else{//using non-primary keys as a backup primary key
                                
                                    q = "SELECT "+column_values+" FROM "+table_name+" WHERE ";
                                    q += Current_PKs_Values() + ";";
                                    condition = "";
                                    try{
                                         rst = stmts[a].executeQuery(q);//selects values of non-pks

                                    }catch(SQLException ex){
                                        System.out.println("Error1 : "+ex.getMessage());
                                    }
                                    while(rst.next()){//building non-pks condition
                                        for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                                            if(i <= Non_PK_Column_names.size() - 2){
                                                if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))condition += Non_PK_Column_names.get(i) +"='"+rst.getString(i+1)+"' AND ";
                                                else condition += Non_PK_Column_names.get(i) +"="+rst.getString(i+1)+" AND ";
                                            }
                                            else {
                                                if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))condition += Non_PK_Column_names.get(i) +"='"+rst.getString(i+1)+"'";
                                                else condition += Non_PK_Column_names.get(i) +"="+rst.getString(i+1);
                                            }
                                        }  
                                    }  
                            }
                            
                        }
                        if(unique_found == false)random(0, k, new String[k],table_name);
                        if(found == true)break;
                
                }
                
                
                if(found){
                    if(pk_changed && unique_found == false){//case sub-pks + non-prime values were found in target table
                        flag = 1;
                        if(!(sync == 3 && a == 0)){

                            set = stmts[a].executeQuery("SELECT * FROM "+table_name+" WHERE "+conditions+" AND "+ condition);
                            set.last();
                            int r1 = set.getRow();
                            rst2.last();
                            int r2 = rst2.getRow();
                            if(r1 >= r2){//case more than one row has been selected we delete all selected rows from target then insert all those selected from source table
                                                        //however this will fail if target has more selected rows than source, this means target has new inserted rows that should not be deleted as they will be inserted later on in source case of Bi Sync or simply belong to target

                                //delete all rst2 rows and insert all all rst rows case # of rows of rst2 rows is smaller than rst's
                                System.out.println("DELETE FROM "+table_name+" WHERE "+conditions+" AND "+ condition + "AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+str+"."+table_name+");");
                                stmts[b].executeUpdate("DELETE FROM "+table_name+" WHERE "+conditions+" AND "+ condition + "AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+str+"."+table_name+");");
                                set.beforeFirst();
                                while(set.next())insertion(table_name);
                                set.close();
                            }

                            else found = false;

                        }   
  
                    }

                    else if(!(sync == 3 && a == 0)){//if it is Bidirectional Sync, only BO -> FE updation is allowed
                            System.out.println("Checking for UPDATE : ");
                            String temp;
                            if(unique_found == false)temp = conditions;
                            else temp = unique_values_condition;
                            
                            for(int i=0 ; i<columns.size() ; i++){
                                IsChar = false;
                                try{
                                       rst = stmts[a].executeQuery("SELECT "+columns.get(i)+" FROM "+table_name+" where "+conditions+";");
                                       rst.next(); compare1 = rst.getString(1);  
                                       
                                       rst2 = stmts[b].executeQuery("SELECT "+columns.get(i)+" FROM "+table_name+" where "+temp+";");
                                       rst2.next(); compare2 = rst2.getString(1);
                                        
                                }catch(SQLException ex){
                                    System.out.println("Error3 : "+ex.getMessage());
                                }
                                
                                data_type = rst.getMetaData().getColumnTypeName(1);
                                if(data_type.contains("CHAR"))IsChar = true;
                                

                                if((compare1 == null && compare2 == null) || compare1 != null && compare1.equals(compare2))continue;
                                else {
                                    //updating
                                    if(IsChar && compare1 != null)compare1 = "'"+compare1+"'";
                                    if(a == 0)q = backoffice.getDatabase()+" ";   
                                    else q = frontend.getDatabase()+" ";
                                    report_list.addElement(q+" - Row#"+(row+1)+" Column '"+columns.get(i).toUpperCase()+"' : '"+compare2+"' changed to '"+compare1+"'");
                                    try{
                                            System.out.println("UPDATE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+conditions+";");
                                            int verify = stmts[b].executeUpdate("UPDATE "+table_name+" SET "+columns.get(i)+"= "+compare1+" WHERE "+temp+";");
                                            if(verify < 0)System.out.println("NOT UPDATED");
                                            else System.out.println("ROW UPDATED");
                                    }catch(SQLException ex){
                                        System.out.println("Error4 : "+ex.getMessage());
                                    }
                                    
                                }
                            }
                        } 
                    }

                if(found == false) insertion(table_name);
                  
            }
            
            
           try{
               //ENABLE FOREIGN KEY CONSTRAINT
               stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
           }catch(SQLException ex){
               System.out.println("Error5 : "+ex.getMessage());
           }
            a = 1;
            b = 0;
        }
        
        }catch (SQLException ex) {
                ex.getMessage();
        }
        
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
            conditions = "";
            try{
                for(int i=0 ; i<PKs_To_Use.length ; i++){
                    names += PKs_To_Use[i];
                    for(int j=0 ; j<Primary_key_list.size() ; j++){    
                        if(PKs_To_Use[i].equals(Primary_key_list.get(j).getName())){
                            conditions += PKs_To_Use[i]+"="+Primary_key_list.get(j).getValue().get(row);
                            break;
                        }
                    }
                    if(i <= PKs_To_Use.length - 2){
                        names += ", ";
                        conditions += " AND ";
                    }
                }
                
                if(flag == 0)query="SELECT "+ names + " FROM "+ table_name +" WHERE "+ conditions + ";";
                else{
                    if(a == 0)str = frontend.getDatabase();
                    else str = backoffice.getDatabase();
                    query = "SELECT * FROM "+table_name+" WHERE "+ conditions + condition + " AND ("+pks+") NOT IN (SELECT "+pks+" FROM "+str+"."+table_name+");";
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
   
    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Synchronization().setVisible(true);
        });
    
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Data_Sync_Button;
    private javax.swing.JButton Golden_Gate_Button;
    private javax.swing.JButton Read_Excel_Button;
    private javax.swing.JButton Structure_Sync_Button;
    private javax.swing.JTextField excel_name;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
