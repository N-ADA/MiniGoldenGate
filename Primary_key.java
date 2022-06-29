/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minigoldengate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nadabounajma
 */
public class Primary_key {
    private String name;
    private ArrayList<String> value = new ArrayList<>();
    private boolean IsChar;
    
    public Primary_key(String name, ResultSet rst){
        this.name = name;
        
        try {
            String data_type = rst.getMetaData().getColumnTypeName(1);
            if(data_type.contains("CHAR"))IsChar = true;
            while(rst.next()){
                if(IsChar)value.add("'"+rst.getString(1)+"'");
                else value.add(rst.getString(1));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getValue() {
        return value;
    }    
}
