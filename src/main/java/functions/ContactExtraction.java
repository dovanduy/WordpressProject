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

	static private String pattern_email = "[a-zA-Z0-9.\\_]{1,50}@[a-zA-Z.]{2,30}|[a-zA-Z0-9.\\_]{1,50}\\(at\\)[a-zA-Z.]{2,30}|[a-zA-Z0-9.\\_]{1,50}\\[at\\][a-zA-Z.]{2,30}";
	static private String pattern_facebook = "http[s]?://[w]?[w]?[w]?[.]?facebook.com/[a-zA-Z0-9.\\_/\\-]{1,50}|follow me on facebook[ ]?[a]?[t]? [\\S]{1,30}|facebook:[ ]?[^<>\" ]{1,30}|facebook.com/[a-zA-Z0-9.\\_/\\-]{1,50}";
	static private String pattern_instagram = "http[s]?://[w]?[w]?[w]?[.]?instagram.com/[a-zA-Z0-9.\\_\\/]{1,50}|follow me on instagram[ ]?[a]?[t]? [\\S]{1,30}|instagram:[ ]?[^<>\" ]{1,30}";

	static private HashSet<String> removeLink;

	static public void init() 
	{
		GettingSource.init();
		removeLink = new HashSet<>();
		removeLink.add("https://www.facebook.com/pages");
		removeLink.add("https://www.facebook.com/wikipedia");
		removeLink.add("https://www.facebook.com/WordPresscom");
		removeLink.add("http://www.facebook.com/wordpresscom");
		removeLink.add("https://twitter.com/share");
		removeLink.add("https://twitter.com/wordpressdotcom");

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(Parameter.file_not_contact)));
			String line;
			while ((line = br.readLine()) != null) {
				removeLink.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public ArrayList<String> extractContactFromSource(String source) 
	{
		HashSet<String> setContacts = new HashSet<>();
		ArrayList<String> listContacts = new ArrayList<String>();

		// Email
		{
			Pattern pattern = Pattern.compile(pattern_email);
			Matcher matcher = pattern.matcher(source);
			if (matcher.find()) {
				do {
					String candidate = matcher.group();
					if (!setContacts.contains(candidate) && !removeLink.contains(candidate)) {
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
				do {
					String candidate = matcher.group();
					if (!setContacts.contains(candidate) && !removeLink.contains(candidate)) {
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
				do {
					String candidate = matcher.group();
					if (!setContacts.contains(candidate) && !removeLink.contains(candidate)) {
						setContacts.add(candidate);
						listContacts.add(candidate);
					}
				} while (matcher.find());
			}
		}

		return listContacts;
	}

	static public ArrayList<String> extractContactFromUrl(String url) throws IOException {
		String sourceUser = GettingSource.getSource(url);
		if (sourceUser.compareToIgnoreCase("break") == 0) 
		{
			System.out.println("break");
			return new ArrayList<>();
		}
		ArrayList<String> listContacts = extractContactFromSource(sourceUser);
		ArrayList<String> listLinkAbout = AboutLinkExtraction.getAboutLink(sourceUser);
		for(int i=0; i<listLinkAbout.size(); i++)
		{
			String linkabout = listLinkAbout.get(i);
			String sourceAbout = GettingSource.getSource(linkabout);
			ArrayList<String> listContacts_about = extractContactFromSource(sourceAbout);
			for (String contact : listContacts_about) {
				if (!listContacts.contains(contact)) {
					listContacts.add(contact);
				}
			}
		}

		return listContacts;
	}
	
	static public ArrayList<String> extractContactFromUrlAndSource(String url, String source) throws IOException {
		if (source.compareToIgnoreCase("break") == 0) 
		{
			System.out.println("break");
			return new ArrayList<>();
		}
		ArrayList<String> listContacts = extractContactFromSource(source);
		ArrayList<String> listLinkAbout = AboutLinkExtraction.getAboutLink(source);
		for(int i=0; i<listLinkAbout.size(); i++)
		{
			String linkabout = listLinkAbout.get(i);
			String sourceAbout = GettingSource.getSource(linkabout);
			ArrayList<String> listContacts_about = extractContactFromSource(sourceAbout);
			for (String contact : listContacts_about) {
				if (!listContacts.contains(contact)) {
					listContacts.add(contact);
				}
			}
		}

		return listContacts;
	}

	public static void main(String[] args) throws IOException 
	{
		String url = "https://mumofdylan.wordpress.com/about/";
		ArrayList<String> arrayListContents = extractContactFromUrl(url);
		for (String content : arrayListContents) {
			System.out.println(content);
		}
	}
}
