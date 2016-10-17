package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import parameter.Parameter;

public class BlogsIO 
{
	public static boolean saveMapBlogs(HashMap<String, Integer> mapBlogs, String file_name) throws IOException {
		FileWriter fw = new FileWriter(file_name, true);
		for (String user : mapBlogs.keySet()) {
			fw.write(user + "\n");
		}
		fw.close();
		return true;
	}
	
	public static boolean saveMapPersonalContact(HashMap<String, String> saveMapPersonalContact, String file_name) throws IOException {
		FileWriter fw = new FileWriter(file_name, true);
		for (String blog : saveMapPersonalContact.keySet()) {
			fw.write(blog + "\n");
			fw.write(saveMapPersonalContact.get(blog) + "\n\n");
		}
		fw.close();
		return true;
	}
	
	public static boolean saveContactToExcelFile(ArrayList<String> rows, String file_name) throws IOException {
		FileWriter fw = new FileWriter(file_name, true);
		for(int i=0; i<rows.size(); i++)
		{
			fw.write(rows.get(i) + "\n");
		}
		fw.close();
		return true;
	}

	public static HashMap<String, Integer> getMapCheckedBlog() throws IOException 
	{
		HashMap<String, Integer> mapBlogs = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(Parameter.file_checked_blog)));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.endsWith("/")) {
				line = line.substring(0, line.length()-1);
			}
			System.out.println(line);
			mapBlogs.put(line, 0);
		}
		br.close();
		return mapBlogs;
	}
	
	public static void main(String [] args) throws IOException
	{
		HashMap<String, Integer> tempMap = getMapCheckedBlog();
		System.out.println(tempMap.size());
	}
}
