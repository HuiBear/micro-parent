/**
 * 
 */
package com.micro.ykh.component.common;



import java.util.Date;


public class TokenInfo {

	private boolean active;
	
	private String client_id;
	
	private String[] scope;
	
	private String user_name;
	
	private String[] aud;
	
	private Date exp;

	private String[] authorities;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String[] getAud() {
		return aud;
	}

	public void setAud(String[] aud) {
		this.aud = aud;
	}

	public Date getExp() {
		return exp;
	}

	public void setExp(Date exp) {
		this.exp = exp;
	}

	public String[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}

 	
}
