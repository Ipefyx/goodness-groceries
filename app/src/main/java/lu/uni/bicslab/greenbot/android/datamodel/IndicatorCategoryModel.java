package lu.uni.bicslab.greenbot.android.datamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndicatorCategoryModel implements Serializable {
	
	private String id;
	private String name;
	private String icon_name;
	private String description;
	
	
	public IndicatorCategoryModel(String id, String name, String icon_name, String description) {
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
	
	public void setName(String indicator_name) {
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