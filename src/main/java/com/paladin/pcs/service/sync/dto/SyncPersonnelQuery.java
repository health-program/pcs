package com.paladin.pcs.service.sync.dto;

import com.paladin.framework.common.OffsetPage;
import com.paladin.framework.common.QueryCondition;
import com.paladin.framework.common.QueryType;

public class SyncPersonnelQuery extends OffsetPage {

	private String identificationId;
	
	private String syncTarget;
	
	private String account;

	@QueryCondition(type = QueryType.EQUAL)
	public String getIdentificationId() {
		return identificationId;
	}

	public void setIdentificationId(String identificationId) {
		this.identificationId = identificationId;
	}

	@QueryCondition(type = QueryType.EQUAL)
	public String getSyncTarget() {
		return syncTarget;
	}

	public void setSyncTarget(String syncTarget) {
		this.syncTarget = syncTarget;
	}

	@QueryCondition(type = QueryType.EQUAL)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}