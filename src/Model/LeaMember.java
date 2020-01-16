package Model;


public class LeaMember {
	private String memName;
	private String sex;
	private String nation;
	private String nativePlace;
	private String birthday;
	private String politicStatus;
	private String joinPartyDate;
	private String joinLeaDate;
	private String _Class;
	public LeaMember(String memName, String sex, String nation, String nativePlace, String birthday, String politicStatus,
			String joinPartyDate, String joinLeaDate, String _Class) {
		this.memName = memName;
		this.sex = sex;
		this.nation = nation;
		this.nativePlace = nativePlace;
		this.birthday = birthday;
		this.politicStatus = politicStatus;
		this.joinPartyDate = joinPartyDate;
		this.joinLeaDate = joinLeaDate;
		this._Class = _Class;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNotion(String nation) {
		this.nation = nation;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPoliticStatus() {
		return politicStatus;
	}
	public void setPoliticStatus(String politicStatus) {
		this.politicStatus = politicStatus;
	}
	
	public String getJoinPartyDate() {
		return joinPartyDate;
	}
	public void setJoinPartyDate(String joinPartyDate) {
		this.joinPartyDate = joinPartyDate;
	}
	public String getJoinLeaDate() {
		return joinLeaDate;
	}
	public void setJoinLeaDate(String joinLeaDate) {
		this.joinLeaDate = joinLeaDate;
	}
	public String get_Class() {
		return _Class;
	}
	public void set_Class(String _Class) {
		this._Class = _Class;
	}
	
	
	
	
	
}
