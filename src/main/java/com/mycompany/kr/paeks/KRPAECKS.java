/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.kr.paeks;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.miracl.core.ED25519.*;
import org.miracl.core.RAND;
import org.miracl.core.SHA3;

/**
 *
 * @author CKM
 */
public class KRPAECKS {
    
    public KRPAECKS(){
        //default
    }
    
    public void Setup(Params params){
        
        int k = 20;
        BIG order = new BIG(ROM.CURVE_Order);
        
        SecureRandom rand = new SecureRandom();
        RAND RNG = new RAND();
        RNG.clean();
	RNG.seed(100,rand.generateSeed(100));
        
        BIG a = BIG.randomnum(order,RNG);
        
        ECP g1 = ECP.generator();
        ECP g2 = g1.mul(a);
        
        params.SetParams(g1, g2,a, order, k);
    } 
    
    public void KeyGen(Params params,PublicKey pk, PrivateKey sk){
        
        int k = params.GetK();
        k = k+1;
        BIG order = params.GetOrder();
        
        SecureRandom rand = new SecureRandom();
        RAND RNG = new RAND();
        RNG.clean();
	RNG.seed(100,rand.generateSeed(100));
        
        BIG s1 = BIG.randomnum(order,RNG);
        //System.out.println("S1 value = "+s1+"\n");
        BIG s2 = BIG.randomnum(order,RNG);
        //System.out.println("\nS2 value = "+s2+"\n");
        
        Polynomial p1 = new Polynomial(k,s1,order);
        Polynomial p2 = new Polynomial(k,s2,order);
        
        sk.SetPrivateKey(p1, p2);
        
        ECP[] Dt = new ECP[k];
        
        for(int i=0; i<k; i++){
            Dt[i] = params.GetG1().mul(p1.getCoeff(i));
            Dt[i].add(params.GetG2().mul(p2.getCoeff(i)));
        }
        
        pk.SetPublicKey(Dt);
    }
    
    public Ciphertext Encrypt(Params params, PublicKey rpk, PrivateKey ssk, String[] kw) throws UnsupportedEncodingException{
        
        ECP C1, C2;
        ECP[] TC1 = new ECP[kw.length];
        ECP[] TC2 = new ECP[kw.length];
        BIG u;
        int k = params.GetK();
        k = k+1;
        BIG order = params.GetOrder();
        
        SecureRandom rand = new SecureRandom();
        RAND RNG = new RAND();
        RNG.clean();
	RNG.seed(100,rand.generateSeed(100));
        
        BIG HashKW = HashToBIG(kw);
        
        for(int i=0; i<kw.length; i++){
            
            BIG r = BIG.randomnum(order,RNG);
            BIG HashKWi = HashToBIG(kw[i]);

            BIG temp = ssk.GetP1().evaluate(HashKW);
            BIG newtemp = BIG.modmul(temp, r, order);
            //System.out.println("Value of Temp ="+temp+"\n");

            C1 = rpk.GetPublicKey(0).mul(HashKWi.powmod(new BIG(0), order));

            for(int j=1; j<k; j++){
                C1.add(rpk.GetPublicKey(j).mul(HashKWi.powmod(new BIG(j), order)));
            }

            TC1[i] = new ECP(C1.mul(newtemp));

            BIG temp2 = ssk.GetP2().evaluate(HashKW);
            BIG newtemp2 = BIG.modmul(temp2, r, order);
            //System.out.println("Value of Temp2 ="+temp2+"\n");

            C2 = rpk.GetPublicKey(0).mul(HashKWi.powmod(new BIG(0), order));

            for(int j=1; j<k; j++){
                C2.add(rpk.GetPublicKey(j).mul(HashKWi.powmod(new BIG(j), order)));
            }

            TC2[i] = new ECP(C2.mul(newtemp2));
        }
        
        u = ssk.GetP2().evaluate(HashKW);
        
        BIG inv = ssk.GetP1().evaluate(HashKW);
        inv.invmodp(order);
        BIG newu = BIG.modmul(u, inv, order);      
        
        return new Ciphertext(TC1,TC2,newu);
    }
    
