package com.paladin.pcs.service.sync;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paladin.pcs.mapper.sync.SyncModelPersonnelMapper;
import com.paladin.pcs.model.sync.SyncModelPersonnel;
import com.paladin.framework.core.ServiceSupport;

@Service
public class SyncModelPersonnelService extends ServiceSupport<SyncModelPersonnel> {

	@Autowired
	private SyncModelPersonnelMapper syncModelPersonnelMapper;
	
	public void updateSyncTime(String name, Date syncTime) {
		syncModelPersonnelMapper.updateSyncTime(name, syncTime);
	}

}