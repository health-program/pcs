package com.paladin.pcs.controller.sync.dto;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.pcs.service.sync.dto.SyncModelPersonnelQuery;

public class SyncModelPersonnelExportCondition extends ExportCondition {

	private SyncModelPersonnelQuery query;

	public SyncModelPersonnelQuery getQuery() {
		return query;
	}

	public void setQuery(SyncModelPersonnelQuery query) {
		this.query = query;
	}

}