    public Trapdoor Trapdoor(Params params, PublicKey pk, PrivateKey sk, String[] kw) throws UnsupportedEncodingException{
        
        ECP T1, T2;
        ECP[] TT1 = new ECP[kw.length];
        ECP[] TT2 = new ECP[kw.length];
        BIG u_cap;
        int k = params.GetK();
        k = k+1;
        BIG order = params.GetOrder();
        
        SecureRandom rand = new SecureRandom();
        RAND RNG = new RAND();
        RNG.clean();
	RNG.seed(100,rand.generateSeed(100));
        
        BIG HashKW = HashToBIG(kw);
        
        for(int i=0; i<kw.length; i++){
            
            BIG r = BIG.randomnum(order,RNG);
            BIG HashKWi = HashToBIG(kw[i]);

            BIG temp = sk.GetP1().evaluate(HashKW);
            BIG newtemp = BIG.modmul(temp, r, order);

            T1 = pk.GetPublicKey(0).mul(HashKWi.powmod(new BIG(0), order));

            for(int j=1; j<k; j++){
                T1.add(pk.GetPublicKey(j).mul(HashKWi.powmod(new BIG(j), order)));
            }

            TT1[i] = new ECP(T1.mul(newtemp));

            BIG temp2 = sk.GetP2().evaluate(HashKW);
            BIG newtemp2 = BIG.modmul(temp2, r, order);

            T2 = pk.GetPublicKey(0).mul(HashKWi.powmod(new BIG(0), order));

            for(int j=1; j<k; j++){
                T2.add(pk.GetPublicKey(j).mul(HashKWi.powmod(new BIG(j), order)));
            }

            TT2[i] = new ECP(T2.mul(newtemp2));
        }
        
        u_cap = sk.GetP2().evaluate(HashKW);
        
        BIG inv = sk.GetP1().evaluate(HashKW);
        inv.invmodp(order);
        BIG newu_cap = BIG.modmul(u_cap, inv, order);
        
        return new Trapdoor(TT1,TT2,newu_cap);
    }
    
    public boolean Test(Params params, Ciphertext c, Trapdoor t){
        boolean flag = false;
        
        ECP SumC1 = c.GetC1(0);
        for(int i=1;i<c.GetC1().length;i++){
            SumC1.add(c.GetC1(i));
        }
        
        ECP SumT2 = t.GetT2(0);
        for(int i=1;i<t.GetT2().length;i++){
            SumT2.add(t.GetT2(i));
        }
        
        ECP left = SumC1.mul(c.GetU());
        left.add(SumT2);
        
        ECP SumC2 = c.GetC2(0);
        for(int i=1;i<c.GetC2().length;i++){
            SumC2.add(c.GetC2(i));
        }
        
        ECP SumT1 = t.GetT1(0);
        for(int i=1;i<t.GetT1().length;i++){
            SumT1.add(t.GetT1(i));
        }
        
        ECP right = SumT1.mul(t.GetU_cap());
        right.add(SumC2);
        
        if(left.equals(right)){
            flag = true;
        }
        
        return flag;
    }
    
    public BIG HashToBIG(String C2) throws UnsupportedEncodingException{
        SHA3 hash = new SHA3(SHA3.SHAKE256);
        byte[] output = new byte[CONFIG_BIG.MODBYTES];
        byte[] t = C2.getBytes("UTF8");
        for(int i=0;i<t.length;i++){
            hash.process(t[i]);
        }
        hash.shake(output, CONFIG_BIG.MODBYTES);
        return BIG.fromBytes(output);
    }
    
