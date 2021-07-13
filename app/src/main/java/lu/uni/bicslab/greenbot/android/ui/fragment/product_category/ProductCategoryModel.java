package lu.uni.bicslab.greenbot.android.ui.fragment.product_category;


public class ProductCategoryModel {
	
	String id;
	String name;
	String icon_name;
	String description;
	
	
	public ProductCategoryModel(String id, String name, String icon_name, String description) {
		this.id = id;
		this.name = name;
		this.icon_name = icon_name;
		this.description = description;
	}
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setIndicator_name(String indicator_name) {
		this.name = indicator_name;
	}
	
	public String getIcon_name() {
		return icon_name;
	}
	
	public void setIcon_name(String icon_name) {
		this.icon_name = icon_name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}