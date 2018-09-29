package findyou.Interface;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnection {	
	public static HttpURLConnection getConnection(String url){
		HttpURLConnection connection = null;
		try {
			// 打开和URL之间的连接
			URL postUrl = new URL(url);
			connection = (HttpURLConnection) postUrl.openConnection();
			
			//URL url = new URL("http://localhost:8080/TestHttpURLConnectionPro/index.jsp");     
			 // URLConnection rulConnection = url.openConnection(); 
			// 此处的urlConnection对象,实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection,故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.如下:
			//HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection; 
			
			 // 设置通用的请求属性
			connection.setDoOutput(true);
			//设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
			connection.setDoInput(true);
			//设置是否从httpUrlConnection读入，默认情况下是true; 
			connection.setRequestMethod("GET");
			// 设定请求的方法为"POST"，默认是GET 
			connection.setUseCaches(false);
			//Post 请求不能使用缓存 所以是false
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Charset", "utf-8");
			connection.setRequestProperty("Accept-Charset", "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return connection;
	}
}
