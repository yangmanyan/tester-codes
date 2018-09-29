package com.qa.utils;

import org.java_websocket.WebSocketImpl;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import com.qa.utils.Base64Util;
import com.qa.utils.RSACode;

import org.json.simple.JSONObject;

/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
public class ExampleClient extends WebSocketClient {

	public ExampleClient( URI serverUri , Draft draft ) {
		super( serverUri, draft );
	}

	public ExampleClient( URI serverURI ) {
		super( serverURI );
	}

	public ExampleClient( URI serverUri, Map<String, String> httpHeaders ) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		
		//String sendMsg = {"cmd":"fuList","params":{"jwt":null,"token":null},"status":{"time":1537264556216}} ;
		
		 JSONObject obj = new JSONObject();

	      obj.put("cmd","fuList");
	      obj.put("num",new Integer(100));
	      obj.put("balance",new Double(1000.21));
	      obj.put("is_vip",new Boolean(true));

	      StringWriter out = new StringWriter();
	      //obj.writeJSONString(out);
	      
	      String jsonText = out.toString();
	      System.out.print(jsonText);
		
		send("Hello, it is me. Mario :)");
		System.out.println( "opened connection" );
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage( String message ) {
		System.out.println( "received: " + message );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public static void main( String[] args ) throws Exception,URISyntaxException, InterruptedException {
		//String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwjDm1HXDw8QH5ZtGMQIl2h/I8E+chOQA8aQ8xCR/+aHnROaN/ZU5Vmd2Zz7g6cAacR9BSm60+iSCYtvEGJKl0WqvbPGJkc8tedjNF1QqgWqkkuE6Udgw2OkEKJCxDg6PrAniR7Cc0io9G8bW4P8JDJjSbbafvMPDDFbVVUWJxxwIDAQAB";
				String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALCMObUdcPDxAflm0YxAiXaH8jwT5yE5ADxpDzEJH/5oedE5o39lTlWZ3ZnPuDpwBpxH0FKbrT6JIJi28QYkqXRaq9s8YmRzy152M0XVCqBaqSS4TpR2DDY6QQokLEODo+sCeJHsJzSKj0bxtbg/wkMmNJttp+8w8MMVtVVRYnHHAgMBAAECgYAOLuW/8CKPqL0A3Uq+WrzwYdGLFApAeATV1Zbb2KDSXnBS56+D346gf+D2p2Jkh3VwfrB0wn7zhC6zNhc86BsY1K6Q7xU8b7asDBqki48H3ExuxjEosEqUdLf9p9pPBCPI3I4CN/EZPEoFjNRNi5ZX/CY4iaOgsXPHEkcxuW3GQQJBAOBo9UpXesZYCsmuuGN5SOy6tXI613NUBvXKXkxBil3ZOqozlaSjv5NSQb2zLeh2DcYfecumfgn04rNM/pLeDmECQQDJZnXWg4VrXdjc9hqu/8rkmxdhsr3ERkX1uKJrDUB+AOdFs6mS9gEHnJ70zqQ2ucbhQ4htulbLc9pBVL5gy+EnAkEArdhhfa/7SsBVyxvxeA4zMkEJ4242Df/gTHTzTDvRxxZL3iKMILlB5gzpJN40CEu8K+miXuOh7HCrVp+k733awQJBAMDkERhS/wXF7F40l3nkIz6wC8TWnEnPxFGDdItzNcF4vAhV+qN2WaYgq11sTHrdk01MkO4G+foCC5dmwq+SlSECQGm58ReqUTRDAKl32VX0vEdVvOj2getVxW6jQjJFiGj8iNDfK+DpiLfns3YjwSlWHGxHz1S6/lQ+58+M+fEyvUs=";
		        
				String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyMDM4OTc5MjkxIiwiZXhwIjoxNTM5NDM4OTUzfQ.2nQIb3ESFQOLk3YAMPlRlSzwWRFk5UNsh0B0de_kG2U";
				String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvMCkMUkh7AJqwUAecmgHZwQbiR4u7ZdOhuzoxZEhAZUjrBarfHvttwfKLFM1r2uXvuu2rrYKjpa1iUV2A4rLeHlPnT07IeelAFiUKbjOaqS1K1ByTjIFCz466B8bMRYIOA6Za5j4OcVaQvpgXWZicshHssLFCeYnj2f5XAYQFiS9It6lJ0gGJWT2YSD6WxMAV1JRCpLJE0rtV5egAqAp9UImsZDjE2mVHXCTjlQKsdi+8jRJatZFLwDqOU0RGlgmwcjdg6u511xWWaQX1G3IhSMRAjrY4FDxxYRKBGrkNBPAp34NodGWL1iEHD+GdR3wRvbIAnLNU0XDf2bEenMPzwIDAQAB";		
				String commKey = String.valueOf(System.currentTimeMillis())+ String.valueOf(System.currentTimeMillis()) + String.valueOf(System.currentTimeMillis()).substring(0, 6);
				
				String params = "jwt=" + token + "&commKey=" + commKey;
				//var jsencrypt = new JSEncrypt();
				//jsencrypt.setPublicKey(IO_DATA.publicKey);
				//IO_DATA.encryptedString = jsencrypt.encrypt(params);
				
				//String login = new String(RSACode.encryptByPublicKey(params,publicKey));
				//String loing_2 = (login);
				//RSACode rsa_result = new RSACode();
				String login = RSACode.encryptBASE64(RSACode.encryptByPublicKey(params,publicKey));
				
		ExampleClient c = new ExampleClient( new URI( "wss://nodejs-test.games.1768.com:8463/primus?login=" + login)); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts		
		c.connect();
		
		
		
		//c.close();
		//c.connectBlocking();
		//c.send( "hi" );
		//Thread.sleep( 100 );
		//c.closeBlocking();
		//c.send();
		
		
	}

}
