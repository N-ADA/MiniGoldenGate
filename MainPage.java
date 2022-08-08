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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author nadabounajma
 */
public class MainPage extends javax.swing.JFrame {

    private DefaultListModel<String> tables_list, report_list;
    List<String> Merged = new ArrayList<>();
    private FrontEnd frontend;
    private BackOffice backoffice;
    int lastjj;
    int nmax12;
    private ExcelReader reading;
    private double time_d=0;
    private Structure_synchronization instance;
    
    public MainPage(){
        initComponents();
    }
    
    public MainPage(FrontEnd frontend, BackOffice backoffice) throws FileNotFoundException, IOException, InvalidFormatException{
        initComponents();
        this.frontend = frontend;
        this.backoffice = backoffice;
        //Data_Sync_Button.setEnabled(false);
        //Golden_Gate_Button.setEnabled(false);
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
        jButton1 = new javax.swing.JButton();
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

        jButton1.setText("Log Out");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(82, 82, 82)
                .addComponent(Read_Excel_Button)
                .addGap(127, 127, 127))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(excel_name))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(excel_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Read_Excel_Button)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton1)
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
        Structure_Sync_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Structure_Sync_ButtonActionPerformed(evt);
            }
        });

        Data_Sync_Button.setText("Data Synchronization");
        Data_Sync_Button.setPreferredSize(new java.awt.Dimension(160, 30));
        Data_Sync_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Data_Sync_ButtonActionPerformed(evt);
            }
        });

        Golden_Gate_Button.setText("Enable GoldenGate");
        Golden_Gate_Button.setPreferredSize(new java.awt.Dimension(160, 30));
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
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(Structure_Sync_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(Data_Sync_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(Golden_Gate_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
        try {
            instance=new Structure_synchronization(frontend,backoffice,reading,report_list) ;
        } catch (Exception ex) {
           System.out.println("Struct sync Error : "+ex.getMessage());
        }
        
        int max=jList.getModel().getSize();
        nmax12=jList.getModel().getSize();
        for (int i = 0; i < jList.getModel().getSize(); i++){    
            String table_name = jList.getModel().getElementAt(i);
            lastjj=instance.call_function(table_name,max);   
        }
      
        new Structure_Report(instance.getReport_list()).setVisible(true);
        
        if(lastjj !=1){
            Data_Sync_Button.setEnabled(true);
            Golden_Gate_Button.setEnabled(true);
        }
        
    }//GEN-LAST:event_Structure_Sync_ButtonActionPerformed

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
            Merged = reading.check_status(excel);
            for(int i=0 ; i<Merged.size() ; i++)tables_list.addElement(Merged.get(i));
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
                ResultSet rst = frontend.TableContent(table_name);
                ResultSet rst2 = backoffice.TableContent(table_name);
                
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
            SYNCHRONIZATION sync;                             
            long startTime, endTime;
            report_list = new DefaultListModel<>();
            try {
                if(frontend.getSID().isEmpty()){
                    sync = new MySQL_Data_Sync(frontend, backoffice, report_list);
                    sync.DISABLING_CONSTRAINTS("");
                }
                else{
                    System.out.println("ORACLE SERVER IS CHOSEN - PROCEEDING TO DISABLE CONSTRAINTS");
                    sync = new ORACLE_Data_Sync(frontend, backoffice, report_list);
                    for(int i=0 ; i<Merged.size() ; i++)sync.DISABLING_CONSTRAINTS(Merged.get(i));
                }
                
                //iterate through the list of tables
                if(!reading.getFE_list().isEmpty())report_list.addElement("FE -> BO");
                for (int i = 0; i < reading.getFE_list().size(); i++) {
                    String table_name = reading.getFE_list().get(i);
                    report_list.addElement("---Checking "+table_name+"---");
                    System.out.println("---Checking "+table_name+"---");
                   
                    startTime = System.nanoTime();
                    sync.Data_Synchronization(table_name, 1);
                    endTime = System.nanoTime();
                    
                    time_d=endTime*0.000000001-startTime*0.000000001;
                    
                    try {
                        sync.call_data(table_name,nmax12,time_d);
                    } catch (Exception ex) {
                        System.out.println("Data sync button error1 : "+ex.getMessage());
                    }
                    
                }
                report_list.addElement("");
                report_list.addElement("");
                if(!reading.getBO_list().isEmpty())report_list.addElement("BO -> FE");
                for (int i = 0; i < reading.getBO_list().size(); i++) {
                    String table_name = reading.getBO_list().get(i);
                    report_list.addElement("---Checking "+table_name+"---");
                    System.out.println("---Checking "+table_name+"---");
                    
                    startTime = System.nanoTime();
                    sync.Data_Synchronization(table_name, 2);
                    endTime = System.nanoTime();
                    
                    time_d=endTime*0.000000001-startTime*0.000000001;
                    try {
                        sync.call_data(table_name,nmax12,time_d);
                    } catch (Exception ex) {
                        System.out.println("Data sync button error2 : "+ex.getMessage());
                    }
                }
                
                report_list.addElement("");
                report_list.addElement("");
                if(!reading.getBID_FE_and_BO().isEmpty())report_list.addElement("FE -> BO && BO -> FE");
                for (int i = 0; i < reading.getBID_FE_and_BO().size(); i++) {
                    String table_name = reading.getBID_FE_and_BO().get(i);
                    report_list.addElement("---Checking "+table_name+"---");
                    System.out.println("---Checking "+table_name+"---");
                    
                    startTime = System.nanoTime();
                    sync.Data_Synchronization(table_name, 3);
                    endTime = System.nanoTime();
                    
                    time_d=endTime*0.000000001-startTime*0.000000001;
                    try {
                        sync.call_data(table_name,nmax12,time_d);
                    } catch (Exception ex) {
                        System.out.println("Data sync button error3 : "+ex.getMessage());
                    }
                }
                
                if(frontend.getSID().isEmpty())sync.ENABLING_CONSTRAINTS("");
                else{
                    System.out.println("PROCEEDING TO ENABLE CONSTRAINTS");
                    for(int i=0 ; i<Merged.size() ; i++)sync.ENABLING_CONSTRAINTS(Merged.get(i));
                }
                new Data_Report(report_list).setVisible(true);
                
            } catch (Exception ex) {
                System.out.println("Data sync button Error : "+ex.getMessage());
            }
       
    }//GEN-LAST:event_Data_Sync_ButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        frontend.CloseConnection();
        backoffice.CloseConnection();
        this.setVisible(false);
        new Login().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

   
    /**
     *
     * @param args
     */
    /*public static void main(String args[]) {
        
       
        java.awt.EventQueue.invokeLater(() -> {
            new MainPage().setVisible(true);
        });    
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Data_Sync_Button;
    private javax.swing.JButton Golden_Gate_Button;
    private javax.swing.JButton Read_Excel_Button;
    private javax.swing.JButton Structure_Sync_Button;
    private javax.swing.JTextField excel_name;
    private javax.swing.JButton jButton1;
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
