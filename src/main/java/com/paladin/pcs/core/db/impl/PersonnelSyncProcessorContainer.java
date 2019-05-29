package com.paladin.pcs.core.db.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paladin.pcs.core.db.AbstractDBSyncProcessorContainer;
import com.paladin.pcs.core.db.DBSyncContainer.DBSyncEnvironment;
import com.paladin.pcs.core.db.DBSyncProcessor;
import com.paladin.pcs.core.db.SyncExceptionHandler;
import com.paladin.pcs.core.db.SyncRecordHandler;
import com.paladin.pcs.model.sync.SyncModelPersonnel;
import com.paladin.pcs.model.sync.SyncTarget;
import com.paladin.pcs.service.sync.SyncModelPersonnelService;
import com.paladin.pcs.service.sync.SyncPersonnelService;

@Component
public class PersonnelSyncProcessorContainer extends AbstractDBSyncProcessorContainer<SyncModelPersonnel> {

	public static final String ID = "personnel";

	@Autowired
	private SyncModelPersonnelService syncModelPersonnelService;
	@Autowired
	private SyncPersonnelService syncPersonnelService;
	@Autowired
	private SyncExceptionHandler syncExceptionHandler;
	@Autowired
	private SyncRecordHandler syncRecordHandler;

	@Override
	public String getId() {
		return ID;
	}
	
	@Override
	public String getName() {
		return "人员账号模块";
	}

	@Override
	public SyncModelPersonnel getProcessorConfig(DBSyncEnvironment environment) {
		if (environment != null) {
			return syncModelPersonnelService.get(environment.getName());
		}
		return null;
	}

	@Override
	public DBSyncProcessor assembleProcessorConfig(DBSyncEnvironment environment, SyncModelPersonnel config) {
		if (environment == null)
			return null;
		DBSyncProcessor processor = getProcessor(environment.getName());
		if (processor == null) {
			processor = new PersonnelSyncProcessor(environment.getName(), getId(), config);
		}

		if (processor instanceof PersonnelSyncProcessor) {
			PersonnelSyncProcessor psp = (PersonnelSyncProcessor) processor;
			psp.setDataSource(environment.getDataBaseSource().getRealDataSource());

			boolean enabled = environment.isEnabled();
			if (enabled) {
				Integer status = config.getStatus();
				enabled = status != null && status == SyncTarget.STATUS_ENABLED;
			}

			psp.setEnabled(enabled);
			psp.setSyncDataHandler(syncPersonnelService);
			psp.setSyncRecordHandler(syncRecordHandler);
			psp.setSyncExceptionHandler(syncExceptionHandler);
		}

		return processor;
	}



}
