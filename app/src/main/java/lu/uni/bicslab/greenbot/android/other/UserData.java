package lu.uni.bicslab.greenbot.android.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		Log.i("UserData getStatus", getPrefs(context).getString("user_status", USER_INVALID));
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
		Log.i("UserData getID", getPrefs(context).getString("user_id", null));
		return getPrefs(context).getString("user_id", null);
	}

	public static void setToken(Context context, String token) {
		getPrefs(context).edit().putString("user_token", token).apply();
	}
	
	public static String getToken(Context context) {
		return getPrefs(context).getString("user_token", null);
	}
	
	// The language/locale chosen by the user
	public static void setLanguage(Context context, String language) {
		getPrefs(context).edit().putString("user_lang", language).apply();
	}
	
	public static String getLanguage(Context context) {
		return getPrefs(context).getString("user_lang", null);
	}


	public static void setPhase2Date(Context context, String date) {
		getPrefs(context).edit().putString("user_phase2_date", date).apply();
	}

	public static String getPhase2Date(Context context) {
		return getPrefs(context).getString("user_phase2_date", "1970-01-01");
	}


	public static void setPhase1Date(Context context, String date) {
		getPrefs(context).edit().putString("user_phase1_date", date).apply();
	}

	public static String getPhase1Date(Context context) {

		return getPrefs(context).getString("user_phase1_date", "1970-01-01");
	}

	public static boolean isPhase2(Context context) {
		Date today = new Date();
		Date phase2;
		try {
			phase2 = new SimpleDateFormat("yyyy-MM-dd").parse(getPhase2Date(context));
		} catch (ParseException e) {
			phase2 = null;
		}
		if(phase2 != null)
			return today.compareTo(phase2) >= 0;
		return true;
	}
	
}
