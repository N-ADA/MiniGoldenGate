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

public class ExcelReader extends JFrame {
 
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
        List<String> FE_list = new ArrayList<>();
        List<String> BO_list = new ArrayList<>();
        List<String> Merged_FE_and_BO = new ArrayList<>();

        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //Check the cell type and format accordingly
                if(cell.getStringCellValue().equals("FE -> BO")==true ){
                    status_check= cell.getColumnIndex();
                }
                else if (cell.getStringCellValue().equals("BO -> FE")==true){
                    Bo_status_check=cell.getColumnIndex();
                }

            }

        }

        while(j<k){
            if(ReadCellData(j,status_check,path).equals("y")==true || ReadCellData(j,status_check,path).equals("Y")==true){

                String vOutput=g.ReadCellData(j,0,path);

                FE_list.add(vOutput);

            }else if(ReadCellData(j,Bo_status_check,path).equals("y")==true || ReadCellData(j,Bo_status_check,path).equals("Y")==true){

                String vOutput=g.ReadCellData(j,0,path);
                BO_list.add(vOutput);
            }       
            j++;       

        }
        
        Merged_FE_and_BO.addAll(BO_list);
        Merged_FE_and_BO.addAll(FE_list);
        
        return Merged_FE_and_BO;
    }
    
}