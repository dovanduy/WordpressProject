package functions;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FirstTimestampExtraction 
{
	@SuppressWarnings("deprecation")
	public static String getFirstTimestamp(String url_tag)
	{
		String timestamp = "";
		
		HttpClient httpclient = HttpClientBuilder.create().build();
		
		HttpPost httppost = new HttpPost(url_tag);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		String responseBody = "";
		
		try{
			responseBody = httpclient.execute(httppost, responseHandler);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Document document = Jsoup.parse(responseBody);
		
		String rawtext = "";
		try {
			rawtext = document.body().getElementsByTag("script").get(0).data().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String [] arrayRawtext = rawtext.split("\n");
		
		String jsonText = "{";
		
		for(int i=1; i<arrayRawtext.length-1; i++)
		{
			jsonText += arrayRawtext[i];
		}
		
		jsonText += "}";
		
		JSONObject jsonObject = null;
		
		try {
			jsonObject = new JSONObject(jsonText);
			
			JSONArray jsonArray = (JSONArray) jsonObject.get("posts");
			
			JSONObject firstObject = (JSONObject) jsonArray.get(0);
			
			timestamp = firstObject.get("post_timestamp").toString();
			
			httpclient.getConnectionManager().shutdown();
			
			return timestamp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static void main(String [] args)
	{
		System.out.println(getFirstTimestamp("https://en.wordpress.com/tag/literature/"));
	}
}
