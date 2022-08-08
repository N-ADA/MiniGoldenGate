/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.minigoldengate;

/**
 *
 * @author nbounajma
 */
public interface SYNCHRONIZATION {
    public void DISABLING_CONSTRAINTS(String table_name);
    public void ENABLING_CONSTRAINTS(String table_name);
    public void Data_Synchronization(String table_name, int direction);
    public void call_data(String table_name,int nmax12,double time);
}
