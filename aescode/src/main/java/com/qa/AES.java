package com.qa;


//import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import com.qa.Base64Util;

public class AES {

	// 加密
	public static String Encrypt(String sSrc, String sKey) throws Exception {
	    
		try{
			if (sKey == null) {
		        System.out.print("Key为空null");
		        return null;
		    }
	    // 判断Key是否�?16�?
	   /* if (sKey.length() != 16) {
	        System.out.print("Key长度不是16�?");
	        return null;
	    }*/

            byte[] kb = sKey.getBytes("utf-8");
            SecretKeySpec sks = new SecretKeySpec(kb, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//算法/模式/补码方式
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            byte[] eb = cipher.doFinal(sSrc.getBytes("utf-8"));
            return new Base64().encodeToString(eb);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
	    
	}

	// 解密
	public static String Decrypt(String sSrc, String sKey) throws Exception {
	    try {
	        // 判断Key是否正确
	        if (sKey == null) {
	            System.out.print("Key为空null");
	            return null;
	        }
	        // 判断Key是否�?16�?
	        /*if (sKey.length() != 16) {
	            System.out.print("Key长度不是16�?");
	            return null;
	        }*/

	        
	        byte[] kb = sKey.getBytes("utf-8");
            SecretKeySpec sks = new SecretKeySpec(kb, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            
            byte[] encrypted1 = Base64Util.decode(sSrc);
            try {
	            byte[] original = cipher.doFinal(encrypted1);
	            String originalString = new String(original,"utf-8");
	            return originalString;
	        } catch (Exception e) {
	            System.out.println(e.toString());
	            return null;
	        }
	        
	    } catch (Exception ex) {
	        System.out.println(ex.toString());
	        return null;
	    }
	}
	
	

	
}
