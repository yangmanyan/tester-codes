package com.qa.utils;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
 
public class socketReqGen{   
      public static void main(String[] arges){   
          try{     
        	  Scanner scan = new Scanner(System.in);
              Socket socket = new Socket ("101.89.64.17", 5019);    
              BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream()));     
              PrintWriter out = new PrintWriter (   
              new BufferedWriter( new OutputStreamWriter( socket.getOutputStream ())), true);   
              String str = scan.nextLine();
              while(!str.equals("quit")){
                  out.println(str);     
                  System.out.println(in.readLine());
                  str = scan.nextLine();
              }
              out.println(str);     
              System.out.println(in.readLine());
              scan.close();
              socket.close();
          }catch(Exception e){   
              System.out.println(e);   
          }   
       }   
}   