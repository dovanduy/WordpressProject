package functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentExtraction {
	
	private static String pattern_content = "<p>[^<>]{30,200}|>[^<>]{30,200}<";
	
	public static ArrayList<String> extractContent(String source)
	{
		HashSet<String> setContent = new HashSet<>();
		ArrayList<String> listContent = new ArrayList<String>();
		

		Pattern pattern = Pattern.compile(pattern_content);
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
		    do 
		    {
		    	String candidate = matcher.group();
		    	if(!setContent.contains(candidate))
		    	{
		    		setContent.add(candidate);
		    		listContent.add(candidate);
		    	}
		    } while (matcher.find());
		}
		
		return listContent;
	}
	
	public static void main(String [] args)
	{
		String source = "<a href=\"https://rockawooly.wordpress.com/2016/08/23/tout-ce-que-mon-fils-ne-sait-pas-faire-et-pourtant/\">Tout ce que mon fils ne sait pas faire&#8230; et pourtant&nbsp;!</a>						</li>";
		System.out.println(extractContent(source));
	}
}
