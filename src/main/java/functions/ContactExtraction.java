package functions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parameter.Parameter;

public class ContactExtraction {
	
	private String pattern_email = "[a-zA-Z0-9.\\_]{1,50}@[a-zA-Z.]{2,30}";
	private String pattern_facebook = "http[s]?://[w]?[w]?[w]?[.]?facebook.com/[a-zA-Z0-9.\\_]{1,50}";
	private String pattern_instagram = "http[s]?://[w]?[w]?[w]?[.]?instagram.com/[a-zA-Z0-9.\\_\\/]{1,50}";
	private String patttern_twitter = "http[s]?://[w]?[w]?[w]?[.]?twitter.com/[a-zA-Z0-9.\\_\\/]{1,50}";;
	private String patttern_name = "my name[ ]?:[ ]?[\\S]{1,20}|my name is [\\S]{1,20}|my name's [\\S]{1,20}|i am |i'm |i&#8217;m | my | me |i have|i think";
	
	private GettingSource mySource;
	private AboutLinkExtraction myAboutLink;
	private HashSet<String> removeLink;
	
	public ContactExtraction()
	{
		mySource = new GettingSource();
		myAboutLink = new AboutLinkExtraction();
		removeLink = new HashSet<>();
		removeLink.add("https://www.facebook.com/pages");
		removeLink.add("https://twitter.com/share");
		removeLink.add("https://twitter.com/wordpressdotcom");
		removeLink.add("https://www.facebook.com/wikipedia");
		
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(Parameter.file_not_contact)));
			String line;
			while((line=br.readLine())!=null)
			{
				removeLink.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(removeLink);
	}
	
	public ArrayList<String> extractContactFromSource(String source)
	{
		HashSet<String> setContacts = new HashSet<>();
		setContacts.add("https://www.facebook.com/WordPresscom");
		setContacts.add("http://www.facebook.com/WordPresscom");
		ArrayList<String> listContacts = new ArrayList<String>();
		
		// Email
		{
			Pattern pattern = Pattern.compile(pattern_email);
			Matcher matcher = pattern.matcher(source);
			if (matcher.find()) {
			    do 
			    {
			    	String candidate = matcher.group();
			    	if(!setContacts.contains(candidate) && !removeLink.contains(candidate))
			    	{
			    		setContacts.add(candidate);
			    		listContacts.add(candidate);
			    	}
			    } while (matcher.find());
			}
		}
		
		// Instagram
		{
			Pattern pattern = Pattern.compile(pattern_instagram);
			Matcher matcher = pattern.matcher(source);
			if (matcher.find()) {
			    do 
			    {
			    	String candidate = matcher.group();
			    	if(!setContacts.contains(candidate) && !removeLink.contains(candidate))
			    	{
			    		setContacts.add(candidate);
			    		listContacts.add(candidate);
			    	}
			    } while (matcher.find());
			}
		}
		
		// Facebook
		{
			Pattern pattern = Pattern.compile(pattern_facebook);
			Matcher matcher = pattern.matcher(source);
			if (matcher.find()) {
			    do 
			    {
			    	String candidate = matcher.group();
			    	if(!setContacts.contains(candidate) && !removeLink.contains(candidate))
			    	{
			    		setContacts.add(candidate);
			    		listContacts.add(candidate);
			    	}
			    } while (matcher.find());
			}
		}
		
		// twitter
		{
			Pattern pattern = Pattern.compile(patttern_twitter);
			Matcher matcher = pattern.matcher(source);
			if (matcher.find()) {
			    do 
			    {
			    	String candidate = matcher.group();
			    	if(!setContacts.contains(candidate) && !removeLink.contains(candidate))
			    	{
			    		setContacts.add(candidate);
			    		listContacts.add(candidate);
			    	}
			    } while (matcher.find());
			}
		}
		
		// personal name
		{
			Pattern pattern = Pattern.compile(patttern_name);
			Matcher matcher = pattern.matcher(source.toLowerCase());
			if (matcher.find()) {
				setContacts.add(Parameter.label_personal_feature);
	    		listContacts.add(Parameter.label_personal_feature);
			}
		}
		
		return listContacts;
	}
	
	public ArrayList<String> extractContactFromUrl(String url) throws IOException
	{
		String sourceUser = mySource.getSource(url);
		if(sourceUser.compareToIgnoreCase("break")==0)
		{
			return new ArrayList<>();
		}
		
		ArrayList<String> listContacts = extractContactFromSource(sourceUser);
		
		if (listContacts.size() > 0) {
			return listContacts;
		} else {
			String linkabout = myAboutLink.getAboutLink(sourceUser);
			if(linkabout.length()>0)
			{
				String sourceAbout = mySource.getSource(linkabout);
				listContacts = extractContactFromSource(sourceAbout);
				if(listContacts.size() > 0)
				{
					return listContacts;
				}
			} else {
//				linkabout = url;
//				if(linkabout.endsWith("/"))
//				{
//					linkabout += "about/";
//				} else {
//					linkabout += "/about/";
//				}
//				
//				String sourceAbout = mySource.getSource(linkabout);
//				listContacts = extractContactFromSource(sourceAbout);
//				if(listContacts.size()>0)
//				{
//					return listContacts;
//				} else {
//					linkabout.replace("/about/", "/contact/about/");
//					sourceAbout = mySource.getSource(linkabout);
//					listContacts = extractContactFromSource(sourceAbout);
//					return listContacts;
//				}
			}
		}
		
		return new ArrayList<>();
	}
	
	public static void main(String [] args) throws IOException
	{
		ContactExtraction object = new ContactExtraction();
		
		String url = "https://waterandflower.wordpress.com";
		System.out.println(object.extractContactFromUrl(url));
		ArrayList<String> arrayListContents = object.extractContactFromSource(url);
		for(String content : arrayListContents)
		{
			System.out.println(content);
		}
		
	}
}
