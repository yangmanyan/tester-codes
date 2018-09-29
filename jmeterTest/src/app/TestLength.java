package app;

//import org.apache.jmeter.*;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.*;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class TestLength extends AbstractJavaSamplerClient {

	private SampleResult results;
    private String testStr;
//��ʼ��������ʵ������ʱÿ���߳̽�ִ��һ�Σ��ڲ��Է�������ǰִ�У�������LoadRunner�е�init����
    public void setupTest(JavaSamplerContext arg0) {
         results = new SampleResult();
         testStr = arg0.getParameter("testString", "");
         if (testStr != null && testStr.length() > 0) {
                results.setSamplerData(testStr);
         }
    }
    
  //���ô���Ĳ������������ö���������õĲ�������ʾ��Jmeter�Ĳ����б���
    public Arguments getDefaultParameters() {
            Arguments params = new Arguments();
            params.addArgument("testStr", "");   //����һ����������ʾ��Jmeter�Ĳ����б��У���һ������Ϊ����Ĭ�ϵ���ʾ���ƣ��ڶ�������ΪĬ��ֵ
           return params;
    }
    
    
	
    //����ִ�е�ѭ���壬�����߳�����ѭ�������Ĳ�ͬ��ִ�ж�Σ�������LoadRunner�е�Action����
    public SampleResult runTest(JavaSamplerContext arg0) {
         int len = 0;
         results.sampleStart();     //����һ�����񣬱�ʾ�����������ʼ�㣬������LoadRunner��lr.start_transaction
         len = testStr.length();
         results.sampleEnd();     //����һ�����񣬱�ʾ��������Ľ����㣬������LoadRunner��lr.end_transaction
         if(len < 5){
                 System.out.println(testStr);
                 results.setSuccessful(false);   //�����������н���ĳɹ���ʧ�ܣ������"false"���ʾ���ʧ�ܣ��������ʾ�ɹ�
        }else   
                results.setSuccessful(true);
       return results;
    }
    
    //����������ʵ������ʱÿ���߳̽�ִ��һ�Σ��ڲ��Է������н�����ִ�У�������LoadRunner�е�end����
    public void teardownTest(JavaSamplerContext arg0) {
    	
    }
	
}
