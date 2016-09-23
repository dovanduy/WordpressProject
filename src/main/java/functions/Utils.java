package functions;

public class Utils {
	public static String generateRow(String blog_url, String author_name, String tag, String contact)
	{
		String row = "";
		contact = contact.replace("[", "").replace("]", "");
		row += blog_url + ", ";
		row += author_name + ", ";
		row += tag + ", ";
		row += contact + "";
		
		return row;
	}
}
