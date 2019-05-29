package com.paladin.pcs.service.sync;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.core.session.UserSession;
import com.paladin.framework.utils.uuid.UUIDUtil;
import com.paladin.pcs.core.db.DeltaSyncProcessor;
import com.paladin.pcs.core.db.SyncRecordHandler;
import com.paladin.pcs.model.sync.SyncRecord;

@Service
public class SyncRecordService extends ServiceSupport<SyncRecord> implements SyncRecordHandler{

	@Override
	public void recordHandle(DeltaSyncProcessor<?> processor, Date syncTime, long cost) {
		
		SyncRecord record = new SyncRecord();
		record.setId(UUIDUtil.createUUID());
		record.setSyncModel(processor.getModel());
		record.setSyncTarget(processor.getName());
		record.setSyncTime(syncTime);
		record.setCostTime(cost);
		
		String triggerBy = "system";
		try {
			UserSession session = UserSession.getCurrentUserSession();
			if(session != null) {
				triggerBy = session.getUserId();
			}
		} catch(Exception e) {
			
		}
		
		record.setTriggerBy(triggerBy);
		save(record);
	}

}