package com.paladin.pcs.controller.sync.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.pcs.service.sync.dto.SyncTargetQuery;

public class SyncTargetExportCondition extends ExportCondition {

	private SyncTargetQuery query;

	public SyncTargetQuery getQuery() {
		return query;
	}

	public void setQuery(SyncTargetQuery query) {
		this.query = query;
	}

}