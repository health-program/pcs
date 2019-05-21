package com.paladin.pcs.controller.sync.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.pcs.service.sync.dto.SyncRecordQuery;

public class SyncRecordExportCondition extends ExportCondition {

	private SyncRecordQuery query;

	public SyncRecordQuery getQuery() {
		return query;
	}

	public void setQuery(SyncRecordQuery query) {
		this.query = query;
	}

}