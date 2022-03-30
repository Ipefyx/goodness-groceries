package lu.uni.bicslab.greenbot.android.other;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerConnection {
	
	private static final String BASE_URL = "https://goodnessgroceries.com/";
	
	private static final String REQUEST_USER_ACCESS = "request_user_access/";
	private static final String FETCH_USER_STATUS = "fetch_user_status/";
	private static final String GET_BOUGHT_PRODUCTS = "get_bought_products/";
	private static final String POST_PRODUCT_REVIEW = "post_product_review/";
	private static final String POST_DEVICE_TOKEN = "device/gcmdevice/";
	
	public interface ErrorCallback {
		void callback(VolleyError error);
	}
	
	public interface GenericJSONCallback {
		void callback(JSONObject object);
	}
	
	public interface UserStatusCallback {
		void callback(String status);
	}
	
	public interface ProductsCallback {
		void callback(String[] products);
	}
	
	
	
	public static void requestUserAccess(Context context, String userID, String[] productCategories, String[] indicatorCategories, UserStatusCallback callback, ErrorCallback errorCallback) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String postUrl = BASE_URL + REQUEST_USER_ACCESS;
		
		JSONObject object = new JSONObject();
		try {
			object.put("participant_id", userID);
			object.put("platform", "android");
			object.put("product_category_1", productCategories.length > 0 ? productCategories[0] : "");
			object.put("product_category_2", productCategories.length > 1 ? productCategories[1] : "");
			object.put("product_category_3", productCategories.length > 2 ? productCategories[2] : "");
			object.put("product_category_4", productCategories.length > 3 ? productCategories[3] : "");
			object.put("indicator_category_1", indicatorCategories.length > 0 ? indicatorCategories[0] : "");
			object.put("indicator_category_2", indicatorCategories.length > 1 ? indicatorCategories[1] : "");
			object.put("indicator_category_3", indicatorCategories.length > 2 ? indicatorCategories[2] : "");
			object.put("indicator_category_4", indicatorCategories.length > 3 ? indicatorCategories[3] : "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("Server connection data sent ", object.toString());
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, object, response -> {
			callback.callback(response.optString("status"));
			
		}, errorCallback::callback);
		
		requestQueue.add(request);
	}
	
	
	
	public static void fetchUserStatus(Context context, String userID, UserStatusCallback callback, ErrorCallback errorCallback) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url = BASE_URL + FETCH_USER_STATUS + userID + "/";
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
			callback.callback(response.optString("status"));
			
		}, errorCallback::callback);
		
		requestQueue.add(request);
	}
	
	
	
	public static void fetchProductsBought(Context context, String userID, ProductsCallback callback, ErrorCallback errorCallback) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url = BASE_URL + GET_BOUGHT_PRODUCTS + userID + "/";
		
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
			String[] products = new String[response.length()];
			for (int i = 0; i < response.length(); i++) {
				products[i] = response.optJSONObject(i).optString("product_ean"); // Only take the product ID, ignore timestamp
			}
			
			callback.callback(products);
			
		}, errorCallback::callback);
		
		requestQueue.add(request);
	}
	
	
	
	public static void sendProductFeedback(Context context, String userID, String productEAN, String selectedIndMain, String selectedIndSecondary, String freeTextInd, boolean priceCheckboxSelected, GenericJSONCallback callback, ErrorCallback errorCallback) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url = BASE_URL + POST_PRODUCT_REVIEW;
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStamp = format.format(calendar.getTime());
		
		JSONObject object = new JSONObject();
		try {
			object.put("participant", userID);
			object.put("product_ean", productEAN);
			object.put("timestamp", timeStamp);
			object.put("selected_indicator_main_id", selectedIndMain);
			object.put("selected_indicator_secondary_id", selectedIndSecondary);
			object.put("free_text_indicator", freeTextInd);
			object.put("price_checkbox_selected", priceCheckboxSelected);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.i("4 server request", object.toString());
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, callback::callback, errorCallback::callback);
		
		requestQueue.add(request);
	}
	
	
	
	public static void sendDeviceToken(Context context, String userID, String deviceToken, GenericJSONCallback callback, ErrorCallback errorCallback) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url = BASE_URL + POST_DEVICE_TOKEN;
		
		JSONObject object = new JSONObject();
		try {
			object.put("name", userID);
			object.put("registration_id", deviceToken);
			object.put("active", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.i("5 server request", object.toString());
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, callback::callback, errorCallback::callback);
		
		requestQueue.add(request);
	}
	
}
