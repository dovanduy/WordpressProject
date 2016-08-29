package functions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import parameter.Parameter;

public class MyLog {
	public static void saveLog(String log)
	{
		try {
			FileWriter fw = new FileWriter(Parameter.file_log, true);
			fw.write("=========== " + (new Date()) + " ==========\n");
			fw.write(log);
			fw.write("\n");
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
