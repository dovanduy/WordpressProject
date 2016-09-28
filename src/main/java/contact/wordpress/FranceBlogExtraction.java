package contact.wordpress;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import data.BlogsIO;
import functions.CheckingFranceUser;
import functions.CheckingPersonalBlog;
import functions.ContactExtraction;
import functions.FirstTimestampExtraction;
import functions.GettingSource;
import functions.MyLog;
import functions.RandomSequence;
import functions.TagsExtraction;
import functions.Utils;
import parameter.Parameter;

public class FranceBlogExtraction 
{
	static HashMap<String, Integer> mapCheckedBlogs = new HashMap<String, Integer>();
	static ContactExtraction contactExtraction = new ContactExtraction();
	
	public static void init() throws IOException {
		System.out.println("Start init ...");
		
		GettingSource.init();
		ContactExtraction.init();
		CheckingFranceUser.init();
		
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
		
		System.out.println("Done init!");
	}
	
	@SuppressWarnings("deprecation")
	public static void doAllTags() throws IOException
	{
		HashMap<String, String> mapTags = TagsExtraction.getListTagsFrance();
		
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
			HashMap<String, String> mapContactBlogsPertag = new HashMap<String, String>();
			ArrayList<String> rows = new ArrayList<>();
			
			int number_post = 1;
			
			System.out.println("=== " + slug + " ===");
			
			System.out.println(urlTag);
			
			String before = FirstTimestampExtraction.getFirstTimestamp(urlTag);
			
			if(before.length()==0)
			{
				continue;
			}
			
			HttpClient httpclient = HttpClientBuilder.create().build();
			
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

					JSONObject page = new JSONObject(responseBody);
					JSONArray posts = page.getJSONArray("posts");

					for (int i = 0; i < posts.length(); i++) 
					{
						JSONObject post = posts.getJSONObject(i);
						String blog_url = post.getString("blog_url").replace("http:", "https:");
						JSONObject post_author = post.getJSONObject("post_author");
						String author_name = post_author.getString("name");
						
						before = post.getLong("post_timestamp") + "";
						if(!mapCheckedBlogs.containsKey(blog_url))
						{
							mapCheckedBlogs.put(blog_url, 0);
							mapBlogsPerTag.put(blog_url, 0);
							
							String source = GettingSource.getSource(blog_url);
							if(CheckingFranceUser.isFrancer(source))
							{
								if(CheckingPersonalBlog.isPersonalBlog(source))
								{
									ArrayList<String> listContact = ContactExtraction.extractContactFromUrlAndSource(blog_url, source);
									if(listContact.size()>0)
									{
										mapContactBlogsPertag.put(blog_url, listContact.toString());
										rows.add(Utils.generateRow(blog_url, author_name, slug, listContact.toString()));
									}
								}
							}
						}
						
						System.out.println("Number posts per tag: " + (number_post++));
						System.out.println("Number blogs per tag: " + mapBlogsPerTag.size());
						System.out.println("Number contacts France per tag: " + mapContactBlogsPertag.size());
						System.out.println("Number all checked blogs: " +  + mapCheckedBlogs.size());
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
				MyLog.saveLog(e.toString());
			}
			
			BlogsIO.saveMapBlogs(mapBlogsPerTag, Parameter.file_checked_blog);
			BlogsIO.saveContactToExcelFile(rows, Parameter.file_contact_blog);
			System.out.println("Save contact done!");
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
	
	public static void main(String[] args) throws Exception 
	{
		init();
		
		// Override system DNS setting with Google free DNS server
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
				
		System.out.println("Get France contacts ...");
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
