package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import functions.CheckingFranceUser;
import functions.GettingSource;

public class TestCheckFrance {
	public static void main(String [] args) throws IOException
	{
		GettingSource gettingSource = new GettingSource();
		CheckingFranceUser checkingFrance = new CheckingFranceUser();
		String fileFrance = "data/frances.txt";
		String fileNotFrance = "data/not-frances.txt";
		FileWriter fw = new FileWriter(fileNotFrance);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileFrance)));
		String line;
		int count = 0;
		while((line = br.readLine())!=null)
		{
			System.out.println(++count);
			System.out.println(line);
			String source = gettingSource.getSource(line);
			boolean checkFrance = checkingFrance.isFrancer(source);
			System.out.println(checkFrance + "\n");
			if(!checkFrance)
			{
				fw.write(line + "\n");
			}
		}
		fw.close();
		br.close();
	}
}
