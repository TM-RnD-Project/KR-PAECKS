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
public class PrivateKey {
    private Polynomial p1,p2;
    
    
    public PrivateKey(){
        //default
    }
    
    public void PrivateKey(Polynomial p1, Polynomial p2){
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public void SetPrivateKey(Polynomial p1, Polynomial p2){
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public void SetP1(Polynomial p1){
     this.p1 = p1;   
    }
    
    public void SetP2(Polynomial p2){
     this.p2 = p2;   
    }
    
    public Polynomial GetP1(){
        return p1;
    }
    
    public Polynomial GetP2(){
        return p2;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append("\n========Begin Private Key=========\n");
        
        str.append("P1: "+p1.toString()+"\n");
        str.append("P2: "+p2.toString()+"\n");
        
        str.append("========End of Private Key========\n");
        
        return str.toString();
    }
}
