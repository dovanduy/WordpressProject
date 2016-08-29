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
	
	public boolean isFrancer(String source)
	{
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
}
