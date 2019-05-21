package com.paladin.pcs.service.sync.dto;


public class SyncTargetDTO {

	// 同步数据库名称
	private String name;

	// 同步数据库地址
	private String url;

	// 同步数据库用户名
	private String username;

	// 同步数据库密码
	private String password;

	// 同步优先级
	private Integer priorityLevel;

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

	public Integer getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

}