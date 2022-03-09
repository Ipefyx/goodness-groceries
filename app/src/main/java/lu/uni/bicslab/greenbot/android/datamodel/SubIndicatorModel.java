package lu.uni.bicslab.greenbot.android.datamodel;


import java.io.Serializable;

public class SubIndicatorModel implements Serializable {
	
	private String name;
	private String description;
	private String file;
	
	public SubIndicatorModel(String name, String description) {
		this.name = name;
		this.description = description;
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

	public String getFile() { return file; }

	public void setFile(String file) { this.file = file; }
}