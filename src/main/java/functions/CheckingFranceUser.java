package functions;

import java.io.IOException;
import java.util.ArrayList;

import parameter.Parameter;

public class CheckingFranceUser 
{
	static FranceLanguageDetection franceDetection;
	
	public static void init()
	{
		try {
			franceDetection = new FranceLanguageDetection();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	private static int countPDF(String source)
	{
		String str = source.toLowerCase();				
		String findStr = ".pdf";
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

		    lastIndex = str.indexOf(findStr, lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += findStr.length();
		    }
		}
		return count;
	}
	
	public static boolean isFrancer(String source)
	{
		// check raw blogs : contain many .pdf file.
		if(countPDF(source)>30)
		{
			System.out.println("Raw blogs : PDF");
			return false;
		}
		
		ArrayList<String> listContents = ContentExtraction.extractContent(source);
		for(int i=0; i<listContents.size(); i++)
		{
			String content = listContents.get(i);
			if(franceDetection.isFrance(content) && content.split(" ").length > Parameter.thread_france)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String [] args) throws IOException
	{
		init();
		GettingSource.init();
		String blog = "https://zebrabeauty101.wordpress.com/";
		String source = GettingSource.getSource(blog);
		System.out.println(source);
		System.out.println(isFrancer(source));
	}
}
