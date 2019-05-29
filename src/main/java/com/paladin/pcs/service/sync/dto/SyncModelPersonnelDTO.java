package com.paladin.pcs.service.sync.dto;

import javax.validation.constraints.NotEmpty;

public class SyncModelPersonnelDTO {

	// 同步数据库名称
	@NotEmpty(message="数据库名称不能为空")
	private String name;

	// 
	@NotEmpty(message="查询SQL语句不能为空")
	private String searchSql;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSearchSql() {
		return searchSql;
	}

	public void setSearchSql(String searchSql) {
		this.searchSql = searchSql;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}