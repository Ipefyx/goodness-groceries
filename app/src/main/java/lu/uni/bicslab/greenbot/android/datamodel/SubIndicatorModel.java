package lu.uni.bicslab.greenbot.android.datamodel;


import java.io.Serializable;

public class SubIndicatorModel implements Serializable {
	
	private String name;
	private String description;
	
	public SubIndicatorModel(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getIndicator_name() {
		return name;
	}
	
	public void setIndicator_name(String indicator_name) {
		this.name = indicator_name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}