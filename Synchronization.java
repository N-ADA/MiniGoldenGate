package com.mycompany.minigoldengate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * @author nadabounajma
 */
public class Synchronization extends javax.swing.JFrame {

    DefaultListModel<String> tables_list;
    DefaultListModel report_list;
    FrontEnd frontend;
    BackOffice backoffice;
    
    public Synchronization(){
        initComponents();
    }
    
    public Synchronization(FrontEnd frontend, BackOffice backoffice) {
        this.frontend = frontend;
        this.backoffice = backoffice;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        // TODO add your handling code here:
        int index=jList.getSelectedIndex();
        String h= tables_list.getElementAt(index);
        try {
            if(table_count_checker(h)==0){
                column_count_checker(h);
                datatype_checker(h);
                data_length_checker(h);
                column_difference(h);
            }
        } catch (FileNotFoundException | SQLException ex) {
            ex.getMessage();
        }
    }//GEN-LAST:event_Structure_Sync_ButtonActionPerformed

    public int table_count_checker(String h) throws FileNotFoundException, SQLException{
        int o = 0;
        int bo=0;
        
        String q4="SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '"+frontend.getDatabase()+"' AND table_name ='"+h+"';";
        ResultSet rst40 = frontend.getStmt().executeQuery(q4);
        while (rst40.next()){
            o=rst40.getInt(1);
        }
                              
        if(o==0){
            System.out.format("the table you have selected:'%s' does not exist in database '"+frontend.getDatabase()+"'\n\n",h);
            return 1;
        }
        String q5="SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '"+backoffice.getDatabase()+"' AND table_name ='"+h+"';";
        ResultSet rst20 = backoffice.getStmt().executeQuery(q5);
        while (rst20.next()){
            bo=rst20.getInt(1);
        }
        if(bo==0){
            System.out.format("the table you have selected:'%s' does not exist in database '"+backoffice.getDatabase()+"'\n\n",h); 
            return 1;
        }
        if(o==0 && bo==0){
            System.out.format("the table you have selected:'%s' does not exist in database '"+frontend.getDatabase()+"' and '"+frontend.getDatabase()+"'\n",h);
            return 1;
        }
        else return 0;
    }
    
    public void column_count_checker(String h) throws SQLException{
        int nb_bo = 0;
        int nb_fe = 0;
        String query="SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+frontend.getDatabase()+"' AND table_name ='"+h+"';";
        ResultSet rst = frontend.getStmt().executeQuery(query);
        while (rst.next()){
                    nb_fe=rst.getInt(1);
        }
        
        String query1="SELECT COUNT(*)FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+backoffice.getDatabase()+"' AND table_name ='"+h+"';"  ;
        ResultSet rst1 = backoffice.getStmt().executeQuery(query1);
        while (rst1.next()){
                nb_bo=rst1.getInt(1);
        }
        if(nb_bo==nb_fe){
            System.out.format("the number of columns of table: %s in the '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"' is the same\n\n",h);
        }else{         
          System.out.format("the number of columns of table : %s in the '"+frontend.getDatabase()+"' and '"+backoffice.getDatabase()+"' is not the same\n\n",h);
        }
             
    }
    
    public void datatype_checker(String h) throws SQLException{
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
     
        String q31="SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+frontend.getDatabase()+"' AND TABLE_NAME= '"+h+"'";
        ResultSet rst44 = backoffice.getStmt().executeQuery(q31);
        while (rst44.next()){
            list4.add(rst44.getString(1));
        }
        String q32="SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+backoffice.getDatabase()+"' AND TABLE_NAME= '"+h+"'";
        ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
        while (rst45.next()){
            list5.add(rst45.getString(1));
        }
        if(list4.equals(list5)==true){
            System.out.format("the table : %s has the same datatypes in both databases\n\n",h);
        }
        else{ 
            System.out.format("the table : %s has different datatypes  in both databases\n\n",h);
        }
                              
    }
    
    public void column_difference(String h) throws SQLException{
                         List<String> list_fe = new ArrayList<String>();
                         List<String> list_bo = new ArrayList<String>();

                     String q1=" Select COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE "
                             + " TABLE_SCHEMA = '"+frontend.getDatabase()+"'  AND table_name ='"+h+"';";
                     ResultSet rst44 = frontend.getStmt().executeQuery(q1);
                              while (rst44.next()){
                                  
                              list_fe.add(rst44.getString(1));
                            }
                              
                     String q2=" Select COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE "
                             + " TABLE_SCHEMA = '"+backoffice.getDatabase()+"'  AND table_name ='"+h+"';";
                      ResultSet rst45 = backoffice.getStmt().executeQuery(q2);
                              while (rst45.next()){
                                  
                              list_bo.add(rst45.getString(1));
                            }
                      List<String> list = new ArrayList<>(CollectionUtils.disjunction(list_fe, list_bo));
                      if(list.isEmpty()!=true){
                      System.out.format("the different columns between the two tables are %s\n\n", list);}
                     

        }

  public void data_length_checker(String h) throws SQLException{
  
                        List<String> list4 = new ArrayList<String>();
                         List<String> list5 = new ArrayList<String>();
                         
                         {
                             
                             String q31="SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+frontend.getDatabase()+"' AND TABLE_NAME= '"+h+"'";
                             ResultSet rst44 = frontend.getStmt().executeQuery(q31);
                              while (rst44.next()){
                                  
                              list4.add(rst44.getString(1));
                            }
                              

                              String q32="SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+backoffice.getDatabase()+"' AND TABLE_NAME= '"+h+"'";
                             ResultSet rst45 = backoffice.getStmt().executeQuery(q32);
                              while (rst45.next()){
                                  
                              list5.add(rst45.getString(1));
                            }
                              if(list4.equals(list5)==true){System.out.format("the table : %s has the same datatypes length in both databases\n",h);
                              }
                              else{ System.out.format("the table : %s has different datatypes length in both databases\n",h); }
                              
                            }
                         
  
  
  }
    
    
    private void Golden_Gate_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Golden_Gate_ButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Golden_Gate_ButtonActionPerformed

    private void excel_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excel_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_excel_nameActionPerformed

    private void Read_Excel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Read_Excel_ButtonActionPerformed
        // TODO add your handling code here:
        String excel = excel_name.getText();
        tables_list = new DefaultListModel<>();
        /*
        **Excel Sheet File Reading --- we get names of tables
        */
        MainFrame k = new MainFrame();
        try {
            k.check_status(excel);
            tables_list.addAll(k.check_status(excel));
        } catch (IOException ex) {
           ex.getMessage();
        }
        
        DisplayTable_OnClick();
        
    }//GEN-LAST:event_Read_Excel_ButtonActionPerformed
    
