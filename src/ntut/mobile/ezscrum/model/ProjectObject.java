package ntut.mobile.ezscrum.model;

public class ProjectObject {
	private String id;
	private String name;
	private String comment;
	private String projectManager;
	private String createDate;
	private String demoDate;
	
	public ProjectObject() {}
	
	public ProjectObject(String id, String name, String comment,
			String projectManager, String createDate, String demoDate) {
		super();
		this.id = id;
		this.name = name;
		this.comment = comment;
		this.projectManager = projectManager;
		this.createDate = createDate;
		this.demoDate = demoDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setDemoDate(String demoDate) {
		this.demoDate = demoDate;
	}

	public String getDemoDate() {
		return demoDate;
	}
}
