package de.Jodu555.NettyBackend.NettyBackend.abstracts;

public abstract class AbstractResponse {
	
	boolean success = false;
	
	boolean redirect = false;
	String url;
	
	public AbstractResponse redirect(String url) {
		this.url = url;
		this.redirect = true;
		return this;
	}
	
	public boolean isRedirect() {
		return redirect;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
}
