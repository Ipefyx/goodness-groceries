package lu.uni.bicslab.greenbot.android.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserData {
	
	public static final String USER_INVALID = "invalid";
	public static final String USER_REQUESTED = "requested";
	public static final String USER_VALID = "valid";
	public static final String USER_ARCHIVED = "archived";
	
	
	
	private static SharedPreferences getPrefs(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	
	
	// The Status of the user, matching with the API statuses
	public static void setStatus(Context context, String status) {
		getPrefs(context).edit().putString("user_status", status).apply();
	}
	
	public static String getStatus(Context context) {
		return getPrefs(context).getString("user_status", USER_INVALID);
	}
	
	
	
	// Whether or not the two first-time screens should be shown
	public static void setFirstTimeVisit(Context context, boolean firstTimeVisit) {
		getPrefs(context).edit().putBoolean("first_time_visit", firstTimeVisit).apply();
	}
	
	public static boolean isFirstTimeVisit(Context context) {
		return getPrefs(context).getBoolean("first_time_visit", true);
	}
	
	
	
	// The participant ID of the user
	public static void setID(Context context, String id) {
		getPrefs(context).edit().putString("user_id", id).apply();
	}
	
	public static String getID(Context context) {
		return getPrefs(context).getString("user_id", null);
	}
	
	
	
	// The language/locale chosen by the user
	public static void setLanguage(Context context, String language) {
		getPrefs(context).edit().putString("user_lang", language).apply();
	}
	
	public static String getLanguage(Context context) {
		return getPrefs(context).getString("user_lang", null);
	}
	
}
