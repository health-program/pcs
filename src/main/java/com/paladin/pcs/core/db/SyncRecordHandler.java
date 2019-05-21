package com.paladin.pcs.core.db;

import java.util.Date;

public interface SyncRecordHandler {
	
	public void recordHandle(DeltaSyncProcessor<?> processor, Date syncTime, long cost);

}
