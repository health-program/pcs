package com.paladin.pcs.controller.sync.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.pcs.service.sync.dto.SyncTaskQuery;

public class SyncTaskExportCondition extends ExportCondition {

	private SyncTaskQuery query;

	public SyncTaskQuery getQuery() {
		return query;
	}

	public void setQuery(SyncTaskQuery query) {
		this.query = query;
	}

}