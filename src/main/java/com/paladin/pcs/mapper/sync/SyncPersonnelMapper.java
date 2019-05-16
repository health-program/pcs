package com.paladin.pcs.mapper.sync;

import com.paladin.pcs.model.sync.SyncPersonnel;

import com.paladin.framework.core.configuration.mybatis.CustomMapper;

public interface SyncPersonnelMapper extends CustomMapper<SyncPersonnel>{
	
	public int updatePersonnel(SyncPersonnel syncPersonnel);
	
	public int deletePersonnel(SyncPersonnel syncPersonnel);
}