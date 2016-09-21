package functions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckingPersonalBlog {
	static String personal_pattern = "i am |i'm |i&#8217;m | my | me |i have|i think|je ";
	public static boolean isPersonalBlog(String source)
	{
		Pattern pattern = Pattern.compile(personal_pattern);
		Matcher matcher = pattern.matcher(source.toLowerCase());
		if (matcher.find()) {
			return true;
		}
		
		return false;
	}
	
	public static void main(String [] args) throws IOException
	{
		GettingSource.init();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("test/blogs.txt")));
		String line;
		int count = 0;
		while((line=br.readLine())!=null)
		{
			System.out.println(++count);
			String source = GettingSource.getSource(line);
			if(isPersonalBlog(source)==false)
			{
				System.out.println(line);
				System.out.println(source.length());
			}
		}
		br.close();
	}
}
