package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class GettingSource {
	
	HttpClient httpclient;
	public GettingSource()
	{
		// Override system DNS setting with Google free DNS server
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		
		httpclient = HttpClientBuilder.create().build();
	}
	
	public String getSource(String url) throws IOException
	{
		StringBuffer result = new StringBuffer();
		HttpResponse response = null;
		try
		{
			HttpPost httppost = new HttpPost(url);
			response = httpclient.execute(httppost);
			
			if(response.toString().contains("Method Not Allowed"))
			{
				System.out.println("Method Not Allowed");
				HttpGet httpget = new HttpGet();
				System.out.println("httpget: https --> http");
				httpget = new HttpGet(url.replace("https", "http"));
				response = httpclient.execute(httpget);
			}
		} catch (Exception e1)
		{
			System.out.println("e1: " + e1.toString());
			MyLog.saveLog(e1.toString());
			if(e1.toString().contains("ClientProtocolException")
					|| e1.toString().contains("UnknownHostException")
					|| e1.toString().contains("ClientProtocolException"))
			{
				return "break";
			}
			
			try{
				HttpGet httpget = new HttpGet();
				System.out.println("httpget: https --> http");
				httpget = new HttpGet(url.replace("https", "http"));
				response = httpclient.execute(httpget);
			} catch (Exception e2) {
				System.out.println("e2: " + e2.toString());
				MyLog.saveLog(e2.toString());

				if(url.startsWith("https"))
				{
					System.out.println("https --> http");
					try{
						HttpPost httppost = new HttpPost(url);
						httppost = new HttpPost(url.replace("https", "http"));
						response = httpclient.execute(httppost);
					} catch (Exception e3)
					{
						System.out.println("e3: " + e3.toString());
						MyLog.saveLog(e3.toString());						
					}
				} else {
					System.out.println("http --> https");
					try{
						HttpPost httppost = new HttpPost(url);
						httppost = new HttpPost(url.replace("https", "http"));
						response = httpclient.execute(httppost);
					} catch (Exception e4)
					{
						System.out.println("e4: " + e4.toString());
						MyLog.saveLog(e4.toString());
					}
				}
			}
		}
		
		if(response!=null)
		{
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
		}
		
		return result.toString();
	}
	
	public static void main(String [] args) throws IOException
	{
		GettingSource object = new GettingSource();
		String url = "https://matfifenbird.wordpress.com/";
		if(args.length>0)
		{
			url = args[0];
		}
		String source = object.getSource(url);
		System.out.println(source);
		
		CheckingFranceUser object2 = new CheckingFranceUser();
		System.out.println(object2.isFrancer(source));
	}
}
