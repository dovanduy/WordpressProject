package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import functions.CheckingFranceUser;
import functions.GettingSource;

// https://en.wordpress.com/wp-admin/admin-ajax.php?action=get_tag_posts&per_page=7&before=1471300358&after=&slug=&blog_id=&feed_id=&list_author=&list_slug=&location=&location_name=&google_places_reference=&offset=&filter=&post_status=&post_type=&voyeur=	
// https://en.wordpress.com/tag/literature/

public class TestHttp 
{
	public static void main(String[] args) throws Exception 
	{
		String url = "https://knitequalsjoy.com/";
		
		if(args.length>0)
		{
			url = args[0];
		}
		
		// Override system DNS setting with Google free DNS server
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		
		try{
		    HttpClient client = HttpClientBuilder.create().build();
		    
		    HttpPost httppost = new HttpPost(url);
		    
		    HttpResponse response = client.execute(httppost);
			System.out.println("Response Code : "
			                + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				System.out.println(line);
			}
			
			// String source = result.toString();
			GettingSource gettingSource = new GettingSource();
			String source = gettingSource.getSource(url);
			
			// CheckingFranceUser checkingFranceUser = new CheckingFranceUser();
			// System.out.println(checkingFranceUser.isFrancer(source));
		    
//		    ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		    String response = client.execute(httppost, responseHandler);
		} catch (Exception e) {
			// System.out.println("Cant get data.");
			e.printStackTrace();
		}
		
		System.out.println("Tam Tong Toc");
	}
}
