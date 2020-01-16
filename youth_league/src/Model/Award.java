package Model;

public class Award {
	private String awardName;
	private String awardLevel;
	private String awardDate;
	private String awardCategory;
	private String _Class;
	
	public Award(String awardName, String awardLevel, String awardDate, String awardCategory) {
		this.awardName = awardName;
		this.awardLevel = awardLevel;
		this.awardDate = awardDate;
		this.awardCategory = awardCategory;
	}
	
	public String get_Class() {
		return _Class;
	}

	public void set_Class(String _Class) {
		this._Class = _Class;
	}

	public String getAwardName() {
		return awardName;
	}
	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}
	public String getAwardLevel() {
		return awardLevel;
	}
	public void setAwardLevel(String awardLevel) {
		this.awardLevel = awardLevel;
	}
	public String getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}
	public String getAwardCategory() {
		return awardCategory;
	}
	public void setAwardCategory(String awardCategory) {
		this.awardCategory = awardCategory;
	}

	
	

}
