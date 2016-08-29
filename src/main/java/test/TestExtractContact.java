package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import functions.ContactExtraction;

public class TestExtractContact 
{
	@SuppressWarnings({"resource" })
	public static void main(String [] args) throws IOException
	{
		ContactExtraction contactExtraction = new ContactExtraction();
		HttpClient httpclient = HttpClientBuilder.create().build();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/user.txt")));
		String line;
		while((line=br.readLine())!=null)
		{
			System.out.println(line);
			
			String url = line + "/about/";
			
			try {
				HttpPost httppost = new HttpPost(url);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				String responseBody = httpclient.execute(httppost, responseHandler);
				
				ArrayList<String> listContacts = contactExtraction.extractContactFromSource(responseBody);
				
				if(listContacts.size()>0)
				{
					System.out.println(listContacts);
				}
			} catch (Exception e)
			{
				// do nothing
			}
			
			System.out.println();
		}
	}
}
