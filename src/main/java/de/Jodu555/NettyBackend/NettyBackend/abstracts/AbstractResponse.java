package de.Jodu555.NettyBackend.NettyBackend.abstracts;

import java.util.concurrent.TimeUnit;

public abstract class AbstractResponse {
	
	boolean success = false;
	
	boolean redirect = false;
	String url;
	
	public AbstractResponse redirect(String url) {
		this.url = url;
		this.redirect = true;
		return this;
	}
	
	public void wait(TimeUnit unit, int time) {
		try {
			unit.sleep(time);
		} catch (Exception e) {
		}
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