    public BIG HashToBIG(String[] C2) throws UnsupportedEncodingException{
        String ConcateString = null;
        
        for(int i=0; i<C2.length; i++){
            ConcateString = ConcateString+C2[i];
        }
        
        SHA3 hash = new SHA3(SHA3.SHAKE256);
        byte[] output = new byte[CONFIG_BIG.MODBYTES];
        byte[] t = ConcateString.getBytes("UTF8");
        for(int i=0;i<t.length;i++){
            hash.process(t[i]);
        }
        hash.shake(output, CONFIG_BIG.MODBYTES);
        return BIG.fromBytes(output);
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        
        String[] KW = {"Urgent", "Please", "Hey"};
        
        KRPAECKS krpaeks = new KRPAECKS();
        Params params = new Params();
        
        PublicKey SenderPK = new PublicKey();
        PrivateKey SenderSK = new PrivateKey();
        
        PublicKey ReceiverPK = new PublicKey();
        PrivateKey ReceiverSK = new PrivateKey();
        
        long SetupTime = System.currentTimeMillis();
        krpaeks.Setup(params);
        long FinalSetupTime = System.currentTimeMillis()-SetupTime;
        System.out.println(params);
        
        long SKeyGenTime = System.currentTimeMillis();
        krpaeks.KeyGen(params, SenderPK, SenderSK);
        long FinalSKeygenTime = System.currentTimeMillis()-SKeyGenTime;
        System.out.println("\n========Begin Sender=========\n");
        System.out.println(SenderSK);
        System.out.println(SenderPK);
        System.out.println("\n========End Sender=========\n");
        
        long RKeyGenTime = System.currentTimeMillis();
        krpaeks.KeyGen(params, ReceiverPK, ReceiverSK);
        long FinalRKeygenTime = System.currentTimeMillis()-RKeyGenTime;
        System.out.println("\n========Begin Receiver=========\n");
        System.out.println(ReceiverSK);
        System.out.println(ReceiverPK);
        System.out.println("\n========End Receiver=========\n");
        
        long CiphertextTime = System.currentTimeMillis();
        Ciphertext c = krpaeks.Encrypt(params, ReceiverPK, SenderSK, KW);
        long FinalCiphertextTime = System.currentTimeMillis()-CiphertextTime;
        System.out.println(c);
        
        long TrapdoorTime = System.currentTimeMillis();
        Trapdoor t = krpaeks.Trapdoor(params, SenderPK, ReceiverSK, KW);
        long FinalTrapdoorTime = System.currentTimeMillis()-TrapdoorTime;
        System.out.println(t);
        
        long TestTime = System.currentTimeMillis();
        boolean Test = krpaeks.Test(params, c, t);
        long FinalTestTime = System.currentTimeMillis()-TestTime;
        
        if(Test){
            System.out.println("Test Successful!\n");
            System.out.println("======Runtime=========\nSetup Time ="+FinalSetupTime+"ms\nSender KeyGen Time ="+FinalSKeygenTime+"ms\nReceiver KeyGen Time ="+FinalRKeygenTime+"ms\nCiphertext Time ="+FinalCiphertextTime+"ms\nTrapdoor Time ="+FinalTrapdoorTime+"ms\nTest Time ="+FinalTestTime+"ms");
            System.out.println("====================");
        }
        else{
            System.out.println("Test Unsuccessful!");
        }
        
        //************************Performance Analysis************************************
//        long TestCiphertextTime = System.currentTimeMillis();
//        krpaeks.TestSQLEnc(params, ReceiverPK, SenderSK, "Forwarded");
//        long FinalTestCiphertextTime = System.currentTimeMillis()-TestCiphertextTime;
//        
//        long TestTrapdoorTime = System.currentTimeMillis();
//        Trapdoor Testt = krpaeks.Trapdoor(params, SenderPK, ReceiverSK, "Forwarded");
//        long FinalTestTrapdoorTime = System.currentTimeMillis()-TestTrapdoorTime;
//        
//        long SqlTestTime = System.currentTimeMillis();
//        int SqlTestCnt = krpaeks.TestSQLTest(params, Testt);
//        long FinalSqlTestTime = System.currentTimeMillis()-SqlTestTime;
//        
//        System.out.println("Total Number of Test:"+SqlTestCnt);
//        System.out.println("===========Runtime===========\nTime for Encrypt All Emails Contain Keyword \"Forwarded\":"+FinalTestCiphertextTime+"ms\nTime for Gen Trapdoor:"+FinalTestTrapdoorTime+"ms\nTime for Test All Ciphertext:"+FinalSqlTestTime+"ms\n");
//        System.out.println("===========End Runtime===========\n");
        //***********************End Performance Analysis**********************************
        
    }
    
    public void TestSQLEnc(Params params, PublicKey rpk, PrivateKey ssk, String[] kw){
        Gson gson = new Gson();
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://192.168.68.110/EnronMailDS";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, "root", "root");
            String sql = "SELECT * FROM JohnArnoldMail";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    boolean containsKeyword = true;
                    String body = rs.getString("body");
                    String date = rs.getString("Date");
                    String to = rs.getString("X-To");
                    if (body != null) {
                        for (String keyword : kw) {
                            if (body.contains(keyword)) {
                                containsKeyword = containsKeyword && true;                                
                            }
                            else{
                                containsKeyword = containsKeyword && false;
                            }
                        }
                    }
                    if (containsKeyword) {
                        // Call your Encrypt function to generate ciphertext
                        Ciphertext ciphertext = Encrypt(params,rpk,ssk,kw);
                        String StringCipher = gson.toJson(ciphertext);
                        // Update the ciphertext in the database
                        String updateSql = "UPDATE JohnArnoldMail SET ciphertext = ? WHERE `Date` = ? AND `X-To`=?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, StringCipher);
                            updateStmt.setString(2, date);
                            updateStmt.setString(3, to); 
                            updateStmt.executeUpdate();
                        }
                    }
                }                
            }   
            catch (SQLException | UnsupportedEncodingException ex) {
            Logger.getLogger(KRPAECKS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(KRPAECKS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int TestSQLTest(Params params, Trapdoor t){
        Gson gson = new Gson();
        int cnt =1;
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://192.168.68.110/EnronMailDS";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, "root", "root");
            String sql = "SELECT ciphertext FROM JohnArnoldMail";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String ciphertextString = rs.getString("ciphertext");
                    if (ciphertextString != null) {
                        Ciphertext c = gson.fromJson(ciphertextString, Ciphertext.class);
                        
                        boolean CntTest = Test( params, c, t);
                        if (CntTest){
                            cnt++;
                        }
                    }
                }
                
            }
            catch (SQLException ex) {
            Logger.getLogger(KRPAECKS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(KRPAECKS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cnt;
    }
}
