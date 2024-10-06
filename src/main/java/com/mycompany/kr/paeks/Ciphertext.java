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
public class Ciphertext {
    ECP[] c1,c2;
    BIG u;
    
    public Ciphertext(){
        //default
    }
    
    public Ciphertext(ECP[] c1, ECP[] c2, BIG u){
        this.c1 = c1;
        this.c2 = c2;
        this.u = u;
    }
    
    public void SetCiphertext(ECP[] c1, ECP[] c2, BIG u){
        this.c1 = c1;
        this.c2 = c2;
        this.u = u;
    }
    
    public void SetC1(ECP[] c1){
        this.c1 = c1;
    }
    
    public void SetC2(ECP[] c2){
        this.c2 = c2;
    }
    
    public void SetU(BIG u){
        this.u = u;
    }
    
    public ECP[] GetC1(){
        return c1;
    }
    
    public ECP[] GetC2(){
        return c2;
    }
    
    public ECP GetC1(int i){
        return c1[i];
    }
    
    public ECP GetC2(int i){
        return c2[i];
    }
    
    public BIG GetU(){
        return u;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append("\n========Begin Ciphertext=========\n");
        
        for(int i=0; i<c1.length; i++){
            str.append("C1,"+i+": "+c1[i]+"\n");
        }
        
        for(int i=0; i<c2.length; i++){
            str.append("C2,"+i+": "+c2[i]+"\n");
        }
        
        str.append("U: "+u+"\n");

        str.append("========End of Ciphertext========\n");
        
        return str.toString();        
    }
}
