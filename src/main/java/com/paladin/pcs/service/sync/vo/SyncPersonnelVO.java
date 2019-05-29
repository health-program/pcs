package com.paladin.pcs.service.sync.vo;

import java.util.Date;

public class SyncPersonnelVO {

	// 身份证
	private String identificationId;

	// 账号
	private String account;

	// md5密码
	private String password;
	
	// 状态
	private String status;

	// 更新时间（目标数据库中）
	private Date updateTime;

	// 同步时间
	private Date syncTime;
	
	// 同步目标
	private String syncTarget;

	public String getIdentificationId() {
		return identificationId;
	}

	public void setIdentificationId(String identificationId) {
		this.identificationId = identificationId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public String getSyncTarget() {
		return syncTarget;
	}

	public void setSyncTarget(String syncTarget) {
		this.syncTarget = syncTarget;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}