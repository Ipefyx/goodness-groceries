package lu.uni.bicslab.greenbot.android.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;
import lu.uni.bicslab.greenbot.android.ui.fragment.product_category.ProductCategoryModel;

public class Utils {
	
	public static String id = "";
	public static final String PREF_NAME = "GREENBOT_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;
	
	
	public static final String name = "name";
	public static final String shrprofilename = "profile";
	
	public static final int user_requested = 1;
	public static final int user_notloggedin = 0;
	public static final int user_loggedin = 2;
	public static final int user_loggedin_firsttime = 3;
	
	
	public static final String ind_cat_environment = "ind_cat_environment";
	public static final String ind_cat_social = "ind_cat_social";
	public static final String ind_cat_good_gevernance = "ind_cat_good_gevernance";
	public static final String ind_cat_economic = "ind_cat_economic";
	
	
	public static int getStatusBarHeight(Context context) {
		int height = (int) context.getResources().getDimension(R.dimen.statusbar_size);
		return height;
	}
	
	public static String getJsonFromAssets(Context context, String fileName) {
		String jsonString;
		try {
			InputStream is = context.getAssets().open(fileName);
			
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			
			jsonString = new String(buffer, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return jsonString;
	}
	
	public static Drawable getDrawableImage(Context c, String imageName) {
		try {
			return ContextCompat.getDrawable(c, c.getResources().getIdentifier(imageName, "drawable", c.getPackageName()));
		} catch (Exception e) {
			return ContextCompat.getDrawable(c, c.getResources().getIdentifier("ic_menu_gallery", "drawable", c.getPackageName()));
		}
		
	}
	
	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}
	
	public static boolean readBoolean(Context context, String key,
												 boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}
	
	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
		
	}
	
	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}
	
	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}
	
	public static SharedPreferences.Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}
	
	public static void saveProfile(Context context, Profile mProfile) {
		
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = pref.edit();
		Gson gson = new Gson();
		String json = gson.toJson(mProfile);
		prefsEditor.putString(shrprofilename, json);
		prefsEditor.commit();
	}
	
	public static Profile readProfileData(Context context) {
		Gson gson = new Gson();
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String json = pref.getString(shrprofilename, "");
		try {
			Profile obj = gson.fromJson(json, Profile.class);
			return obj;
		} catch (Exception e) {
			return null;
			
		}
	}
	
	
	
	private static List<IndicatorModel> indicatorList = null;
	private static List<IndicatorCategoryModel> indicatorCategoryList = null;
	private static List<ProductCategoryModel> productCategoryList = null;
	private static List<ProductModel> productList = null;
	
	// Respective methods for loading the various JSON data files
	public static List<IndicatorModel> getIndicatorList(Context context) {
		if (indicatorList != null)
			return new ArrayList<>(indicatorList);
		
		String jsonFileString = getJsonFromAssets(context, "indicators.json");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<IndicatorModel>>() {}.getType();
		indicatorList = gson.fromJson(jsonFileString, type);
		
		return indicatorList;
	}
	
	public static List<IndicatorCategoryModel> getIndicatorCategoryList(Context context) {
		if (indicatorCategoryList != null)
			return new ArrayList<>(indicatorCategoryList);
		
		String jsonFileString = getJsonFromAssets(context, "indicator_categories.json");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<IndicatorCategoryModel>>() {}.getType();
		indicatorCategoryList = gson.fromJson(jsonFileString, type);
		
		return indicatorCategoryList;
	}
	
	public static List<ProductCategoryModel> getProductCategoryList(Context context) {
		if (productCategoryList != null)
			return new ArrayList<>(productCategoryList);
		
		// Currently hardcoded
		productCategoryList = Arrays.asList(
				new ProductCategoryModel("local_organic", context.getResources().getString(R.string.biolocal), "prod_cat_localorganic", ""),
				new ProductCategoryModel("imported_organic", context.getResources().getString(R.string.bioimporte), "prod_cat_importedorganic", ""),
				new ProductCategoryModel("local_conventional", context.getResources().getString(R.string.conlocal), "prod_cat_localconventional", ""),
				new ProductCategoryModel("imported_conventional", context.getResources().getString(R.string.conimporte), "prod_cat_importedconventional", "")
		);
		
		return productCategoryList;
	}
	
	public static List<ProductModel> getProductList(Context context) {
		if (productList != null)
			return new ArrayList<>(productList);
		
		String jsonFileString = getJsonFromAssets(context, "products.json");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<ProductModel>>() {}.getType();
		productList = gson.fromJson(jsonFileString, type);
		
		return productList;
	}
	
	
}
