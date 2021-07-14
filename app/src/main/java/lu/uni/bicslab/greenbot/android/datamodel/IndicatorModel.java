package lu.uni.bicslab.greenbot.android.datamodel;

import java.io.Serializable;
import java.util.List;

public class IndicatorModel implements Serializable {
	
	private String id;
	private String name;
	private String category_id;
	private String icon_name;
	private String general_description;
	
	private boolean applicable;
	public List<SubIndicatorModel> sub_indicators;
	
	// TODO: Remove when reworking Feedback Page
	private int selectionnumber = 0;
	
	// TODO: Remove when reworking Compare Page
	private boolean isSelected = false;
	
	public IndicatorModel(String id, String name, String category_id, String icon_name, String general_description) {
		this.id = id;
		this.name = name;
		this.category_id = category_id;
		this.icon_name = icon_name;
		this.general_description = general_description;
	}
	
	public void mergeBaseIndicator(IndicatorModel base) {
		name = base.name;
		category_id = base.category_id;
		icon_name = base.icon_name;
		general_description = base.general_description;
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory_id() {
		return category_id;
	}
	
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
	public String getIcon_name() {
		return icon_name;
	}
	
	public void setIcon_name(String icon_name) {
		this.icon_name = icon_name;
	}
	
	public String getGeneral_description() {
		return general_description;
	}
	
	public void setGeneral_description(String general_description) {
		this.general_description = general_description;
	}
	
	
	
	
	public int getSelectionnumber() {
		return selectionnumber;
	}
	
	public void setSelectionnumber(int selectionnumber) {
		this.selectionnumber = selectionnumber;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}