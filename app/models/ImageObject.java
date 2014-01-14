package models;

public class ImageObject extends ObjectModel {

	private String original_url;
	
	public ImageObject(String original_url, String web_preview_url) {
		super();
		this.original_url = original_url;
		this.web_preview_url = web_preview_url;
		setType();
	}


	private String web_preview_url;
	
	
	public String getOriginal_url() {
		return original_url;
	}


	public void setOriginal_url(String original_url) {
		this.original_url = original_url;
	}


	public String getWeb_preview_url() {
		return web_preview_url;
	}


	public void setWeb_preview_url(String web_preview_url) {
		this.web_preview_url = web_preview_url;
	}


	@Override
	public void setType() {
		this.objectType = ObjectType.IMAGE;
	}

}
