package FriendTracker.Friendtracker.friendtracker.utilities;

import java.util.Set;

public class Utilities {
	
	/**
	 * Creates a simple comma-delimited list of strings.
	 * @param strings the strings to concatenate 
	 * @return a list of all the strings, separated by commas
	 */
	public static String concatenateStringSet(Set<String> strings)
	{
		String toReturn = "";
		if(null != strings && !strings.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for(String str : strings)
			{
				sb.append(str).append(", ");
			}
			toReturn = sb.substring(0, sb.length() - 2);
		}
		
		return toReturn;
	}
}
