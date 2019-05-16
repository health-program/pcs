package com.paladin.pcs.controller.sync.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.pcs.service.sync.dto.SyncExceptionQuery;

public class SyncExceptionExportCondition extends ExportCondition {

	private SyncExceptionQuery query;

	public SyncExceptionQuery getQuery() {
		return query;
	}

	public void setQuery(SyncExceptionQuery query) {
		this.query = query;
	}

}