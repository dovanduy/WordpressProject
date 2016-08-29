package functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutLinkExtraction {
	private static String pattern_email = "http[s]?://[a-zA-Z0-9.\\_\\/]{1,50}/about[a-zA-Z\\-\\_]{0,20}";
	
	public String getAboutLink(String source)
	{
		Pattern pattern = Pattern.compile(pattern_email);
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			String candidate = matcher.group();
	    	return candidate;
		}
		
		return "";
	}
	
	public static void main(String [] args)
	{
		AboutLinkExtraction object = new AboutLinkExtraction();
		String source = "<a href=\"https://tarahodgson.wordpress.com/about-real-women/\">";
		System.out.println(object.getAboutLink(source));
	}
}
