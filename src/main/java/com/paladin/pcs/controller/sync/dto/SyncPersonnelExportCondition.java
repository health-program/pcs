package com.paladin.pcs.controller.sync.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.pcs.service.sync.dto.SyncPersonnelQuery;

public class SyncPersonnelExportCondition extends ExportCondition {

	private SyncPersonnelQuery query;

	public SyncPersonnelQuery getQuery() {
		return query;
	}

	public void setQuery(SyncPersonnelQuery query) {
		this.query = query;
	}

}