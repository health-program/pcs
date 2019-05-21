package com.paladin.pcs.model.sync;

import java.util.Date;
import javax.persistence.Id;

public class SyncRecord {

	// 
	@Id
	private String id;

	// 同步目标
	private String syncTarget;

	// 同步模块
	private String syncModel;

	// 触发者
	private String triggerBy;

	// 同步时间
	private Date syncTime;

	// 花费时间（毫秒）
	private Long costTime;

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

	public String getSyncModel() {
		return syncModel;
	}

	public void setSyncModel(String syncModel) {
		this.syncModel = syncModel;
	}

	public String getTriggerBy() {
		return triggerBy;
	}

	public void setTriggerBy(String triggerBy) {
		this.triggerBy = triggerBy;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

}