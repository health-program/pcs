package com.paladin.pcs.service.sync;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.core.copy.SimpleBeanCopier.SimpleBeanCopyUtil;
import com.paladin.pcs.core.db.DeltaSyncProcessor;
import com.paladin.pcs.core.db.SyncDataHandler;
import com.paladin.pcs.core.db.impl.PersonnelAccount;
import com.paladin.pcs.mapper.sync.SyncPersonnelMapper;
import com.paladin.pcs.model.sync.SyncPersonnel;

@Service
public class SyncPersonnelService extends ServiceSupport<SyncPersonnel> implements SyncDataHandler<PersonnelAccount>{

	@Autowired
	private SyncModelPersonnelService syncModelPersonnelService;	
	@Autowired
	private SyncPersonnelMapper syncPersonnelMapper;

	public void saveData(PersonnelAccount pa) {
		SyncPersonnel sp = new SyncPersonnel();
		SimpleBeanCopyUtil.simpleCopy(pa, sp);
		if (syncPersonnelMapper.updatePersonnel(sp) == 0) {
			syncPersonnelMapper.insert(sp);
		}
	}

	@Override
	public void updateSyncTime(DeltaSyncProcessor<PersonnelAccount> processor, Date syncTime) {	
		syncModelPersonnelService.updateSyncTime(processor.getName(), syncTime);		
	}

}
