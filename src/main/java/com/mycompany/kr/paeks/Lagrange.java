package com.mycompany.kr.paeks;

import java.math.BigInteger;
import org.miracl.core.ED25519.*;

/**
 * Provide Lagrange interpolation operations.
 * @author TanSY
 */
public class Lagrange{
    BIG[] x;
    BIG order;
    
    public Lagrange(){
        //default, do nothing
    }
    
    /**
    * Initialize the set S for Lagrange coefficient.
    * @param x the set S
    * @param order the curve order    
    */
    public Lagrange(BIG[] x, BIG order){
        this.x = x;
        this.order = order;
    }    
    
    
    /**
     * To reconstruct the polynomial q(x) and evaluate it at a specific point x.
     * For testing purposes.
     * @param x x-coordinates
     * @param y y-coordinates
     * @param point the point to be evaluated at
     * @param order curve order
     * @return q(point)
     */
    public BIG test(BIG[] x, BIG[] y, BIG point, BIG order){
        BIG z = new BIG();
        z.zero();
        int n = this.x.length;

        for(int count = 0; count<n; count++)
            {                
                //BIG temp = CoefficientAt(x[count], point);
                BIG temp = toBIG(CoefficientAt(toBigInteger(x[count]), toBigInteger(point)));
                
                y[count].mod(order);
                z.add(BIG.modmul(temp, y[count], order));
                //z = temp.plus(z);
                z.mod(order);
            }
        return z;
    }
    
    
    /**
     * Compute the Lagrange coefficient for an x[i] \in S at a specific point.
     * @param xi an attribute in the set S = this.x
     * @param point the point to be evaluated at
     * @return Lagrange coefficient for x[i] \in x the point specified. 
     */
    public BIG CoefficientAt(BIG xi, BIG point){
        return toBIG(CoefficientAt(toBigInteger(xi), toBigInteger(point)));
        /*
        //some unknown bugs with the BIG maths operations
        xi.mod(order);
        point.mod(order);
        //BigInteger z = point;
        int n = x.length;

        //Initialisation of variable
        BIG numerator = new BIG();
        numerator.one();
        numerator.mod(order);
        BIG denominator = new BIG();
        denominator.one();
        denominator.mod(order);

        for(int count2 = 0; count2<n; count2++)
        {
            if (!x[count2].equals(xi))
            {
              BIG temp = point.minus(x[count2]);
              temp.mod(order);
              
              //numerator = numerator.multiply(point.subtract(x[count2])).mod(order);
              numerator = BIG.modmul(numerator, point.minus(x[count2]), order);
              //numerator.mod(order);
              
              temp = xi.minus(x[count2]);
              temp.mod(order);
              
              //denominator = denominator.multiply(xi.subtract(x[count2])).mod(order);             
              denominator = BIG.modmul(denominator, temp, order);
              //denominator.mod(order);
            } 
        }
        
        denominator.invmodp(order);
        return BIG.modmul(numerator, denominator, order);
        //return numerator.multiply(denominator.modInverse(order)).mod(order);
        */
    }
    
    /**
     * Compute the Lagrange coefficient for an x[i] \in S at a specific point.
     * @param xi an attribute in the set S = this.x
     * @param point the point to be evaluated at
     * @return Lagrange coefficient for x[i] \in x the point specified. 
     */
    public BigInteger CoefficientAt(BigInteger xi, BigInteger point){
        //BigInteger z = point;
        int n = x.length;
        BigInteger[] x = toBigInteger(this.x);
        BigInteger order = toBigInteger(this.order);
        
        //Initialisation of variable
        BigInteger numerator = BigInteger.ONE; 
        BigInteger denominator = BigInteger.ONE;

        for(int count2 = 0; count2<n; count2++)
        {
            if (!x[count2].equals(xi))
            {
              numerator = numerator.multiply(point.subtract(x[count2])).mod(order);
              //System.out.println("num = "+ numerator);

              denominator = denominator.multiply(xi.subtract(x[count2])).mod(order);
              //System.out.println("den = " + denominator);
            } 
        }
             
        return numerator.multiply(denominator.modInverse(order)).mod(order);
    }
    
    public BigInteger toBigInteger(BIG num){
        byte[] b = new byte[CONFIG_BIG.MODBYTES];
        num.toBytes(b);
        return new BigInteger(1,b);
        //return new BigInteger(num.toString().replaceFirst("^0+(?!$)", ""),16);
    }
    
    public BigInteger[] toBigInteger(BIG[] num){
        BigInteger[] NUM = new BigInteger[num.length];
        
        for(int i=0;i<num.length;i++){
            NUM[i] = toBigInteger(num[i]);
        }
        
        return NUM;
        //return new BigInteger(num.toString().replaceFirst("^0+(?!$)", ""),16);
    }
    
    public BIG toBIG(BigInteger num){
        byte[] b = new byte[CONFIG_BIG.MODBYTES];        
        byte[] val = num.toByteArray();
        
        int j=val.length-1;
        for(int i=b.length-1;i>=0;i--){
            if(j>-1){
                b[i] = val[j];
                j--;
            }else{
                if(num.signum()==-1){
                    b[i] = (byte) 0xff;
                }else{
                    b[i] = (byte) 0x00;
                }
            }
        }
        
        return BIG.fromBytes(b);
    }
    
    public BIG[] toBIG(BigInteger[] num){
        BIG[] number = new BIG[num.length];
        
        for(int i=0;i<num.length;i++){
            number[i] = toBIG(num[i]);
        }
        
        return number;        
    }
}