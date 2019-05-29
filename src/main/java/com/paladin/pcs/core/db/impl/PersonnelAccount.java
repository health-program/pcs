package com.paladin.pcs.core.db.impl;

import java.util.Date;

public class PersonnelAccount {
	private String syncTarget;
	private String identificationId;
	private String account;
	private String password;
	private Date updateTime;
	private Date syncTime;
	private String status;

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

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
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
