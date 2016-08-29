package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import parameter.Parameter;

public class Users 
{
	public static boolean saveMapUser(HashMap<String, Integer> mapUsers) throws IOException
	{
		FileWriter fw = new FileWriter(Parameter.file_new_user, true);
		for(String user : mapUsers.keySet())
		{
			fw.write(user + "\n");
		}
		fw.close();
		return true;
	}
	
	public static boolean saveMapUserContact(HashMap<String, String> mapUsersContact) throws IOException
	{
		FileWriter fw = new FileWriter(Parameter.file_user_contact, true);
		for(String user : mapUsersContact.keySet())
		{
			fw.write(user + "\n");
			fw.write(mapUsersContact.get(user) + "\n\n");
		}
		fw.close();
		return true;
	}
	
	public static boolean saveMapUserFrance(HashMap<String, Integer> mapUsersFrance) throws IOException
	{
		FileWriter fw = new FileWriter(Parameter.file_user_france, true);
		for(String user : mapUsersFrance.keySet())
		{
			fw.write(user + "\n");
		}
		fw.close();
		return true;
	}
	
	public static HashMap<String, Integer> getMapUser() throws IOException
	{
		HashMap<String, Integer> mapUsers = new HashMap<String, Integer>();
		
		// old
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Parameter.file_old_user)));
			
			String line;
			while((line = br.readLine())!=null)
			{
				if(!line.endsWith("/"))
				{
					line = line + "/";
				}
				mapUsers.put(line, 0);
			}
			
			br.close();
		}
		
		// new
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Parameter.file_new_user)));
			
			String line;
			while((line = br.readLine())!=null)
			{
				if(!line.endsWith("/"))
				{
					line = line + "/";
				}
				mapUsers.put(line, 0);
			}
			
			br.close();
		}
		
		return mapUsers;
	}
	
	public static HashMap<String, Integer> getMapUserNotCheckContact() throws IOException
	{
		HashMap<String, Integer> mapUsers = new HashMap<String, Integer>();
		
		// new
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Parameter.file_new_user)));
			
			String line;
			while((line = br.readLine())!=null)
			{
				if(!line.endsWith("/"))
				{
					line = line + "/";
				}
				mapUsers.put(line, 0);
			}
			
			br.close();
		}
		
		return mapUsers;
	}
	
	public static boolean saveMapUserCheckedContact(HashMap<String, Integer> mapUsersCheckedContact) throws IOException
	{
		FileWriter fw = new FileWriter(Parameter.file_user_checked_contact, true);
		for(String user : mapUsersCheckedContact.keySet())
		{
			fw.write(user + "\n");
		}
		fw.close();
		
		return true;
	}
	
	public static boolean saveMapUserNotContact(HashMap<String, Integer> mapUsersNotContact) throws IOException
	{
		FileWriter fw = new FileWriter(Parameter.file_user_not_contact, true);
		for(String user : mapUsersNotContact.keySet())
		{
			fw.write(user + "\n");
		}
		fw.close();
		return true;
	}
	
	public static HashMap<String, Integer> getMapUserCheckedContact() throws IOException
	{
		HashMap<String, Integer> mapUsers = new HashMap<String, Integer>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Parameter.file_user_checked_contact)));
		
		String line;
		while((line = br.readLine())!=null)
		{
			if(!line.endsWith("/"))
			{
				line = line + "/";
			}
			mapUsers.put(line, 0);
		}
		
		br.close();
		
		return mapUsers;
	}
}
