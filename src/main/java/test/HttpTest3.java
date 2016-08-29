package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpTest3 {
	private static String getUrlSource(String url) throws IOException {
		
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		
		URL urlObject;
		InputStream is = null;
		BufferedReader br;
		String line;
		String source = "";
		try {
			urlObject = new URL(url);
			is = urlObject.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				source += line + "\n";
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		
		return source;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getUrlSource("https://gracednotes.com/"));
	}
}
