package lu.uni.bicslab.greenbot.android.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductModel implements Serializable {
	
	private String code;
	private String name;
	private String description;
	private String type;
	private String category;
	private String provider;
	private String image_url;
	
	
	public List<IndicatorModel> indicators;
	
	
	public ProductModel(String code, String name, String description, String type, String category, String provider, String image_url) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.type = type;
		this.category = category;
		this.provider = provider;
		this.image_url = image_url;
	}
	
	public ProductModel(ProductModel other) {
		// Copy constructor for deep copy
		
		this.code = other.code;
		this.name = other.name;
		this.description = other.description;
		this.type = other.type;
		this.category = other.category;
		this.provider = other.provider;
		this.image_url = other.image_url;
		
		indicators = new ArrayList<>();
		for (IndicatorModel ind : other.indicators) {
			indicators.add(new IndicatorModel(ind));
		}
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
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
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

	/***
	 * Provides list featured indicators only of the product
	 * @return A list of featured indicators
	 */
	public List<IndicatorModel> getFeaturedIndicators() {
		return indicators.stream().filter(ind -> ind.isApplicable() && ind.getDescription().length() > 0 /*&& ind.sub_indicators.size() > 0*/).collect(Collectors.toList());
	}

	/***
	 * Lets know if the given indicator is featured in the product
	 * @param indicator The indicator to check
	 * @return true if is is featured otherwise false
	 */
	public boolean isFeatured(IndicatorModel indicator) {
		return indicators.stream().filter(ind -> ind.isApplicable()
				&& ind.getDescription().length() > 0
				/*&& ind.sub_indicators.size() > 0*/
				/*&& ind.getId().equals(indicator.getId())).collect(Collectors.toList()).size() > 0*/
				&& indicator.getId().contains(ind.getId())).collect(Collectors.toList()).size() > 0;
	}

}