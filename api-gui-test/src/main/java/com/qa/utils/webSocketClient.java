package com.qa.utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
 
//import com.cn.service.impl.SearchPersonServiceImpl;
 
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import com.qa.utils.AES;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
 
public class webSocketClient {
	
    private static Logger logger = Logger.getLogger(webSocketClient.class);
    public static WebSocketClient client;
    public static String respons = new String();
	//var jsencrypt = new JSEncrypt();
    public static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALCMObUdcPDxAflm0YxAiXaH8jwT5yE5ADxpDzEJH/5oedE5o39lTlWZ3ZnPuDpwBpxH0FKbrT6JIJi28QYkqXRaq9s8YmRzy152M0XVCqBaqSS4TpR2DDY6QQokLEODo+sCeJHsJzSKj0bxtbg/wkMmNJttp+8w8MMVtVVRYnHHAgMBAAECgYAOLuW/8CKPqL0A3Uq+WrzwYdGLFApAeATV1Zbb2KDSXnBS56+D346gf+D2p2Jkh3VwfrB0wn7zhC6zNhc86BsY1K6Q7xU8b7asDBqki48H3ExuxjEosEqUdLf9p9pPBCPI3I4CN/EZPEoFjNRNi5ZX/CY4iaOgsXPHEkcxuW3GQQJBAOBo9UpXesZYCsmuuGN5SOy6tXI613NUBvXKXkxBil3ZOqozlaSjv5NSQb2zLeh2DcYfecumfgn04rNM/pLeDmECQQDJZnXWg4VrXdjc9hqu/8rkmxdhsr3ERkX1uKJrDUB+AOdFs6mS9gEHnJ70zqQ2ucbhQ4htulbLc9pBVL5gy+EnAkEArdhhfa/7SsBVyxvxeA4zMkEJ4242Df/gTHTzTDvRxxZL3iKMILlB5gzpJN40CEu8K+miXuOh7HCrVp+k733awQJBAMDkERhS/wXF7F40l3nkIz6wC8TWnEnPxFGDdItzNcF4vAhV+qN2WaYgq11sTHrdk01MkO4G+foCC5dmwq+SlSECQGm58ReqUTRDAKl32VX0vEdVvOj2getVxW6jQjJFiGj8iNDfK+DpiLfns3YjwSlWHGxHz1S6/lQ+58+M+fEyvUs=";
    
   // public static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyMDM4OTc5MjkxIiwiZXhwIjoxNTM5NDM4OTUzfQ.2nQIb3ESFQOLk3YAMPlRlSzwWRFk5UNsh0B0de_kG2U";
    public static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyMDM4OTc5MzAxIiwiZXhwIjoxNTM5OTQ5OTA1fQ.NsgoIebgmvvNaEqPhbxA6STkHBRz8a29SLZChkAW0yI";
    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvMCkMUkh7AJqwUAecmgHZwQbiR4u7ZdOhuzoxZEhAZUjrBarfHvttwfKLFM1r2uXvuu2rrYKjpa1iUV2A4rLeHlPnT07IeelAFiUKbjOaqS1K1ByTjIFCz466B8bMRYIOA6Za5j4OcVaQvpgXWZicshHssLFCeYnj2f5XAYQFiS9It6lJ0gGJWT2YSD6WxMAV1JRCpLJE0rtV5egAqAp9UImsZDjE2mVHXCTjlQKsdi+8jRJatZFLwDqOU0RGlgmwcjdg6u511xWWaQX1G3IhSMRAjrY4FDxxYRKBGrkNBPAp34NodGWL1iEHD+GdR3wRvbIAnLNU0XDf2bEenMPzwIDAQAB";		    
    public static String commKey = String.valueOf(System.currentTimeMillis())+ String.valueOf(System.currentTimeMillis()) + String.valueOf(System.currentTimeMillis()).substring(0, 6);
	public static String jwtToken = new String();
	public static Date pingTime = new Date();
	public static webSocketClientExample pingTime_s = new webSocketClientExample();
	
	
	public static JSONObject  formatParams (String cmd,JSONObject p) {
		String a = new String();
		a ="{\"cmd\":\"" + cmd + "\",\"params\":{\"jwt\":\""+ jwtToken +"\",\"token\":\""+ jwtToken +"\"},\"status\":{\"time\":"+ String.valueOf(System.currentTimeMillis()) +"}}";
		JSONObject b =  JSONObject.fromObject(a); 
		JSONObject c = b.getJSONObject("params");
		c.putAll(p);
		b.put("params",c);
		System.out.print("\t\r加密后数据----" + b.toString() + "\t\r");
		return b;
	}
	
