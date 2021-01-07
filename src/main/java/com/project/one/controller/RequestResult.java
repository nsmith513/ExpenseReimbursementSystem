package com.project.one.controller;

/**
 * Sent as JSON responses for POST requests.
 * 
 * @author Nicholas Smith
 */
public class RequestResult {
	private boolean success;
	private String what;
	private Object[] data;
	
	public RequestResult() {}
	
	public RequestResult(boolean success, String what, Object[] data) {
		super();
		this.success = success;
		this.what = what;
		this.data = data;
	}
	
	public RequestResult(boolean success) {
		super();
		this.success = success;
		this.what = success ? "success" : "failure";
		this.data = null;
	}
	
	public RequestResult(String what) {
		super();
		this.success = false;
		this.what = what;
		this.data = null;
	}
	
	public RequestResult(Object[] data) {
		super();
		this.success = true;
		this.what = "success";
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}
	
}
