package com.paladin.pcs.core.db;

import java.util.Date;

public interface SyncDataHandler<T> {

	public void saveData(T t);
	
	public void updateSyncTime(DeltaSyncProcessor<T> processor, Date syncTime);
	
}
