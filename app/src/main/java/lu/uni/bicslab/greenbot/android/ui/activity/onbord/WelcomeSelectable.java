package lu.uni.bicslab.greenbot.android.ui.activity.onbord;

public class WelcomeSelectable {
	
	private String description;
	private String image;
	private int color;
	private boolean selected = false;
	
	
	public WelcomeSelectable(String description, String image, int color) {
		this.description = description;
		this.image = image;
		this.color = color;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}

//	private static List<IndicatorCategoryModel> getIndicatorCategories(Context context) {
//		String jsonFileStringIndicator = Utils.getJsonFromAssets(context, "indicator_categories.json");
//		Gson gson = new Gson();
//		Type listUserTypeIndicator = new TypeToken<List<IndicatorCategoryModel>>() {}.getType();
//		return gson.fromJson(jsonFileStringIndicator, listUserTypeIndicator);
//	}
//	
//	public static String[] getTitle(Context context) {
//		return getIndicatorCategories(context).stream().map(IndicatorCategoryModel::getIndicator_name).toArray(String[]::new);
//	}
//	
//	public static String[] getImage(Context context) {
//		return null;
////		return getIndicatorCategories(context).stream().map(IndicatorCategoryModel::getIcon_name).toArray(String[]::new);
//	}
//	
//	public static String[] getTitle2(Context context) {
//		return null;
////		return getIndicatorCategories(context).stream().map(IndicatorCategoryModel::getDescription).toArray(String[]::new);
//	}
//	
//	public static Drawable[] getIndicatorDrawable(Context mcontext) {
//		Drawable[] titleArray = new Drawable[]{
//				ContextCompat.getDrawable(mcontext, R.drawable.indicator_cat_socialwellbeing),
//				ContextCompat.getDrawable(mcontext, R.drawable.indicator_cat_environment),
//				ContextCompat.getDrawable(mcontext, R.drawable.indicator_cat_economicwellbeing),
//				ContextCompat.getDrawable(mcontext, R.drawable.indicator_cat_goodgovernance)
//		};
//		return titleArray;
//	}
}
