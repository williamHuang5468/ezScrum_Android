package ntut.mobile.ezscrum.model;

public class TagObject {
	private String tagID = "";
	private String tagName = "";
	
	public String getTagID() {
		return tagID;
	}
	
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public String toString() {
		String str = "tagID: " + tagID + ", tagName: " + tagName;
		return str;
	}
	
	public boolean equals(TagObject tag) {
		return (tagName.equals(tag.getTagName()));
	}
}
