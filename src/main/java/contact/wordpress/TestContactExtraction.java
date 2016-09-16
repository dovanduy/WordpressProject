package contact.wordpress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import functions.ContactExtraction;
import parameter.Parameter;

public class TestContactExtraction 
{
	static ContactExtraction contactExtraction = new ContactExtraction();

	public static void init() throws IOException {
		System.out.println("Start init ...");
		File folder = new File(Parameter.folder_data);
		if (!(folder.exists() && folder.isDirectory())) {
			new File(Parameter.folder_data).mkdir();
		}
		System.out.println("Done init!");
	}

	public static void doAllBlogs() throws ClientProtocolException, IOException 
	{
		String file_blogs = "test/blogs.txt";
		String file_contact = "test/contacts.txt";
		String file_not_contact = "test/cannot_contacts.txt";
		HashMap<String, Integer> mapNewPersonalNotContact = new HashMap<>();
		HashMap<String, String> mapNewPersonalContact = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_blogs)));
		String blog_url;
		int count = 0;
		while((blog_url=br.readLine())!=null)
		{
			System.out.println(++count);
			System.out.println(blog_url);
			ArrayList<String> listContact = contactExtraction.extractContactFromUrl(blog_url);
			if (listContact.size() > 0) 
			{
				if (listContact.size() == 1 && listContact.contains(Parameter.label_personal_feature)) 
				{
					mapNewPersonalNotContact.put(blog_url, 0);
					System.out.println("cannot_contact");
				} else {
					if (listContact.contains(Parameter.label_personal_feature)) {
						listContact.remove(Parameter.label_personal_feature);
						System.out.println("contact");
					}
					mapNewPersonalContact.put(blog_url, listContact.toString());
				}
			}
		}
		br.close();
		
		// save contact
		{
			FileWriter fw = new FileWriter(file_contact);
			for(String key : mapNewPersonalContact.keySet())
			{
				fw.write(key + "\n");
				fw.write(mapNewPersonalContact.get(key) + "\n");
				fw.write("\n");
			}
			fw.close();
		}
		
		// save not contact
		{
			{
				FileWriter fw = new FileWriter(file_not_contact);
				for(String key : mapNewPersonalNotContact.keySet())
				{
					fw.write(key + "\n");
				}
				fw.close();
			}
		}
		
		System.out.println("Save contact done!");
	}

	public static void main(String[] args) throws Exception {
		doAllBlogs();
	}
}
