package models.git;

public class Commit {

	private String url;
	private String author;
	private String message;

	public Commit(String url, String author, String message) {
		this.url = url;
		this.author = author;
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "{\"url\" : \"" + getUrl() + "\" , \"author\" : \""
				+ getAuthor() + "\" , \"message\" : \"" + getMessage() + "\"}";
	}
}