    public static void main(String[] args) throws Exception {
    	BasicConfigurator.configure(); //强制打开Log4j������
        try {
        	
        	//String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwjDm1HXDw8QH5ZtGMQIl2h/I8E+chOQA8aQ8xCR/+aHnROaN/ZU5Vmd2Zz7g6cAacR9BSm60+iSCYtvEGJKl0WqvbPGJkc8tedjNF1QqgWqkkuE6Udgw2OkEKJCxDg6PrAniR7Cc0io9G8bW4P8JDJjSbbafvMPDDFbVVUWJxxwIDAQAB";
			//String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALCMObUdcPDxAflm0YxAiXaH8jwT5yE5ADxpDzEJH/5oedE5o39lTlWZ3ZnPuDpwBpxH0FKbrT6JIJi28QYkqXRaq9s8YmRzy152M0XVCqBaqSS4TpR2DDY6QQokLEODo+sCeJHsJzSKj0bxtbg/wkMmNJttp+8w8MMVtVVRYnHHAgMBAAECgYAOLuW/8CKPqL0A3Uq+WrzwYdGLFApAeATV1Zbb2KDSXnBS56+D346gf+D2p2Jkh3VwfrB0wn7zhC6zNhc86BsY1K6Q7xU8b7asDBqki48H3ExuxjEosEqUdLf9p9pPBCPI3I4CN/EZPEoFjNRNi5ZX/CY4iaOgsXPHEkcxuW3GQQJBAOBo9UpXesZYCsmuuGN5SOy6tXI613NUBvXKXkxBil3ZOqozlaSjv5NSQb2zLeh2DcYfecumfgn04rNM/pLeDmECQQDJZnXWg4VrXdjc9hqu/8rkmxdhsr3ERkX1uKJrDUB+AOdFs6mS9gEHnJ70zqQ2ucbhQ4htulbLc9pBVL5gy+EnAkEArdhhfa/7SsBVyxvxeA4zMkEJ4242Df/gTHTzTDvRxxZL3iKMILlB5gzpJN40CEu8K+miXuOh7HCrVp+k733awQJBAMDkERhS/wXF7F40l3nkIz6wC8TWnEnPxFGDdItzNcF4vAhV+qN2WaYgq11sTHrdk01MkO4G+foCC5dmwq+SlSECQGm58ReqUTRDAKl32VX0vEdVvOj2getVxW6jQjJFiGj8iNDfK+DpiLfns3YjwSlWHGxHz1S6/lQ+58+M+fEyvUs=";
	        
			//String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyMDM4OTc5MjkxIiwiZXhwIjoxNTM5NDM4OTUzfQ.2nQIb3ESFQOLk3YAMPlRlSzwWRFk5UNsh0B0de_kG2U";
			//String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvMCkMUkh7AJqwUAecmgHZwQbiR4u7ZdOhuzoxZEhAZUjrBarfHvttwfKLFM1r2uXvuu2rrYKjpa1iUV2A4rLeHlPnT07IeelAFiUKbjOaqS1K1ByTjIFCz466B8bMRYIOA6Za5j4OcVaQvpgXWZicshHssLFCeYnj2f5XAYQFiS9It6lJ0gGJWT2YSD6WxMAV1JRCpLJE0rtV5egAqAp9UImsZDjE2mVHXCTjlQKsdi+8jRJatZFLwDqOU0RGlgmwcjdg6u511xWWaQX1G3IhSMRAjrY4FDxxYRKBGrkNBPAp34NodGWL1iEHD+GdR3wRvbIAnLNU0XDf2bEenMPzwIDAQAB";		
			//String commKey = String.valueOf(System.currentTimeMillis())+ String.valueOf(System.currentTimeMillis()) + String.valueOf(System.currentTimeMillis()).substring(0, 6);
			
			String params = "jwt=" + token + "&commKey=" + commKey;
		    
			//var jsencrypt = new JSEncrypt();
			//jsencrypt.setPublicKey(IO_DATA.publicKey);
			//IO_DATA.encryptedString = jsencrypt.encrypt(params);
			
			//String login = new String(RSACode.encryptByPublicKey(params,publicKey));
			//String loing_2 = (login);
			//RSACode rsa_result = new RSACode();
			String login = RSACode.encryptBASE64(RSACode.encryptByPublicKey(params,publicKey));
			
			//String login_2 = RSACode.encryptBASE64(RSACode.encryptByPublicKey("123456",publicKey));
			//System.out.print("\r\t login_2===="+login_2+"\r\t");        	
        	
            client = new WebSocketClient(new URI("wss://nodejs-test.games.1768.com:8463/primus?login="+login),new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                	 logger.info("打开连接");
                }
 
                @Override
                public void onMessage(String msg) {
                	 logger.info("\t\r 接受的数据=========="+msg+"\t\r");
                	 if(msg.equals("over")){
                		 client.close();
                	 }else if(msg.contains("primus::ping")){
                		 System.out.print("+++我是ping+++");                		 
                		 if(new Date().getTime()-pingTime.getTime() >= 1000) {
                			 //pingTime_s.onPond();
                			 pingTime = new Date();
                			 System.out.print("\t\r++++我还在====\t\r");
                			 client.send("\"primus::pong::"+String.valueOf(System.currentTimeMillis())+"\"");
                		 }
                		 
                	 }else {
                		 //webSocketClientExample.respons = msg;
                		 try {               			
							String decryptstr = AES.Decrypt(msg,commKey);
							System.out.print(decryptstr);
							
							JSONObject json = JSONObject.fromObject(decryptstr);
							if(json.get("cmd").toString().equals("conn::init"))
								jwtToken = json.get("res").toString();	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

                	 }
                	 
                }
 
                @Override
                public void onClose(int i, String s, boolean b) {
                	 logger.info("链接关闭");
                }
 
                @Override
                public void onError(Exception e){
                    e.printStackTrace();
                    logger.info("链接出错关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
 
        
        client.connect();
        logger.info(client.getDraft());
       while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
    	   logger.info("链接...");
        }
       //���ӳɹ�,������Ϣ
	
    	String a = "inRoom";
    	JSONObject p = JSONObject.fromObject("{\"roomId\":1}");   	
    	
    	//webSocketClientExample.formatParams(a,p)
    	String rq = "\""+AES.Encrypt(webSocketClientExample.formatParams(a,p).toString(), commKey)+"\"";
    	System.out.print("\tr\t加密后参数数据"+rq+"\t\r");
    	client.send(rq);
    	
       
    }
	
}
