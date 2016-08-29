package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import functions.ContactExtraction;

public class TestHttp2 {
	public static void main(String [] args) throws ClientProtocolException, IOException
	{
		// https://3spoonsofbutter.wordpress.com
		// https://extraordinarydaysblog.com 
		// https://alittleyellowhouse.com
		
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		
		String url = "https://wordpress.com/tags";

		HttpClient client = HttpClientBuilder.create().build();
		
		System.out.println("start");
		HttpPost post = new HttpPost(url);
		// HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(post);
		System.out.println("Response Code : "
		                + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		System.out.println(result);
	}
}
