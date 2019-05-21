package com.paladin.pcs.core.db.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paladin.pcs.core.db.DBSyncContainer.DBSyncEnvironment;
import com.paladin.pcs.core.db.DBSyncProcessor;
import com.paladin.pcs.core.db.DBSyncProcessorContainer;
import com.paladin.pcs.core.db.SyncExceptionHandler;
import com.paladin.pcs.core.db.SyncRecordHandler;
import com.paladin.pcs.model.sync.SyncModelPersonnel;
import com.paladin.pcs.service.sync.SyncModelPersonnelService;
import com.paladin.pcs.service.sync.SyncPersonnelService;

@Component
public class PersonnelSyncProcessorContainer implements DBSyncProcessorContainer {

	@Autowired
	private SyncModelPersonnelService syncModelPersonnelService;
	@Autowired
	private SyncPersonnelService syncPersonnelService;
	@Autowired
	private SyncExceptionHandler syncExceptionHandler;
	@Autowired
	private SyncRecordHandler syncRecordHandler;

	private Map<String, DBSyncProcessor> processorMap = new ConcurrentHashMap<>();

	@Override
	public DBSyncProcessor addProcessor(DBSyncEnvironment environment) {
		String name = environment.getName();
		SyncModelPersonnel smp = syncModelPersonnelService.get(name);
		if (smp != null) {
			PersonnelSyncProcessor processor = new PersonnelSyncProcessor(name, getId(), smp);
			processor.setDataSource(environment.getDataBaseSource().getRealDataSource());
			processor.setSyncDataHandler(syncPersonnelService);
			processor.setSyncRecordHandler(syncRecordHandler);
			processor.setSyncExceptionHandler(syncExceptionHandler);
			
			processorMap.put(name, processor);
			return processor;
		}
		return null;
	}

	@Override
	public DBSyncProcessor removeProcessor(DBSyncEnvironment environment) {
		return processorMap.remove(environment.getName());
	}

	@Override
	public DBSyncProcessor getProcessor(DBSyncEnvironment environment) {
		return processorMap.get(environment.getName());
	}

	@Override
	public String getId() {
		return "personnel";
	}
}
