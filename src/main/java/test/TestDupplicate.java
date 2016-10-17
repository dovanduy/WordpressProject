package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TestDupplicate {
	
	static String file = "/home/thanhtam/Desktop/checked-blogs.txt";
	static String file_out = "/home/thanhtam/Desktop/checked-blogs2.txt";
	
	public static void main(String [] args) throws IOException 
	{
		HashMap<String, Integer> mapDupplicate = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		while((line=br.readLine())!=null)
		{
			String blog = line.split(",")[0];
			if(mapDupplicate.containsKey(blog))
			{
				mapDupplicate.put(blog, mapDupplicate.get(blog) + 1);
			} else {
				mapDupplicate.put(blog, 1);
			}
		} 
		br.close();
		
		// show
		FileWriter fw = new FileWriter(file_out);
		for(String blog : mapDupplicate.keySet())
		{
			if(mapDupplicate.get(blog)>1)
			{
				System.out.println(blog);
				System.err.println(mapDupplicate.get(blog));
				System.out.println();
			}
			
			fw.write(blog + "\n");
		}
		fw.close();
		
		System.out.println("All done!");
	}
}
