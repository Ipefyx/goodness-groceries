package lu.uni.bicslab.greenbot.android.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;
import lu.uni.bicslab.greenbot.android.datamodel.SubIndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductCategoryModel;

public class Utils {
	
	public static final String PRODUCT_IMAGE_PREFIX = "product_";
	
	
	// TODO: Remove when reworking compare page
	public static final String ind_cat_environment = "ind_cat_environment";
	public static final String ind_cat_social = "ind_cat_social";
	public static final String ind_cat_good_governance = "ind_cat_governance";
	public static final String ind_cat_economic = "ind_cat_economic";
	
	
	
	public interface DialogCallback {
		void callBack();
	}
	
	public static void showLanguageDialog(Context context, boolean showDesc, DialogCallback callback) {
		LayoutInflater factory = LayoutInflater.from(context);
		
		View dialogView = factory.inflate(R.layout.dialog_choose_language, null);
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setView(dialogView);
		
		ImageButton btn_fr = dialogView.findViewById(R.id.btn_fr);
		ImageButton btn_en = dialogView.findViewById(R.id.btn_en);
		TextView title_text = dialogView.findViewById(R.id.title_text);
		TextView desc_text = dialogView.findViewById(R.id.desc_text);
		Button btn_next = dialogView.findViewById(R.id.btn_next);
		
		if (!showDesc)
			desc_text.setVisibility(View.GONE);
			
			btn_fr.setOnClickListener(v -> {
			btn_fr.setBackgroundResource(R.drawable.flag_style);
			btn_en.setBackgroundResource(0);
			
			setLocale(context, "fr");
			//Manually update the dialog text to reflect locale change
			title_text.setText(R.string.language);
			desc_text.setText(R.string.language_change_later);
			btn_next.setText(R.string.next);
		});
		
		btn_en.setOnClickListener(v -> {
			btn_en.setBackgroundResource(R.drawable.flag_style);
			btn_fr.setBackgroundResource(0);
			
			setLocale(context, "en");
			//Manually update the dialog text to reflect locale change
			title_text.setText(R.string.language);
			desc_text.setText(R.string.language_change_later);
			btn_next.setText(R.string.next);
		});
		
		btn_next.setOnClickListener(v -> {
			dialog.dismiss();
			UserData.setLanguage(context, Locale.getDefault().getLanguage());
			callback.callBack();
		});
		
		// Need this to reset the flag styling correctly, couldn't figure out how to do it properly in the layout
		btn_fr.setBackgroundResource(R.drawable.flag_style);
		btn_fr.setBackgroundResource(0);
		btn_en.setBackgroundResource(R.drawable.flag_style);
		btn_en.setBackgroundResource(0);
		
		// Set the language selected by default
		String currentLang = UserData.getLanguage(context);
		if (currentLang != null && currentLang.equals("fr")) {
			btn_fr.callOnClick();
		} else {
			btn_en.callOnClick();
		}
		
		dialog.show();
	}
	
	
	public static void setLocale(Context context, String languageCode) {
		Locale locale = new Locale(languageCode);
		Locale.setDefault(locale);
		Resources resources = context.getResources();
		Configuration config = resources.getConfiguration();
		config.setLocale(locale);
		resources.updateConfiguration(config, resources.getDisplayMetrics());
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
	
	
	private static List<IndicatorModel> indicatorList = null;
	private static List<IndicatorCategoryModel> indicatorCategoryList = null;
	private static List<ProductCategoryModel> productCategoryList = null;
	private static List<ProductModel> productList = null;
	
	// Respective methods for loading the various JSON data files
	public static List<IndicatorModel> getIndicatorList(Context context) {
		if (indicatorList == null)
			readIndicatorList(context);
		
		return new ArrayList<>(indicatorList);
	}
	
	public static List<IndicatorCategoryModel> getIndicatorCategoryList(Context context) {
		if (indicatorCategoryList == null)
			readIndicatorCategoryList(context);
		
		return new ArrayList<>(indicatorCategoryList);
	}
	
	public static List<ProductCategoryModel> getProductCategoryList(Context context) {
		if (productCategoryList == null)
			readProductCategoryList(context);
		
		return new ArrayList<>(productCategoryList);
	}
	
	public static List<ProductModel> getProductList(Context context) {
		if (productList == null)
			readProductList(context);
		
		// Deep copy product list to avoid reference problems with the nested indicator instances
		List<ProductModel> productListCopy = new ArrayList<>();
		for (ProductModel p : productList) {
			productListCopy.add(new ProductModel(p));
		}
		return productListCopy;
	}
	
	
	
	private static void readIndicatorList(Context context) {
		String jsonFileString = getJsonFromAssets(context, "indicators.json");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<IndicatorModel>>() {}.getType();
		indicatorList = gson.fromJson(jsonFileString, type);
		
		//Translate strings and lowercase icon names
		for (IndicatorModel ind : indicatorList) {
			ind.setIcon_name(ind.getIcon_name().toLowerCase());
			
			ind.setName(getStringByResName(context, ind.getName()));
			ind.setGeneral_description(getStringByResName(context, ind.getGeneral_description()));
		}
	}
	
	private static void readIndicatorCategoryList(Context context) {
		String jsonFileString = getJsonFromAssets(context, "indicator_categories.json");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<IndicatorCategoryModel>>() {}.getType();
		indicatorCategoryList = gson.fromJson(jsonFileString, type);
		
		//Translate strings and lowercase icon names
		for (IndicatorCategoryModel ind : indicatorCategoryList) {
			ind.setIcon_name(ind.getIcon_name().toLowerCase());
			
			ind.setName(getStringByResName(context, ind.getName()));
			ind.setDescription(getStringByResName(context, ind.getDescription()));
		}
	}
	
	private static void readProductCategoryList(Context context) {
		// Currently hardcoded
		productCategoryList = Arrays.asList(
				new ProductCategoryModel("local_organic", context.getResources().getString(R.string.PRODUCT_CATEGORY_LOCAL_ORGANIC), "prod_cat_localorganic", ""),
				new ProductCategoryModel("imported_organic", context.getResources().getString(R.string.PRODUCT_CATEGORY_IMPORTED_ORGANIC), "prod_cat_importedorganic", ""),
				new ProductCategoryModel("local_conventional", context.getResources().getString(R.string.PRODUCT_CATEGORY_LOCAL_CONVENTIONAL), "prod_cat_localconventional", ""),
				new ProductCategoryModel("imported_conventional", context.getResources().getString(R.string.PRODUCT_CATEGORY_IMPORTED_CONVENTIONAL), "prod_cat_importedconventional", "")
		);
	}
	
	private static void readProductList(Context context) {
		String jsonFileString = getJsonFromAssets(context, "products.json");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<ProductModel>>() {}.getType();
		productList = gson.fromJson(jsonFileString, type);
		
		// Merge the indicator with sub-indicators with the actual indicator data
		// Translate strings
		// Remove all non-applicable indicators
		// And adjust image_url
		for (ProductModel product : productList) {
			product.setName(getStringByResName(context, product.getName()));
			product.setDescription(getStringByResName(context, product.getDescription()));
			product.setType(getStringByResName(context, product.getType()));
			
			product.setImage_url(PRODUCT_IMAGE_PREFIX + product.getImage_url());

			for (IndicatorModel ind : product.indicators) {
				ind.mergeBaseIndicator(getIndicatorByID(context, ind.getId()));

				for (SubIndicatorModel sub : ind.sub_indicators) {
					sub.setName(getStringByResName(context, sub.getName()));
					sub.setDescription(getStringByResName(context, sub.getDescription()));
					sub.setFile(sub.getFile());
				}
			}
		}
	}

	// TODO: REMOVE ? //
	public static boolean isIndicatorApplicable(Context context, String productId, String indicatorId) {
		ProductModel product = getProductByCode(context, productId);
		Optional<IndicatorModel> match = product.indicators.stream().filter(ind -> ind.getId().equals(indicatorId)).findAny();

		IndicatorModel ind = match.orElse(null);

		if(ind != null) return ind.isApplicable();
		return false;
	}
	
	public static IndicatorModel getIndicatorByID(Context context, String id) {
		Optional<IndicatorModel> match = getIndicatorList(context).stream().filter(ind -> ind.getId().equals(id)).findFirst();
		return match.orElse(null);
	}

	public static IndicatorCategoryModel getIndicatorCategoryById(Context context, String id) {
		Optional<IndicatorCategoryModel> match = getIndicatorCategoryList(context).stream().filter(ind -> ind.getId().equals(id)).findFirst();
		return match.orElse(null);
	}
	
	public static ProductCategoryModel getProductCategoryByID(Context context, String id) {
		Optional<ProductCategoryModel> match = getProductCategoryList(context).stream().filter(p -> p.getId().equals(id)).findFirst();
		return match.orElse(null);
	}
	
	public static ProductModel getProductByCode(Context context, String code) {
		Optional<ProductModel> match = getProductList(context).stream().filter(p -> p.getCode().equals(code)).findFirst();
		return match.orElse(null);
	}
	
	public static List<ProductModel> getProductsByType(Context context, String type) {
		return getProductList(context).stream().filter(p -> p.getType().equals(type)).collect(Collectors.toList());
	}




	public static String getStringByResName(Context ctx, String str) {
		int resID = ctx.getResources().getIdentifier(str, "string", ctx.getPackageName());

		if (resID == 0) {
			throw new RuntimeException("String Resource \""+str+"\" could not be found.");
		}
		
		return ctx.getString(resID);
	}

	/**
	 * Transform given Textview in a clickable hyperlink to the given uri
	 * @param ctx
	 * @param textView
	 * @param url
	 */
	public static void linkify(Context ctx, TextView textView, String url) {
		textView.setMovementMethod(LinkMovementMethod.getInstance());

		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				ctx.startActivity(intent);
			}

		});
	}
	
}
