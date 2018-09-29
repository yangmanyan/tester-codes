package com.testng.test.maven_testng;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.testng.TestNG;

public class some_ep_01 {

public static void main(String[] args) throws IOException {

/*����˵��
*zipFileDir �û��ϴ���zip��Ŀ¼
*projectDir �������տ�ִ�нű�������Ŀ¼
*packageContent ��Ҫ�ύ���û��ϴ��ű����������package����
*/
String zipFileDir="D:\\Android\\jars";
String projectDir="D:\\Android\\workspace\\MqcDemo\\src\\demo0325\\";
String packageContent="package demo0325;";
String projectSrcDir="demo0325";

//�ڹ���Ŀ¼demo0325���������û���ִ�еĽű�
try{
addScript.main(zipFileDir,projectDir,packageContent);
System.out.println("�����û��ű�������");
}catch(Exception e){
System.out.println("��ִ�нű�����ʧ��");
}

//��ȡ���������ɵ��û���ִ�нű�����
String[] scriptNames=addScript.getUserScriptNames();
//��ȡxml�ļ�����Ҫִ�е�class��name����ֵ
String[] xmlClassSZ=new String[scriptNames.length];
for(int i=0;i<scriptNames.length;i++){
String scriptName=scriptNames[i].toString().substring(0,scriptNames[i].length()-5);
xmlClassSZ[i]=projectSrcDir+"."+scriptName;
System.out.println(scriptName);
}

//����testng.xml�ļ�
CreateTestngXml(xmlClassSZ);

//��������testng.xml�ļ�
RunTestngXml();


}


//����testng.xml�ļ�
public static void CreateTestngXml(String[] xmlClass) throws IOException{
//����Documentʵ��  
        Document document = DocumentHelper.createDocument();  
        //��¼Ҫ�����xml�ļ�λ��  
        String xmlfilepath=System.getProperty("user.dir")+"\\testng.xml"; 
        //�������ڵ�suite��������name����Ϊxmlsuitename  
        Element root = document.addElement( "suite" ).addAttribute("name", "exeAppiumTestCase");  
        //�����ڵ�test��������name������  
        Element test = root.addElement( "test" ).addAttribute("name", "test"); 
        //�����ڵ�classes��������  
        Element classes = test.addElement( "classes" );         
        //�����ڵ�classs��������name����  
        for(int i=0;i<xmlClass.length;i++){
            Element classs= classes.addElement( "class" ).addAttribute("name",xmlClass[i].toString()); 
        }
                  
        //����DocType  
        //��һ������������    
        //�ڶ���������PUBLIC URI  
        //������������SYSTEM URI  
        document.addDocType("suite", null,"http://testng.org/testng-1.0.dtd");  
        //�����ʽ����  
        OutputFormat format = OutputFormat.createPrettyPrint();   
        format = OutputFormat.createCompactFormat();  
        //�����������  
        format.setEncoding("UTF-8");  
        //����XML�ļ�  
        XMLWriter writer= new XMLWriter(new OutputStreamWriter(new FileOutputStream(xmlfilepath),format.getEncoding()),format);   
        writer.write( document );   
        writer.close();  
        document=null;     
}


//����testng.xml�ļ�
public static void RunTestngXml(){
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<String>();
        suites.add(System.getProperty("user.dir")+"\\testng.xml");
        testNG.setTestSuites(suites);
        testNG.run();
}
}