package models;

public class JenkinsObject extends ObjectModel {

	private String name;
	private int number;
	private String status;
	private String url;

	public JenkinsObject(String name, int number, String status, String url) {
		this.name = name;
		this.number = number;
		this.status = status;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setType() {
		this.objectType = ObjectType.JENKINS;
	}


}
