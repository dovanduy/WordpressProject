package contact.wordpress;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import data.BlogsIO;
import functions.ContactExtraction;
import functions.FirstTimestampExtraction;
import functions.MyLog;
import functions.RandomSequence;
import functions.TagsExtraction;
import parameter.Parameter;

@SuppressWarnings("deprecation")
public class EnglishBlogExtraction 
{
	static HashMap<String, Integer> mapCheckedBlogs = new HashMap<String, Integer>();
	static HashMap<String, Integer> mapOriginPersonalBlogs = new HashMap<String, Integer>();
	static HashMap<String, Integer> mapAllBlogs = new HashMap<String, Integer>();
	static ContactExtraction contactExtraction = new ContactExtraction();
	
	public static void init() throws IOException {
		System.out.println("Start init ...");
		
		File folder = new File(Parameter.folder_data);
		if(!(folder.exists() && folder.isDirectory()))
		{
			new File(Parameter.folder_data).mkdir();
		}
		
		File file_checked = new File(Parameter.file_checked_blog);
		if(file_checked.exists() && !file_checked.isDirectory()) 
		{ 
			mapCheckedBlogs = BlogsIO.getMapCheckedBlog();
		}
		
		File file_personal = new File(Parameter.file_personal_blog_getted_contact);
		if(file_personal.exists() && !file_personal.isDirectory()) 
		{ 
			mapOriginPersonalBlogs = BlogsIO.getMapPersonalBlog();					
		}
		
		for(String blog : mapCheckedBlogs.keySet())
		{
			mapAllBlogs.put(blog, 0);
		}
		for(String blog : mapOriginPersonalBlogs.keySet())
		{
			mapAllBlogs.put(blog, 0);
		}
		
		System.out.println("Done init!");
	}
	
	public static void doAllTags() throws ClientProtocolException, IOException
	{
		HashMap<String, String> mapTags = TagsExtraction.getListTagsEnglish();
		
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
			
			HashMap<String, Integer> mapBlogsPerTag = new HashMap<String, Integer>();
			HashMap<String, String> mapNewPersonalContactPertag = new HashMap<String, String>();
			HashMap<String, Integer> mapNewPersonalNotContactPertag = new HashMap<String, Integer>();
			
			int number_post = 1;
			
			System.out.println("=== " + slug + " ===");
			
			System.out.println(urlTag);
			
			String before = FirstTimestampExtraction.getFirstTimestamp(urlTag);
			
			if(before.length()==0)
			{
				continue;
			}
			
			HttpClient httpclient = HttpClientBuilder.create().build();
			
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

					JSONObject page = new JSONObject(responseBody);
					JSONArray posts = page.getJSONArray("posts");
					for (int i = 0; i < posts.length(); i++) 
					{
						JSONObject post = posts.getJSONObject(i);
						String blog_url = post.get("blog_url").toString().replace("http:", "https:");
						before = post.getLong("post_timestamp") + "";
						if(!mapAllBlogs.containsKey(blog_url))
						{
							mapAllBlogs.put(blog_url, 0);
							mapBlogsPerTag.put(blog_url, 0);
							ArrayList<String> listContact = contactExtraction.extractContactFromUrl(blog_url);
							if(listContact.size()>0)
							{
								if(listContact.size()==1 && listContact.contains(Parameter.label_personal_feature))
								{
									mapNewPersonalNotContactPertag.put(blog_url, 0);
								} else {
									if (listContact.contains(Parameter.label_personal_feature))
									{
										listContact.remove(Parameter.label_personal_feature);
									}
									mapNewPersonalContactPertag.put(blog_url, listContact.toString());
								}
							}
						}
						
						System.out.println("Number posts per tag: " + (number_post++));
						System.out.println("Number blogs per tag: " + mapBlogsPerTag.size());
						System.out.println("Number personal-contact per tag: " + mapNewPersonalContactPertag.size());
						System.out.println("Number blogs all tags: " +  + mapAllBlogs.size());
						System.out.println("Blog: " + blog_url);
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
			}
			
			BlogsIO.saveMapBlogs(mapBlogsPerTag, Parameter.file_checked_blog);
			BlogsIO.saveMapPersonalContact(mapNewPersonalContactPertag, Parameter.file_personal_blog_getted_contact);
			BlogsIO.saveMapBlogs(mapNewPersonalNotContactPertag, Parameter.file_personal_blog_not_get_contact);
			System.out.println("Save personal contact blogs done!");
			System.out.println();
			mapNewPersonalContactPertag = new HashMap<>();
			try{
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e)
			{
				System.out.println("False httpclient.getConnectionManager().shutdown();");
				MyLog.saveLog(e.toString());
			}
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		// Override system DNS setting with Google free DNS server
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		
		init();
		
		System.out.println("Extracting contacts of personal blogs in English ...");
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
