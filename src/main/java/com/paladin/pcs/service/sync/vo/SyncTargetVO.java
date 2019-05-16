package com.paladin.pcs.service.sync.vo;

import java.util.Date;

public class SyncTargetVO {

	// 同步目标名称
	private String name;

	// 描述
	private String desc;

	// 最近一次更新时间
	private Date lastUpdateTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}