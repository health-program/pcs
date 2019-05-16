package com.paladin.pcs.core;

public class SyncTarget {
	// 名称，唯一
	private String name;
	// 数据库地址
	private String url;
	// 数据库用户名
	private String username;
	// 数据库连接密码
	private String password;	
	// 优先级
	private int priorityLevel;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(int priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	
	
	
}
