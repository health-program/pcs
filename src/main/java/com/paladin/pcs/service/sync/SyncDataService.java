package com.paladin.pcs.service.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paladin.pcs.core.db.DBSyncContainer;
import com.paladin.pcs.core.db.DBSyncContainer.DBSyncEnvironment;
import com.paladin.pcs.core.db.DBSyncProcessor;
import com.paladin.pcs.core.db.impl.PersonnelSyncProcessorContainer;
import com.paladin.pcs.model.sync.SyncModelPersonnel;
import com.paladin.pcs.model.sync.SyncTarget;

@Service
public class SyncDataService {

	@Autowired
	private DBSyncContainer dBSyncContainer;

	@Autowired
	private SyncTargetService syncTargetService;

	@Autowired
	private SyncModelPersonnelService syncModelPersonnelService;

	@Transactional
	public boolean saveSyncTarget(SyncTarget model) {
		if (model != null && syncTargetService.save(model) > 0) {
			dBSyncContainer.addSyncEnvironment(model);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean updateSyncTarget(SyncTarget model) {
		if (model != null && syncTargetService.update(model) > 0) {
			dBSyncContainer.updateSyncEnvironment(model);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean removeSyncTarget(String id) {
		if (syncTargetService.removeByPrimaryKey(id) > 0) {
			dBSyncContainer.removeSyncEnvironment(id);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean saveSyncModelPersonnel(SyncModelPersonnel model) {
		if (model != null && syncModelPersonnelService.save(model) > 0) {
			PersonnelSyncProcessorContainer processContainer = (PersonnelSyncProcessorContainer) dBSyncContainer
					.getSyncProcessorContainer(PersonnelSyncProcessorContainer.ID);
			if (processContainer != null) {
				return processContainer.addProcessor(dBSyncContainer.getSyncEnvironment(model.getName())) != null;
			}

		}
		return false;
	}

	@Transactional
	public boolean updateSyncModelPersonnel(SyncModelPersonnel model) {
		if (model != null && syncModelPersonnelService.update(model) > 0) {
			PersonnelSyncProcessorContainer processContainer = (PersonnelSyncProcessorContainer) dBSyncContainer
					.getSyncProcessorContainer(PersonnelSyncProcessorContainer.ID);
			if (processContainer != null) {
				return processContainer.updateProcessor(dBSyncContainer.getSyncEnvironment(model.getName())) != null;
			}
		}
		return false;
	}

	@Transactional
	public boolean removeSyncModelPersonnel(String id) {
		if (syncModelPersonnelService.removeByPrimaryKey(id) > 0) {
			PersonnelSyncProcessorContainer processContainer = (PersonnelSyncProcessorContainer) dBSyncContainer
					.getSyncProcessorContainer(PersonnelSyncProcessorContainer.ID);
			if (processContainer != null) {
				return processContainer.removeProcessor(id) != null;
			}
		}

		return false;
	}

	public boolean updateProcessorStatus(String name, String model, Integer status) {
		DBSyncProcessor processor = dBSyncContainer.getSyncProcessor(name, model);
		if (processor != null) {
			DBSyncEnvironment environment = dBSyncContainer.getSyncEnvironment(name);
			boolean enabled = environment.isEnabled();
			if (enabled) {
				enabled = status != null && status == SyncTarget.STATUS_ENABLED;
			}
			processor.setEnabled(enabled);		
		}
		return true;
	}

	public boolean syncData(String name, String model) {
		DBSyncProcessor processor = dBSyncContainer.getSyncProcessor(name, model);
		if (processor != null) {
			if(processor.isRunning()) return true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					processor.sync();
				}				
			}).start();	
		}
		return true;
	}

	public boolean isProcessorRunning(String name, String model) {
		DBSyncProcessor processor = dBSyncContainer.getSyncProcessor(name, model);
		if (processor != null) {
			return processor.isRunning();
		}
		return false;
	}

}
