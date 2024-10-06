/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kr.paeks;


import org.miracl.core.ED25519.*;
/**
 *
 * @author CKM
 */
public class PublicKey {
    ECP[] Dt;
    
    public PublicKey(){
        //default
    }
    
    public PublicKey(ECP[] Dt){
        this.Dt = Dt;
    }
    
    public void SetPublicKey(ECP[] Dt){
        this.Dt = Dt;
    }
    
    public ECP[] GetPublicKey(){
        return Dt;
    }
    
    public ECP GetPublicKey(int i){
        return Dt[i];
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append("\n========Begin Public Key=========\n");

        for(int i=0; i < Dt.length; i++){
            str.append("Dt[" + i + "]: "+Dt[i]+",\n");
        }
        
        str.append("========End of Public Key========\n");
        
        return str.toString();        
    }
}
