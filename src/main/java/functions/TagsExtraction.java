package functions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import parameter.Parameter;

@SuppressWarnings("deprecation")
public class TagsExtraction {
	public static HashMap<String, String> getListTagsEnglish() throws ClientProtocolException, IOException {
		System.out.println("Getting all tags in https://wordpress.com/tags/...");

		HashMap<String, String> mapTags = new HashMap<String, String>();

		HttpClient httpclient = HttpClientBuilder.create().build();

		try {
			String url = Parameter.wordpress_tag_english;

			HttpPost httppost = new HttpPost(url);
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			String responseBody = httpclient.execute(httppost, responseHandler);

			Document document = Jsoup.parse(responseBody);

			for (Element element : document.body().getElementsByTag("a")) 
			{
				String urlTag = element.attr("href").replace("http:", "https:");

				if (urlTag.contains(Parameter.wordpress_prefix_tag_english)) 
				{
					String tag = urlTag.replace(Parameter.wordpress_prefix_tag_english, "");
					tag = tag.replace("/", "");
					mapTags.put(urlTag, tag);
				}
			}

		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		System.out.println("Done get tags!");
		return mapTags;
	}
	
	public static HashMap<String, String> getListTagsFrance() throws ClientProtocolException, IOException {
		System.out.println("Getting all tags in " + Parameter.wordpress_tag_france + " ...");

		HashMap<String, String> mapTags = new HashMap<String, String>();

		HttpClient httpclient = HttpClientBuilder.create().build();

		try {
			String url = Parameter.wordpress_tag_france;

			HttpPost httppost = new HttpPost(url);
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responseBody = "";
			
			try{
				responseBody = httpclient.execute(httppost, responseHandler);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			Document document = Jsoup.parse(responseBody);

			for (Element element : document.body().getElementsByTag("a")) 
			{
				String urlTag = element.attr("href").replace("http:", "https:");

				if (urlTag.contains(Parameter.wordpress_prefix_tag_france)) 
				{
					String tag = urlTag.replace(Parameter.wordpress_prefix_tag_france, "");
					tag = tag.replace("/", "");
					mapTags.put(urlTag, tag);
				}
			}

		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		System.out.println("Done get tags!");
		return mapTags;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		System.out.println(getListTagsFrance());
	}
}