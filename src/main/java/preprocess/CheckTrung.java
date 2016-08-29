package preprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import parameter.Parameter;

public class CheckTrung {
	public static void main(String [] args) throws IOException
	{
		System.out.println("Start...");
		HashMap<String, Integer> mapUsers = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Parameter.file_old_user)));
		String line;
		while((line = br.readLine())!=null)
		{
			line = line.replace("com/", "com");
			if(!line.startsWith("https"))
			{
				line = "https://" + line;
			}
			
			mapUsers.put(line, 0);
		}
		br.close();
		
		FileWriter fw = new FileWriter("data/users2.txt");
		
		for(String user : mapUsers.keySet())
		{
			fw.write(user + "\n");
		}
		fw.close();
		
		System.out.println("All done");
	}
}
