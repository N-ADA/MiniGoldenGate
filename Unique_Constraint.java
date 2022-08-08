/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minigoldengate;
import java.util.ArrayList;

/**
 *
 * @author nadabounajma
 */
public class Unique_Constraint {
    private String constraint;
    private ArrayList<String> columns;
    
    public Unique_Constraint(ArrayList<String> columns){
        this.columns = columns;
    }

    public String getConstraint() {
        return constraint;
    }

    public ArrayList<String> getColumns() {
        return columns;
    } 
    
}