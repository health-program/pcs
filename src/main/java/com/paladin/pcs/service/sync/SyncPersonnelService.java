package com.paladin.pcs.service.sync;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.core.copy.SimpleBeanCopier;
import com.paladin.framework.core.copy.SimpleBeanCopier.SimpleBeanCopyUtil;
import com.paladin.framework.utils.JsonUtil;
import com.paladin.framework.utils.uuid.UUIDUtil;
import com.paladin.pcs.core.PersonnelSyncProcessor.AccountStatus;
import com.paladin.pcs.core.PersonnelSyncProcessor.PersonnelAccount;
import com.paladin.pcs.mapper.sync.SyncPersonnelMapper;
import com.paladin.pcs.model.sync.SyncException;
import com.paladin.pcs.model.sync.SyncPersonnel;
import com.paladin.pcs.model.sync.SyncTarget;

@Service
public class SyncPersonnelService extends ServiceSupport<SyncPersonnel> {

	@Autowired
	private SyncTargetService syncTargetService;

	@Autowired
	private SyncExceptionService syncExceptionService;

	@Autowired
	private SyncPersonnelMapper syncPersonnelMapper;

	public void syncPersonnelAccount(List<PersonnelAccount> pas, String syncTarget, long lastUpdateTime) {

		if (pas != null && pas.size() > 0) {
			for (PersonnelAccount pa : pas) {
				if (pa == null)
					continue;

				try {
					SyncPersonnel sp = new SyncPersonnel();
					SimpleBeanCopyUtil.simpleCopy(pa, sp);
					sp.setSyncTarget(syncTarget);

					AccountStatus status = pa.getStatus();
					if (status == AccountStatus.ENABLED) {
						if (syncPersonnelMapper.updatePersonnel(sp) == 0) {
							syncPersonnelMapper.insert(sp);
						}
					} else {
						syncPersonnelMapper.deletePersonnel(sp);
					}
				} catch (Exception e) {

					Date now = new Date();
					SyncException se = new SyncException();
					se.setId(UUIDUtil.createUUID());
					se.setSyncTarget(syncTarget);
					se.setSyncTime(now);
					se.setSyncContent(substring(JsonUtil.getJson(pa), 500));
					se.setException(substring(e.getMessage(), 500));
					se.setCreateTime(now);

					syncExceptionService.save(se);

				}

			}
									
			SyncTarget target = syncTargetService.get(syncTarget);
		}

	}

	private String substring(String str, int max) {
		return str != null ? str.substring(0, max) : null;
	}

}
