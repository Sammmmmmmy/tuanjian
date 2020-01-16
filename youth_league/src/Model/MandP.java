package Model;

public class MandP {
	private String stuMPName;
	private String MPContent;
	private String MPlevel;
	private String MPDate;
	private String MPCategory;
	private String _Class;
	public MandP(String stuMPName, String mPContent, String mPlevel, String mPDate, String mPCategory,String Class) {
		super();
		this.stuMPName = stuMPName;
		MPContent = mPContent;
		MPlevel = mPlevel;
		MPDate = mPDate;
		MPCategory = mPCategory;
		this._Class = Class;
	}
	
	public String get_Class() {
		return _Class;
	}

	public void set_Class(String _Class) {
		this._Class = _Class;
	}

	public String getStuMPName() {
		return stuMPName;
	}
	public void setStuMPName(String stuMPName) {
		this.stuMPName = stuMPName;
	}
	public String getMPContent() {
		return MPContent;
	}
	public void setMPContent(String mPContent) {
		MPContent = mPContent;
	}
	public String getMPlevel() {
		return MPlevel;
	}
	public void setMPlevel(String mPlevel) {
		MPlevel = mPlevel;
	}
	public String getMPDate() {
		return MPDate;
	}
	public void setMPDate(String mPDate) {
		MPDate = mPDate;
	}
	public String getMPCategory() {
		return MPCategory;
	}
	public void setMPCategory(String mPCategory) {
		MPCategory = mPCategory;
	}
	
	
}
