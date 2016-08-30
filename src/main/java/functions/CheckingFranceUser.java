package functions;

import java.io.IOException;
import java.util.ArrayList;

public class CheckingFranceUser 
{
	FranceLanguageDetection franceDetection;
	
	public CheckingFranceUser()
	{
		try {
			franceDetection = new FranceLanguageDetection();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	private int countPDF(String source)
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
	
	public boolean isFrancer(String source)
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
			// System.out.println(content);
			if(franceDetection.isFrance(content))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String [] args)
	{
		CheckingFranceUser object = new CheckingFranceUser();
		System.out.println(object.isFrancer(".pdf akjdhkfjashdkfjhaskl/adkjsf.pdf"));
	}
}
