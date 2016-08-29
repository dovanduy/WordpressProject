package functions;


import java.io.IOException;
import java.util.List;

//import com.detectlanguage.DetectLanguage;
//import com.detectlanguage.errors.APIError;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public class FranceLanguageDetection 
{
	List<LanguageProfile> languageProfiles;
	LanguageDetector languageDetector;
	TextObjectFactory textObjectFactory;
	
	public FranceLanguageDetection() throws IOException
	{
		//load all languages:
		languageProfiles = new LanguageProfileReader().readAllBuiltIn();

		//build language detector:
		languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
				.withProfiles(languageProfiles)
				.build();

		//create a text object factory
		textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

	}
	
//	private boolean testDetectLanguage(String input) throws APIError
//	{
//		DetectLanguage.apiKey = "83154f113806dc4bb213f921571ec8ac";
//		String language = DetectLanguage.simpleDetect(input);
//		System.out.println(language);
//		return false;
//	}
	
	public boolean isFrance(String input)
	{
		TextObject textObject = textObjectFactory.forText(input);
		LdLocale language;
		
		try {
			language = languageDetector.detect(textObject).get();
			if(language.toString().equals("fr"))
			{
				return true;
			}
		} catch (Exception e)
		{
			return false;
		}

		return false;
	}
	
	public static void main(String [] args) throws IOException
	{
		FranceLanguageDetection object = new FranceLanguageDetection();
		String input = ">Tout ce que mon fils ne sait pas faire&#8230; et pourtant&nbsp;!<";
		System.out.println(object.isFrance(input));
	}
}
