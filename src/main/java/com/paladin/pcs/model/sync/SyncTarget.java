package com.paladin.pcs.model.sync;

import java.util.Date;
import javax.persistence.Id;

public class SyncTarget {

	// 同步目标名称
	@Id
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