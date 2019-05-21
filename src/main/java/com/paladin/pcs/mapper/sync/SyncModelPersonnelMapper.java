package com.paladin.pcs.mapper.sync;

import com.paladin.pcs.model.sync.SyncModelPersonnel;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.paladin.framework.core.configuration.mybatis.CustomMapper;

public interface SyncModelPersonnelMapper extends CustomMapper<SyncModelPersonnel> {

	int updateSyncTime(@Param("name") String name, @Param("syncTime") Date syncTime);

}