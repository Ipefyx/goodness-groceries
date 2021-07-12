package lu.uni.bicslab.greenbot.android.ui.fragment.indicator;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
	
	public String code;
	public String name;
	public String description;
	public String type;
	public String prod_cat_icon;
	public String provider;
	public String image_url;
	public List<IndicatorModel> indicators;
	
	
	public ProductModel(String code, String name, String description, String type, String prod_cat_icon, String provider, String image_url, List<IndicatorModel> indicators) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.type = type;
		this.prod_cat_icon = prod_cat_icon;
		this.provider = provider;
		this.image_url = image_url;
		this.indicators = indicators;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getProd_cat_icon() {
		return prod_cat_icon;
	}
	
	public void setProd_cat_icon(String prod_cat_icon) {
		this.prod_cat_icon = prod_cat_icon;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getImage_url() {
		return image_url;
	}
	
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	public List<IndicatorModel> getIndicators() {
		return indicators;
	}
	
	public void setIndicators(List<IndicatorModel> indicators) {
		this.indicators = indicators;
	}
}