package wordpress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import data.Users;
import functions.CheckingFranceUser;
import functions.FirstTimestampExtraction;
import functions.GettingSource;
import functions.MyLog;
import functions.RandomSequence;
import functions.TagsExtraction;
import parameter.Parameter;

public class FranceUserExtractionSomeTags 
{
	static GettingSource gettingsource = new GettingSource();	
	static CheckingFranceUser checkingFranceUser = new CheckingFranceUser();
	static HashMap<String, Integer> mapUsers = new HashMap<String, Integer>();
	
	@SuppressWarnings("deprecation")
	public static void doAllTags() throws IOException
	{
		// Override system DNS setting with Google free DNS server
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		
		File f = new File(Parameter.file_new_user);
		if(f.exists() && !f.isDirectory()) 
		{ 
			mapUsers = Users.getMapUser();
		} else {
			new File("data").mkdir();
			mapUsers = new HashMap<>();
		}
		
		HashMap<String, String> mapTags = getTagsFromFile();
		
		String [] arrayUrls = new String[mapTags.size()];
		String [] arraySlugs = new String[mapTags.size()];
		
		int index = 0;
		for(String urlTag : mapTags.keySet())
		{
			arrayUrls[index] = urlTag;
			arraySlugs[index] = mapTags.get(urlTag);
			index++;
		}
		
		ArrayList<Integer> listRandomSequences = RandomSequence.getRandomSequences(mapTags.size());
		
		for(int k=0; k<listRandomSequences.size(); k++)
		{
			int indexTag = listRandomSequences.get(k);
			String urlTag = arrayUrls[indexTag];
			String slug = arraySlugs[indexTag];
			
			HashMap<String, Integer> mapUsers_perTag_france = new HashMap<String, Integer>();
			HashMap<String, Integer> mapUsers_perTag = new HashMap<String, Integer>();
			
			int number_post = 0;
			
			System.out.println("=== " + slug + " ===");
			
			System.out.println(urlTag);
			
			String before = FirstTimestampExtraction.getFirstTimestamp(urlTag);
			
			if(before.length()==0)
			{
				continue;
			}
			
			HttpClient httpclient;
			try{
				 httpclient = HttpClientBuilder.create().build();
			} catch (Exception e)
			{
				System.out.println("False HttpClientBuilder.create().build();");
				continue;
			}
			
			try 
			{
				while (true) 
				{
					String url = "https://en.wordpress.com/wp-admin/admin-ajax.php?action=get_tag_posts&per_page=7&before="
							+ before
							+ "&after=&slug="
							+ slug
							+ "&blog_id=&feed_id=&list_author=&list_slug=&location=&location_name=&google_places_reference=&offset=&filter=&post_status=&post_type=&voyeur=";

					HttpPost httppost = new HttpPost(url);

					ResponseHandler<String> responseHandler = new BasicResponseHandler();

					String responseBody = "";
					
					try{
						responseBody = httpclient.execute(httppost, responseHandler);
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					// System.out.println(url);
					// System.out.println(responseBody);
					JSONObject page = new JSONObject(responseBody);

					JSONArray posts = page.getJSONArray("posts");

					for (int i = 0; i < posts.length(); i++) 
					{
						JSONObject post = posts.getJSONObject(i);
						
						String blog_url = post.get("blog_url").toString().replace("http:", "https:");
						before = post.getLong("post_timestamp") + "";
						if(!mapUsers.containsKey(blog_url))
						{
							mapUsers.put(blog_url, 0);
							mapUsers_perTag.put(blog_url, 0);
							String source = gettingsource.getSource(blog_url);
							if(checkingFranceUser.isFrancer(source))
							{
								mapUsers_perTag_france.put(blog_url, 0);
							}
						}
						
						System.out.println(blog_url);
						System.out.println("Number post per tag: " + (++number_post));
						System.out.println("Number user per tag: " + mapUsers_perTag.size());
						System.out.println("Number user france per tag: " + mapUsers_perTag_france.size());
						System.out.println("Number user all tag: " +  + mapUsers.size());
						System.out.println();
					}
					
					if(number_post > Parameter.limit_post)
					{
						System.out.println("Break: limit post " + Parameter.limit_post);
						break;
					}
					
					if (posts.length() < 7) 
					{
						System.out.println("Break: end of post");
						break;
					}
				}
			}  catch (Exception e) {
				e.printStackTrace();
				MyLog.saveLog(e.toString());
			}
			
			Users.saveMapUser(mapUsers_perTag);
			Users.saveMapUserFrance(mapUsers_perTag_france);
			System.out.println("Save mapUsers done!");
			System.out.println();
			try{
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e)
			{
				System.out.println("False httpclient.getConnectionManager().shutdown();");
				MyLog.saveLog(e.toString());
			}
		}
	}
	
	public static HashMap<String, String> getTagsFromFile() throws IOException
	{
		HashMap<String, String> mapTags = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Parameter.file_tags)));
		String line;
		while((line = br.readLine())!=null)
		{
			if(!line.endsWith("/"))
			{
				line = line + "/";
			}
			
			String tag = line.replace(Parameter.wordpress_prefix_tag_france, "");
			tag = tag.replace("/", "");
			
			mapTags.put(line, tag);
		}
			
		br.close();
		return mapTags;
	}
	
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Classifying france users ...");
		
		if(args.length>0)
		{
			Parameter.limit_post = Integer.parseInt(args[0]);
		}
		
		while(true)
		{
			doAllTags();
		}
	}
}