    public void DisplayTable_OnClick(){
        jList.setModel(tables_list);
        
        jList.addMouseListener(new MouseListener(){
           
            @Override
            public void mousePressed(MouseEvent e) {
                Object[] row;
                int index = jList.getSelectedIndex();
                String table_name = tables_list.getElementAt(index);
                ResultSet FE_content = frontend.TableContent(table_name);
                ResultSet BO_content = backoffice.TableContent(table_name);
                
                try {
                    ResultSetMetaData rsmd = FE_content.getMetaData();
                    int c = rsmd.getColumnCount();
                    DefaultTableModel dtm = new DefaultTableModel();
                    for (int i = 1; i <= c; i++)dtm.addColumn(rsmd.getColumnName(i)); 
                    while (FE_content.next()) {
                        row = new Object[c];
                        for (int i = 0; i < c; i++)row[i] = FE_content.getString(i + 1);
                        dtm.addRow(row);
                    }
                    jTable1.setModel(dtm);
                    jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
                    for (int i = 0; i < c; i++)jTable1.getColumnModel().getColumn(i).setPreferredWidth(100);
                    
                    if(BO_content.isBeforeFirst()){
                        ResultSetMetaData rsmd2 = BO_content.getMetaData();
                        int c2 = rsmd2.getColumnCount();
                        DefaultTableModel dtm2 = new DefaultTableModel();
                        for (int i = 1; i <= c2; i++)dtm2.addColumn(rsmd2.getColumnName(i));
                        while (BO_content.next()) {
                        row = new Object[c2];
                        for (int i = 0; i < c2; i++)row[i] = BO_content.getString(i + 1);
                        dtm2.addRow(row);
                        }
                        jTable2.setModel(dtm2);
                        jTable2.setAutoResizeMode(jTable2.AUTO_RESIZE_OFF);
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
        //iterate through the list of the tables
        for (int i = 0; i < jList.getModel().getSize(); i++) {
            String table_name = jList.getModel().getElementAt(i);
            
           //give table_name to data_synchronization function
           report_list.addElement("     ---Checking "+table_name+"---");
           Data_Synchronization(table_name);
          
        }
        new Data_Sync_Report(report_list).setVisible(true);
    }//GEN-LAST:event_Data_Sync_ButtonActionPerformed
   
    public void Data_Synchronization(String table_name){
        
        String key="", data_type = null;
        String compare1 = "",compare2="";
        ResultSet rst, rst2;
        
        int columns_count=0;
        int rows_count=0;
        int verify;
        boolean IsChar = false;
        
        try{
            
            rst2 = backoffice.getStmt().executeQuery("show tables LIKE '"+table_name+"';");
            if(rst2.next()){
                //SET @TRIGGER_DISABLED = 1; can this be used?
                backoffice.getStmt().executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
            
                rst = frontend.getStmt().executeQuery("SELECT count(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name ='"+table_name+"' AND table_schema ='"+frontend.getDatabase()+"';");
                while(rst.next())columns_count = Integer.parseInt(rst.getString(1));
            
                rst = frontend.getStmt().executeQuery("SELECT count(*) FROM "+table_name+";");
                while(rst.next())rows_count = Integer.parseInt(rst.getString(1));
            
                String[] column_names_array = new String[columns_count-1];
                String[] primary_key_values_array = new String[rows_count];
                
                //data synchronization
                //select column name that is primary key
                rst = frontend.getStmt().executeQuery("SELECT COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA = '"+frontend.getDatabase()+"' AND TABLE_NAME = '"+table_name+"' AND COLUMN_KEY = 'PRI';");
                while(rst.next()){
                    key = rst.getString(1);
                }

                rst = frontend.getStmt().executeQuery("SHOW COLUMNS FROM "+table_name+" where FIELD != '"+key+"';");   
                int index =-1;
                while(rst.next()){
                    column_names_array[++index] = rst.getString(1);
                }

                int x = -1;   
                rst = frontend.getStmt().executeQuery("SELECT "+key+" FROM "+table_name+";");
                data_type = rst.getMetaData().getColumnTypeName(1);
                while(rst.next()){
                    if(data_type.contains("CHAR"))primary_key_values_array[++x] = "'"+rst.getString(1)+"'";
                    else primary_key_values_array[++x] = rst.getString(1);
                }
                
                for(x = 0 ; x < primary_key_values_array.length ; x++){
                    
                    rst2 = backoffice.getStmt().executeQuery("SELECT "+key+" FROM "+table_name+" where "+key+"="+primary_key_values_array[x]+";");
                    if(rst2.next()){// if primary key value exists in backoffice compare its columns
                        for(int i=0 ; i<column_names_array.length ; i++){
                                IsChar = false;
                                rst = frontend.getStmt().executeQuery("SELECT "+column_names_array[i]+" FROM "+table_name+" where "+key+"="+primary_key_values_array[x]+";");
                                while(rst.next()){
                                    compare1 = rst.getString(1);  
                                }
                                
                                data_type = rst.getMetaData().getColumnTypeName(1);
                                if(data_type.contains("CHAR")){
                                    IsChar = true;
                                }
                               
                                rst2 = backoffice.getStmt().executeQuery("SELECT "+column_names_array[i]+" FROM "+table_name+" where "+key+"="+primary_key_values_array[x]+";");
                                while(rst2.next()){
                                    compare2 = rst2.getString(1);
                                }
                                
                                if(compare1 == null && compare2 == null || compare1.equals(compare2) )continue;
                                else {
                                    //updating column
                                    report_list.addElement("Row#"+(x+1)+" - Column '"+column_names_array[i]+"' : Value '"+compare2+"' changed to '"+compare1+"'");
                                    if(IsChar){
                                        compare1 = "'"+compare1+"'";
                                        IsChar = false;
                                    }
                                    verify = backoffice.getStmt().executeUpdate("UPDATE "+table_name+" SET "+column_names_array[i]+"= "+compare1+" WHERE "+key+"="+primary_key_values_array[x]+";");
                                    if (verify > 0) {
                                        report_list.addElement("ROW UPDATED");
                                    }
                                    else {
                                            report_list.addElement("ROW NOT UPDATED");
                                    }
                                }
                                
                        }
                    }
                    
                    else{
                        report_list.addElement("Inserting Row with Primary Key(s) "+primary_key_values_array[x]+" in "+backoffice.getDatabase()+"...");
                        String[] row_values = new String[column_names_array.length];
                        String insert_query = "INSERT INTO "+table_name+" ("+key+", ";
                        
                        for (int i=0 ; i<column_names_array.length ; i++){
                            rst = frontend.getStmt().executeQuery("SELECT "+column_names_array[i]+" FROM "+table_name+" where "+key+"="+primary_key_values_array[x]+";");
                            data_type = rst.getMetaData().getColumnTypeName(1);
                            if(data_type.contains("CHAR"))IsChar = true;
                            while(rst.next()){
                                if(IsChar){
                                    row_values[i] = "'"+rst.getString(1)+"'";
                                    IsChar = false;
                                }
                                else row_values[i] = rst.getString(1);
                            }
                            insert_query += column_names_array[i];
                            if(i <= column_names_array.length - 2)insert_query += ", ";
                        }
                        
                        insert_query += ") VALUES ("+primary_key_values_array[x]+", ";
                        for(int i=0 ; i<row_values.length ; i++){
                            insert_query += row_values[i];
                            if(i <= row_values.length - 2)insert_query += ", ";
                        }
                        
                        insert_query+=");";
                        verify = backoffice.getStmt().executeUpdate(insert_query);
                        
                        if (verify > 0){
                            report_list.addElement("ROW INSERTED\n");
                        }
                        else {
                            report_list.addElement("ROW NOT INSERTED\n");
                        }
                    }
                }
                
                
                backoffice.getStmt().executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
            }
            
            else{
                 report_list.addElement("Creating Table "+table_name+" in "+backoffice.getDatabase()+"...");
                 frontend.getStmt().execute("CREATE TABLE "+backoffice.getDatabase()+"."+table_name+" SELECT * FROM "+table_name+";");
                 rst2 = backoffice.getStmt().executeQuery("SHOW TABLES LIKE '"+table_name+"';");
                 if(rst2.next())report_list.addElement("TABLE CREATED");
                 else{
                    report_list.addElement("FAILED TO CREATE TABLE");
                 }
            }
            
            
        }catch(SQLException ex){
            ex.getMessage();
        }
    }
   
   
    public static void main(String args[]) {
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Synchronization().setVisible(true);
            }
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
