package com.mycompany.minigoldengate;
/**
 *
 * @author Yassine Chtaini
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;   
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader extends JFrame {
        List<String> FE_list = new ArrayList<>();
        List<String> BO_list = new ArrayList<>();
        List<String> BID_FE_and_BO = new ArrayList<>();            
        List<String> Merged_FE_and_BO = new ArrayList<>();
        public ExcelReader(){
    }

  

   
    public Sheet reading(String em)throws FileNotFoundException, IOException{ 
               
        Workbook wb;    
        FileInputStream fis= new FileInputStream(em);
        wb=new XSSFWorkbook(fis); 
        Sheet sheet=wb.getSheetAt(0);
        
        return sheet;
    }
 
    
    public String ReadCellData(int vRow, int vColumn,String path) throws FileNotFoundException, IOException{  
        
       String value;
       Row row=reading(path).getRow(vRow); 
       Cell cell=row.getCell(vColumn); 
       value=cell.getStringCellValue(); 
    
       return value;               
    }  
  
   public List check_status(String path) throws FileNotFoundException, IOException{
 
        ExcelReader g = new ExcelReader();
        int status_check=0;
        int Bo_status_check=0;
        Workbook wb; 
        FileInputStream fis= new FileInputStream(path);
        wb=new XSSFWorkbook(fis);
        Sheet sheet=wb.getSheetAt(0);

        Sheet mn=g.reading(path);

        int j=0;
        int k=mn.getLastRowNum()+1;
       

        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //Check the cell type and format accordingly
                if(cell.getStringCellValue().equals("FE->BO")==true ){
                    status_check= cell.getColumnIndex();
                }
                else if (cell.getStringCellValue().equals("BO->FE")==true){
                    Bo_status_check=cell.getColumnIndex();
                }

            }

        }

        while(j<k){
            if((ReadCellData(j,status_check,path).equals("y")==true || ReadCellData(j,status_check,path).equals("Y")==true)
                    && (ReadCellData(j,Bo_status_check,path).equals("n")==true || ReadCellData(j,Bo_status_check,path).equals("N")==true)){

                String vOutput=g.ReadCellData(j,0,path);

                FE_list.add(vOutput);

            }else if((ReadCellData(j,Bo_status_check,path).equals("y")==true || ReadCellData(j,Bo_status_check,path).equals("Y")==true) 
                    && (ReadCellData(j,status_check,path).equals("n")==true || ReadCellData(j,status_check,path).equals("N")==true)){
                    
                String vOutput=g.ReadCellData(j,0,path);
                BO_list.add(vOutput);
            } else if ((ReadCellData(j,Bo_status_check,path).equals("y")==true || ReadCellData(j,Bo_status_check,path).equals("Y")==true )
                    && (ReadCellData(j,status_check,path).equals("y")==true || ReadCellData(j,status_check,path).equals("Y")==true))  {
                
                 String vOutput=g.ReadCellData(j,0,path);
                  BID_FE_and_BO.add(vOutput);
            
            }   
            j++;       

        }
        
        Merged_FE_and_BO.addAll(BO_list);
        Merged_FE_and_BO.addAll(FE_list);
        Merged_FE_and_BO.addAll(BID_FE_and_BO);
        
        
        return Merged_FE_and_BO;
    }
   
    public int size_col(String path) throws IOException{
        
        ExcelReader g = new ExcelReader();
        int status_check=0;
        int Bo_status_check=0;
        Workbook wb; 
        FileInputStream fis= new FileInputStream(path);
        wb=new XSSFWorkbook(fis);
        Sheet sheet=wb.getSheetAt(0);

        Sheet mn=g.reading(path);

        int j=0;
        int kl=mn.getLastRowNum()+1;
    return kl;
    }
     public List<String> getFE_list() {
        return FE_list;
    }

   

    public List<String> getBO_list() {
        return BO_list;
    }

   

      public List<String> getBID_FE_and_BO() {
        return BID_FE_and_BO;
    }
}