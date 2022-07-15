package com.mycompany.minigoldengate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import static java.lang.Math.ceil;

/**
 *
 * @author nadabounajma
 */
public class Synchronization extends javax.swing.JFrame {

    Statement[] stmts = new Statement[3];
    DefaultListModel<String> tables_list, report_list;
    FrontEnd frontend;
    BackOffice backoffice;
    ResultSet rst, rst2, rst3, set;
    ArrayList<Primary_key> Primary_key_list = new ArrayList<>();
    ArrayList<String> Non_PK_Column_names = new ArrayList<>();
    String pks = "", names = "", conditions = "", condition = "", query;
    int row, flag, row_diff, jar=0, row_count,jj;
    boolean found, pk_changed,mult_rows;
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
        if(jar == 0){
            Data_Sync_Button.setEnabled(true);
            Golden_Gate_Button.setEnabled(true);
        }
        
    }//GEN-LAST:event_Structure_Sync_ButtonActionPerformed

   
    public void call(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{  
        if (jj==2){
            comment_list.clear();
            comment_list.add("Struture is Sycnhronized");
            report_list.addElement("Struture is Sycnhronized");
           
            create_excel(table_name,row_fe,row_bo,time,"Y",comment_list,direction);
            
            comment_list.clear();
        }else if (jj==1){
            create_excel(table_name,row_fe,row_bo,time,"N",comment_list,direction);
          
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
        
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        String q;

        if(reading.FE_list.contains(table_name)==true && reading.getBO_list().contains(table_name)==false){
            direction="FE->BO";
            q ="show columns FROM "+table_name+" where `Key` = 'PRI';";

            rst = frontend.getStmt().executeQuery(q);
            while (rst.next())list1.add(rst.getString(1));
            
            
            q = "show columns FROM "+table_name+" where `Key` = 'PRI';"  ;
            rst2 = backoffice.getStmt().executeQuery(q);

            while (rst2.next())list2.add(rst2.getString(1));
            
            if(!list2.equals(list1)==true){
                  report_list.addElement("The table in "+backoffice.getDatabase()+" contains a wrong primary key");
                  comment_list.add("The table in "+backoffice.getDatabase()+" contains a wrong primary key");
                  list_filtering.add("N");
            }
        }
        
        else if(reading.FE_list.contains(table_name)==false && reading.BO_list.contains(table_name)==true){
            direction="BO->FE";
           
            q = "show columns FROM "+table_name+" where `Key` = 'PRI';" ;
            rst = frontend.getStmt().executeQuery(q);
            while (rst.next())list1.add(rst.getString(1));
            
            q = "show columns FROM "+table_name+" where `Key` = 'PRI';"  ;
            rst2 = backoffice.getStmt().executeQuery(q);
            while (rst2.next())list2.add(rst2.getString(1));
           
            if(!list2.equals(list1)==true){
                report_list.addElement("The table in "+frontend.getDatabase()+" contains a wrong primary key");
                comment_list.add("The table in "+frontend.getDatabase()+" contains a wrong primary key");
                list_filtering.add("N");
            }

        }
        
        else if(reading.getBID_FE_and_BO().contains(table_name)==true ){
            
            direction="Bidirectional";
            q = "show columns FROM "+table_name+" where `Key` = 'PRI';"  ;
            rst = frontend.getStmt().executeQuery(q);
            while (rst.next())list1.add(rst.getString(1));
            
            q = "show columns FROM "+table_name+" where `Key` = 'PRI';"  ;
            rst2 = backoffice.getStmt().executeQuery(q);
            while (rst2.next())list2.add(rst2.getString(1));
            
            if(!list2.equals(list1)==true){
                report_list.addElement("The table in "+frontend.getDatabase()+" contains a wrong primary key");
                comment_list.add("The table in "+frontend.getDatabase()+" contains a wrong primary key");
                list_filtering.add("N");
            }
        }

    }

    public void row_count(String table_name) throws SQLException{

        String q ="SELECT COUNT(*) FROM "+table_name+";"  ;
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())row_fe=rst.getInt(1);
        

        q = "SELECT COUNT(*) FROM "+table_name+";"  ;
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())row_bo=rst2.getInt(1);
        
        if (row_bo!=row_fe){         
          report_list.addElement("Nb of row is not the same");
          comment_list.add("Nb of row is not the same");
          list_filtering.add("N");
        }

  }

    public int table_count_checker(String table_name) throws FileNotFoundException, SQLException, IOException, IOException, IOException, IOException, InvalidFormatException{
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
            

        String q = "show tables like '"+table_name+"';";
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())list1.add(rst.getString(1));
        
        q = "show tables from like '"+table_name+"' ;";
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())list2.add(rst2.getString(1));
        
        if(list2.isEmpty()==true && list1.isEmpty()==true){
            report_list.addElement("The table you have selected:'"+table_name+"' does not exist in database '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"'");
            comment_list.add("Table does not exist in both databases");
            row_fe=0;
            row_bo=0;
            create_excel(table_name,row_fe,row_bo,time,"N",comment_list,direction);
            comment_list.clear();
            return 1;
        }                     
        if(list1.isEmpty()==true){
            report_list.addElement("The table you have selected:'"+table_name+"' does not exist in database '"+frontend.getDatabase()+"'");
            comment_list.add("Table does not exist in database '"+frontend.getDatabase());
            row_bo=0;
            row_fe=0;
            create_excel(table_name,row_fe,row_bo,time,"N",comment_list,direction);
             comment_list.clear();
            return 1;
        }

        if(list2.isEmpty()==true){
            report_list.addElement("The table you have selected:'"+table_name+"' does not exist in database '"+backoffice.getDatabase()+"'"); 
            comment_list.add("Table does not exist in database '"+backoffice.getDatabase());
            row_fe=0;
            row_bo=0;
            create_excel(table_name,row_fe,row_bo,time,"N",comment_list,direction);
            comment_list.clear();
            return 1;
        }

        return 0;
    }

    
   
    public void column_count_checker(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{

        String q ="show columns from "+table_name+";";
        rst = frontend.getStmt().executeQuery(q);
        int c = 0;
        while (rst.next())c++;
        nb_fe=c ;
        
        c = 0;
        q = "show columns from "+table_name+";"  ;
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst.next())c++; 
        nb_bo=c ;

        if(nb_bo!=nb_fe){         
          report_list.addElement("Nb of columns  in the '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"' is not the same");
          comment_list.add("Nb of columns  in the '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"' is not the same");
          list_filtering.add("N");
        }

    }



    public void datatype_checker(String table_name) throws SQLException{
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String q = "desc "+table_name+";";
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())list1.add(rst.getString(2));
        
        q = "desc "+table_name+";";
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())list2.add(rst2.getString(2));
        
        
        if(!(list1.containsAll(list2)==true && list2.containsAll(list1)==true)){ 
           report_list.addElement("Different datatypes in both databases");
           comment_list.add("Different datatypes in both databases");
           list_filtering.add("N");
        }

    }
 


    public void column_difference(String table_name) throws SQLException{
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        
        String q = "desc "+table_name+";";
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())list1.add(rst.getString(1));
        
        
        q = "desc "+table_name+";";
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())list2.add(rst2.getString(1));
        
        
        List<String> list = new ArrayList<>(CollectionUtils.disjunction(list1, list2));
        
        if(list.isEmpty()!=true){
            report_list.addElement("The different columns between the two tables are "+list);
            comment_list.add("The different columns between the two tables are "+list);
            list_filtering.add("N");
        }
    }
 
    public void default_func(String table_name) throws SQLException{
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String q = "desc "+table_name+";";
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())list1.add(rst.getString(5));
        
        
        q = "desc "+table_name+";";
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())list2.add(rst2.getString(5));
        
        
        if(!(list1.size()==list2.size() && list1.equals(list2)==true && list2.equals(list1)==true)){ 
           report_list.addElement("Different DEFAULT field in both databases");
           comment_list.add("Different DEFAULT field in both databases");
           list_filtering.add("N");
        }
    
    }
    
    
    public void null_func(String table_name) throws SQLException{
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String q = "desc "+table_name+";";
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())list1.add(rst.getString(3));
        
        
        q = "desc "+table_name+";";
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())list2.add(rst2.getString(3));
        
        
        if( list1.size()==list2.size()&& list1.equals(list2)==true && list2.equals(list1)==true){
           System.out.println(list2+table_name+list1);
        }
        else{ 
           report_list.addElement("different NULL field in both databases");
           comment_list.add("different NULL field in both databases");
           list_filtering.add("N");
        }
    
    
    }
    public int data_length_checker(String table_name) throws SQLException, IOException, FileNotFoundException, InvalidFormatException{

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String q = "SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME= '"+table_name+"'";
        rst = frontend.getStmt().executeQuery(q);
        while (rst.next())list1.add(rst.getString(1));
        

        q = "SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME= '"+table_name+"'";
        rst2 = backoffice.getStmt().executeQuery(q);
        while (rst2.next())list2.add(rst2.getString(1));
        
        if(list1.equals(list2)==true) return 1;
        else{
             report_list.addElement(" different datatypes length in both databases\n");
             comment_list.add(" different datatypes length in both databases\n");
             list_filtering.add("N");
            return 3;
        }
        
   }

    
    public void create_excel(String table_name,int Rows_FE,int Rows_BO,double time,String Status,ArrayList<String> comment_list,String direction) throws FileNotFoundException, IOException, InvalidFormatException{
            
        int max=jList.getModel().getSize();
        File xlsxFile = new File("Details.xlsx");
        String listString = String.join(", ", comment_list);
        Object[][] newinfo = {{table_name,Rows_FE,Rows_BO,time,Status,listString,direction}};
        FileInputStream inputStream = new FileInputStream(xlsxFile);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getLastRowNum();
        Object[][] newinfo1 = {{"table_name","Rows_FE","Rows_BO","Elapsed time(s)","Status","Comments","Direction"}};
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
        if(Status.contains("N"))jar++;
                    
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
        String q, data_type, attributes, pks_val;
        String compare1 = "",compare2="";
        int k;
        boolean IsChar;
        
        while(++loop != 2){
            Primary_key_list.clear();
            Non_PK_Column_names.clear();
            if(a == 0)stmts[2] = frontend.getStmt2();
            else stmts[2] = backoffice.getStmt2();
            
            try{
                //DISABLE FOREIGN KEY CONSTRAINTS   
                stmts[b].executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
                //MAKING SURE NOT NULL CONSTRAINTS WILL NOT CAUSE PROBLEMS FOR TESTING MATTERS - It is a Structure Sync task
                rst = stmts[a].executeQuery("desc "+table_name+";");
                while(rst.next()){
                    if(rst.getString(3).equals("YES"))
                        stmts[b].executeUpdate("ALTER TABLE "+table_name+" MODIFY "+rst.getString(1)+" "+rst.getString(2)+" "+"NULL;");    
                }
            }catch(SQLException ex){
                System.out.println("Error0 : "+ex.getMessage());
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
            
            rst = stmts[a].executeQuery("show columns from "+table_name+" where `key` != 'PRI';");
            while(rst.next()){
                Non_PK_Column_names.add(rst.getString(1));
            }
            
            pks = "";
            for(int i=0 ; i<Primary_key_list.size() ; i++){
                pks += Primary_key_list.get(i).getName();
                if(i <= Primary_key_list.size() - 2)pks += ", ";
            }
           
            for(row = 0 ; row < Primary_key_list.get(0).getValue().size() ; row++){
                found = false;
                pk_changed = false;
                flag = 0;

                for (k = Primary_key_list.size(); k >= 1; k--) {
                        if(k < Primary_key_list.size())pk_changed = true;
                        random(0, k, new String[k],table_name);
                        if(found == true)break;
                }
                
                if(found == true){
                    if(pk_changed == true){
                        found = false;
                        flag = 1;
                        attributes = "";
                        pks_val = "";
                        for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                            attributes += Non_PK_Column_names.get(i);
                            if(i <= Non_PK_Column_names.size() - 2)attributes += ", ";

                        }
                        q = "SELECT "+attributes+" FROM "+table_name+" WHERE ";
                        for(int i=0 ; i<Primary_key_list.size() ; i++){
                            pks_val += Primary_key_list.get(i).getName() + "=" + Primary_key_list.get(i).getValue().get(row);
                            if(i <= Primary_key_list.size() - 2)pks_val += " AND ";
                        }

                        q += pks_val + ";";
                        condition = "";
                        try{
                             rst = stmts[a].executeQuery(q);
                        }catch(SQLException ex){
                            System.out.println("Error1 : "+ex.getMessage());
                        }
                        while(rst.next()){
                            for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                                if(i <= Non_PK_Column_names.size() - 2){
                                    if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))condition += Non_PK_Column_names.get(i) +"='"+rst.getString(i+1)+"' AND ";
                                    else condition += Non_PK_Column_names.get(i) +"="+rst.getString(i+1)+" AND ";
                                }
                                else {
                                    if(rst.getMetaData().getColumnTypeName(i+1).contains("CHAR"))condition += Non_PK_Column_names.get(i) +"='"+rst.getString(i+1)+"';";
                                    else condition += Non_PK_Column_names.get(i) +"="+rst.getString(i+1)+";";
                                }
                            }  
                        }

                        for (k = Primary_key_list.size(); k >= 1; k--) {
                                random(0, k, new String[k],table_name);
                                if(found == true && !(sync == 3 && a == 0)){
                                    
                                    set = stmts[a].executeQuery(query);
                                    set.last();
                                    int r1 = set.getRow();
                                    rst2.last();
                                    int r2 = rst2.getRow();
                                    if(r1 >= r2){//case more than one row has been selected we delete all selected rows from target then insert all those selected from source table
                                                                //however this will fail if target has more selected rows than source, this means target has new inserted rows that should not be deleted as they will be inserted later on in source case of Bi Sync or simply belong to target
                        
                                        //delete all rst2 rows and insert all all rst rows case # of rows of rst2 rows is smaller than rst's
                                        System.out.println("DELETE FROM "+table_name+" WHERE "+conditions+" AND "+ condition);
                                        stmts[b].executeUpdate("DELETE FROM "+table_name+" WHERE "+conditions+" AND "+ condition);
                                        set.beforeFirst();
                                        while(set.next())insertion(table_name);
                                        set.close();
                                    }
                                    
                                    break;
                                    
                                }   

                        }

                    }

                    else if(!(sync == 3 && a == 0)){   
                            //if it is Bidirectional Sync, only BO -> FE updation is allowed 
                            for(int i=0 ; i<Non_PK_Column_names.size() ; i++){
                                IsChar = false;
                                try{
                                       rst = stmts[a].executeQuery("SELECT "+Non_PK_Column_names.get(i)+" FROM "+table_name+" where "+conditions+";");
                                       rst.next(); compare1 = rst.getString(1);  
                                       
                                       rst2 = stmts[b].executeQuery("SELECT "+Non_PK_Column_names.get(i)+" FROM "+table_name+" where "+conditions+";");
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
                                    String str;
                                    if(a == 0)str = backoffice.getDatabase()+" ";   
                                    else str = frontend.getDatabase()+" ";
                                    report_list.addElement(str+" - Row#"+(row+1)+" Column '"+Non_PK_Column_names.get(i).toUpperCase()+"' : '"+compare2+"' changed to '"+compare1+"'");
                                    try{
                                            stmts[b].executeUpdate("UPDATE "+table_name+" SET "+Non_PK_Column_names.get(i)+"= "+compare1+" WHERE "+conditions+";");
                                    }catch(SQLException ex){
                                        System.out.println("Error4 : "+ex.getMessage());
                                    }
                                    
                                }
                            }
                        } 
                    }

                if(found == false)insertion(table_name);
                  
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
        }catch(SQLException ex){
            System.out.println("Insertion error : "+ex.getMessage());
        }
        
        String str = "INSERTING INTO ";
        if(b == 0)str += frontend.getDatabase()+" ";
        else str += backoffice.getDatabase()+" ";
        str += table_name+" VALUES ("+content+")";
        report_list.addElement(str);
        stmts[b].executeUpdate("INSERT INTO "+table_name+"("+Col_Names+") VALUES ("+content+");");
        System.out.println("INSERT INTO "+table_name+"("+Col_Names+") VALUES ("+content+");");

    }
   
    public void random(int n, int k, String[] PKs_To_Use, String table_name){
        if (k == 0 && PKs_To_Use.length >= ceil(Primary_key_list.size()/2) && found == false){
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
                else query = "SELECT * FROM "+table_name+" WHERE "+conditions+" AND "+ condition;
                System.out.println("RANDOM : "+query);
                rst2 = stmts[b].executeQuery(query);
                if(rst2.isBeforeFirst()){
                    found = true;
                }

            }catch(SQLException ex){
                    System.out.println("random Error : " + ex.getMessage());
            }
                    
        }
        
        else{
                if(found || (PKs_To_Use.length < ceil(Primary_key_list.size()/2)))return;
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
