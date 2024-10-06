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
public class Trapdoor {
    ECP[] t1,t2;
    BIG u_cap;
    
    public Trapdoor(){
        //default
    }
    
    public Trapdoor(ECP[] t1, ECP[] t2, BIG u_cap){
        this.t1 = t1;
        this.t2 = t2;
        this.u_cap = u_cap;
    }
    
    public void SetTrapdoor(ECP[] t1, ECP[] t2, BIG u_cap){
        this.t1 = t1;
        this.t2 = t2;
        this.u_cap = u_cap;
    }
    
    public void SetT1(ECP[] t1){
        this.t1 = t1;
    }
    
    public void SetT2(ECP[] t2){
        this.t2 = t2;
    }
    
    public void SetU_cap(BIG u_cap){
        this.u_cap = u_cap;
    }
    
    public ECP[] GetT1(){
        return t1;
    }
    
    public ECP[] GetT2(){
        return t2;
    }
    
    public ECP GetT1(int i){
        return t1[i];
    }
    
    public ECP GetT2(int i){
        return t2[i];
    }
    
    public BIG GetU_cap(){
        return u_cap;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append("\n========Begin Trapdoor=========\n");
        
        for(int i=0; i<t1.length; i++){
            str.append("T1,"+i+": "+t1[i]+"\n");
        }
        
        for(int i=0; i<t2.length; i++){
            str.append("T2,"+i+": "+t2[i]+"\n");
        }
        str.append("U_cap: "+u_cap+"\n");

        str.append("========End of Trapdoor========\n");
        
        return str.toString();        
    }
}
