package wordpress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import data.Users;
import functions.ContactExtraction;

public class ContactUsersExtraction {
	private static int limit_batch = 100;
	private static ContactExtraction contactExtraction;

	public static void usersContactExtraction() throws IOException {
		// Override system DNS setting with Google free DNS server
		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");

		HashMap<String, Integer> mapUsers = Users.getMapUserNotCheckContact();
		System.out.println("mapUsers.size() = " + mapUsers.size());

		// remove checked user from mapUsers
		HashMap<String, Integer> mapUsersCheckedContact = Users.getMapUserCheckedContact();
		for (String checkedUser : mapUsersCheckedContact.keySet()) {
			if (mapUsers.containsKey(checkedUser)) {
				mapUsers.remove(checkedUser);
			}
		}

		HashMap<String, String> mapUsersContact = new HashMap<>();
		HashMap<String, Integer> mapUsersCheckedContact_Batch = new HashMap<>();
		HashMap<String, Integer> mapUsersNotContact = new HashMap<>();

		int count = 0;

		for (String user : mapUsers.keySet()) 
		{
			System.out.println(++count);
			
			mapUsersCheckedContact_Batch.put(user, 0);
			System.out.println(user);
			ArrayList<String> listContacts = contactExtraction.extractContactFromUrl(user);
			System.out.println(listContacts);
			
			if(listContacts.size() > 0)
			{
				mapUsersContact.put(user, listContacts.toString());
			} else {
				mapUsersNotContact.put(user, 0);
			}
			
			System.out.println();
			if (count % limit_batch == 0) {
				Users.saveMapUserContact(mapUsersContact);
				Users.saveMapUserCheckedContact(mapUsersCheckedContact_Batch);
				Users.saveMapUserNotContact(mapUsersNotContact);
				mapUsersContact = new HashMap<>();
				mapUsersCheckedContact_Batch = new HashMap<>();
				mapUsersNotContact = new HashMap<>();
			}
		}
		
		Users.saveMapUserContact(mapUsersContact);
		Users.saveMapUserCheckedContact(mapUsersCheckedContact_Batch);
		Users.saveMapUserNotContact(mapUsersNotContact);
	}

	public static void main(String[] args) throws IOException {
		
		System.out.println("Classifying user contact ...");
		
		if (args.length > 0) {
			limit_batch = Integer.parseInt(args[0]);
		}

		contactExtraction = new ContactExtraction();
		usersContactExtraction();
		
		System.out.println("All done! Check file ===> contact-users.txt <===");
	}
}
