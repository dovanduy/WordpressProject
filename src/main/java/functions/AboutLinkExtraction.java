package functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutLinkExtraction {
	private static String pattern_about = "http[s]?://[a-zA-Z0-9\\-.\\_\\/]{1,50}/about[a-zA-Z\\-\\_]{0,20}|http[s]?://[a-zA-Z0-9\\-.\\_\\/]{1,50}/contact[a-zA-Z\\-\\_]{0,20}|http[s]?://[a-zA-Z0-9\\-.\\_\\/]{1,50}/the-author|http[s]?://[a-zA-Z0-9\\-.\\_\\/]{1,50}/social";
	
	public static ArrayList<String> getAboutLink(String source)
	{
		ArrayList<String> listLinkAbout = new ArrayList<>();
		Pattern pattern = Pattern.compile(pattern_about);
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			do {
				String candidate = matcher.group();
				listLinkAbout.add(candidate);
			} while (matcher.find());
		}
		
		return listLinkAbout;
	}
	
	public static void main(String [] args) throws IOException
	{
		GettingSource.init();
		String source = GettingSource.getSource("https://john-l-monk.com/");
		System.out.println(source);
		System.out.println(getAboutLink("<a href=\"https://john-l-monk.com/contact-me/\">"));
	}
}
