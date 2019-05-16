package com.paladin.pcs.service.sync.vo;

import java.util.Date;

public class SyncExceptionVO {

	// 
	private String id;

	// 同步目标
	private String syncTarget;

	// 同步内容
	private String syncContent;

	// 同步时间
	private Date syncTime;

	// 异常内容
	private String exception;

	// 创建时间
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSyncTarget() {
		return syncTarget;
	}

	public void setSyncTarget(String syncTarget) {
		this.syncTarget = syncTarget;
	}

	public String getSyncContent() {
		return syncContent;
	}

	public void setSyncContent(String syncContent) {
		this.syncContent = syncContent;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}