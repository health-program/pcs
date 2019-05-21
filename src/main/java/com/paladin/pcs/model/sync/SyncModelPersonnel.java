package com.paladin.pcs.model.sync;

import java.util.Date;

import javax.persistence.Id;

public class SyncModelPersonnel {

	// 同步数据库名称
	@Id
	private String name;

	// 
	private String sql;

	// 身份证域
	private String identificationIdField;

	// 账号域
	private String accountField;

	// 密码域
	private String passwordField;

	// 更新时间域
	private String updateTimeField;

	// 状态域
	private String statusField;
	
	// 启用状态
	private Integer status;
	
	// 最近同步时间
	private Date lastSyncTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getIdentificationIdField() {
		return identificationIdField;
	}

	public void setIdentificationIdField(String identificationIdField) {
		this.identificationIdField = identificationIdField;
	}

	public String getAccountField() {
		return accountField;
	}

	public void setAccountField(String accountField) {
		this.accountField = accountField;
	}

	public String getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(String passwordField) {
		this.passwordField = passwordField;
	}

	public String getUpdateTimeField() {
		return updateTimeField;
	}

	public void setUpdateTimeField(String updateTimeField) {
		this.updateTimeField = updateTimeField;
	}

	public String getStatusField() {
		return statusField;
	}

	public void setStatusField(String statusField) {
		this.statusField = statusField;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

}