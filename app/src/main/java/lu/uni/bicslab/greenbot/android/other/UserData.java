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
	
	
	
	public static void setStatus(Context context, String status) {
		getPrefs(context).edit().putString("user_status", status).apply();
	}
	
	public static String getStatus(Context context) {
		return getPrefs(context).getString("user_status", USER_INVALID);
	}
	
	
	
	public static void setFirstTime(Context context, boolean firstTime) {
		getPrefs(context).edit().putBoolean("first_time", firstTime).apply();
	}
	
	public static boolean getFirstTime(Context context) {
		return getPrefs(context).getBoolean("first_time", true);
	}
	
	
	
	public static void setID(Context context, String id) {
		getPrefs(context).edit().putString("user_id", id).apply();
	}
	
	public static String getID(Context context) {
		return getPrefs(context).getString("user_id", null);
	}
	
}
