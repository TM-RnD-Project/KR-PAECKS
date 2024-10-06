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
public class Params {
    
    private ECP g1,g2;
    private BIG order, msk;
    private int k;
    
    public Params(){
        //defualt
    }
    
    public Params(ECP g1, ECP g2, BIG msk, BIG order, int k){
        this.g1 = g1;
        this.g2 = g2;
        this.msk = msk;
        this.order = order;
        this.k = k;
    }
    
    public void SetParams(ECP g1, ECP g2, BIG msk, BIG order, int k){
        this.g1 = g1;
        this.g2 = g2;
        this.msk = msk;
        this.order = order;
        this.k = k;
    }
    
    public ECP GetG1(){
        return g1;
    }
    
    public ECP GetG2(){
        return g2;
    }
    
    public BIG GetOrder(){
        return order;
    }
    
    public BIG GetMsk(){
        return msk;
    }
    
    public int GetK(){
        return k;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append("\n========Begin Params=========\n");
        
        str.append("g1: "+g1+"\n");
        str.append("g2: "+g2+"\n");
        str.append("msk: "+msk+"\n");
        str.append("order: "+order+"\n");
        str.append("k: "+k+"\n");
        str.append("========End of Params========\n");
        
        return str.toString();
    }
}
