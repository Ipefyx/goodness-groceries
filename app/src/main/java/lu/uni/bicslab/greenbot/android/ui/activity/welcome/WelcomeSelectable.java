package lu.uni.bicslab.greenbot.android.ui.activity.welcome;

public class WelcomeSelectable {
	
	private String id;
	private String description;
	private String image;
	private int color;
	private boolean selected = false;
	
	
	public WelcomeSelectable(String id, String description, String image, int color) {
		this.id = id;
		this.description = description;
		this.image = image;
		this.color = color;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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

}
