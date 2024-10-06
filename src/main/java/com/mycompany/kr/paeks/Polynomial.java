package com.mycompany.kr.paeks;

import java.security.SecureRandom;
import org.miracl.core.ED25519.BIG;
import org.miracl.core.RAND;


/**
 * Polynomial operations.
 * @author TanSY
 */
public class Polynomial{
    private int degree;
    private BIG[] coeff;
    private BIG order;
    
    public Polynomial(){
        //default, do nothing
    }
    
    public Polynomial(int degree, BIG sk, BIG order){
        //Init all parameters
        this.degree = degree;
        this.order = order;
        
        SecureRandom rand = new SecureRandom();
        RAND RNG = new RAND();
        RNG.clean();
	RNG.seed(100,rand.generateSeed(100));                       
        
        coeff = new BIG[degree];
        coeff[0] = new BIG(sk);
        for(int i=1;i<degree; i++){
             coeff[i]=BIG.randomnum(order,RNG);
        }
    }
    
    public BIG evaluate(BIG x){
        BIG t2;
        BIG accum = new BIG(0);
        for(int j=0; j<degree; j++){
            //t2 =coeff[j].multiply(x.pow(j).mod(order)).mod(order);
            //accum = accum.add(t2);
            
            t2 = BIG.modmul(coeff[j], x.powmod(new BIG(j), order), order);
            accum.add(t2);
            accum.mod(order);
        }
        
        return accum;    
    } 
    
    public int getDegree() {
        return degree;
    }

    public BIG[] getCoeff() {
        return coeff;
    }
    
    public BIG getCoeff(int i) {
        return coeff[i];
    }

    public BIG getOrder() {
        return order;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        str.append("\n========Begin Polynomial=========\n");
        
        str.append("degree: "+degree+"\n");
        str.append("order: "+order+"\n");
        
        for(int i=0; i<coeff.length; i++)
        str.append("coeff["+i+"]: " + coeff[i] + "\n");
        
        str.append("========End of Polynomial========\n");
        
        return str.toString();
    }
}
